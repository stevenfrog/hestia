/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.hestia.warroom.frontend.actions;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.hestia.warroom.entities.BasePricingRecord;
import com.hestia.warroom.entities.CWPricingRecord;
import com.hestia.warroom.entities.DateEffectiveEntity;
import com.hestia.warroom.entities.MyPricing;
import com.hestia.warroom.entities.NewListAndMultiplier;
import com.hestia.warroom.entities.NewPPT;
import com.hestia.warroom.entities.PricingRecord;
import com.hestia.warroom.entities.Product;
import com.hestia.warroom.entities.ShipPoint;
import com.hestia.warroom.entities.Status;
import com.hestia.warroom.frontend.Helper;
import com.hestia.warroom.services.HestiaWarRoomServiceException;
import com.hestia.warroom.services.dto.InputPriceRecordDTO;
import com.hestia.warroom.services.dto.ModificationTO;
import com.hestia.warroom.services.dto.PagedResult;
import com.hestia.warroom.services.dto.RecalculatedPriceTO;
import com.hestia.warroom.services.impl.CalculationUtil;

/**
 * <p>
 * This action is called when to apply a change to one or more pricing records from the searchResults, and perform the
 * adjustment calculation without actually saving the changes to the pricing records.
 * </p>
 *
 * <p>
 * <b>Thread Safety: </b> It's mutable and not thread safe. The struts framework will guarantee that it's used in the
 * thread safe model.
 * </p>
 *
 * v1.2 - Search Enhancements - added support for NON-CW prices
 * v1.3 - Hestia War Room Enhancements
 *      - added support for save all and undo
 * @author TCSASSEMBLER
 * @version 1.3
 * @since Price Calculations assembly.
 */
@SuppressWarnings("serial")
public class ApplyInputPriceChangesAction extends SearchAction {

    /**
     * <p>
     * Represents the class name used in logging.
     * </p>
     */
    private static final String CLASS_NAME = ApplyInputPriceChangesAction.class.getName();

    /**
     * Session holder for changed input price.
     */
    protected static final String USER_TO_PERSIST_INPUT_PRICE = "USER_TO_PERSIST_INPUT_PRICE";

    /**
     * The CW pricing record IDs to be affected.
     */
    private long[] pricingRecordIds;

    /**
     * The number of saved entity.
     */
    private int numSavedEntities;

    /**
     * The change that will be applied to selected records.
     */
    private ModificationTO delta;

    /**
     * The return message.
     */
    private String returnMessage;

    /**
     * AJAX results.
     */
    private List<RecalculatedPriceTO> modifiedRecords = new ArrayList<RecalculatedPriceTO>();

    /**
     * <p>
     * Create the instance.
     * </p>
     */
    public ApplyInputPriceChangesAction() {
    }

    /**
     * <p>
     * This action is called to update selected records with a new adjustment for input price.
     * </p>
     *
     * @return SUCCESS to indicate that the operation was successful
     * @throws HestiaWarRoomServiceException if error occurs while applying changes.
     */
    @Override
    @SuppressWarnings("unchecked")
    public String execute() throws HestiaWarRoomServiceException {
        String signature = CLASS_NAME + ".execute()";
        // logging entrance
        Helper.logEntrance(getLogger(), signature, new String[] { "pricingRecordIds", "delta" }, new Object[] {
            getPricingRecordIds(), getDelta() });

        if (pricingRecordIds != null) {
            HashSet<Long> recordsToModify = new HashSet<Long>();
            for (int i = 0; i < pricingRecordIds.length; i++) {
                recordsToModify.add(pricingRecordIds[i]);
            }

            // If search is new, use new empty data
            if (((Boolean)getSession().get(NEED_CLEAR_TO_BE_PERSISTED_DATA) == true) || getSession().get(USER_TO_PERSIST_INPUT_PRICE) == null){
                getSession().put(USER_TO_PERSIST_INPUT_PRICE, new HashMap<String, DateEffectiveEntity>());
                getSession().put(USER_TO_PERSIST_MY_PRICING, new HashMap<String, MyPricing>());
                getSession().put(NEED_CLEAR_TO_BE_PERSISTED_DATA, false);
            }

            // Get records to persistent from session
            Map<String, DateEffectiveEntity> recordsToPersist = (Map<String, DateEffectiveEntity>) getSession().get(USER_TO_PERSIST_INPUT_PRICE);

            // Get myPricing to persistent from session
            Map<String, MyPricing> myPricingToPersist = (Map<String, MyPricing>)getSession().get(USER_TO_PERSIST_MY_PRICING);

            // first, the current search results so we have all the records we need
            PagedResult<InputPriceRecordDTO> priceRecords = (PagedResult<InputPriceRecordDTO>) getSession().get(
                USER_SEARCH_RESULTS);

            List<InputPriceRecordDTO> matches = priceRecords.getValues();
            for (InputPriceRecordDTO recordHolder : matches) {
                if ("cwtype".equalsIgnoreCase(getWhichType())) {
                    CWPricingRecord record = recordHolder.getCwPricingRecord();
                    if (record != null && recordsToModify.contains(record.getId())) {

                        // generate adjustments based on the modification submitted
                        CalculationUtil.applyChangeToCWRecord(getCurrentUser(), recordHolder, delta);

                        recordsToPersist.put(""+record.getId(), cloneNewListAndMultiplier(recordHolder.getListPriceAdjustment(), recordHolder.getMultiplierAdjustment(), record.getId()));

                        // recalculate the net price with the applied changes and include in output
                        modifiedRecords.add(CalculationUtil.calculateCWPricing(record,
                            recordHolder.getListPriceAdjustment(), recordHolder.getMultiplierAdjustment()));

                        storeMyPricingIntoSession(myPricingToPersist, recordHolder);
                    }
                } else {
                    PricingRecord record = recordHolder.getPricingRecord();
                    if (record != null && recordsToModify.contains(record.getId())) {
                        // generate adjustments based on the modification submitted
                        CalculationUtil.applyChangeToNonCWRecord(getCurrentUser(), recordHolder, delta);

                        recordsToPersist.put(""+record.getId(), cloneNewPPT(recordHolder.getNewPPT(), record.getId()));

                        // recalculate the net price with the applied changes and include in output
                        modifiedRecords.add(CalculationUtil.calculateNonCWPricing(record,
                            recordHolder.getNewPPT()));

                        storeMyPricingIntoSession(myPricingToPersist, recordHolder);
                    }
                }

            }

        }

        Helper.logExit(getLogger(), signature, SUCCESS, getCurrentUser());
        return SUCCESS;
    }

    /**
     * Store the my pricing into session.
     *
     * @param myPricingToPersist
     *            the myPricing map
     * @param recordHolder
     *            the record to be persisted
     */
    private void storeMyPricingIntoSession(Map<String, MyPricing> myPricingToPersist,
        InputPriceRecordDTO recordHolder) {
        String username = getCurrentUser().getUsername();
        Date startDate = delta.getStartDate();

        // Store mypricing in session
        MyPricing myPricing = new MyPricing();
        myPricing.setCreatedOn(startDate);
        myPricing.setCreatedBy(username);
        myPricing.setUpdatedBy(username);
        myPricing.setUpdatedOn(startDate);
        BasePricingRecord basePricingRecord = recordHolder.getPricingRecord();
        if (basePricingRecord == null) {
            basePricingRecord = recordHolder.getCwPricingRecord();
        }
        myPricing.setName(basePricingRecord.getProduct().getAlternateCode1());
        myPricing.setProgress(100d);
        myPricing.setStatus(Status.FINISHED);
        myPricing.setCwPricingRecord(recordHolder.getCwPricingRecord());
        myPricing.setPricingRecord(recordHolder.getPricingRecord());
        myPricingToPersist.put("" + basePricingRecord.getId(), myPricing);
    }

    /**
     * <p>
     * Save all changed data in database.
     * </p>
     *
     * @throws HestiaWarRoomServiceException
     *             the exception propagating from the service layer.
     * @return SUCCESS
     * @since Hestia War Room Enhancements
     */
    @SuppressWarnings("unchecked")
    public String saveAll() throws HestiaWarRoomServiceException {
        String signature = CLASS_NAME + ".saveAll()";
        // logging entrance
        Helper.logEntrance(getLogger(), signature, null, null);

        Map<String, DateEffectiveEntity> recordsToPersist = (Map<String, DateEffectiveEntity>) getSession().get(USER_TO_PERSIST_INPUT_PRICE);
        Map<String, MyPricing> myPricingToPersist = (Map<String, MyPricing>)getSession().get(USER_TO_PERSIST_MY_PRICING);

        numSavedEntities = 0;

        if(recordsToPersist == null || recordsToPersist.isEmpty()){
            Helper.logExit(getLogger(), signature, SUCCESS, getCurrentUser());
            return SUCCESS;
        }

        // If value is NewListAndMultiplier, check both list price and multiplier must be set together
        for (Map.Entry<String, DateEffectiveEntity> entry : recordsToPersist.entrySet()) {
            DateEffectiveEntity value = entry.getValue();
            if (value instanceof NewListAndMultiplier) {
                NewListAndMultiplier newListAndMultiplier = (NewListAndMultiplier)value;
                if(newListAndMultiplier.getListPrice() == null || newListAndMultiplier.getMultiplier() == null){
                    returnMessage = "ListPrice and Multiplier must be set together.";

                    Helper.logExit(getLogger(), signature, SUCCESS, getCurrentUser());
                    return SUCCESS;
                }
            }
        }

        // persist the data in database
        for (Map.Entry<String, DateEffectiveEntity> entry : recordsToPersist.entrySet()) {
            DateEffectiveEntity value = entry.getValue();

            MyPricing myPricing =  myPricingToPersist.get(entry.getKey());

            if (value instanceof NewListAndMultiplier) {
                long entityId = getNewListAndMultiplierService().persistNewListAndMultiplierWithRecordId(getServiceContext(), (NewListAndMultiplier)value);
                myPricing.setRelatedValueId(entityId);
            }else if (value instanceof NewPPT) {
                long entityId = getNewPPTService().persistNewPPTWithRecordId(getServiceContext(), (NewPPT) value);
                myPricing.setRelatedValueId(entityId);
            }

            numSavedEntities++;
        }

        getSession().put(USER_TO_PERSIST_INPUT_PRICE, null);

        Helper.logExit(getLogger(), signature, SUCCESS, getCurrentUser());
        return SUCCESS;
    }

    /**
     * <p>
     * Undo all change.
     * </p>
     *
     * @throws HestiaWarRoomServiceException
     *             the exception propagating from the service layer.
     * @return SUCCESS
     * @since Hestia War Room Enhancements
     */
    @SuppressWarnings("unchecked")
    public String undo() throws HestiaWarRoomServiceException {
        String signature = CLASS_NAME + ".undo()";
        // logging entrance
        Helper.logEntrance(getLogger(), signature, null, null);

        numSavedEntities = 0;

        Map<String, DateEffectiveEntity> recordsToPersist = (Map<String, DateEffectiveEntity>) getSession().get(USER_TO_PERSIST_INPUT_PRICE);
        if(recordsToPersist != null && !recordsToPersist.isEmpty()){
            numSavedEntities = recordsToPersist.size();
        }

        getSession().put(USER_TO_PERSIST_INPUT_PRICE, null);

        Helper.logExit(getLogger(), signature, SUCCESS, getCurrentUser());
        return SUCCESS;
    }

    /**
     * Clone the entity.
     *
     * @param origin
     *            the original entity
     * @param recordId
     *            the record id
     * @return the cloned entity
     */
    private NewListAndMultiplier cloneNewListAndMultiplier(NewListAndMultiplier listPrice,
        NewListAndMultiplier multiplier, long recordId) {
        NewListAndMultiplier clone = new NewListAndMultiplier();
        if (listPrice != null) {
            clone.setId(listPrice.getId());
            clone.setCreatedBy(listPrice.getCreatedBy());
            clone.setCreatedOn(listPrice.getCreatedOn());
            clone.setUpdatedBy(listPrice.getUpdatedBy());
            clone.setUpdatedOn(listPrice.getUpdatedOn());
            clone.setStartDate(listPrice.getStartDate());
            clone.setEndDate(listPrice.getEndDate());
            clone.setComment(listPrice.getComment());
            clone.setListPrice(listPrice.getListPrice());
        }
        if (multiplier != null) {
            clone.setId(multiplier.getId());
            clone.setCreatedBy(multiplier.getCreatedBy());
            clone.setCreatedOn(multiplier.getCreatedOn());
            clone.setUpdatedBy(multiplier.getUpdatedBy());
            clone.setUpdatedOn(multiplier.getUpdatedOn());
            clone.setStartDate(multiplier.getStartDate());
            clone.setEndDate(multiplier.getEndDate());
            clone.setComment(multiplier.getComment());
            clone.setMultiplier(multiplier.getMultiplier());

        }

        clone.setUpdatedBy(getCurrentUser().getUsername());
        clone.setUpdatedOn(new Date());
        clone.setPricingRecord(new CWPricingRecord());
        clone.getPricingRecord().setId(recordId);
        return clone;
    }

    /**
     * Clone the entity.
     *
     * @param origin
     *            the original entity
     * @param recordId
     *            the record id
     * @return the cloned entity
     */
    private NewPPT cloneNewPPT(NewPPT ppt, long recordId) {
        NewPPT clone = new NewPPT();
        clone.setId(ppt.getId());
        clone.setCreatedBy(ppt.getCreatedBy());
        clone.setCreatedOn(ppt.getCreatedOn());
        clone.setUpdatedBy(ppt.getUpdatedBy());
        clone.setUpdatedOn(ppt.getUpdatedOn());
        clone.setStartDate(ppt.getStartDate());
        clone.setEndDate(ppt.getEndDate());
        clone.setComment(ppt.getComment());
        clone.setValue(ppt.getValue());

        clone.setUpdatedBy(getCurrentUser().getUsername());
        clone.setUpdatedOn(new Date());
        clone.setPricingRecord(new PricingRecord());
        clone.getPricingRecord().setId(recordId);
        return clone;
    }

    /**
     * Clone the entity.
     *
     * @param origin
     *            the original entity
     * @return the cloned entity
     */
    private CWPricingRecord cloneCWPricingRecord(CWPricingRecord record, NewListAndMultiplier listPriceAdjustment, NewListAndMultiplier multiplierAdjustment) {
        CWPricingRecord clone = new CWPricingRecord();
        clone.setId(record.getId());
        if(listPriceAdjustment != null){
            clone.setInitialListPrice(listPriceAdjustment.getListPrice());
        }
        if(multiplierAdjustment != null){
            clone.setInitialMultiplier(multiplierAdjustment.getMultiplier());
        }
        clone.setProduct(new Product());
        clone.getProduct().setId(record.getProduct().getId());
        clone.setShipPoint(new ShipPoint());
        clone.getShipPoint().setId(record.getShipPoint().getId());
        return clone;
    }

    /**
     * Clone the entity.
     *
     * @param origin
     *            the original entity
     * @return the cloned entity
     */
    private PricingRecord clonePricingRecord(PricingRecord record, NewPPT newppt) {
        PricingRecord clone = new PricingRecord();
        clone.setId(record.getId());
        clone.setInitialPPT(newppt.getValue());
        clone.setProduct(new Product());
        clone.getProduct().setId(record.getProduct().getId());
        clone.setShipPoint(new ShipPoint());
        clone.getShipPoint().setId(record.getShipPoint().getId());
        return clone;
    }

    /**
     * Gets the value of the field <code>pricingRecordIds</code>.
     *
     * @return the pricingRecordIds
     */
    public long[] getPricingRecordIds() {
        return pricingRecordIds;
    }

    /**
     * Sets the value of the field <code>pricingRecordIds</code>.
     *
     * @param pricingRecordIds the pricingRecordIds to set
     */
    public void setPricingRecordIds(long[] pricingRecordIds) {
        this.pricingRecordIds = pricingRecordIds;
    }

    /**
     * Gets the value of the field <code>delta</code>.
     *
     * @return the delta
     */
    public ModificationTO getDelta() {
        return delta;
    }

    /**
     * Sets the value of the field <code>delta</code>.
     *
     * @param delta the delta to set
     */
    public void setDelta(ModificationTO delta) {
        this.delta = delta;
    }

    /**
     * Gets the value of the field <code>modifiedRecords</code>.
     *
     * @return the modifiedRecords
     */
    public List<RecalculatedPriceTO> getModifiedRecords() {
        return modifiedRecords;
    }

    /**
     * Sets the value of the field <code>modifiedRecords</code>.
     *
     * @param modifiedRecords the modifiedRecords to set
     */
    public void setModifiedRecords(List<RecalculatedPriceTO> modifiedRecords) {
        this.modifiedRecords = modifiedRecords;
    }

    /**
     * <p>
     * Retrieves the numSavedEntities field.
     * </p>
     *
     * @return the value of numSavedEntities
     */
    public int getNumSavedEntities() {
        return numSavedEntities;
    }

    /**
     * <p>
     * Sets the value to numSavedEntities field.
     * </p>
     *
     * @param numSavedEntities the value of numSavedEntities to set
     */
    public void setNumSavedEntities(int numSavedEntities) {
        this.numSavedEntities = numSavedEntities;
    }

    /**
     * <p>
     * Retrieves the returnMessage field.
     * </p>
     *
     * @return the value of returnMessage
     */
    public String getReturnMessage() {
        return returnMessage;
    }

    /**
     * <p>
     * Sets the value to returnMessage field.
     * </p>
     *
     * @param returnMessage the value of returnMessage to set
     */
    public void setReturnMessage(String returnMessage) {
        this.returnMessage = returnMessage;
    }

}
