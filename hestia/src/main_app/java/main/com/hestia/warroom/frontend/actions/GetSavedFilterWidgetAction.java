/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.hestia.warroom.frontend.actions;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;

import com.hestia.warroom.entities.User;
import com.hestia.warroom.entities.UserFilterRecord;
import com.hestia.warroom.services.HestiaWarRoomServiceException;
import com.hestia.warroom.services.LocalUserFilterRecordService;
import com.hestia.warroom.services.UserFilterRecordService;
import com.hestia.warroom.services.dto.PagedResult;
import com.hestia.warroom.services.dto.ServiceContext;
import com.hestia.warroom.services.dto.SortingType;
import com.hestia.warroom.services.dto.UserFilterRecordSearchCriteria;

/**
 * <p>
 * This action provides method to load saved filters for the dropdown widget.
 * </p>
 *
 * <p>
 * <strong>Thread safety:</strong> This action is mutable but it will be used effectively thread safe by Struts
 * framework.
 * </p>
 *
 * v1.1 - Hestia War Room Enhancements
 *      - added support to not show criteria filter
 *
 * @author TCSASSEMBLER
 * @version 1.1
 * @since 1.1 Hestia War Room Enhancements
 */
public class GetSavedFilterWidgetAction extends BaseAction {

    /**
     * <p>
     * The serial version uid.
     * </p>
     */
    private static final long serialVersionUID = -5318238552939162776L;


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
     * Default empty constructor.
     * </p>
     */
    public GetSavedFilterWidgetAction() {
    }

    /**
     * <p>
     * Performs execution of this action. This action will provide the userFilterRecords for the widget.
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
        ActionHelper.logEntrance(getLogger(), signature, new String[]{}, new Object[]{}, user);
        ActionHelper.checkNull(getLogger(), signature, context, "context", user);
        try {
            UserFilterRecordSearchCriteria userFilterRecordCriteria = new UserFilterRecordSearchCriteria();
            userFilterRecordCriteria.setSortingType(SortingType.DESC); // latest first
            userFilterRecordCriteria.setPageSize(0); // all
            userFilterRecordCriteria.setCriteriaFlag(false); // ignore criteria filter
            // search for records:
            userFilterRecords = userFilterRecordService.search(context, userFilterRecordCriteria);

        } catch (HestiaWarRoomServiceException e) {
            throw ActionHelper.logException(getLogger(), signature, e, user);
        }
        return ActionHelper.logExit(getLogger(), signature, new String[]{SUCCESS}, user);
    }

    /**
     * <p>
     * Performs checking if action was configured properly.
     * </p>
     *
     * @throws IllegalStateException
     *             if the action wasn't configured properly.(userFilterRecordService should be non-null)
     */
    @PostConstruct
    public void checkInit() {
        String signature = getClass().getName() + "#checkInit()";
        ActionHelper.logEntrance(getLogger(), signature, null, null, null);
        ActionHelper.checkNull(getLogger(), signature, userFilterRecordService, "userFilterRecordService", null);
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

}
