/*
 * Copyright (C) 2012 TopCoder Inc., All Rights Reserved.
 */
package com.hestia.warroom.services;

import com.hestia.warroom.entities.NewListAndMultiplier;
import com.hestia.warroom.services.dto.NewListAndMultiplierSearchCriteria;
import com.hestia.warroom.services.dto.ServiceContext;

/**
 * <p>
 * This interface defines service to manage new list and multiplier. It extends GenericService.
 * </p>
 *
 * <p>
 * <strong>Thread Safety: </strong> Implementation should be thread safe after initialization.
 * </p>
 *
 * v1.1 - Hestia War Room Enhancements
 *      - added persistNewListAndMultiplierWithRecordId method to persist newListAndMultiplier only with PricingRecordId
 * @author zsudraco, sparemax, TCSADSSEMBLER
 * @version 1.1
 */
public interface NewListAndMultiplierService extends
    GenericService<NewListAndMultiplier, NewListAndMultiplierSearchCriteria> {
    /**
     * Persist NewListAndMultiplier just with record id.
     *
     * @param context
     *            the context
     * @param newListAndMultiplier
     *            the newListAndMultiplier
     * @return the persist entity id
     * @throws HestiaWarRoomServiceException
     *             if any error occurs
     * @since 1.1
     */
    public long persistNewListAndMultiplierWithRecordId(ServiceContext context, NewListAndMultiplier newListAndMultiplier)
        throws HestiaWarRoomServiceException;
}
