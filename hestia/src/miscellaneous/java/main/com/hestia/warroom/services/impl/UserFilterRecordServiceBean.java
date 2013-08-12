/*
 * Copyright (C) 2012 TopCoder Inc., All Rights Reserved.
 */
package com.hestia.warroom.services.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.hestia.warroom.entities.Branch;
import com.hestia.warroom.entities.Category;
import com.hestia.warroom.entities.Market;
import com.hestia.warroom.entities.Product;
import com.hestia.warroom.entities.ProductType;
import com.hestia.warroom.entities.ShipPoint;
import com.hestia.warroom.entities.User;
import com.hestia.warroom.entities.UserFilterRecord;
import com.hestia.warroom.entities.Vendor;
import com.hestia.warroom.services.HestiaWarRoomNonUserException;
import com.hestia.warroom.services.HestiaWarRoomPersistenceException;
import com.hestia.warroom.services.HestiaWarRoomServiceException;
import com.hestia.warroom.services.LocalUserFilterRecordService;
import com.hestia.warroom.services.RemoteUserFilterRecordService;
import com.hestia.warroom.services.dto.PagedResult;
import com.hestia.warroom.services.dto.ServiceContext;
import com.hestia.warroom.services.dto.UserFilterRecordSearchCriteria;

/**
 * <p>
 * This bean provides services to manage user filter record. It extends BaseService. It adds a method to
 * retrieve last saved user filter record.
 * </p>
 *
 * <p>
 * <strong>Thread Safety: </strong> This class is not totally thread safe because it is mutable, but after
 * initialization it is immutable and then can be used safely.
 * </p>
 *
 * v1.1 - Export and Search Filter - initial integration fixes v1.2 - Hestia War Room Enhancements - every one
 * can search all user's UserFilterRecord - add support to isCriteria
 *
 * @author zsudraco, sparemax, stevenfrog, TCSASSEMBLER
 * @version 1.2
 */
@Stateless
@Local(LocalUserFilterRecordService.class)
@Remote(RemoteUserFilterRecordService.class)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class UserFilterRecordServiceBean extends BaseService<UserFilterRecord, UserFilterRecordSearchCriteria>
    implements LocalUserFilterRecordService, RemoteUserFilterRecordService {

    /**
     * The sql string for retrieve last saved record.
     */
    private static final String SQL_RETRIVE_LAST_SAVED_RECORD = "SELECT e FROM UserFilterRecord e"
        + " where e.owner.id = :owner and e.criteriaFlag = :isCriteria ORDER BY e.updatedOn DESC";

    /**
     * <p>
     * Represents the class name.
     * </p>
     */
    private final String className = getClass().getName();

    /**
     * Creates an instance of UserFilterRecordServiceBean.
     */
    public UserFilterRecordServiceBean() {
        // Empty
    }

    /**
     * This method retrieves the last saved user filter record. Null is returned if none is found.
     *
     * @param context
     *            the service context
     *
     * @return the last saved user filter record, or null if none is found.
     *
     * @throws IllegalArgumentException
     *             if context is null
     * @throws HestiaWarRoomNonUserException
     *             if context.user is null
     * @throws HestiaWarRoomPersistenceException
     *             if there is persistence error
     */
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public UserFilterRecord retrieveLastSavedUserFilter(ServiceContext context)
        throws HestiaWarRoomNonUserException, HestiaWarRoomPersistenceException {
        String signature = className + ".retrieveLastSavedUserFilter(ServiceContext context)";
        Logger logger = getLogger();

        return retriveLastSavedRecord(context, signature, logger, false);
    }

    /**
     * This method creates the entity, and return created entity.
     *
     * @param context
     *            the service context
     * @param value
     *            the entity to create
     *
     * @return the created entity
     *
     * @throws IllegalArgumentException
     *             if context or value is null
     * @throws HestiaWarRoomNonUserException
     *             if context.user is null
     * @throws HestiaWarRoomPersistenceException
     *             if there is persistence error
     */
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public UserFilterRecord create(ServiceContext context, UserFilterRecord value)
        throws HestiaWarRoomNonUserException, HestiaWarRoomPersistenceException {
        String signature = className + ".create(ServiceContext context, UserFilterRecord value)";
        Logger logger = getLogger();

        // Log entry
        Helper.logEntrance(logger, signature, new String[] {"context", "value"}, new Object[] {context, value});

        Helper.checkNull(logger, signature, context, "context");
        Helper.checkNull(logger, signature, value, "value");

        UserFilterRecordSearchCriteria criteria = new UserFilterRecordSearchCriteria();
        criteria.setName(value.getName());
        criteria.setOwner(context.getUser());

        PagedResult<UserFilterRecord> results = search(context, criteria);
        if (results != null && results.getTotal() > 0) {
            throw Helper.logException(logger, signature, new HestiaWarRoomPersistenceException(
                "Filter name already exists."));

        }

        value.setOwner(context.getUser());
        // find each entity
        if (value.getBranches() != null) {
            List<Branch> branches = new ArrayList<Branch>();
            for (Branch branch : value.getBranches()) {
                branches.add(getEntityManager().find(Branch.class, branch.getId()));
            }
            value.setBranches(branches);
        }
        if (value.getCategories() != null) {
            List<Category> categories = new ArrayList<Category>();
            for (Category category : value.getCategories()) {
                categories.add(getEntityManager().find(Category.class, category.getId()));
            }
            value.setCategories(categories);
        }
        if (value.getMarkets() != null) {
            List<Market> markets = new ArrayList<Market>();
            for (Market market : value.getMarkets()) {
                markets.add(getEntityManager().find(Market.class, market.getId()));
            }
            value.setMarkets(markets);
        }
        if (value.getProducts() != null) {
            List<Product> products = new ArrayList<Product>();
            for (Product product : value.getProducts()) {
                products.add(getEntityManager().find(Product.class, product.getId()));
            }
            value.setProducts(products);
        }
        if (value.getShipPoints() != null) {
            List<ShipPoint> shippoints = new ArrayList<ShipPoint>();
            for (ShipPoint shippoint : value.getShipPoints()) {
                shippoints.add(getEntityManager().find(ShipPoint.class, shippoint.getId()));
            }
            value.setShipPoints(shippoints);
        }
        if (value.getVendors() != null) {
            List<Vendor> vendors = new ArrayList<Vendor>();
            for (Vendor vendor : value.getVendors()) {
                vendors.add(getEntityManager().find(Vendor.class, vendor.getId()));
            }
            value.setVendors(vendors);
        }
        if (value.getProductTypes() != null) {
            List<ProductType> productTypes = new ArrayList<ProductType>();
            for (ProductType productType : value.getProductTypes()) {
                productTypes.add(getEntityManager().find(ProductType.class, productType.getId()));
            }
            value.setProductTypes(productTypes);
        }

        // Delegate to super class method
        UserFilterRecord newRecord = super.create(context, value);

        // Log exit
        Helper.logExit(logger, signature, new Object[] {newRecord});
        return newRecord;
    }

    /**
     * This method searches entities with pagination support, and return created entity. If page number is 0,
     * then all records are returned in a single page. Returned value is not null.
     *
     * @param context
     *            the service context
     * @param criteria
     *            the search criteria
     *
     * @return the paged result
     *
     * @throws IllegalArgumentException
     *             if context or criteria.sortingType is null, criteria.sortingColumn is null/empty or
     *             criteria.page &lt; 0, or (criteria.pageSize &lt; 1 and criteria.page &gt; 0)
     * @throws HestiaWarRoomPersistenceException
     *             if there is persistence error
     * @throws HestiaWarRoomNonUserException
     *             if context.user is null
     */
    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public PagedResult<UserFilterRecord> search(ServiceContext context, UserFilterRecordSearchCriteria criteria)
        throws HestiaWarRoomNonUserException, HestiaWarRoomPersistenceException {
        String signature = className + ".search(ServiceContext context, K criteria)";

        // Log entry
        Helper.logEntrance(getLogger(), signature, new String[] {"context", "criteria"}, new Object[] {context,
            criteria});

        PagedResult<UserFilterRecord> results = super.search(context, criteria);

        List<UserFilterRecord> values = results.getValues();
        for (UserFilterRecord record : values) {
            String msg = "UserFilterRecord [" + record.getId() + "] Name: " + record.getName();
            getLogger().debug(msg);
        }
        Helper.logExit(getLogger(), signature, new Object[] {results});
        return results;
    }

    /**
     * This is used to get where clause used in the search method. Change in 1.1: search against username,
     * firstName, lastName.
     *
     * @param criteria
     *            the search criteria, it is not null
     *
     * @return the where clause, not null, may be empty
     */
    @Override
    protected String getWhereClause(UserFilterRecordSearchCriteria criteria) {
        String where = "where 1=1";
        if (criteria.getOwner() != null) {
            where += " and e.owner.id = :owner";
        }
        if (criteria.getName() != null && !criteria.getName().isEmpty()) {
            where += " and e.name = :name";
        }
        if (criteria.getCriteriaFlag() != null) {
            where += " and e.criteriaFlag = :criteriaFlag";
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
    protected void setQueryParameters(Query query, UserFilterRecordSearchCriteria criteria) {
        String name = criteria.getName();
        if (name != null && !criteria.getName().isEmpty()) {
            query.setParameter("name", name);
        }
        User owner = criteria.getOwner();
        if (owner != null) {
            query.setParameter("owner", owner.getId());
        }
        Boolean criteriaFlag = criteria.getCriteriaFlag();
        if (criteriaFlag != null) {
            query.setParameter("criteriaFlag", criteriaFlag);
        }
    }

    /**
     * This method retrieves the last saved criteria record. Null is returned if none is found.
     *
     * @param context
     *            the service context
     *
     * @return the last saved criteria record, or null if none is found.
     *
     * @throws IllegalArgumentException
     *             if context is null
     * @throws HestiaWarRoomNonUserException
     *             if context.user is null
     * @throws HestiaWarRoomPersistenceException
     *             if there is persistence error
     * @throws HestiaWarRoomServiceException
     *             if there is other error
     * @since 1.2 Hestia War Room Enhancements
     */
    public UserFilterRecord retrieveLastSavedCriteria(ServiceContext context) throws HestiaWarRoomServiceException {
        String signature = className + ".retrieveLastSavedCriteria(ServiceContext context)";
        Logger logger = getLogger();

        return retriveLastSavedRecord(context, signature, logger, true);
    }

    /**
     * Get the last saved record.
     *
     * @param context
     *            the context
     * @param signature
     *            the signature
     * @param logger
     *            the logger
     * @return the last saved record, or null if none is found.
     * @throws HestiaWarRoomNonUserException
     * @throws HestiaWarRoomNonUserException
     *             if context.user is null
     * @throws HestiaWarRoomPersistenceException
     *             if there is persistence error
     * @throws HestiaWarRoomServiceException
     *             if there is other error
     */
    @SuppressWarnings("unchecked")
    private UserFilterRecord retriveLastSavedRecord(ServiceContext context, String signature, Logger logger,
        boolean isCriteria) throws HestiaWarRoomNonUserException, HestiaWarRoomPersistenceException {
        // Log entry
        Helper.logEntrance(logger, signature, new String[] {"context"}, new Object[] {context});

        Helper.checkNull(logger, signature, context, "context");

        try {
            Helper.getUser(logger, signature, context);

            Query query = getEntityManager().createQuery(SQL_RETRIVE_LAST_SAVED_RECORD);

            query.setParameter("isCriteria", isCriteria);
            query.setParameter("owner", context.getUser().getId());

            // Query only 1 record
            query.setMaxResults(1);
            List<UserFilterRecord> list = query.getResultList();

            UserFilterRecord result = list.isEmpty() ? null : list.get(0);

            // Log exit
            Helper.logExit(logger, signature, new Object[] {result});
            return result;

        } catch (IllegalStateException e) {
            // Log exception
            throw Helper.logException(logger, signature, new HestiaWarRoomPersistenceException(
                "The entity manager has been closed.", e));
        } catch (PersistenceException e) {
            // Log exception
            throw Helper.logException(logger, signature, new HestiaWarRoomPersistenceException(
                "An error has occurred while accessing the persistence.", e));
        }
    }
}
