/*
 * Copyright (C) 2012 TopCoder Inc., All Rights Reserved.
 */
package com.hestia.warroom.entities;

/**
 * <p>
 * Represents my pricing. It is a Javabean. It has one no-parameter constructor that is empty. Each field of this class
 * has a getter and setter. There is no validation done in the setters. The properties just set and get the values.
 * </p>
 *
 * <p>
 * <strong>Thread Safety: </strong> This class is mutable and not thread safe.
 * </p>
 *
 * v1.1 - Export and Search Filter
 *      - pushed down actual ID attribute to allow compile time JPA enhancement
 * v1.2 - Hestia War Room Enhancements
 *      - added support for searching one single record and edit it
 * @author Selena, sparemax, TCSASSEMBLER
 * @version 1.2
 */
public class MyPricing extends AuditableEntity {

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
     * Represents the progress.
     * </p>
     *
     * <p>
     * Fully mutable, has getter and setter. Setter performs no validation. Not initialized. Can be any value.
     * </p>
     */
    private double progress;
    /**
     * <p>
     * Represents the status.
     * </p>
     *
     * <p>
     * Fully mutable, has getter and setter. Setter performs no validation. Not initialized. Can be any value.
     * </p>
     */
    private Status status;

    /**
     * The branch id.
     * @since 1.2 Hestia War Room Enhancements
     */
    private long branchId;

    /**
     * The branch id.
     * @since 1.2 Hestia War Room Enhancements
     */
    private long relatedValueId;

    /**
     * <p>
     * Represents the criteria.
     * </p>
     *
     * <p>
     * Fully mutable, has getter and setter. Setter performs no validation. Not initialized. Can be any value.
     * </p>
     */
    private UserFilterRecord criteria;
    /**
     * <p>
     * Represents the pricing record.
     * </p>
     *
     * <p>
     * Fully mutable, has getter and setter. Setter performs no validation. Not initialized. Can be any value.
     * </p>
     */
    private PricingRecord pricingRecord;
    /**
     * <p>
     * Represents the CW pricing record.
     * </p>
     *
     * <p>
     * Fully mutable, has getter and setter. Setter performs no validation. Not initialized. Can be any value.
     * </p>
     */
    private CWPricingRecord cwPricingRecord;

    /**
     * Creates an instance of MyPricing.
     */
    public MyPricing() {
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
     * Gets the progress.
     *
     * @return the progress.
     */
    public double getProgress() {
        return progress;
    }

    /**
     * Sets the progress.
     *
     * @param progress
     *            the progress.
     */
    public void setProgress(double progress) {
        this.progress = progress;
    }

    /**
     * Gets the status.
     *
     * @return the status.
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Sets the status.
     *
     * @param status
     *            the status.
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * Gets the criteria.
     *
     * @return the criteria.
     */
    public UserFilterRecord getCriteria() {
        return criteria;
    }

    /**
     * Sets the criteria.
     *
     * @param criteria
     *            the criteria.
     */
    public void setCriteria(UserFilterRecord criteria) {
        this.criteria = criteria;
    }

    /**
     * Gets the pricing record.
     *
     * @return the pricing record.
     */
    public PricingRecord getPricingRecord() {
        return pricingRecord;
    }

    /**
     * Sets the pricing record.
     *
     * @param pricingRecord
     *            the pricing record.
     */
    public void setPricingRecord(PricingRecord pricingRecord) {
        this.pricingRecord = pricingRecord;
    }

    /**
     * Gets the CW pricing record.
     *
     * @return the CW pricing record.
     */
    public CWPricingRecord getCwPricingRecord() {
        return cwPricingRecord;
    }

    /**
     * Sets the CW pricing record.
     *
     * @param cwPricingRecord
     *            the CW pricing record.
     */
    public void setCwPricingRecord(CWPricingRecord cwPricingRecord) {
        this.cwPricingRecord = cwPricingRecord;
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

    /**
     * <p>
     * Retrieves the relatedValueId field.
     * </p>
     *
     * @return the value of relatedValueId
     */
    public long getRelatedValueId() {
        return relatedValueId;
    }

    /**
     * <p>
     * Sets the value to relatedValueId field.
     * </p>
     *
     * @param relatedValueId the value of relatedValueId to set
     */
    public void setRelatedValueId(long relatedValueId) {
        this.relatedValueId = relatedValueId;
    }


}