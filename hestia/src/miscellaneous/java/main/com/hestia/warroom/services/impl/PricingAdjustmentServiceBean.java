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
import com.hestia.warroom.entities.PricingAdjustment;
import com.hestia.warroom.services.HestiaWarRoomServiceException;
import com.hestia.warroom.services.LocalPricingAdjustmentService;
import com.hestia.warroom.services.RemotePricingAdjustmentService;
import com.hestia.warroom.services.dto.PricingAdjustmentSearchCriteria;
import com.hestia.warroom.services.dto.ServiceContext;

/**
 * <p>
 * This bean provides services to manage pricing adjustment. It extends BaseService.
 * </p>
 *
 * <p>
 * <strong>Thread Safety: </strong> This class is not totally thread safe because it is mutable, but after
 * initialization it is immutable and then can be used safely.
 * </p>
 *
 * v1.1 - Hestia War Room Enhancements
 *      - added persistPricingAdjustmentWithBranchId method to persist pricingAdjustment only with branchId
 *
 * @author zsudraco, sparemax, TCSASSEMBLER
 * @version 1.1
 */
@Stateless
@Local(LocalPricingAdjustmentService.class)
@Remote(RemotePricingAdjustmentService.class)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class PricingAdjustmentServiceBean extends BaseService<PricingAdjustment, PricingAdjustmentSearchCriteria>
    implements LocalPricingAdjustmentService, RemotePricingAdjustmentService {
    /**
     * Creates an instance of PricingAdjustmentServiceBean.
     */
    public PricingAdjustmentServiceBean() {
        // Empty
    }

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
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public long persistPricingAdjustmentWithBranchId(ServiceContext context, PricingAdjustment pricingAdjustment)
        throws HestiaWarRoomServiceException {
        Branch branch = getEntityManager().find(Branch.class, pricingAdjustment.getBranch().getId());
        pricingAdjustment.setBranch(branch);

        long entityId;
        if(pricingAdjustment.getId() > 0){
            super.update(context, pricingAdjustment);
            entityId = pricingAdjustment.getId();
        }else{
            PricingAdjustment result = super.create(context, pricingAdjustment);
            entityId = result.getId();
        }

        return entityId;

    }
}
