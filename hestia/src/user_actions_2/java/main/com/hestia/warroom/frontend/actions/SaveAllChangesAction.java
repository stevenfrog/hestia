/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.hestia.warroom.frontend.actions;

import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;

import com.hestia.warroom.entities.MyPricing;
import com.hestia.warroom.entities.User;
import com.hestia.warroom.frontend.Helper;
import com.hestia.warroom.services.HestiaWarRoomServiceException;
import com.hestia.warroom.services.LocalMyPricingService;
import com.hestia.warroom.services.MyPricingService;
import com.hestia.warroom.services.dto.ServiceContext;

/**
 * <p>
 * This action is called when "Save All" button is clicked while editing the pricing.
 * </p>
 *
 * <p>
 * <strong>Thread Safety: </strong> It's mutable and not thread safe. The struts framework will guarantee that
 * it's used in the thread safe model.
 * </p>
 *
 * v1.1 - Hestia War Room Enhancements
 *      - added saveMyPricing to save pricing
 *
 * @author woodjhon, TCSDEVELOPER, TCSASSEMBLER
 * @version 1.1
 */
public class SaveAllChangesAction extends BaseAction {
    /**
     * <p>
     * The serial version uid.
     * </p>
     */
    private static final long serialVersionUID = -5476359364115369032L;

    /**
     * <p>
     * Represents the class name used in logging.
     * </p>
     */
    private static final String CLASS_NAME = SaveAllChangesAction.class.getName();

    /**
     * <p>
     * The myPricingService used to represents the my pricing management service.
     * </p>
     * <p>
     * It's injected by Spring IOC and mutable. It can't be null after initialization.
     * </p>
     * @since Hestia War Room Enhancements
     */
//    @EJB(name="MyPricingServiceBean", beanInterface=LocalMyPricingService.class)
    private MyPricingService myPricingService;

    /**
     * Create the instance.
     */
    public SaveAllChangesAction() {
    }

    /**
     * Check that the required fields are injected.(for this component all the injected fields are required.
     *
     * @throws com.hestia.warroom.services.HestiaWarRoomServiceConfigurationException is thrown if
     *  myPricingService is missing
     */
    @PostConstruct
    protected void checkInit() {
        String signature = "SaveAllChangesAction#checkInit";
        Action2BHelper.checkNull(getLogger(), signature , myPricingService, "myPricingService", getCurrentUser());
    }

    /**
     * This action is called when "Save All" button is clicked while editing the pricing.
     * It saves all changes to the pricing, currently stored in the session, into persistence and removes all these
     * changes from the session.
     * Call MyPricingService#update method for each affected pricing (i.e. those stored in the session).
     *
     * @return The action execution result.
     * @throws HestiaWarRoomServiceException if any error occurs
     */
    public String execute() throws HestiaWarRoomServiceException {
        String signature = getClass().getName() + "#execute";
        User user = getCurrentUser();
        Action2BHelper.logEntrance(getLogger(), signature, new String[]{},
                new Object[]{}, user);
        try {
            ServiceContext context = getServiceContext();
            Map<String, Object> session = getSession();
            String changedPricingPrefix = Action2BHelper.getChangedPricingPrefix();
            Set<String> keySet = session.keySet();
            for (String key : keySet) {
                // get all the MyPricing entities in the session whose key begins with the prefix changedpricingPrefix
                // then call myPricingService.update(context, myPricing) to update each entity.
                if (key.startsWith(changedPricingPrefix)) {
                    myPricingService.update(context, (MyPricing) session.get(key));
                }
            }
        } catch (HestiaWarRoomServiceException e) {
            throw Action2BHelper.logException(getLogger(), signature, e, user);
        }
        return SUCCESS;
    }

    /**
     * Get my pricing service.
     *
     * @return the MyPricingService
     */
    public MyPricingService getMyPricingService() {
        return myPricingService;
    }

    /**
     * Set my pricing service.
     *
     * @param myPricingService the myPricingService to set
     */
    public void setMyPricingService(MyPricingService myPricingService) {
        this.myPricingService = myPricingService;
    }
}

