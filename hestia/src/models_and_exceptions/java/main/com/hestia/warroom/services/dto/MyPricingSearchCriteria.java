/*
 * Copyright (C) 2012 TopCoder Inc., All Rights Reserved.
 */
package com.hestia.warroom.services.dto;

/**
 * <p>
 * Represents my pricing search criteria. It is a Javabean. It has one no-parameter constructor that is empty.
 * Each field of this class has a getter and setter. There is no validation done in the setters. The
 * properties just set and get the values.
 * </p>
 *
 * <p>
 * <strong>Thread Safety: </strong> This class is mutable and not thread safe.
 * </p>
 *
 * v1.1 - Hestia War Room Enhancements
 *      - added field ownername
 *
 * @author Selena, sparemax, TCSASSEMBLER
 * @version 1.1
 */
public class MyPricingSearchCriteria extends BaseCriteria {
    /**
     * <p>
     * Represents the owner name.
     * </p>
     *
     * <p>
     * Fully mutable, has getter and setter. Setter performs no validation. Not initialized. Can be any value.
     * </p>
     */
    private String ownername;

    /**
     * Creates an instance of MyPricingSearchCriteria.
     */
    public MyPricingSearchCriteria() {
        // Empty
    }

    /**
     * <p>
     * Retrieves the ownername field.
     * </p>
     *
     * @return the value of ownername
     */
    public String getOwnername() {
        return ownername;
    }

    /**
     * <p>
     * Sets the value to ownername field.
     * </p>
     *
     * @param ownername
     *            the value of ownername to set
     */
    public void setOwnername(String ownername) {
        this.ownername = ownername;
    }

}