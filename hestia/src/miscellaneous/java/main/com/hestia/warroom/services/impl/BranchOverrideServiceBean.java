/*
 * Copyright (C) 2012 TopCoder Inc., All Rights Reserved.
 */
package com.hestia.warroom.services.impl;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import com.hestia.warroom.entities.Branch;
import com.hestia.warroom.entities.BranchOverride;
import com.hestia.warroom.services.HestiaWarRoomServiceException;
import com.hestia.warroom.services.LocalBranchOverrideService;
import com.hestia.warroom.services.RemoteBranchOverrideService;
import com.hestia.warroom.services.dto.BranchOverrideSearchCriteria;
import com.hestia.warroom.services.dto.ServiceContext;

/**
 * <p>
 * This bean provides services to manage branch override. It extends BaseService.
 * </p>
 *
 * <p>
 * <strong>Thread Safety: </strong> This class is not totally thread safe because it is mutable, but after
 * initialization it is immutable and then can be used safely.
 * </p>
 *
 * v1.1 - Hestia War Room Enhancements
 *      - added persistBranchOverrideWithBranchId method to persist branchOverride only with branchId
 * @author zsudraco, sparemax, TCSADSSEMBLER
 * @version 1.1
 */
@Stateless
@Local(LocalBranchOverrideService.class)
@Remote(RemoteBranchOverrideService.class)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class BranchOverrideServiceBean extends BaseService<BranchOverride, BranchOverrideSearchCriteria> implements
    LocalBranchOverrideService, RemoteBranchOverrideService {
    /**
     * Creates an instance of BranchOverrideServiceBean.
     */
    public BranchOverrideServiceBean() {
        // Empty
    }

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
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public long persistBranchOverrideWithBranchId(ServiceContext context, BranchOverride branchOverride)
        throws HestiaWarRoomServiceException {
        Branch branch = getEntityManager().find(Branch.class, branchOverride.getBranch().getId());
        branchOverride.setBranch(branch);

        long entityId;
        if(branchOverride.getId() > 0){
            super.update(context, branchOverride);
            entityId = branchOverride.getId();
        }else{
            BranchOverride result = super.create(context, branchOverride);
            entityId = result.getId();
        }

        return entityId;

    }
}
