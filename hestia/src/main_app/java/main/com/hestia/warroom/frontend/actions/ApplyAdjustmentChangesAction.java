/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.hestia.warroom.frontend.actions;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.hestia.warroom.entities.BasePricingRecord;
import com.hestia.warroom.entities.Branch;
import com.hestia.warroom.entities.CWPricingRecord;
import com.hestia.warroom.entities.MyPricing;
import com.hestia.warroom.entities.PricingAdjustment;
import com.hestia.warroom.entities.Status;
import com.hestia.warroom.frontend.Helper;
import com.hestia.warroom.services.HestiaWarRoomPersistenceException;
import com.hestia.warroom.services.HestiaWarRoomServiceException;
import com.hestia.warroom.services.dto.BranchCWPricingSheetTO;
import com.hestia.warroom.services.dto.InputPriceRecordDTO;
import com.hestia.warroom.services.dto.ModificationTO;
import com.hestia.warroom.services.dto.ModificationType;
import com.hestia.warroom.services.dto.ProductPriceRecordDTO;
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
 * v1.1 - Hestia War Room Enhancements
 *      - added support for save all and undo
 *
 * @author stevenfrog, TCSASSEMBLER
 * @version 1.1
 * @since Assembly Adjustments Freight and Branch Overrides
 */
@SuppressWarnings("serial")
public class ApplyAdjustmentChangesAction extends SearchAction {

    /**
     * <p>
     * Represents the class name used in logging.
     * </p>
     */
    private static final String CLASS_NAME = ApplyAdjustmentChangesAction.class.getName();

    /**
     * The default date format.
     */
    public static final String DEFAULT_DATE_FORMAT = "MM/dd/yyyy";

    /**
     * Session holder for changed adjustment.
     */
    protected static final String USER_TO_PERSIST_ADJUSTMENT = "USER_TO_PERSIST_ADJUSTMENT";

    /**
     * The date format instance.
     */
    public final DateFormat DATE_FORMAT = new SimpleDateFormat(DEFAULT_DATE_FORMAT);

    /**
     * The CW pricing record IDs to be affected.
     */
    private String[] productIds;

    /**
     * The number of saved entity.
     */
    private int numSavedEntities;

    /**
     * The change that will be applied to selected records.
     */
    private ModificationTO delta;

    /**
     * AJAX results.
     */
    private List<BranchCWPricingSheetTO> ajaxPricingSheets = new ArrayList<BranchCWPricingSheetTO>();

    /**
     * <p>
     * Create the instance.
     * </p>
     */
    public ApplyAdjustmentChangesAction() {
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
        Helper.logEntrance(getLogger(), signature, new String[] { "productIds", "delta" }, new Object[] {
            getProductIds(), getDelta() });

        if (productIds != null) {
            HashSet<String> recordsToModify = new HashSet<String>();
            for (int i = 0; i < productIds.length; i++) {
                recordsToModify.add(productIds[i]);
            }

            // If search is new, use new empty data
            if (((Boolean)getSession().get(NEED_CLEAR_TO_BE_PERSISTED_DATA) == true) || getSession().get(USER_TO_PERSIST_ADJUSTMENT) == null){
                getSession().put(USER_TO_PERSIST_ADJUSTMENT, new HashMap<String, PricingAdjustment>());
                getSession().put(USER_TO_PERSIST_MY_PRICING, new HashMap<String, MyPricing>());
                getSession().put(NEED_CLEAR_TO_BE_PERSISTED_DATA, false);
            }

            // Get records to persistent from session
            Map<String, PricingAdjustment> recordsToPersist = (Map<String, PricingAdjustment>) getSession().get(USER_TO_PERSIST_ADJUSTMENT);

            // Get myPricing to persistent from session
            Map<String, MyPricing> myPricingToPersist = (Map<String, MyPricing>)getSession().get(USER_TO_PERSIST_MY_PRICING);

            // first, the current search results so we have all the records we need
            Map<String, BranchCWPricingSheetTO> matches = (Map<String, BranchCWPricingSheetTO>) getSession().get(
                USER_PRICING_SHEET);

            for (BranchCWPricingSheetTO recordHolder : matches.values()) {
                ProductPriceRecordDTO record = recordHolder.getAdjustments();
                InputPriceRecordDTO inputPrice = record.getInputPrice();

                BasePricingRecord basePricing = inputPrice.getCwPricingRecord();
                if (basePricing == null) {
                    basePricing = inputPrice.getPricingRecord();
                }

                long branchId = basePricing.getProduct().getCategory().getBranch().getId();

                String recordKey = basePricing.getId() + "_" + branchId;

                if (recordsToModify.contains(recordKey)) {
                    String key = generatePricingKey(inputPrice);

                    // get original value
                    BranchCWPricingSheetTO matchSheet = matches.get(key);
                    String originalValueStr = matchSheet.getNewPricingAdjustment();

                    BranchCWPricingSheetTO sheet = changeAdjustment(inputPrice, originalValueStr, recordsToPersist, myPricingToPersist, recordKey, branchId);

                    String username = getCurrentUser().getUsername();
                    sheet.setNewPricingAdjustment(sheet.getPricingAdjustment());
                    sheet.setNewPricingAdjustmentCreatedBy(username);
                    sheet.setNewPricingAdjustmentComment(delta.getComments());
                    sheet.setNewPricingAdjustmentStartDate(delta.getStartDate());
                    sheet.setNewPricingAdjustmentEndDate(converterToDate(delta.getEndDate()));

                    ajaxPricingSheets.add(sheet);

                    matchSheet.setNewPricingAdjustment(sheet.getPricingAdjustment());
                    matchSheet.setNewPricingAdjustmentCreatedBy(username);
                    matchSheet.setNewPricingAdjustmentComment(delta.getComments());
                    matchSheet.setNewPricingAdjustmentStartDate(delta.getStartDate());
                    matchSheet.setNewPricingAdjustmentEndDate(converterToDate(delta.getEndDate()));
                    matchSheet.setBeforeFreightPrice(sheet.getBeforeFreightPrice());
                    matchSheet.setAdjustedFreightCost(sheet.getAdjustedFreightCost());
                    matchSheet.setTotalPrice(sheet.getTotalPrice());
                    matchSheet.setBranchOverride(sheet.getBranchOverride());
                    matchSheet.setFinalPrice(sheet.getFinalPrice());
                }
            }
        }


        Helper.logExit(getLogger(), signature, SUCCESS, getCurrentUser());
        return SUCCESS;
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

        Map<String, PricingAdjustment> recordsToPersist = (Map<String, PricingAdjustment>) getSession().get(USER_TO_PERSIST_ADJUSTMENT);

        numSavedEntities = 0;

        if(recordsToPersist == null || recordsToPersist.isEmpty()){
            Helper.logExit(getLogger(), signature, SUCCESS, getCurrentUser());
            return SUCCESS;
        }

        // persist the data in database
        for (Map.Entry<String, PricingAdjustment> entry : recordsToPersist.entrySet()) {
            PricingAdjustment value = entry.getValue();

            getPricingAdjustmentService().persistPricingAdjustmentWithBranchId(getServiceContext(), value);

            numSavedEntities++;
        }

        getSession().put(USER_TO_PERSIST_ADJUSTMENT, null);

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

        Map<String, PricingAdjustment> recordsToPersist = (Map<String, PricingAdjustment>) getSession().get(USER_TO_PERSIST_ADJUSTMENT);
        if(recordsToPersist != null && !recordsToPersist.isEmpty()){
            numSavedEntities = recordsToPersist.size();
        }

        getSession().put(USER_TO_PERSIST_ADJUSTMENT, null);

        Helper.logExit(getLogger(), signature, SUCCESS, getCurrentUser());
        return SUCCESS;
    }

    /**
     * Change the adjustment.
     *
     * v1.1 - Hestia War Room Enhancements - added support for save all and undo
     *
     * @param recordHolder
     *            the record
     * @param originalValueStr
     *            the original value string
     * @return the BranchCWPricingSheetTO
     * @throws HestiaWarRoomPersistenceException
     *             if any error occurs
     */
    private BranchCWPricingSheetTO changeAdjustment(InputPriceRecordDTO recordHolder, String originalValueStr,
        Map<String, PricingAdjustment> recordsToPersist, Map<String, MyPricing> myPricingToPersist, String recordKey, long branchId)
        throws HestiaWarRoomPersistenceException {
        ProductPriceRecordDTO adjustment = getPricingRecordsManagementService().populateProductAdjustment(
            getServiceContext(), recordHolder, new Date());

        String username = getCurrentUser().getUsername();
        Date startDate = delta.getStartDate();

        PricingAdjustment pricingAdjustment = adjustment.getPricingAdjustment().getAdjustment();
        // create record to be persisted
        if (pricingAdjustment == null) {
            pricingAdjustment = new PricingAdjustment();
            adjustment.getPricingAdjustment().setAdjustment(pricingAdjustment);

            String recordTypeName;
            long recordId;
            CWPricingRecord cwPricingRecord = recordHolder.getCwPricingRecord();
            if (cwPricingRecord != null) {
                recordTypeName = "CWPricingRecord";
                recordId = cwPricingRecord.getId();
            } else {
                recordTypeName = "PricingRecord";
                recordId = recordHolder.getPricingRecord().getId();
            }

            pricingAdjustment.setRecordId(recordId);
            pricingAdjustment.setRecordTypeName(recordTypeName);
        }

        // Change the adjustment value by web value string
        if (ModificationType.PERCENTAGE.equals(delta.getType())) {
            if (originalValueStr != null) {
                BigDecimal originalValue = new BigDecimal(originalValueStr);
                BigDecimal value = originalValue.multiply(new BigDecimal(delta.getValue()).divide(new BigDecimal(
                    100)));

                pricingAdjustment.setValue(value);
            }
        } else {
            if (ModificationType.ADDER.equals(delta.getType())) {
                BigDecimal originalValue;
                if (originalValueStr == null) {
                    originalValue = new BigDecimal(0);
                } else {
                    originalValue = new BigDecimal(originalValueStr);
                }
                BigDecimal value = originalValue.add(new BigDecimal(delta.getValue()));
                pricingAdjustment.setValue(value);
            } else {
                pricingAdjustment.setValue(new BigDecimal(delta.getValue()));
            }
        }

        pricingAdjustment.setUpdatedBy(username);
        pricingAdjustment.setUpdatedOn(startDate);
        pricingAdjustment.setStartDate(startDate);
        pricingAdjustment.setEndDate(converterToDate(delta.getEndDate()));
        pricingAdjustment.setComment(delta.getComments());

        // Store pricingAdjustment in session
        recordsToPersist.put(recordKey, clonePricingAdjustment(pricingAdjustment, branchId));

        // Store mypricing in session
        MyPricing myPricing = new MyPricing();
        myPricing.setCreatedOn(startDate);
        myPricing.setCreatedBy(username);
        myPricing.setUpdatedBy(username);
        myPricing.setUpdatedOn(startDate);
        BasePricingRecord basePricingRecord = recordHolder.getPricingRecord();
        if(basePricingRecord == null){
            basePricingRecord = recordHolder.getCwPricingRecord();
        }
        myPricing.setName(basePricingRecord.getProduct().getAlternateCode1());
        myPricing.setProgress(100d);
        myPricing.setStatus(Status.FINISHED);
        myPricing.setCwPricingRecord(recordHolder.getCwPricingRecord());
        myPricing.setPricingRecord(recordHolder.getPricingRecord());
        myPricing.setBranchId(branchId);
        myPricingToPersist.put(recordKey, myPricing);

        return CalculationUtil.calculateFinalPrice(adjustment);
    }

    /**
     * Clone the entity.
     *
     * @param origin
     *            the original entity
     * @param branchId
     *            the branch id
     * @return the cloned entity
     */
    private PricingAdjustment clonePricingAdjustment(PricingAdjustment origin, long branchId) {
        PricingAdjustment clone = new PricingAdjustment();
        clone.setId(origin.getId());
        clone.setCreatedBy(origin.getCreatedBy());
        clone.setCreatedOn(origin.getCreatedOn());
        clone.setUpdatedBy(origin.getUpdatedBy());
        clone.setUpdatedOn(origin.getUpdatedOn());
        clone.setStartDate(origin.getStartDate());
        clone.setEndDate(origin.getEndDate());
        clone.setComment(origin.getComment());
        clone.setValue(origin.getValue());
        clone.setRecordId(origin.getRecordId());
        clone.setRecordTypeName(origin.getRecordTypeName());
        clone.setBranch(new Branch());
        clone.getBranch().setId(branchId);
        return clone;
    }

    /**
     * Convert the string to date.
     *
     * @param date
     *            the date string
     * @return the date instance
     */
    private Date converterToDate(String date) {
        try {
            return DATE_FORMAT.parse(date);
        } catch (ParseException e) {
            // Ignore
        }
        return new Date();
    }

    /**
     * <p>
     * Retrieves the productIds field.
     * </p>
     *
     * @return the value of productIds
     */
    public String[] getProductIds() {
        return productIds;
    }

    /**
     * <p>
     * Sets the value to productIds field.
     * </p>
     *
     * @param productIds the value of productIds to set
     */
    public void setProductIds(String[] productIds) {
        this.productIds = productIds;
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
     * <p>
     * Retrieves the ajaxPricingSheets field.
     * </p>
     *
     * @return the value of ajaxPricingSheets
     */
    public List<BranchCWPricingSheetTO> getAjaxPricingSheets() {
        return ajaxPricingSheets;
    }

    /**
     * <p>
     * Sets the value to ajaxPricingSheets field.
     * </p>
     *
     * @param ajaxPricingSheets the value of ajaxPricingSheets to set
     */
    public void setAjaxPricingSheets(List<BranchCWPricingSheetTO> ajaxPricingSheets) {
        this.ajaxPricingSheets = ajaxPricingSheets;
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

}
