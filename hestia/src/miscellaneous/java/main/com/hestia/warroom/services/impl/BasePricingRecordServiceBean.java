/*
 * Copyright (C) 2012 TopCoder Inc., All Rights Reserved.
 */
package com.hestia.warroom.services.impl;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.Query;

import com.hestia.warroom.entities.BasePricingRecord;
import com.hestia.warroom.entities.CWPricingRecord;
import com.hestia.warroom.entities.PricingRecord;
import com.hestia.warroom.services.HestiaWarRoomServiceException;
import com.hestia.warroom.services.LocalBasePricingRecordService;
import com.hestia.warroom.services.RemoteBasePricingRecordService;
import com.hestia.warroom.services.dto.BasePricingRecordSearchCriteria;
import com.hestia.warroom.services.dto.ServiceContext;

/**
 * <p>
 * This bean provides services to manage CWPricingRecord and PricingRecord. It extends BaseService.
 * </p>
 *
 * <p>
 * <strong>Thread Safety: </strong> This class is not totally thread safe because it is mutable, but after
 * initialization it is immutable and then can be used safely.
 * </p>
 *
 * v1.0 -Hestia War Room Enhancements - added for save all in page 'Input Price'
 *
 * @author TCSASSEMLBER
 * @version 1.0
 */
@Stateless
@Local(LocalBasePricingRecordService.class)
@Remote(RemoteBasePricingRecordService.class)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class BasePricingRecordServiceBean extends BaseService<BasePricingRecord, BasePricingRecordSearchCriteria>
    implements LocalBasePricingRecordService, RemoteBasePricingRecordService {

    /**
     * The sql string for update cw pricing record.
     */
    private static final String SQL_UPDTE_CW_PRICING_RECORD = "UPDATE CWPRICINGRECORD SET INITIALLISTPRICE = ?,"
        + " INITIALMULTIPLIER = ? WHERE ID = ?";

    /**
     * The sql string for update cw pricing record.
     */
    private static final String SQL_UPDTE_CW_PRICING_RECORD_ONLY_PRICE = "UPDATE CWPRICINGRECORD SET INITIALLISTPRICE = ?"
                    + " WHERE ID = ?";

    /**
     * The sql string for update cw pricing record.
     */
    private static final String SQL_UPDTE_CW_PRICING_RECORD_ONLY_MULTIPLIER = "UPDATE CWPRICINGRECORD SET"
                    + " INITIALMULTIPLIER = ? WHERE ID = ?";

    /**
     * The sql string for update pricing record.
     */
    private static final String SQL_UPDTE_PRICING_RECORD = "UPDATE PRICINGRECORD SET INITIALPPT = ? WHERE ID = ?";

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
    @Override
    public void updateCWPricingRecord(ServiceContext context, CWPricingRecord cwPricingRecord)
        throws HestiaWarRoomServiceException {
        Query query;
        if (cwPricingRecord.getInitialListPrice() == null) {
            query = getEntityManager().createNativeQuery(SQL_UPDTE_CW_PRICING_RECORD_ONLY_MULTIPLIER);
        } else if (cwPricingRecord.getInitialMultiplier() == null) {
            query = getEntityManager().createNativeQuery(SQL_UPDTE_CW_PRICING_RECORD_ONLY_PRICE);
        } else {
            query = getEntityManager().createNativeQuery(SQL_UPDTE_CW_PRICING_RECORD);
        }

        int i = 0;
        if (cwPricingRecord.getInitialListPrice() != null) {
            query.setParameter(++i, cwPricingRecord.getInitialListPrice().doubleValue());
        }
        if (cwPricingRecord.getInitialMultiplier() != null) {
            query.setParameter(++i, cwPricingRecord.getInitialMultiplier().doubleValue());
        }
        query.setParameter(++i, cwPricingRecord.getId());

        query.executeUpdate();

    }

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
    @Override
    public void updatePricingRecord(ServiceContext context, PricingRecord pricingRecord)
        throws HestiaWarRoomServiceException {
        Query query = getEntityManager().createNativeQuery(SQL_UPDTE_PRICING_RECORD);
        int i = 0;
        query.setParameter(++i, pricingRecord.getInitialPPT().doubleValue());
        query.setParameter(++i, pricingRecord.getId());

        query.executeUpdate();
    }

}
