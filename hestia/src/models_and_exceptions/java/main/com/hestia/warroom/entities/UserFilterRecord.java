/*
 * Copyright (C) 2012 TopCoder Inc., All Rights Reserved.
 */
package com.hestia.warroom.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Represents user filter record. It is a Javabean. It has one no-parameter constructor that is empty. Each field of
 * this class has a getter and setter. There is no validation done in the setters. The properties just set and get the
 * values.
 * </p>
 *
 * <p>
 * <strong>Thread Safety: </strong> This class is mutable and not thread safe.
 * </p>
 *
 * v1.1 - Search enhancements - support product filter
 * v1.2 - Export and Search Filter - added ship points
 *      - pushed down actual ID attribute to allow compile time JPA enhancement
 * v1.3 - Hestia War Room Enhancements
 *      - added field criteriaFlag for distinguish my saved filter and my pricing criteria
 * @author Selena, sparemax, stevenfrog. TCSASSEMBLER
 * @version 1.3
 */
public class UserFilterRecord extends AuditableEntity {

    /**
     * The maximum length for short description.
     */
    private static final int MAX_DISPLAY_LENGTH = 40;

    /**
     * <p>
     * Represents the ID.
     * </p>
     *
     * <p>
     * Fully mutable, has getter and setter. Setter performs no validation. Not initialized. Can be any value.
     * </p>
     */
    private long id;

    /**
     * <p>
     * Represents the name.
     * </p>
     *
     * <p>
     * Fully mutable, has getter and setter. Setter performs no validation. Not initialized. Can be any value.
     * </p>
     */
    private String name;
    /**
     * <p>
     * Represents the owner.
     * </p>
     *
     * <p>
     * Fully mutable, has getter and setter. Setter performs no validation. Not initialized. Can be any value.
     * </p>
     */
    private User owner;
    /**
     * <p>
     * Represents the filter type.
     * </p>
     *
     * <p>
     * Fully mutable, has getter and setter. Setter performs no validation. Not initialized. Can be any value.
     * </p>
     */
    private FilterType type;

    /**
     * <p>
     * Represents the products.
     * </p>
     *
     * <p>
     * Fully mutable, has getter and setter. Setter performs no validation. Not initialized. Can be any value.
     * </p>
     * @since 1.1
     */
    private List<Product> products;

    /**
     * <p>
     * Represents the product types.
     * </p>
     *
     * <p>
     * Fully mutable, has getter and setter. Setter performs no validation. Not initialized. Can be any value.
     * </p>
     */
    private List<ProductType> productTypes;
    /**
     * <p>
     * Represents the vendors.
     * </p>
     *
     * <p>
     * Fully mutable, has getter and setter. Setter performs no validation. Not initialized. Can be any value.
     * </p>
     */
    private List<Vendor> vendors;

    /**
     * <p>
     * Represents the shipPoints.
     * </p>
     * @since 1.2
     */
    private List<ShipPoint> shipPoints;
    /**
     * <p>
     * Represents the branches.
     * </p>
     *
     * <p>
     * Fully mutable, has getter and setter. Setter performs no validation. Not initialized. Can be any value.
     * </p>
     */
    private List<Branch> branches;
    /**
     * <p>
     * Represents the markets.
     * </p>
     *
     * <p>
     * Fully mutable, has getter and setter. Setter performs no validation. Not initialized. Can be any value.
     * </p>
     */
    private List<Market> markets;
    /**
     * <p>
     * Represents the categories.
     * </p>
     *
     * <p>
     * Fully mutable, has getter and setter. Setter performs no validation. Not initialized. Can be any value.
     * </p>
     */
    private List<Category> categories;
    /**
     * <p>
     * Represents the "ready to export" flag.
     * </p>
     *
     * <p>
     * Fully mutable, has getter and setter. Setter performs no validation. Not initialized. Can be any value.
     * </p>
     */
    private boolean readyToExport;
    /**
     * <p>
     * Represents the "isCriteria" flag.
     * </p>
     *
     * <p>
     * Fully mutable, has getter and setter. Setter performs no validation. Not initialized. Can be any value.
     * </p>
     * @since Hestia War Room Enhancements
     */
    private boolean criteriaFlag = false;

    /**
     * Creates an instance of UserFilterRecord.
     */
    public UserFilterRecord() {
        // Empty
    }

    /**
     * Gets the name.
     *
     * @return the name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     *
     * @param name
     *            the name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the owner.
     *
     * @return the owner.
     */
    public User getOwner() {
        return owner;
    }

    /**
     * Sets the owner.
     *
     * @param owner
     *            the owner.
     */
    public void setOwner(User owner) {
        this.owner = owner;
    }

    /**
     * Gets the filter type.
     *
     * @return the filter type.
     */
    public FilterType getType() {
        return type;
    }

    /**
     * Sets the filter type.
     *
     * @param type
     *            the filter type.
     */
    public void setType(FilterType type) {
        this.type = type;
    }

    /**
     * Gets the product types.
     *
     * @return the product types.
     */
    public List<ProductType> getProductTypes() {
        return productTypes;
    }

    /**
     * Sets the product types.
     *
     * @param productTypes
     *            the product types.
     */
    public void setProductTypes(List<ProductType> productTypes) {
        this.productTypes = productTypes;
    }

    /**
     * Gets the vendors.
     *
     * @return the vendors.
     */
    public List<Vendor> getVendors() {
        return vendors;
    }

    /**
     * Sets the vendors.
     *
     * @param vendors
     *            the vendors.
     */
    public void setVendors(List<Vendor> vendors) {
        this.vendors = vendors;
    }

    /**
     * Gets the branches.
     *
     * @return the branches.
     */
    public List<Branch> getBranches() {
        return branches;
    }

    /**
     * Sets the branches.
     *
     * @param branches
     *            the branches.
     */
    public void setBranches(List<Branch> branches) {
        this.branches = branches;
    }

    /**
     * Gets the markets.
     *
     * @return the markets.
     */
    public List<Market> getMarkets() {
        return markets;
    }

    /**
     * Sets the markets.
     *
     * @param markets
     *            the markets.
     */
    public void setMarkets(List<Market> markets) {
        this.markets = markets;
    }

    /**
     * Gets the categories.
     *
     * @return the categories.
     */
    public List<Category> getCategories() {
        return categories;
    }

    /**
     * Sets the categories.
     *
     * @param categories
     *            the categories.
     */
    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    /**
     * Gets the "ready ot export" flag.
     *
     * @return the "ready ot export" flag.
     */
    public boolean isReadyToExport() {
        return readyToExport;
    }

    /**
     * Sets the "ready ot export" flag.
     *
     * @param readyToExport
     *            the "ready ot export" flag.
     */
    public void setReadyToExport(boolean readyToExport) {
        this.readyToExport = readyToExport;
    }

    /**
     * <p>
     * Retrieves the criteriaFlag field.
     * </p>
     *
     * @return the value of criteriaFlag
     */
    public boolean isCriteriaFlag() {
        return criteriaFlag;
    }

    /**
     * <p>
     * Sets the value to criteriaFlag field.
     * </p>
     *
     * @param criteriaFlag the value of criteriaFlag to set
     */
    public void setCriteriaFlag(boolean criteriaFlag) {
        this.criteriaFlag = criteriaFlag;
    }

    /**
     * Gets the ID.
     *
     * @return the ID.
     * @since Vendor and Category Management
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the ID.
     *
     * @param id the ID.
     * @since Vendor and Category Management
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Gets the value of the field <code>products</code>.
     * @return the products
     */
    public List<Product> getProducts() {
        return products;
    }

    /**
     * Sets the value of the field <code>products</code>.
     * @param products the products to set
     */
    public void setProducts(List<Product> products) {
        this.products = products;
    }

    /**
     * Gets the formatted display for this filter.
     * If the full description exceeds MAX_DISPLAY_LENGTH, it is truncated
     * @return the description based on selected items
     */
    public String getShortDescription() {
        String full = getFullDescription();
        if (full.length() > MAX_DISPLAY_LENGTH) {
            return full.substring(0, MAX_DISPLAY_LENGTH) + "...";
        }

        return full;
    }

    /**
     * Gets the full display for this filter.
     * @return the description based on selected items
     */
    public String getFullDescription() {
        StringBuilder sb = new StringBuilder();
        if (shipPoints != null && !shipPoints.isEmpty()) {
            List<String> items = new ArrayList<String>();
            for (ShipPoint item : shipPoints) {
                items.add(item.getName());
            }
            sb.append("Vendor=").append(items.toString());
            sb.append(" ");
        }


        if (productTypes != null && !productTypes.isEmpty()) {
            List<String> items = new ArrayList<String>();
            for (ProductType item : productTypes) {
                items.add(item.getName());
            }
            sb.append("Type=").append(items.toString());
            sb.append(" ");
        }

        if (categories != null && !categories.isEmpty()) {
            List<String> items = new ArrayList<String>();
            for (Category item : categories) {
                items.add(item.getName());
            }
            sb.append("Category=").append(items.toString());
            sb.append(" ");
        }

        if (products != null && !products.isEmpty()) {
            List<String> items = new ArrayList<String>();
            for (Product item : products) {
                items.add(item.getAlternateCode1());
            }
            sb.append("Product Code=").append(items.toString());
            sb.append(" ");
        }

        if (markets != null && !markets.isEmpty()) {
            List<String> items = new ArrayList<String>();
            for (Market item : markets) {
                items.add(item.getName());
            }
            sb.append("Market=").append(items.toString());
            sb.append(" ");
        }

        if (branches != null && !branches.isEmpty()) {
            List<String> items = new ArrayList<String>();
            for (Branch item : branches) {
                items.add(item.getProfitCenter());
            }
            sb.append("Branch=").append(items.toString());
            sb.append(" ");
        }

        return sb.toString();
    }

    /**
     * Gets the value of the field <code>shipPoints</code>.
     * @return the shipPoints
     * @since 1.2
     */
    public List<ShipPoint> getShipPoints() {
        return shipPoints;
    }

    /**
     * Sets the value of the field <code>shipPoints</code>.
     * @param shipPoints the shipPoints to set
     * @since 1.2
     */
    public void setShipPoints(List<ShipPoint> shipPoints) {
        this.shipPoints = shipPoints;
    }

    /**
     * Gets the short name.
     *
     * @return the short name.
     * @since 1.2
     */
    public String getShortName() {
        if (name != null && name.length() > 20) {
            return name.substring(0, 20) + "...";
        }
        return name;
    }

}