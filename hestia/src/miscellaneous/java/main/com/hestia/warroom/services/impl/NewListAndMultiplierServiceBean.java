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

import com.hestia.warroom.entities.CWPricingRecord;
import com.hestia.warroom.entities.NewListAndMultiplier;
import com.hestia.warroom.services.HestiaWarRoomServiceException;
import com.hestia.warroom.services.LocalNewListAndMultiplierService;
import com.hestia.warroom.services.RemoteNewListAndMultiplierService;
import com.hestia.warroom.services.dto.NewListAndMultiplierSearchCriteria;
import com.hestia.warroom.services.dto.ServiceContext;

/**
 * <p>
 * This bean provides services to manage new list and multiplier. It extends BaseService.
 * </p>
 *
 * <p>
 * <strong>Thread Safety: </strong> This class is not totally thread safe because it is mutable, but after
 * initialization it is immutable and then can be used safely.
 * </p>
 *
 * v1.1 - Hestia War Room Enhancements
 *      - added persistNewListAndMultiplierWithRecordId method to persist newListAndMultiplier only with PricingRecordId
 * @author zsudraco, sparemax, TCSADSSEMBLER
 * @version 1.1
 */
@Stateless
@Local(LocalNewListAndMultiplierService.class)
@Remote(RemoteNewListAndMultiplierService.class)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class NewListAndMultiplierServiceBean extends
    BaseService<NewListAndMultiplier, NewListAndMultiplierSearchCriteria> implements LocalNewListAndMultiplierService,
    RemoteNewListAndMultiplierService {
    /**
     * Creates an instance of NewListAndMultiplierServiceBean.
     */
    public NewListAndMultiplierServiceBean() {
        // Empty
    }

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
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public long persistNewListAndMultiplierWithRecordId(ServiceContext context, NewListAndMultiplier newListAndMultiplier)
        throws HestiaWarRoomServiceException {
        CWPricingRecord cwPricingRecord = getEntityManager().find(CWPricingRecord.class, newListAndMultiplier.getPricingRecord().getId());
        newListAndMultiplier.setPricingRecord(cwPricingRecord);

        long entityId;
        if(newListAndMultiplier.getId() > 0){
            super.update(context, newListAndMultiplier);
            entityId = newListAndMultiplier.getId();
        }else{
            NewListAndMultiplier result = super.create(context, newListAndMultiplier);
            entityId = result.getId();
        }

        return entityId;

    }
}
