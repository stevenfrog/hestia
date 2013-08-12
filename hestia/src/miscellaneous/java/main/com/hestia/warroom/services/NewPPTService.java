/*
 * Copyright (C) 2012 TopCoder Inc., All Rights Reserved.
 */
package com.hestia.warroom.services;

import com.hestia.warroom.entities.NewPPT;
import com.hestia.warroom.services.dto.NewPPTSearchCriteria;
import com.hestia.warroom.services.dto.ServiceContext;

/**
 * <p>
 * This interface defines service to manage new PPT. It extends GenericService.
 * </p>
 *
 * <p>
 * <strong>Thread Safety: </strong> Implementation should be thread safe after initialization.
 * </p>
 *
 * v1.1 - Hestia War Room Enhancements - added persistNewPPTWithRecordId method to persist newPPT only with
 * PricingRecordId
 *
 * @author zsudraco, sparemax, TCSADSSEMBLER
 * @version 1.1
 */
public interface NewPPTService extends GenericService<NewPPT, NewPPTSearchCriteria> {
    /**
     * Persist NewPPT just with record id.
     *
     * @param context
     *            the context
     * @param newPPT
     *            the newPPT
     * @return the persist entity id
     * @throws HestiaWarRoomServiceException
     *             if any error occurs
     * @since 1.1 Hestia War Room Enhancements
     */
    public long persistNewPPTWithRecordId(ServiceContext context, NewPPT newPPT)
        throws HestiaWarRoomServiceException;
}
