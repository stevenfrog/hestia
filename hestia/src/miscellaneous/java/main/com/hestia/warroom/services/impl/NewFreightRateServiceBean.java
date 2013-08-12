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
import com.hestia.warroom.entities.NewFreightRate;
import com.hestia.warroom.services.HestiaWarRoomServiceException;
import com.hestia.warroom.services.LocalNewFreightRateService;
import com.hestia.warroom.services.RemoteNewFreightRateService;
import com.hestia.warroom.services.dto.NewFreightRateSearchCriteria;
import com.hestia.warroom.services.dto.ServiceContext;

/**
 * <p>
 * This bean provides services to manage new freight rate. It extends BaseService.
 * </p>
 *
 * <p>
 * <strong>Thread Safety: </strong> This class is not totally thread safe because it is mutable, but after
 * initialization it is immutable and then can be used safely.
 * </p>
 *
 * v1.1 - Hestia War Room Enhancements
 *      - added persistNewFreightRateWithBranchId method to persist newFreightRate only with branchId
 * @author zsudraco, sparemax, TCSADSSEMBLER
 * @version 1.1
 */
@Stateless
@Local(LocalNewFreightRateService.class)
@Remote(RemoteNewFreightRateService.class)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class NewFreightRateServiceBean extends BaseService<NewFreightRate, NewFreightRateSearchCriteria> implements
    LocalNewFreightRateService, RemoteNewFreightRateService {
    /**
     * Creates an instance of NewFreightRateServiceBean.
     */
    public NewFreightRateServiceBean() {
        // Empty
    }

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
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public long persistNewFreightRateWithBranchId(ServiceContext context, NewFreightRate newFreightRate)
        throws HestiaWarRoomServiceException {
        Branch branch = getEntityManager().find(Branch.class, newFreightRate.getBranch().getId());
        newFreightRate.setBranch(branch);

        long entityId;
        if(newFreightRate.getId() > 0){
            super.update(context, newFreightRate);
            entityId = newFreightRate.getId();
        }else{
            NewFreightRate result = super.create(context, newFreightRate);
            entityId = result.getId();
        }

        return entityId;
    }
}
