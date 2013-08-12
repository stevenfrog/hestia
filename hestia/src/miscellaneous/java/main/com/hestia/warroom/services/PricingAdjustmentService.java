/*
 * Copyright (C) 2012 TopCoder Inc., All Rights Reserved.
 */
package com.hestia.warroom.services;

import com.hestia.warroom.entities.PricingAdjustment;
import com.hestia.warroom.services.dto.PricingAdjustmentSearchCriteria;
import com.hestia.warroom.services.dto.ServiceContext;

/**
 * <p>
 * This interface defines service to manage pricing adjustment. It extends GenericService.
 * </p>
 *
 * <p>
 * <strong>Thread Safety: </strong> Implementation should be thread safe after initialization.
 * </p>
 *
 * v1.1 - Hestia War Room Enhancements
 *      - added persistPricingAdjustmentWithBranchId method to persist pricingAdjustment only with branchId
 *
 * @author zsudraco, sparemax, TCSASSEMBLER
 * @version 1.1
 */
public interface PricingAdjustmentService extends
    GenericService<PricingAdjustment, PricingAdjustmentSearchCriteria> {
    /**
     * Persist pricingAdjustment just with branch id.
     *
     * @param context
     *            the context
     * @param pricingAdjustment
     *            the pricingAdjustment
     * @return the persist entity id
     * @throws HestiaWarRoomServiceException
     *             if any error occurs
     * @since 1.1
     */
    public long persistPricingAdjustmentWithBranchId(ServiceContext context, PricingAdjustment pricingAdjustment)
        throws HestiaWarRoomServiceException;
}
