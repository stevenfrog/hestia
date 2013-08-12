/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.hestia.warroom.services;

import com.hestia.warroom.entities.BasePricingRecord;
import com.hestia.warroom.entities.CWPricingRecord;
import com.hestia.warroom.entities.PricingRecord;
import com.hestia.warroom.services.dto.BasePricingRecordSearchCriteria;
import com.hestia.warroom.services.dto.ServiceContext;

/**
 * <p>
 * This interface defines service to manage CWPricingRecord and PricingRecord. It extends GenericService.
 * </p>
 *
 * <p>
 * <strong>Thread Safety: </strong> Implementation should be thread safe after initialization.
 * </p>
 *
 * v1.0 -Hestia War Room Enhancements
 *      - added for save all in page 'Input Price'
 *
 * @author TCSASSEMLBER
 * @version 1.0
 */
public interface BasePricingRecordService extends
    GenericService<BasePricingRecord, BasePricingRecordSearchCriteria> {
    /**
     * Update the CWPricingRecord.
     *
     * @param context
     *            the context
     * @param cwPricingRecord
     *            the cwPricingRecord
     * @throws HestiaWarRoomServiceException
     *             if any error occurs
     */
    public void updateCWPricingRecord(ServiceContext context, CWPricingRecord cwPricingRecord)
        throws HestiaWarRoomServiceException;
    /**
     * Update the PricingRecord.
     *
     * @param context
     *            the context
     * @param pricingRecord
     *            the pricingRecord
     * @throws HestiaWarRoomServiceException
     *             if any error occurs
     */
    public void updatePricingRecord(ServiceContext context, PricingRecord pricingRecord)
                    throws HestiaWarRoomServiceException;
}
