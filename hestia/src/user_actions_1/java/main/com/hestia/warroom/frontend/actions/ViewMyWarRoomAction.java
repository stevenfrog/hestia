/*
 * Copyright (C) 2012 TopCoder Inc., All Rights Reserved.
 */
package com.hestia.warroom.frontend.actions;

import java.util.Date;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;

import com.hestia.warroom.entities.MyPricing;
import com.hestia.warroom.entities.User;
import com.hestia.warroom.entities.UserFilterRecord;
import com.hestia.warroom.frontend.Helper;
import com.hestia.warroom.services.HestiaWarRoomServiceException;
import com.hestia.warroom.services.LocalMyPricingService;
import com.hestia.warroom.services.LocalUserFilterRecordService;
import com.hestia.warroom.services.MyPricingService;
import com.hestia.warroom.services.UserFilterRecordService;
import com.hestia.warroom.services.dto.MyPricingSearchCriteria;
import com.hestia.warroom.services.dto.PagedResult;
import com.hestia.warroom.services.dto.ServiceContext;
import com.hestia.warroom.services.dto.SortingType;
import com.hestia.warroom.services.dto.UserFilterRecordSearchCriteria;

/**
 * <p>
 * This action provides method to load data for view my room page. It extends BaseAction and uses injected services for
 * business processing.
 * </p>
 *
 * <p>
 * Sample configurations:
 * <pre>
 *     &lt;bean id=&quot;viewMyWarRoomAction&quot;
 *      class=&quot;com.hestia.warroom.frontend.actions.ViewMyWarRoomAction&quot;&gt;
 *         &lt;property name=&quot;myPricingService&quot; ref=&quot;myPricingServiceBean&quot;/&gt;
 *         &lt;property name=&quot;userFilterRecordService&quot; ref=&quot;userFilterRecordServiceBean&quot;/&gt;
 *      &lt;/bean&gt;
 * </pre>
 * </p>
 *
 * <p>
 * <strong>Thread safety:</strong> This action is mutable but it will be used effectively thread safe by Struts
 * framework.
 * </p>
 * v1.1 - Hestia War Room Enhancements
 *      - added method saveMyPricing
 *      - added support for last criteria
 *      - added support for search one single record
 *      - added support for delete related value of my pricing
 *
 * @author Urmass, TCSDEVELOPER, TCSASSEMBLER
 * @version 1.1
 */
public class ViewMyWarRoomAction extends BaseAction {
    /**
     * <p>
     * The serial version uid.
     * </p>
     */
    private static final long serialVersionUID = 172848156599424767L;

    /**
     * <p>
     * Represents the class name used in logging.
     * </p>
     */
    private static final String CLASS_NAME = ViewMyWarRoomAction.class.getName();

    /**
     * <p>
     * Represents the UserFilterRecordService used by this action.
     * </p>
     *
     * <p>
     * It is used in business methods of the action, in setter and getter.
     * </p>
     *
     * <p>
     * It can have any value, but should be non-null when checkInit() is called.
     * </p>
     *
     * <p>
     * It is fully mutable.
     * </p>
     */
    @EJB(name = "UserFilterRecordServiceBean", beanInterface = LocalUserFilterRecordService.class)
    private UserFilterRecordService userFilterRecordService;

    /**
     * The filter id.
     * @since 1.1
     */
    private long filterId;

    /**
     * The criteria id.
     * @since 1.1 Hestia War Room Enhancements
     */
    private long criteriaId;

    /**
     * <p>
     * Represents the product id.
     * </p>
     * @since 1.1 Hestia War Room Enhancements
     */
    private long productId;

    /**
     * <p>
     * Represents the branch id.
     * </p>
     * @since 1.1 Hestia War Room Enhancements
     */
    private long branchId;

    /**
     * <p>
     * Represents the related value id.
     * </p>
     * @since 1.1 Hestia War Room Enhancements
     */
    private long relatedValueId;

    /**
     * <p>
     * Represents the flag whether deleted related value.
     * </p>
     * @since 1.1 Hestia War Room Enhancements
     */
    private boolean hasDeleteRelatedValue;

    /**
     * <p>
     * Represents the user filter record criteria used by this action.
     * </p>
     *
     * <p>
     * It is used in business methods of the action, in setter and getter.
     * </p>
     *
     * <p>
     * It can have any value.
     * </p>
     *
     * <p>
     * It is fully mutable.
     * </p>
     */
    private UserFilterRecordSearchCriteria userFilterRecordCriteria;

    /**
     * The latest user filter record.
     */
    private UserFilterRecord latestUserFilterRecord;

    /**
     * The latest criteria record.
     * @since 1.1 Hestia War Room Enhancements
     */
    private UserFilterRecord latestCriteriaRecord;

    /**
     * <p>
     * Represents the user filter records used by this action.
     * </p>
     *
     * <p>
     * It is used in business methods of the action, in setter and getter.
     * </p>
     *
     * <p>
     * It can have any value.
     * </p>
     *
     * <p>
     * It is fully mutable.
     * </p>
     */
    private PagedResult<UserFilterRecord> userFilterRecords;

    /**
     * <p>
     * Represents the my pricings used by this action.
     * </p>
     *
     * <p>
     * It is used in business methods of the action, in setter and getter.
     * </p>
     *
     * <p>
     * It can have any value.
     * </p>
     *
     * <p>
     * It is fully mutable.
     * </p>
     */
    private PagedResult<MyPricing> myPricings;

    /**
     * <p>
     * Represents the my pricing criteria used by this action.
     * </p>
     *
     * <p>
     * It is used in business methods of the action, in setter and getter.
     * </p>
     *
     * <p>
     * It can have any value.
     * </p>
     *
     * <p>
     * It is fully mutable.
     * </p>
     */
    private MyPricingSearchCriteria myPricingSearchCriteria;

    /**
     * <p>
     * Represents the MyPricingService used by this action.
     * </p>
     *
     * <p>
     * It is used in business methods of the action, in setter and getter.
     * </p>
     *
     * <p>
     * It can have any value, but should be non-null when checkInit() is called.
     * </p>
     *
     * <p>
     * It is fully mutable.
     * </p>
     */
    @EJB(name = "MyPricingServiceBean", beanInterface = LocalMyPricingService.class)
    private MyPricingService myPricingService;

    /**
     * <p>
     * Default empty constructor.
     * </p>
     */
    public ViewMyWarRoomAction() {
    }

    /**
     * <p>
     * Opens the requested filter and redirects the user to the view for that filter.
     * </p>
     *
     * @return logical result of the execution as String
     *
     * @throws IllegalStateException
     *             if the action is in illegal state (context is null)
     * @throws HestiaWarRoomServiceException
     *             if any error occurs in the inner services
     */
    public String openFilter() throws HestiaWarRoomServiceException {
        String signature = getClass().getName() + "#openFilter";
        ServiceContext context = getServiceContext();
        User user = getCurrentUser();
        ActionHelper.logEntrance(getLogger(), signature, new String[] {"filterId"}, new Object[] {filterId}, user);
        ActionHelper.checkNull(getLogger(), signature, context, "context", user);
        try {
            UserFilterRecord filter = userFilterRecordService.retrieve(context, filterId);
            if (filter == null) {
                throw ActionHelper.logException(getLogger(), signature, new HestiaWarRoomServiceException(
                    "Could not load requested filter."), user);
            }

            return ActionHelper.logExit(getLogger(), signature, new String[]{filter.getType().name()}, user);
        } catch (HestiaWarRoomServiceException e) {
            throw ActionHelper.logException(getLogger(), signature, e, user);
        }
    }

    /**
     * <p>
     * Performs execution of this action. This action will provide the myPricings and userFilterRecords for the page.
     * </p>
     *
     * @return logical result of the execution as String, SUCCESS in case of success.
     *
     * @throws IllegalStateException
     *             if the action is in illegal state (context is null)
     * @throws HestiaWarRoomServiceException
     *             if any error occurs in the inner services
     */
    public String execute() throws HestiaWarRoomServiceException {
        String signature = getClass().getName() + "#execute";
        ServiceContext context = getServiceContext();
        User user = getCurrentUser();
        ActionHelper.logEntrance(getLogger(), signature, new String[]{"userFilterRecordCriteria",
            "myPricingSearchCriteria"}, new Object[]{userFilterRecordCriteria, myPricingSearchCriteria}, user);
        ActionHelper.checkNull(getLogger(), signature, context, "context", user);
        try {
            int defaultPageSize = ActionHelper.getDefaultPageSize();
            // create criteria if they are null:
            if (userFilterRecordCriteria == null) {
                userFilterRecordCriteria = new UserFilterRecordSearchCriteria();
                userFilterRecordCriteria.setPage(1);
                userFilterRecordCriteria.setPageSize(defaultPageSize);
                userFilterRecordCriteria.setSortingColumn("updatedOn");
                userFilterRecordCriteria.setSortingType(SortingType.DESC);
                userFilterRecordCriteria.setCriteriaFlag(false);
            }

            if (myPricingSearchCriteria == null) {
                myPricingSearchCriteria = new MyPricingSearchCriteria();
                myPricingSearchCriteria.setPage(1);
                myPricingSearchCriteria.setPageSize(defaultPageSize);
                myPricingSearchCriteria.setSortingColumn("name");
                myPricingSearchCriteria.setSortingType(SortingType.ASC);
            }
            myPricingSearchCriteria.setOwnername(user.getUsername());
            // search for records:
            // search user filter without user id
            userFilterRecords = userFilterRecordService.search(context, userFilterRecordCriteria);
            myPricings = myPricingService.search(context, myPricingSearchCriteria);

            Date currentSessionStart = getCurrentUser().getLastLogin();
            UserFilterRecord filter = userFilterRecordService.retrieveLastSavedUserFilter(getServiceContext());
            UserFilterRecord criteria = userFilterRecordService.retrieveLastSavedCriteria(getServiceContext());

            Date lastLogin = (Date) getSession().get("lastLoginTimeStamp");
            if (filter != null && lastLogin != null) {
                // ARS 2.5 The war room will include the pricing, search filter that was saved in user's last login.
                // so we display only if it is from the LAST login
                if (filter.getUpdatedOn().after(lastLogin) && currentSessionStart.after(filter.getUpdatedOn())) {
                    latestUserFilterRecord = filter;
                }
            }
            if (criteria != null && lastLogin != null) {
                // ARS 2.5 The war room will include the pricing, search filter that was saved in user's last login.
                // so we display only if it is from the LAST login
                if (criteria.getUpdatedOn().after(lastLogin) && currentSessionStart.after(criteria.getUpdatedOn())) {
                    latestCriteriaRecord = criteria;

                    MyPricing myPricing = myPricingService.getMyPricingByCriteriaId(getServiceContext(),
                        criteria.getId());
                    latestCriteriaRecord.setName(myPricing.getName());
                }
            }
        } catch (HestiaWarRoomServiceException e) {
            throw ActionHelper.logException(getLogger(), signature, e, user);
        }
        return ActionHelper.logExit(getLogger(), signature, new String[]{SUCCESS}, user);
    }

    /**
     * <p>
     * Save my pricing data in database.
     * </p>
     *
     * @throws HestiaWarRoomServiceException
     *             the exception propagating from the service layer.
     * @return SUCCESS
     * @since Hestia War Room Enhancements
     */
    @SuppressWarnings("unchecked")
    public String saveMyPricing() throws HestiaWarRoomServiceException {
        String signature = CLASS_NAME + ".saveMyPricing()";
        // logging entrance
        Helper.logEntrance(getLogger(), signature, null, null);

        Map<String, MyPricing> myPricingToPersist = (Map<String, MyPricing>)getSession().get(USER_TO_PERSIST_MY_PRICING);

        if(myPricingToPersist == null || myPricingToPersist.isEmpty()){
            return SUCCESS;
        }

        // create the my pricing in database
        for (Map.Entry<String, MyPricing> entry : myPricingToPersist.entrySet()) {
            MyPricing value = entry.getValue();

            UserFilterRecord criteria = new UserFilterRecord();
            criteria.setId(criteriaId);
            value.setCriteria(criteria);

            myPricingService.persistMyPricingWithCriteriaId(getServiceContext(), value);
        }

        getSession().put(USER_TO_PERSIST_MY_PRICING, null);

        Helper.logExit(getLogger(), signature, SUCCESS, getCurrentUser());
        return SUCCESS;
    }

    /**
     * <p>
     * Delete the related value.
     * </p>
     *
     * @throws HestiaWarRoomServiceException
     *             the exception propagating from the service layer.
     * @return SUCCESS
     * @since Hestia War Room Enhancements
     */
    public String deleteValueByMyPricing() throws HestiaWarRoomServiceException {
        String signature = CLASS_NAME + ".deleteValueByMyPricing()";
        // logging entrance
        Helper.logEntrance(getLogger(), signature, null, null);

        hasDeleteRelatedValue = myPricingService.deleteValueByMyPricing(getServiceContext(), criteriaId, relatedValueId);


        Helper.logExit(getLogger(), signature, SUCCESS, getCurrentUser());
        return SUCCESS;
    }

    /**
     * <p>
     * Performs checking if action was configured properly.
     * </p>
     *
     * @throws IllegalStateException
     *             if the action wasn't configured properly.(userFilterRecordService and myPricingService should be
     *             non-null)
     */
    @PostConstruct
    public void checkInit() {
        String signature = getClass().getName() + "#checkInit()";
        ActionHelper.logEntrance(getLogger(), signature, null, null, null);
        ActionHelper.checkNull(getLogger(), signature, userFilterRecordService, "userFilterRecordService", null);
        ActionHelper.checkNull(getLogger(), signature, myPricingService, "myPricingService", null);
        ActionHelper.logExit(getLogger(), signature, null, null);
    }

    /**
     * <p>
     * Getter for the field "userFilterRecordService".
     * </p>
     *
     * @return the value of the field "userFilterRecordService"
     */
    public UserFilterRecordService getUserFilterRecordService() {
        return userFilterRecordService;
    }

    /**
     * <p>
     * Setter for the field "userFilterRecordService".
     * </p>
     *
     * @param userFilterRecordService
     *            the value of the field "userFilterRecordService" to set
     */
    public void setUserFilterRecordService(UserFilterRecordService userFilterRecordService) {
        this.userFilterRecordService = userFilterRecordService;
    }

    /**
     * <p>
     * Getter for the field "myPricingService".
     * </p>
     *
     * @return the value of the field "myPricingService"
     */
    public MyPricingService getMyPricingService() {
        return myPricingService;
    }

    /**
     * <p>
     * Setter for the field "myPricingService".
     * </p>
     *
     * @param myPricingService
     *            the value of the field "myPricingService" to set
     */
    public void setMyPricingService(MyPricingService myPricingService) {
        this.myPricingService = myPricingService;
    }

    /**
     * <p>
     * Getter for the field "userFilterRecordCriteria".
     * </p>
     *
     * @return the value of the field "userFilterRecordCriteria"
     */
    public UserFilterRecordSearchCriteria getUserFilterRecordCriteria() {
        return userFilterRecordCriteria;
    }

    /**
     * <p>
     * Setter for the field "userFilterRecordCriteria".
     * </p>
     *
     * @param userFilterRecordCriteria
     *            the value of the field "userFilterRecordCriteria" to set
     */
    public void setUserFilterRecordCriteria(UserFilterRecordSearchCriteria userFilterRecordCriteria) {
        this.userFilterRecordCriteria = userFilterRecordCriteria;
    }

    /**
     * <p>
     * Getter for the field "myPricingSearchCriteria".
     * </p>
     *
     * @return the value of the field "myPricingSearchCriteria"
     */
    public MyPricingSearchCriteria getMyPricingSearchCriteria() {
        return myPricingSearchCriteria;
    }

    /**
     * <p>
     * Setter for the field "myPricingSearchCriteria".
     * </p>
     *
     * @param myPricingSearchCriteria
     *            the value of the field "myPricingSearchCriteria" to set
     */
    public void setMyPricingSearchCriteria(MyPricingSearchCriteria myPricingSearchCriteria) {
        this.myPricingSearchCriteria = myPricingSearchCriteria;
    }

    /**
     * <p>
     * Getter for the field "userFilterRecords".
     * </p>
     *
     * @return the value of the field "userFilterRecords"
     */
    public PagedResult<UserFilterRecord> getUserFilterRecords() {
        return userFilterRecords;
    }

    /**
     * <p>
     * Setter for the field "userFilterRecords".
     * </p>
     *
     * @param userFilterRecords
     *            the value of the field "userFilterRecords" to set
     */
    public void setUserFilterRecords(PagedResult<UserFilterRecord> userFilterRecords) {
        this.userFilterRecords = userFilterRecords;
    }

    /**
     * <p>
     * Getter for the field "myPricings".
     * </p>
     *
     * @return the value of the field "myPricings"
     */
    public PagedResult<MyPricing> getMyPricings() {
        return myPricings;
    }

    /**
     * <p>
     * Setter for the field "myPricings".
     * </p>
     *
     * @param myPricings
     *            the value of the field "myPricings" to set
     */
    public void setMyPricings(PagedResult<MyPricing> myPricings) {
        this.myPricings = myPricings;
    }

    /**
     * Gets the value of the field <code>filterId</code>.
     * @return the filterId
     */
    public long getFilterId() {
        return filterId;
    }

    /**
     * Sets the value of the field <code>filterId</code>.
     * @param filterId the filterId to set
     */
    public void setFilterId(long filterId) {
        this.filterId = filterId;
    }

    /**
     * Gets the value of the field <code>latestUserFilterRecord</code>.
     * @return the latestUserFilterRecord
     */
    public UserFilterRecord getLatestUserFilterRecord() {
        return latestUserFilterRecord;
    }

    /**
     * Sets the value of the field <code>latestUserFilterRecord</code>.
     * @param latestUserFilterRecord the latestUserFilterRecord to set
     */
    public void setLatestUserFilterRecord(UserFilterRecord latestUserFilterRecord) {
        this.latestUserFilterRecord = latestUserFilterRecord;
    }

    /**
     * <p>
     * Retrieves the criteriaId field.
     * </p>
     *
     * @return the value of criteriaId
     */
    public long getCriteriaId() {
        return criteriaId;
    }

    /**
     * <p>
     * Sets the value to criteriaId field.
     * </p>
     *
     * @param criteriaId the value of criteriaId to set
     */
    public void setCriteriaId(long criteriaId) {
        this.criteriaId = criteriaId;
    }

    /**
     * <p>
     * Retrieves the latestCriteriaRecord field.
     * </p>
     *
     * @return the value of latestCriteriaRecord
     */
    public UserFilterRecord getLatestCriteriaRecord() {
        return latestCriteriaRecord;
    }

    /**
     * <p>
     * Sets the value to latestCriteriaRecord field.
     * </p>
     *
     * @param latestCriteriaRecord the value of latestCriteriaRecord to set
     */
    public void setLatestCriteriaRecord(UserFilterRecord latestCriteriaRecord) {
        this.latestCriteriaRecord = latestCriteriaRecord;
    }

    /**
     * <p>
     * Retrieves the productId field.
     * </p>
     *
     * @return the value of productId
     */
    public long getProductId() {
        return productId;
    }

    /**
     * <p>
     * Sets the value to productId field.
     * </p>
     *
     * @param productId the value of productId to set
     */
    public void setProductId(long productId) {
        this.productId = productId;
    }

    /**
     * <p>
     * Retrieves the branchId field.
     * </p>
     *
     * @return the value of branchId
     */
    public long getBranchId() {
        return branchId;
    }

    /**
     * <p>
     * Sets the value to branchId field.
     * </p>
     *
     * @param branchId the value of branchId to set
     */
    public void setBranchId(long branchId) {
        this.branchId = branchId;
    }

    /**
     * <p>
     * Retrieves the relatedValueId field.
     * </p>
     *
     * @return the value of relatedValueId
     */
    public long getRelatedValueId() {
        return relatedValueId;
    }

    /**
     * <p>
     * Sets the value to relatedValueId field.
     * </p>
     *
     * @param relatedValueId the value of relatedValueId to set
     */
    public void setRelatedValueId(long relatedValueId) {
        this.relatedValueId = relatedValueId;
    }

    /**
     * <p>
     * Retrieves the hasDeleteRelatedValue field.
     * </p>
     *
     * @return the value of hasDeleteRelatedValue
     */
    public boolean isHasDeleteRelatedValue() {
        return hasDeleteRelatedValue;
    }

    /**
     * <p>
     * Sets the value to hasDeleteRelatedValue field.
     * </p>
     *
     * @param hasDeleteRelatedValue the value of hasDeleteRelatedValue to set
     */
    public void setHasDeleteRelatedValue(boolean hasDeleteRelatedValue) {
        this.hasDeleteRelatedValue = hasDeleteRelatedValue;
    }

}
