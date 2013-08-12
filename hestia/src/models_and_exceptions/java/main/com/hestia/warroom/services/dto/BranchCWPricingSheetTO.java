/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.hestia.warroom.services.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * This performs the branch optimum pricing calculations.
 *
 * v1.1 - Adjustments Freight and Branch Overrides
 *      - added some fields to support for page Adjustments, Freight and Branch Overrides
 *      - add field for lowest net price
 * v1.2 - Hestia War Room Enhancements
 *      - added field for newPricingAdjustment
 * @author stevenfrog
 * @since Price Calculations Assembly
 * @version 1.2
 */
public class BranchCWPricingSheetTO implements Serializable {

    /**
     * Serial ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * All adjustments.
     */
    private ProductPriceRecordDTO adjustments;

    /**
     * Pricing record identifier.
     * @since Adjustments Freight and Branch Overrides
     */
    private long pricingRecordId;

    /**
     * Flag for lowest net per product id.
     * @since Adjustments Freight and Branch Overrides
     */
    private boolean lowestNet = false;

    /**
     * Final list price.
     * @since Adjustments Freight and Branch Overrides
     */
    private String finalListPrice;

    /**
     * Initial multiplier.
     * @since Adjustments Freight and Branch Overrides
     */
    private String finalMultiplier;

    /**
     * Product identifier.
     */
    private long productId;

    /**
     * Final calculated price.
     */
    private String finalPrice;

    /**
     * Initial list price.
     */
    private String initialListPrice;

    /**
     * Initial multiplier.
     */
    private String initialMultiplier;

    /**
     * Ship point.
     */
    private String shipPoint;

    /**
     * Category.
     */
    private String category;

    /**
     * Product code.
     */
    private String productCode;

    /**
     * Description.
     */
    private String description;

    /**
     * New list price.
     */
    private String newListPrice;

    /**
     * New multiplier.
     */
    private String newMultiplier;

    /**
     * New net price.
     */
    private String newNetPrice;

    /**
     * Initial net price.
     */
    private String initialNetPrice;

    /**
     * Pricing adjustment.
     */
    private String pricingAdjustment;

    /**
     * Start date of pricing adjustment.
     */
    private Date pricingAdjustmentStartDate;

    /**
     * End date of pricing adjustment.
     */
    private Date pricingAdjustmentEndDate;

    /**
     * Comment of pricing adjustment.
     */
    private String pricingAdjustmentComment;

    /**
     * Create user of pricing adjustment.
     */
    private String pricingAdjustmentCreatedBy;

    /**
     * New Pricing adjustment.
     * @since 1.2 Hestia War Room Enhancements
     */
    private String newPricingAdjustment;

    /**
     * Start date of new list price.
     * @since 1.2 Hestia War Room Enhancements
     */
    private Date newPricingAdjustmentStartDate;

    /**
     * End date of new list price.
     * @since 1.2 Hestia War Room Enhancements
     */
    private Date newPricingAdjustmentEndDate;

    /**
     * Comment of new list price.
     * @since 1.2 Hestia War Room Enhancements
     */
    private String newPricingAdjustmentComment;

    /**
     * Create user of new list price.
     * @since 1.2 Hestia War Room Enhancements
     */
    private String newPricingAdjustmentCreatedBy;

    /**
     * Price before freight.
     */
    private String beforeFreightPrice;

    /**
     * Freight rate.
     */
    private String freightRate;

    /**
     * Freight adjustment.
     */
    private String freightAdjustment;

    /**
     * Total price.
     */
    private String totalPrice;

    /**
     * Branch override.
     */
    private String branchOverride;

    /**
     * New Branch override.
     * @since Adjustments Freight and Branch Overrides
     */
    private String newBranchOverride;

    /**
     * Start date of new branch override.
     * @since Adjustments Freight and Branch Overrides
     */
    private Date newBranchOverrideStartDate;

    /**
     * End date of new branch override.
     * @since Adjustments Freight and Branch Overrides
     */
    private Date newBranchOverrideEndDate;

    /**
     * Comment of new branch override.
     * @since Adjustments Freight and Branch Overrides
     */
    private String newBranchOverrideComment;

    /**
     * Create user of new branch override.
     * @since Adjustments Freight and Branch Overrides
     */
    private String newBranchOverrideCreatedBy;

    /**
     * Weight per foot.
     */
    private String weightPerFoot;

    /**
     * The freight cost.
     */
    private String freightCost;

    /**
     * The new freight cost.
     */
    private String newFreightCost;

    /**
     * Start date of new freight cost.
     * @since Adjustments Freight and Branch Overrides
     */
    private Date newFreightCostStartDate;

    /**
     * End date of new freight cost.
     * @since Adjustments Freight and Branch Overrides
     */
    private Date newFreightCostEndDate;

    /**
     * Comment of new freight cost.
     * @since Adjustments Freight and Branch Overrides
     */
    private String newFreightCostComment;

    /**
     * Create user of new freight cost.
     * @since Adjustments Freight and Branch Overrides
     */
    private String newFreightCostCreatedBy;

    /**
     * The adjusted freight cost.
     */
    private String adjustedFreightCost;

     /**
     * The adjusted freight rate.
     */
    private String adjustedFreightRate;

    /**
     * The product type.
     */
    private String productType;

    /**
     * The branch.
     */
    private String branch;

    /**
     * The market.
     */
    private String market;

    /**
     * The market id.
     * @since Adjustments Freight and Branch Overrides
     */
    private long marketId;

    /**
     * The branch id.
     * @since Adjustments Freight and Branch Overrides
     */
    private long branchId;

    /**
     * The product type id.
     * @since Adjustments Freight and Branch Overrides
     */
    private long productTypeId;

    /**
     * The category id.
     * @since Adjustments Freight and Branch Overrides
     */
    private long categoryId;

    /**
     * The vendor id.
     * @since Adjustments Freight and Branch Overrides
     */
    private long vendorId;

    /**
     * The ship point id.
     * @since Adjustments Freight and Branch Overrides
     */
    private long shippointId;

    /**
     * Empty constructor.
     */
    public BranchCWPricingSheetTO() {
    }

    /**
     * Gets the value of the field <code>productId</code>.
     *
     * @return the productId
     */
    public long getProductId() {
        return productId;
    }

    /**
     * Sets the value of the field <code>productId</code>.
     *
     * @param productId the productId to set
     */
    public void setProductId(long productId) {
        this.productId = productId;
    }

    /**
     * Gets the value of the field <code>finalPrice</code>.
     *
     * @return the finalPrice
     */
    public String getFinalPrice() {
        return finalPrice;
    }

    /**
     * Sets the value of the field <code>finalPrice</code>.
     *
     * @param finalPrice the finalPrice to set
     */
    public void setFinalPrice(String finalPrice) {
        this.finalPrice = finalPrice;
    }

    /**
     * Gets the value of the field <code>shipPoint</code>.
     *
     * @return the shipPoint
     */
    public String getShipPoint() {
        return shipPoint;
    }

    /**
     * Sets the value of the field <code>shipPoint</code>.
     *
     * @param shipPoint the shipPoint to set
     */
    public void setShipPoint(String shipPoint) {
        this.shipPoint = shipPoint;
    }

    /**
     * Gets the value of the field <code>category</code>.
     *
     * @return the category
     */
    public String getCategory() {
        return category;
    }

    /**
     * Sets the value of the field <code>category</code>.
     *
     * @param category the category to set
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Gets the value of the field <code>productCode</code>.
     *
     * @return the productCode
     */
    public String getProductCode() {
        return productCode;
    }

    /**
     * Sets the value of the field <code>productCode</code>.
     *
     * @param productCode the productCode to set
     */
    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    /**
     * Gets the value of the field <code>description</code>.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the field <code>description</code>.
     *
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the value of the field <code>newListPrice</code>.
     *
     * @return the newListPrice
     */
    public String getNewListPrice() {
        return newListPrice;
    }

    /**
     * Sets the value of the field <code>newListPrice</code>.
     *
     * @param newListPrice the newListPrice to set
     */
    public void setNewListPrice(String newListPrice) {
        this.newListPrice = newListPrice;
    }

    /**
     * Gets the value of the field <code>newMultiplier</code>.
     *
     * @return the newMultiplier
     */
    public String getNewMultiplier() {
        return newMultiplier;
    }

    /**
     * Sets the value of the field <code>newMultiplier</code>.
     *
     * @param newMultiplier the newMultiplier to set
     */
    public void setNewMultiplier(String newMultiplier) {
        this.newMultiplier = newMultiplier;
    }

    /**
     * Gets the value of the field <code>newNetPrice</code>.
     *
     * @return the newNetPrice
     */
    public String getNewNetPrice() {
        return newNetPrice;
    }

    /**
     * Sets the value of the field <code>newNetPrice</code>.
     *
     * @param newNetPrice the newNetPrice to set
     */
    public void setNewNetPrice(String newNetPrice) {
        this.newNetPrice = newNetPrice;
    }

    /**
     * Gets the value of the field <code>initialNetPrice</code>.
     *
     * @return the initialNetPrice
     */
    public String getInitialNetPrice() {
        return initialNetPrice;
    }

    /**
     * Sets the value of the field <code>initialNetPrice</code>.
     *
     * @param initialNetPrice the initialNetPrice to set
     */
    public void setInitialNetPrice(String initialNetPrice) {
        this.initialNetPrice = initialNetPrice;
    }

    /**
     * Gets the value of the field <code>pricingAdjustment</code>.
     *
     * @return the pricingAdjustment
     */
    public String getPricingAdjustment() {
        return pricingAdjustment;
    }

    /**
     * Sets the value of the field <code>pricingAdjustment</code>.
     *
     * @param pricingAdjustment the pricingAdjustment to set
     */
    public void setPricingAdjustment(String pricingAdjustment) {
        this.pricingAdjustment = pricingAdjustment;
    }

    /**
     * Gets the value of the field <code>beforeFreightPrice</code>.
     *
     * @return the beforeFreightPrice
     */
    public String getBeforeFreightPrice() {
        return beforeFreightPrice;
    }

    /**
     * Sets the value of the field <code>beforeFreightPrice</code>.
     *
     * @param beforeFreightPrice the beforeFreightPrice to set
     */
    public void setBeforeFreightPrice(String beforeFreightPrice) {
        this.beforeFreightPrice = beforeFreightPrice;
    }

    /**
     * Gets the value of the field <code>freightRate</code>.
     *
     * @return the freightRate
     */
    public String getFreightRate() {
        return freightRate;
    }

    /**
     * Sets the value of the field <code>freightRate</code>.
     *
     * @param freightRate the freightRate to set
     */
    public void setFreightRate(String freightRate) {
        this.freightRate = freightRate;
    }

    /**
     * Gets the value of the field <code>freightAdjustment</code>.
     *
     * @return the freightAdjustment
     */
    public String getFreightAdjustment() {
        return freightAdjustment;
    }

    /**
     * Sets the value of the field <code>freightAdjustment</code>.
     *
     * @param freightAdjustment the freightAdjustment to set
     */
    public void setFreightAdjustment(String freightAdjustment) {
        this.freightAdjustment = freightAdjustment;
    }

    /**
     * <p>
     * Retrieves the totalPrice field.
     * </p>
     *
     * @return the value of totalPrice
     */
    public String getTotalPrice() {
        return totalPrice;
    }

    /**
     * <p>
     * Sets the value to totalPrice field.
     * </p>
     *
     * @param totalPrice the value of totalPrice to set
     */
    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    /**
     * Gets the value of the field <code>branchOverride</code>.
     *
     * @return the branchOverride
     */
    public String getBranchOverride() {
        return branchOverride;
    }

    /**
     * Sets the value of the field <code>branchOverride</code>.
     *
     * @param branchOverride the branchOverride to set
     */
    public void setBranchOverride(String branchOverride) {
        this.branchOverride = branchOverride;
    }

    /**
     * Gets the value of the field <code>weightPerFoot</code>.
     *
     * @return the weightPerFoot
     */
    public String getWeightPerFoot() {
        return weightPerFoot;
    }

    /**
     * Sets the value of the field <code>weightPerFoot</code>.
     *
     * @param weightPerFoot the weightPerFoot to set
     */
    public void setWeightPerFoot(String weightPerFoot) {
        this.weightPerFoot = weightPerFoot;
    }

    /**
     * Gets the value of the field <code>initialListPrice</code>.
     *
     * @return the initialListPrice
     */
    public String getInitialListPrice() {
        return initialListPrice;
    }

    /**
     * Sets the value of the field <code>initialListPrice</code>.
     *
     * @param initialListPrice the initialListPrice to set
     */
    public void setInitialListPrice(String initialListPrice) {
        this.initialListPrice = initialListPrice;
    }

    /**
     * Gets the value of the field <code>initialMultiplier</code>.
     *
     * @return the initialMultiplier
     */
    public String getInitialMultiplier() {
        return initialMultiplier;
    }

    /**
     * Sets the value of the field <code>initialMultiplier</code>.
     *
     * @param initialMultiplier the initialMultiplier to set
     */
    public void setInitialMultiplier(String initialMultiplier) {
        this.initialMultiplier = initialMultiplier;
    }

    /**
     * Gets the value of the field <code>freightCost</code>.
     * @return the freightCost
     */
    public String getFreightCost() {
        return freightCost;
    }

    /**
     * Sets the value of the field <code>freightCost</code>.
     * @param freightCost the freightCost to set
     */
    public void setFreightCost(String freightCost) {
        this.freightCost = freightCost;
    }

    /**
     * Gets the value of the field <code>adjustedFreightCost</code>.
     * @return the adjustedFreightCost
     */
    public String getAdjustedFreightCost() {
        return adjustedFreightCost;
    }

    /**
     * Sets the value of the field <code>adjustedFreightCost</code>.
     * @param adjustedFreightCost the adjustedFreightCost to set
     */
    public void setAdjustedFreightCost(String adjustedFreightCost) {
        this.adjustedFreightCost = adjustedFreightCost;
    }

    /**
     * Gets the value of the field <code>productType</code>.
     * @return the productType
     */
    public String getProductType() {
        return productType;
    }

    /**
     * Sets the value of the field <code>productType</code>.
     * @param productType the productType to set
     */
    public void setProductType(String productType) {
        this.productType = productType;
    }

    /**
     * Gets the value of the field <code>branch</code>.
     * @return the branch
     */
    public String getBranch() {
        return branch;
    }

    /**
     * Sets the value of the field <code>branch</code>.
     * @param branch the branch to set
     */
    public void setBranch(String branch) {
        this.branch = branch;
    }

    /**
     * Gets the value of the field <code>market</code>.
     * @return the market
     */
    public String getMarket() {
        return market;
    }

    /**
     * Sets the value of the field <code>market</code>.
     * @param market the market to set
     */
    public void setMarket(String market) {
        this.market = market;
    }

    /**
     * <p>
     * Retrieves the pricingAdjustmentStartDate field.
     * </p>
     *
     * @return the value of pricingAdjustmentStartDate
     * @since Adjustments Freight and Branch Overrides
     */
    public Date getPricingAdjustmentStartDate() {
        return pricingAdjustmentStartDate;
    }

    /**
     * <p>
     * Sets the value to pricingAdjustmentStartDate field.
     * </p>
     *
     * @param pricingAdjustmentStartDate the value of pricingAdjustmentStartDate to set
     * @since Adjustments Freight and Branch Overrides
     */
    public void setPricingAdjustmentStartDate(Date pricingAdjustmentStartDate) {
        this.pricingAdjustmentStartDate = pricingAdjustmentStartDate;
    }

    /**
     * <p>
     * Retrieves the pricingAdjustmentEndDate field.
     * </p>
     *
     * @return the value of pricingAdjustmentEndDate
     * @since Adjustments Freight and Branch Overrides
     */
    public Date getPricingAdjustmentEndDate() {
        return pricingAdjustmentEndDate;
    }

    /**
     * <p>
     * Sets the value to pricingAdjustmentEndDate field.
     * </p>
     *
     * @param pricingAdjustmentEndDate the value of pricingAdjustmentEndDate to set
     * @since Adjustments Freight and Branch Overrides
     */
    public void setPricingAdjustmentEndDate(Date pricingAdjustmentEndDate) {
        this.pricingAdjustmentEndDate = pricingAdjustmentEndDate;
    }

    /**
     * <p>
     * Retrieves the pricingAdjustmentComment field.
     * </p>
     *
     * @return the value of pricingAdjustmentComment
     * @since Adjustments Freight and Branch Overrides
     */
    public String getPricingAdjustmentComment() {
        return pricingAdjustmentComment;
    }

    /**
     * <p>
     * Sets the value to pricingAdjustmentComment field.
     * </p>
     *
     * @param pricingAdjustmentComment the value of pricingAdjustmentComment to set
     * @since Adjustments Freight and Branch Overrides
     */
    public void setPricingAdjustmentComment(String pricingAdjustmentComment) {
        this.pricingAdjustmentComment = pricingAdjustmentComment;
    }

    /**
     * <p>
     * Retrieves the pricingAdjustmentCreatedBy field.
     * </p>
     *
     * @return the value of pricingAdjustmentCreatedBy
     * @since Adjustments Freight and Branch Overrides
     */
    public String getPricingAdjustmentCreatedBy() {
        return pricingAdjustmentCreatedBy;
    }

    /**
     * <p>
     * Sets the value to pricingAdjustmentCreatedBy field.
     * </p>
     *
     * @param pricingAdjustmentCreatedBy the value of pricingAdjustmentCreatedBy to set
     * @since Adjustments Freight and Branch Overrides
     */
    public void setPricingAdjustmentCreatedBy(String pricingAdjustmentCreatedBy) {
        this.pricingAdjustmentCreatedBy = pricingAdjustmentCreatedBy;
    }

    /**
     * <p>
     * Retrieves the newBranchOverride field.
     * </p>
     *
     * @return the value of newBranchOverride
     * @since Adjustments Freight and Branch Overrides
     */
    public String getNewBranchOverride() {
        return newBranchOverride;
    }

    /**
     * <p>
     * Sets the value to newBranchOverride field.
     * </p>
     *
     * @param newBranchOverride the value of newBranchOverride to set
     * @since Adjustments Freight and Branch Overrides
     */
    public void setNewBranchOverride(String newBranchOverride) {
        this.newBranchOverride = newBranchOverride;
    }


    /**
     * <p>
     * Retrieves the newBranchOverrideStartDate field.
     * </p>
     *
     * @return the value of newBranchOverrideStartDate
     * @since Adjustments Freight and Branch Overrides
     */
    public Date getNewBranchOverrideStartDate() {
        return newBranchOverrideStartDate;
    }

    /**
     * <p>
     * Sets the value to newBranchOverrideStartDate field.
     * </p>
     *
     * @param newBranchOverrideStartDate the value of newBranchOverrideStartDate to set
     * @since Adjustments Freight and Branch Overrides
     */
    public void setNewBranchOverrideStartDate(Date newBranchOverrideStartDate) {
        this.newBranchOverrideStartDate = newBranchOverrideStartDate;
    }

    /**
     * <p>
     * Retrieves the newBranchOverrideEndDate field.
     * </p>
     *
     * @return the value of newBranchOverrideEndDate
     * @since Adjustments Freight and Branch Overrides
     */
    public Date getNewBranchOverrideEndDate() {
        return newBranchOverrideEndDate;
    }

    /**
     * <p>
     * Sets the value to newBranchOverrideEndDate field.
     * </p>
     *
     * @param newBranchOverrideEndDate the value of newBranchOverrideEndDate to set
     * @since Adjustments Freight and Branch Overrides
     */
    public void setNewBranchOverrideEndDate(Date newBranchOverrideEndDate) {
        this.newBranchOverrideEndDate = newBranchOverrideEndDate;
    }

    /**
     * <p>
     * Retrieves the newBranchOverrideComment field.
     * </p>
     *
     * @return the value of newBranchOverrideComment
     * @since Adjustments Freight and Branch Overrides
     */
    public String getNewBranchOverrideComment() {
        return newBranchOverrideComment;
    }

    /**
     * <p>
     * Sets the value to newBranchOverrideComment field.
     * </p>
     *
     * @param newBranchOverrideComment the value of newBranchOverrideComment to set
     * @since Adjustments Freight and Branch Overrides
     */
    public void setNewBranchOverrideComment(String newBranchOverrideComment) {
        this.newBranchOverrideComment = newBranchOverrideComment;
    }

    /**
     * <p>
     * Retrieves the newBranchOverrideCreatedBy field.
     * </p>
     *
     * @return the value of newBranchOverrideCreatedBy
     * @since Adjustments Freight and Branch Overrides
     */
    public String getNewBranchOverrideCreatedBy() {
        return newBranchOverrideCreatedBy;
    }

    /**
     * <p>
     * Sets the value to newBranchOverrideCreatedBy field.
     * </p>
     *
     * @param newBranchOverrideCreatedBy the value of newBranchOverrideCreatedBy to set
     * @since Adjustments Freight and Branch Overrides
     */
    public void setNewBranchOverrideCreatedBy(String newBranchOverrideCreatedBy) {
        this.newBranchOverrideCreatedBy = newBranchOverrideCreatedBy;
    }

    /**
     * <p>
     * Retrieves the marketId field.
     * </p>
     *
     * @return the value of marketId
     * @since Adjustments Freight and Branch Overrides
     */
    public long getMarketId() {
        return marketId;
    }

    /**
     * <p>
     * Sets the value to marketId field.
     * </p>
     *
     * @param marketId the value of marketId to set
     * @since Adjustments Freight and Branch Overrides
     */
    public void setMarketId(long marketId) {
        this.marketId = marketId;
    }

    /**
     * <p>
     * Retrieves the branchId field.
     * </p>
     *
     * @return the value of branchId
     * @since Adjustments Freight and Branch Overrides
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
     * @since Adjustments Freight and Branch Overrides
     */
    public void setBranchId(long branchId) {
        this.branchId = branchId;
    }

    /**
     * <p>
     * Retrieves the productTypeId field.
     * </p>
     *
     * @return the value of productTypeId
     * @since Adjustments Freight and Branch Overrides
     */
    public long getProductTypeId() {
        return productTypeId;
    }

    /**
     * <p>
     * Sets the value to productTypeId field.
     * </p>
     *
     * @param productTypeId the value of productTypeId to set
     * @since Adjustments Freight and Branch Overrides
     */
    public void setProductTypeId(long productTypeId) {
        this.productTypeId = productTypeId;
    }

    /**
     * <p>
     * Retrieves the categoryId field.
     * </p>
     *
     * @return the value of categoryId
     * @since Adjustments Freight and Branch Overrides
     */
    public long getCategoryId() {
        return categoryId;
    }

    /**
     * <p>
     * Sets the value to categoryId field.
     * </p>
     *
     * @param categoryId the value of categoryId to set
     * @since Adjustments Freight and Branch Overrides
     */
    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * <p>
     * Retrieves the vendorId field.
     * </p>
     *
     * @return the value of vendorId
     * @since Adjustments Freight and Branch Overrides
     */
    public long getVendorId() {
        return vendorId;
    }

    /**
     * <p>
     * Sets the value to vendorId field.
     * </p>
     *
     * @param vendorId the value of vendorId to set
     * @since Adjustments Freight and Branch Overrides
     */
    public void setVendorId(long vendorId) {
        this.vendorId = vendorId;
    }

    /**
     * <p>
     * Retrieves the shippointId field.
     * </p>
     *
     * @return the value of shippointId
     * @since Adjustments Freight and Branch Overrides
     */
    public long getShippointId() {
        return shippointId;
    }

    /**
     * <p>
     * Sets the value to shippointId field.
     * </p>
     *
     * @param shippointId the value of shippointId to set
     * @since Adjustments Freight and Branch Overrides
     */
    public void setShippointId(long shippointId) {
        this.shippointId = shippointId;
    }

    /**
     * <p>
     * Retrieves the adjustments field.
     * </p>
     *
     * @return the value of adjustments
     * @since Adjustments Freight and Branch Overrides
     */
    public ProductPriceRecordDTO getAdjustments() {
        return adjustments;
    }

    /**
     * <p>
     * Sets the value to adjustments field.
     * </p>
     *
     * @param adjustments the value of adjustments to set
     * @since Adjustments Freight and Branch Overrides
     */
    public void setAdjustments(ProductPriceRecordDTO adjustments) {
        this.adjustments = adjustments;
    }

    /**
     * <p>
     * Retrieves the pricingRecordId field.
     * </p>
     *
     * @return the value of pricingRecordId
     */
    public long getPricingRecordId() {
        return pricingRecordId;
    }

    /**
     * <p>
     * Sets the value to pricingRecordId field.
     * </p>
     *
     * @param pricingRecordId the value of pricingRecordId to set
     * @since Adjustments Freight and Branch Overrides
     */
    public void setPricingRecordId(long pricingRecordId) {
        this.pricingRecordId = pricingRecordId;
    }

    /**
     * <p>
     * Retrieves the lowestNet field.
     * </p>
     *
     * @return the value of lowestNet
     * @since Adjustments Freight and Branch Overrides
     */
    public boolean isLowestNet() {
        return lowestNet;
    }

    /**
     * <p>
     * Sets the value to lowestNet field.
     * </p>
     *
     * @param lowestNet the value of lowestNet to set
     * @since Adjustments Freight and Branch Overrides
     */
    public void setLowestNet(boolean lowestNet) {
        this.lowestNet = lowestNet;
    }

    /**
     * <p>
     * Retrieves the finalListPrice field.
     * </p>
     *
     * @return the value of finalListPrice
     * @since Adjustments Freight and Branch Overrides
     */
    public String getFinalListPrice() {
        return finalListPrice;
    }

    /**
     * <p>
     * Sets the value to finalListPrice field.
     * </p>
     *
     * @param finalListPrice the value of finalListPrice to set
     * @since Adjustments Freight and Branch Overrides
     */
    public void setFinalListPrice(String finalListPrice) {
        this.finalListPrice = finalListPrice;
    }

    /**
     * <p>
     * Retrieves the finalMultiplier field.
     * </p>
     *
     * @return the value of finalMultiplier
     * @since Adjustments Freight and Branch Overrides
     */
    public String getFinalMultiplier() {
        return finalMultiplier;
    }

    /**
     * <p>
     * Sets the value to finalMultiplier field.
     * </p>
     *
     * @param finalMultiplier the value of finalMultiplier to set
     * @since Adjustments Freight and Branch Overrides
     */
    public void setFinalMultiplier(String finalMultiplier) {
        this.finalMultiplier = finalMultiplier;
    }

    /**
     * <p>
     * Retrieves the adjustedFreightRate field.
     * </p>
     *
     * @return the value of adjustedFreightRate
     */
    public String getAdjustedFreightRate() {
        return adjustedFreightRate;
    }

    /**
     * <p>
     * Sets the value to adjustedFreightRate field.
     * </p>
     *
     * @param adjustedFreightRate the value of adjustedFreightRate to set
     */
    public void setAdjustedFreightRate(String adjustedFreightRate) {
        this.adjustedFreightRate = adjustedFreightRate;
    }

    /**
     * <p>
     * Retrieves the newFreightCost field.
     * </p>
     *
     * @return the value of newFreightCost
     */
    public String getNewFreightCost() {
        return newFreightCost;
    }

    /**
     * <p>
     * Sets the value to newFreightCost field.
     * </p>
     *
     * @param newFreightCost the value of newFreightCost to set
     */
    public void setNewFreightCost(String newFreightCost) {
        this.newFreightCost = newFreightCost;
    }

    /**
     * <p>
     * Retrieves the newFreightCostStartDate field.
     * </p>
     *
     * @return the value of newFreightCostStartDate
     */
    public Date getNewFreightCostStartDate() {
        return newFreightCostStartDate;
    }

    /**
     * <p>
     * Sets the value to newFreightCostStartDate field.
     * </p>
     *
     * @param newFreightCostStartDate the value of newFreightCostStartDate to set
     */
    public void setNewFreightCostStartDate(Date newFreightCostStartDate) {
        this.newFreightCostStartDate = newFreightCostStartDate;
    }

    /**
     * <p>
     * Retrieves the newFreightCostEndDate field.
     * </p>
     *
     * @return the value of newFreightCostEndDate
     */
    public Date getNewFreightCostEndDate() {
        return newFreightCostEndDate;
    }

    /**
     * <p>
     * Sets the value to newFreightCostEndDate field.
     * </p>
     *
     * @param newFreightCostEndDate the value of newFreightCostEndDate to set
     */
    public void setNewFreightCostEndDate(Date newFreightCostEndDate) {
        this.newFreightCostEndDate = newFreightCostEndDate;
    }

    /**
     * <p>
     * Retrieves the newFreightCostComment field.
     * </p>
     *
     * @return the value of newFreightCostComment
     */
    public String getNewFreightCostComment() {
        return newFreightCostComment;
    }

    /**
     * <p>
     * Sets the value to newFreightCostComment field.
     * </p>
     *
     * @param newFreightCostComment the value of newFreightCostComment to set
     */
    public void setNewFreightCostComment(String newFreightCostComment) {
        this.newFreightCostComment = newFreightCostComment;
    }

    /**
     * <p>
     * Retrieves the newFreightCostCreatedBy field.
     * </p>
     *
     * @return the value of newFreightCostCreatedBy
     */
    public String getNewFreightCostCreatedBy() {
        return newFreightCostCreatedBy;
    }

    /**
     * <p>
     * Sets the value to newFreightCostCreatedBy field.
     * </p>
     *
     * @param newFreightCostCreatedBy the value of newFreightCostCreatedBy to set
     */
    public void setNewFreightCostCreatedBy(String newFreightCostCreatedBy) {
        this.newFreightCostCreatedBy = newFreightCostCreatedBy;
    }

    /**
     * <p>
     * Retrieves the newPricingAdjustment field.
     * </p>
     *
     * @return the value of newPricingAdjustment
     */
    public String getNewPricingAdjustment() {
        return newPricingAdjustment;
    }

    /**
     * <p>
     * Sets the value to newPricingAdjustment field.
     * </p>
     *
     * @param newPricingAdjustment the value of newPricingAdjustment to set
     */
    public void setNewPricingAdjustment(String newPricingAdjustment) {
        this.newPricingAdjustment = newPricingAdjustment;
    }

    /**
     * <p>
     * Retrieves the newPricingAdjustmentStartDate field.
     * </p>
     *
     * @return the value of newPricingAdjustmentStartDate
     */
    public Date getNewPricingAdjustmentStartDate() {
        return newPricingAdjustmentStartDate;
    }

    /**
     * <p>
     * Sets the value to newPricingAdjustmentStartDate field.
     * </p>
     *
     * @param newPricingAdjustmentStartDate the value of newPricingAdjustmentStartDate to set
     */
    public void setNewPricingAdjustmentStartDate(Date newPricingAdjustmentStartDate) {
        this.newPricingAdjustmentStartDate = newPricingAdjustmentStartDate;
    }

    /**
     * <p>
     * Retrieves the newPricingAdjustmentEndDate field.
     * </p>
     *
     * @return the value of newPricingAdjustmentEndDate
     */
    public Date getNewPricingAdjustmentEndDate() {
        return newPricingAdjustmentEndDate;
    }

    /**
     * <p>
     * Sets the value to newPricingAdjustmentEndDate field.
     * </p>
     *
     * @param newPricingAdjustmentEndDate the value of newPricingAdjustmentEndDate to set
     */
    public void setNewPricingAdjustmentEndDate(Date newPricingAdjustmentEndDate) {
        this.newPricingAdjustmentEndDate = newPricingAdjustmentEndDate;
    }

    /**
     * <p>
     * Retrieves the newPricingAdjustmentComment field.
     * </p>
     *
     * @return the value of newPricingAdjustmentComment
     */
    public String getNewPricingAdjustmentComment() {
        return newPricingAdjustmentComment;
    }

    /**
     * <p>
     * Sets the value to newPricingAdjustmentComment field.
     * </p>
     *
     * @param newPricingAdjustmentComment the value of newPricingAdjustmentComment to set
     */
    public void setNewPricingAdjustmentComment(String newPricingAdjustmentComment) {
        this.newPricingAdjustmentComment = newPricingAdjustmentComment;
    }

    /**
     * <p>
     * Retrieves the newPricingAdjustmentCreatedBy field.
     * </p>
     *
     * @return the value of newPricingAdjustmentCreatedBy
     */
    public String getNewPricingAdjustmentCreatedBy() {
        return newPricingAdjustmentCreatedBy;
    }

    /**
     * <p>
     * Sets the value to newPricingAdjustmentCreatedBy field.
     * </p>
     *
     * @param newPricingAdjustmentCreatedBy the value of newPricingAdjustmentCreatedBy to set
     */
    public void setNewPricingAdjustmentCreatedBy(String newPricingAdjustmentCreatedBy) {
        this.newPricingAdjustmentCreatedBy = newPricingAdjustmentCreatedBy;
    }


}
