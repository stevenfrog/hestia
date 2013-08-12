/*
 * Copyright (C) 2012 TopCoder Inc., All Rights Reserved.
 */
package com.hestia.warroom.services;

import com.hestia.warroom.entities.NewFreightRate;
import com.hestia.warroom.services.dto.NewFreightRateSearchCriteria;
import com.hestia.warroom.services.dto.ServiceContext;

/**
 * <p>
 * This interface defines service to manage new freight rate. It extends GenericService.
 * </p>
 *
 * <p>
 * <strong>Thread Safety: </strong> Implementation should be thread safe after initialization.
 * </p>
 *
 * v1.1 - Hestia War Room Enhancements
 *      - added persistNewFreightRateWithBranchId method to persist newFreightRate only with branchId
 * @author zsudraco, sparemax, TCSADSSEMBLER
 * @version 1.1
 */
public interface NewFreightRateService extends GenericService<NewFreightRate, NewFreightRateSearchCriteria> {
    /**
     * Persist NewFreightRate just with branch id.
     *
     * @param context
     *            the context
     * @param newFreightRate
     *            the newFreightRate
     * @return the persist entity id
     * @throws HestiaWarRoomServiceException
     *             if any error occurs
     * @since 1.1
     */
    public long persistNewFreightRateWithBranchId(ServiceContext context, NewFreightRate newFreightRate)
        throws HestiaWarRoomServiceException;
}
