/*
 * Copyright (C) 2012 TopCoder Inc., All Rights Reserved.
 */
package com.hestia.warroom.services;

import com.hestia.warroom.entities.BranchOverride;
import com.hestia.warroom.services.dto.BranchOverrideSearchCriteria;
import com.hestia.warroom.services.dto.ServiceContext;

/**
 * <p>
 * This interface defines service to manage branch override. It extends GenericService.
 * </p>
 *
 * <p>
 * <strong>Thread Safety: </strong> Implementation should be thread safe after initialization.
 * </p>
 *
 * v1.1 - Hestia War Room Enhancements
 *      - added persistBranchOverrideWithBranchId method to persist branchOverride only with branchId
 * @author zsudraco, sparemax, TCSADSSEMBLER
 * @version 1.1
 */
public interface BranchOverrideService extends GenericService<BranchOverride, BranchOverrideSearchCriteria> {
    /**
     * Persist BranchOverride just with branch id.
     *
     * @param context
     *            the context
     * @param branchOverride
     *            the branch override
     * @return the persist entity id
     * @throws HestiaWarRoomServiceException
     *             if any error occurs
     * @since 1.1
     */
    public long persistBranchOverrideWithBranchId(ServiceContext context, BranchOverride branchOverride)
        throws HestiaWarRoomServiceException;
}
