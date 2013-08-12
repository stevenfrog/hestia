/*
 * Copyright (C) 2012 TopCoder Inc., All Rights Reserved.
 */
package com.hestia.warroom.services;

import com.hestia.warroom.entities.MyPricing;
import com.hestia.warroom.services.dto.MyPricingSearchCriteria;
import com.hestia.warroom.services.dto.ServiceContext;

/**
 * <p>
 * This interface defines service to manage my pricing. It extends GenericService.
 * </p>
 *
 * <p>
 * <strong>Thread Safety: </strong> Implementation should be thread safe after initialization.
 * </p>
 *
 * v1.1 - Hestia War Room Enhancements
 *      - added persistMyPricingWithCriteriaId method to persist myPricing
 *      - added getMyPricingByCriteriaId method
 *      - added deleteValueByMyPricing to delete value
 * @author zsudraco, sparemax, TCSADSSEMBLER
 * @version 1.1
 */
public interface MyPricingService extends GenericService<MyPricing, MyPricingSearchCriteria> {
    /**
     * Persist MyPricing with criteria id.
     *
     * @param context
     *            the context
     * @param MyPricing
     *            the myPricing
     * @throws HestiaWarRoomServiceException
     *             if any error occurs
     * @since 1.1
     */
    public void persistMyPricingWithCriteriaId(ServiceContext context, MyPricing myPricing)
        throws HestiaWarRoomServiceException;

    /**
     * Get MyPricing by criteria id.
     *
     * @param context
     *            the context
     * @param criteriaId
     *            criteria id
     * @return the myPricing
     * @throws HestiaWarRoomServiceException
     *             if any error occurs
     * @since 1.1
     */
    public MyPricing getMyPricingByCriteriaId(ServiceContext context, long criteriaId)
        throws HestiaWarRoomServiceException;

    /**
     * Delete value by my pricing.
     *
     * @param context
     *            the context
     * @param criteriaId
     *            criteria id
     * @param myPricingId
     *            myPricing id
     * @return return the flag whether delete value
     * @throws HestiaWarRoomServiceException
     *             if any error occurs
     * @since 1.1
     */
    public boolean deleteValueByMyPricing(ServiceContext context, long criteriaId, long myPricingId)
                    throws HestiaWarRoomServiceException;
}
