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

import com.hestia.warroom.entities.NewPPT;
import com.hestia.warroom.entities.PricingRecord;
import com.hestia.warroom.services.HestiaWarRoomServiceException;
import com.hestia.warroom.services.LocalNewPPTService;
import com.hestia.warroom.services.RemoteNewPPTService;
import com.hestia.warroom.services.dto.NewPPTSearchCriteria;
import com.hestia.warroom.services.dto.ServiceContext;

/**
 * <p>
 * This bean provides services to manage new PPT. It extends BaseService.
 * </p>
 *
 * <p>
 * <strong>Thread Safety: </strong> This class is not totally thread safe because it is mutable, but after
 * initialization it is immutable and then can be used safely.
 * </p>
 *
 * v1.1 - Hestia War Room Enhancements
 *      - added persistNewPPTWithRecordId method to persist newPPT only with PricingRecordId
 * @author zsudraco, sparemax, TCSADSSEMBLER
 * @version 1.1
 */
@Stateless
@Local(LocalNewPPTService.class)
@Remote(RemoteNewPPTService.class)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class NewPPTServiceBean extends BaseService<NewPPT, NewPPTSearchCriteria> implements LocalNewPPTService,
    RemoteNewPPTService {
    /**
     * Creates an instance of NewPPTServiceBean.
     */
    public NewPPTServiceBean() {
        // Empty
    }

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
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public long persistNewPPTWithRecordId(ServiceContext context, NewPPT newPPT)
        throws HestiaWarRoomServiceException {
        PricingRecord pricingRecord = getEntityManager().find(PricingRecord.class, newPPT.getPricingRecord().getId());
        newPPT.setPricingRecord(pricingRecord);

        long entityId;
        if(newPPT.getId() > 0){
            super.update(context, newPPT);
            entityId = newPPT.getId();
        }else{
            NewPPT result = super.create(context, newPPT);
            entityId = result.getId();
        }

        return entityId;

    }
}
