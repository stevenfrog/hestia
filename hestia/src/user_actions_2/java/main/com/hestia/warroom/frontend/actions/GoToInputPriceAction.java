/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.hestia.warroom.frontend.actions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.hestia.warroom.entities.Branch;
import com.hestia.warroom.entities.Category;
import com.hestia.warroom.entities.Market;
import com.hestia.warroom.entities.Product;
import com.hestia.warroom.entities.ProductType;
import com.hestia.warroom.entities.UserFilterRecord;
import com.hestia.warroom.entities.Vendor;
import com.hestia.warroom.frontend.Helper;
import com.hestia.warroom.services.HestiaWarRoomServiceException;
import com.hestia.warroom.services.dto.BranchSearchCriteria;
import com.hestia.warroom.services.dto.CategorySearchCriteria;
import com.hestia.warroom.services.dto.MarketSearchCriteria;
import com.hestia.warroom.services.dto.ProductSearchCriteria;
import com.hestia.warroom.services.dto.ProductTypeSearchCriteria;
import com.hestia.warroom.services.dto.SortingType;
import com.hestia.warroom.services.dto.VendorSearchCriteria;

/**
 * <p>
 * This action is called when "Input Prices" is clicked in main navigation.
 * </p>
 *
 * <p>
 * <b>Thread Safety: </b>
 * It's mutable and not thread safe. The struts framework will guarantee that it's used in the thread safe model.
 * </p>
 *
 * <p>
 * <b>Sample configuration:</b><br>
 * Please refer {@link BaseGoToAction}
 * </p>
 *
 * v1.1 - Hestia War Room Enhancements
 *      - added support for search one single record
 *
 * @author woodjhon, DEVELOPER, TCSASSEMBLER
 * @version 1.1
 */
@SuppressWarnings("serial")
public class GoToInputPriceAction extends BaseGoToAction {
    /**
     * <p>
     * Represents the vendors from database.
     * </p>
     */
    private List<Vendor> allVendors;

    /**
     * <p>
     * Represents the branches from database.
     * </p>
     */
    private List<Branch> allBranches;

    /**
     * <p>
     * Represents the types from database.
     * </p>
     */
    private List<ProductType> allTypes;

    /**
     * <p>
     * Represents the markets from database.
     * </p>
     */
    private List<Market> allMarkets;

    /**
     * <p>
     * Represents the categories from database.
     * </p>
     */
    private List<Category> allCategories;

    /**
     * <p>
     * Represents the products from database.
     * </p>
     */
    private List<Product> allProducts;

    /**
     * <p>
     * Represents the product id.
     * </p>
     * @since 1.1 Hestia War Room Enhancements
     */
    private long productId;

    /**
     * <p>
     * Represents the branch id.
     * </p>
     * @since 1.1 Hestia War Room Enhancements
     */
    private long branchId;

    /**
     * <p>
     * Load the data from database.
     * </p>
     *
     * v1.1 - Hestia War Room Enhancements
     *      - added support for search one single record
     * @return SUCCESS to indicate that the operation was successful
     * @throws HestiaWarRoomServiceException
     *             if error occurs while searching input prices.
     */
    public String execute() throws HestiaWarRoomServiceException {
        String signature = "GoToInputPriceAction.execute()";

        Logger logger = getLogger();
        Helper.logEntrance(logger, signature, null, null);
        VendorSearchCriteria vendorSearchCriteria = new VendorSearchCriteria();
        vendorSearchCriteria.setSortingColumn("name");
        vendorSearchCriteria.setSortingType(SortingType.ASC);

        BranchSearchCriteria branchSearchCriteria = new BranchSearchCriteria();
        branchSearchCriteria.setSortingColumn("branchNumber");
        branchSearchCriteria.setSortingType(SortingType.ASC);

        ProductTypeSearchCriteria productTypeSearchCriteria = new ProductTypeSearchCriteria();
        productTypeSearchCriteria.setSortingColumn("name");
        productTypeSearchCriteria.setSortingType(SortingType.ASC);

        MarketSearchCriteria marketSearchCriteria = new MarketSearchCriteria();
        marketSearchCriteria.setSortingColumn("name");
        marketSearchCriteria.setSortingType(SortingType.ASC);

        CategorySearchCriteria categorySearchCriteria = new CategorySearchCriteria();
        categorySearchCriteria.setSortingColumn("name");
        categorySearchCriteria.setSortingType(SortingType.ASC);

        ProductSearchCriteria productSearchCriteria = new ProductSearchCriteria();
        productSearchCriteria.setSortingColumn("productCode");
        productSearchCriteria.setSortingType(SortingType.ASC);

        try {
            allVendors = getVendorService().search(getServiceContext(), vendorSearchCriteria)
                .getValues();
            allBranches = getBranchService().search(getServiceContext(), branchSearchCriteria).getValues();
            allTypes = getProductTypeService().search(getServiceContext(), productTypeSearchCriteria).getValues();
            allMarkets = getMarketService().search(getServiceContext(), marketSearchCriteria).getValues();
            allCategories = getCategoryService().search(getServiceContext(), categorySearchCriteria).getValues();
            allProducts = getProductService().search(getServiceContext(), productSearchCriteria).getValues();
            Map<String,Object> session = getSession();
            session.put("allVendors", allVendors);
            session.put("allBranches", allBranches);
            session.put("allMarkets", allMarkets);
            session.put("allCategories", allCategories);
            session.put("allProducts", allProducts);

            if (isLoadFilter()) {
                UserFilterRecord filter = getUserFilterRecordService().retrieve(getServiceContext(), getFilterId());
                if (filter == null ) {
                    throw new HestiaWarRoomServiceException("You have no access to the requested resource.");
                }

                if(productId > 0){
                    if(filter.getProducts() == null){
                        filter.setProducts(new ArrayList<Product>());
                    }
                    filter.getProducts().clear();
                    Product product = new Product();
                    product.setId(productId);
                    filter.getProducts().add(product);
                }
                if(branchId > 0){
                    if(filter.getBranches() == null){
                        filter.setBranches(new ArrayList<Branch>());
                    }
                    filter.getBranches().clear();
                    Branch branch = new Branch();
                    branch.setId(branchId);
                    filter.getBranches().add(branch);
                }

                setUserFilterRecord(filter);
            }
        } catch (HestiaWarRoomServiceException e) {
            throw Helper.logException(logger, signature, e, e.getMessage());
        }
        return SUCCESS;
    }

    /**
     * <p>
     * Gets the allVendors.
     * </p>
     *
     * @return the allVendors.
     */
    public List<Vendor> getAllVendors() {
        return allVendors;
    }

    /**
     * <p>
     * Gets the allBranches.
     * </p>
     *
     * @return the allBranches.
     */
    public List<Branch> getAllBranches() {
        return allBranches;
    }

    /**
     * <p>
     * Gets the allTypes.
     * </p>
     *
     * @return the allTypes.
     */
    public List<ProductType> getAllTypes() {
        return allTypes;
    }

    /**
     * <p>
     * Gets the allMarkets.
     * </p>
     *
     * @return the allMarkets.
     */
    public List<Market> getAllMarkets() {
        return allMarkets;
    }

    /**
     * <p>
     * Gets the allCategories.
     * </p>
     *
     * @return the allCategories.
     */
    public List<Category> getAllCategories() {
        return allCategories;
    }

    /**
     * <p>
     * Gets the allProducts.
     * </p>
     *
     * @return the allProducts.
     */
    public List<Product> getAllProducts() {
        return this.allProducts;
    }

    /**
     * <p>
     * Retrieves the productId field.
     * </p>
     *
     * @return the value of productId
     */
    public long getProductId() {
        return productId;
    }

    /**
     * <p>
     * Sets the value to productId field.
     * </p>
     *
     * @param productId the value of productId to set
     */
    public void setProductId(long productId) {
        this.productId = productId;
    }

    /**
     * <p>
     * Retrieves the branchId field.
     * </p>
     *
     * @return the value of branchId
     */
    public long getBranchId() {
        return branchId;
    }

    /**
     * <p>
     * Sets the value to branchId field.
     * </p>
     *
     * @param branchId the value of branchId to set
     */
    public void setBranchId(long branchId) {
        this.branchId = branchId;
    }

}
