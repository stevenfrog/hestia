/*
 * Copyright (C) 2012 TopCoder Inc., All Rights Reserved.
 */
package com.hestia.warroom.services.impl;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.hestia.warroom.entities.FilterType;
import com.hestia.warroom.entities.MyPricing;
import com.hestia.warroom.entities.NewFreightRate;
import com.hestia.warroom.entities.NewListAndMultiplier;
import com.hestia.warroom.entities.NewPPT;
import com.hestia.warroom.entities.UserFilterRecord;
import com.hestia.warroom.services.HestiaWarRoomServiceException;
import com.hestia.warroom.services.LocalMyPricingService;
import com.hestia.warroom.services.RemoteMyPricingService;
import com.hestia.warroom.services.dto.MyPricingSearchCriteria;
import com.hestia.warroom.services.dto.ServiceContext;

/**
 * <p>
 * This bean provides services to manage my pricing. It extends BaseService.
 * </p>
 *
 * <p>
 * <strong>Thread Safety: </strong> This class is not totally thread safe because it is mutable, but after
 * initialization it is immutable and then can be used safely.
 * </p>
 *
 * v1.1 - Hestia War Room Enhancements
 *      - added persistMyPricingWithCriteriaId method to persist myPricing
 *      - added getMyPricingByCriteriaId method
 *      - added deleteValueByMyPricing to delete value
 *
 * @author zsudraco, sparemax, TCSADSSEMBLER
 * @version 1.1
 */
@Stateless
@Local(LocalMyPricingService.class)
@Remote(RemoteMyPricingService.class)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class MyPricingServiceBean extends BaseService<MyPricing, MyPricingSearchCriteria> implements
    LocalMyPricingService, RemoteMyPricingService {
    /**
     * <p>
     * Represents the class name.
     * </p>
     */
    private final String CLASSNAME = MyPricingServiceBean.class.getName();

    /**
     * The sql string for insert record.
     */
    private static final String SQL_INSERT = "INSERT INTO MYPRICING(ID, NAME, PROGRESS, CREATION_USER, CREATION_DATE,"
        + " MODIFICATION_USER, MODIFICATION_DATE, STATUS, PRICERECORD, CWPRICERECORD, CRITERIA, BRANCHID, RELATEDVALUEID)"
        + " VALUES(MYPRICINGSEQ.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    /**
     * The sql string for get record by criteria id.
     */
    private static final String SQL_GET_BY_CRITERIAID = "select e from MyPricing e where e.criteria.id = :criteriaId";

    /**
     * Creates an instance of MyPricingServiceBean.
     */
    public MyPricingServiceBean() {
        // Empty
    }

    /**
     * Persist MyPricing with criteria id.
     *
     * @param context
     *            the context
     * @param MyPricing
     *            the myPricing
     * @throws HestiaWarRoomServiceException
     *             if any error occurs
     * @since 1.1
     */
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void persistMyPricingWithCriteriaId(ServiceContext context, MyPricing myPricing)
        throws HestiaWarRoomServiceException {
        String signature = CLASSNAME
            + ".persistMyPricingWithCriteriaId(ServiceContext context, MyPricing myPricing)";
        Logger logger = getLogger();

        // Log entry
        Helper.logEntrance(logger, signature, new String[] {"context", "myPricing"}, new Object[] {context,
            myPricing});

        // I use native query, because I always get error when all program deploy to JBoss
        Query query = getEntityManager().createNativeQuery(SQL_INSERT);
        int i = 0;
        query.setParameter(++i, myPricing.getName());
        query.setParameter(++i, myPricing.getProgress());
        query.setParameter(++i, myPricing.getCreatedBy());
        query.setParameter(++i, myPricing.getCreatedOn());
        query.setParameter(++i, myPricing.getUpdatedBy());
        query.setParameter(++i, myPricing.getUpdatedOn());
        query.setParameter(++i, myPricing.getStatus().ordinal());
        if(myPricing.getPricingRecord() == null){
            query.setParameter(++i, null);
        }else{
            query.setParameter(++i, myPricing.getPricingRecord().getId());
        }
        if(myPricing.getCwPricingRecord() == null){
            query.setParameter(++i, null);
        }else{
            query.setParameter(++i, myPricing.getCwPricingRecord().getId());
        }
        query.setParameter(++i, myPricing.getCriteria().getId());
        query.setParameter(++i, myPricing.getBranchId());
        query.setParameter(++i, myPricing.getRelatedValueId());

        query.executeUpdate();

        // Log exit
        Helper.logExit(logger, signature, null);
    }

    /**
     * Get MyPricing by criteria id.
     *
     * @param context
     *            the context
     * @param criteriaId
     *            criteria id
     * @return the myPricing
     * @throws HestiaWarRoomServiceException
     *             if any error occurs
     * @since 1.1
     */
    @SuppressWarnings("unchecked")
    @Override
    public MyPricing getMyPricingByCriteriaId(ServiceContext context, long criteriaId)
        throws HestiaWarRoomServiceException {
        String signature = CLASSNAME + ".getMyPricingByCriteriaId(ServiceContext context, long criteriaId)";
        Logger logger = getLogger();

        // Log entry
        Helper.logEntrance(logger, signature, new String[] {"context", "criteriaId"}, new Object[] {context,
            criteriaId});

        Query query = getEntityManager().createQuery(SQL_GET_BY_CRITERIAID);

        query.setParameter("criteriaId", criteriaId);

        // Query only 1 record
        query.setMaxResults(1);
        List<MyPricing> list = query.getResultList();

        MyPricing result = list.isEmpty() ? null : list.get(0);

        // Log exit
        Helper.logExit(logger, signature, new Object[] {result});
        return result;
    }

    /**
     * Delete value by my pricing.
     *
     * @param context
     *            the context
     * @param criteriaId
     *            criteria id
     * @param relatedValueId
     *            relatedValueId
     * @return return the flag whether delete value
     * @throws HestiaWarRoomServiceException
     *             if any error occurs
     * @since 1.1
     */
    @Override
    public boolean deleteValueByMyPricing(ServiceContext context, long criteriaId, long relatedValueId)
                    throws HestiaWarRoomServiceException {
        String signature = CLASSNAME + ".deleteValueByMyPricing(ServiceContext context, long criteriaId, long relatedValueId)";
        Logger logger = getLogger();

        // Log entry
        Helper.logEntrance(logger, signature, new String[] {"context", "criteriaId", "relatedValueId"}, new Object[] {context,
            criteriaId, relatedValueId});

        boolean result = false;
        UserFilterRecord userFilterRecord = getEntityManager().find(UserFilterRecord.class, criteriaId);
        MyPricing myPricing = getMyPricingByCriteriaId(context, criteriaId);

        if(userFilterRecord.getType() == FilterType.FREIGHT_FILTER){
            NewFreightRate newNewFreightRate  = getEntityManager().find(NewFreightRate.class, relatedValueId);
            if(newNewFreightRate != null){
                getEntityManager().remove(newNewFreightRate);
                result = true;
            }
        }else if(userFilterRecord.getType() == FilterType.INPUT_PRICE_FILTER){
            if(myPricing.getCwPricingRecord() != null){
                NewListAndMultiplier newListAndMultiplier = getEntityManager().find(NewListAndMultiplier.class, relatedValueId);
                if(newListAndMultiplier != null){
                    getEntityManager().remove(newListAndMultiplier);
                    result = true;
                }
            }else if(myPricing.getPricingRecord() != null){
                NewPPT newPPT = getEntityManager().find(NewPPT.class, relatedValueId);
                if(newPPT != null){
                    getEntityManager().remove(newPPT);
                    result = true;
                }
            }
        }

        // Log exit
        Helper.logExit(logger, signature, new Object[] {result});
        return result;
    }

    /**
     * This is used to get where clause used in the search method.
     *
     * @param criteria
     *            the search criteria, it is not null
     *
     * @return the where clause, not null, may be empty
     */
    @Override
    protected String getWhereClause(MyPricingSearchCriteria criteria) {
        String where = "where 1=1";
        if (criteria.getOwnername() != null) {
            where += " and e.createdBy = :ownername";
        }
        return where;
    }

    /**
     * This is used to set query parameters used in the search method.
     *
     * @param query
     *            the query, it is not null
     * @param criteria
     *            the search criteria, it is not null
     */
    @Override
    protected void setQueryParameters(Query query, MyPricingSearchCriteria criteria) {
        String ownername = criteria.getOwnername();
        if (ownername != null && !ownername.isEmpty()) {
            query.setParameter("ownername", ownername);
        }
    }
}
