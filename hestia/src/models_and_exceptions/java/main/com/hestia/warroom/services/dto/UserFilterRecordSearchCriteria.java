/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.hestia.warroom.services.dto;

import com.hestia.warroom.entities.User;

/**
 * <p>
 * Represents user filter record search criteria. It is a Javabean. It has one no-parameter constructor that is empty.
 * Each field of this class has a getter and setter. There is no validation done in the setters. The properties just set
 * and get the values.
 * </p>
 *
 * <p>
 * <strong>Thread Safety: </strong> This class is mutable and not thread safe.
 * </p>
 *
 * v1.1 - Hestia War Room Enhancements
 *      - add support to criteriaFlag
 *
 * @author stevenfrog
 * @version 1.1
 * @since Export and Search Filter
 */
public class UserFilterRecordSearchCriteria extends BaseCriteria {
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
     * Owner.
     * </p>
     *
     * <p>
     * Fully mutable, has getter and setter. Setter performs no validation. Not initialized. Can be any value.
     * </p>
     */
    private User owner;
    /**
     * <p>
     * criteriaFlag.
     * </p>
     *
     * <p>
     * Fully mutable, has getter and setter. Setter performs no validation. Not initialized. Can be any value.
     * </p>
     * @since v1.1 Hestia War Room Enhancements
     */
    private Boolean criteriaFlag;

    /**
     * Creates an instance of UserFilterRecordSearchCriteria.
     */
    public UserFilterRecordSearchCriteria() {
        // Empty
    }

    /**
     * Gets the value of the field <code>name</code>.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the field <code>name</code>.
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the value of the field <code>owner</code>.
     *
     * @return the owner
     */
    public User getOwner() {
        return owner;
    }

    /**
     * Sets the value of the field <code>owner</code>.
     *
     * @param owner the owner to set
     */
    public void setOwner(User owner) {
        this.owner = owner;
    }

    /**
     * <p>
     * Retrieves the criteriaFlag field.
     * </p>
     *
     * @return the value of criteriaFlag
     */
    public Boolean getCriteriaFlag() {
        return criteriaFlag;
    }

    /**
     * <p>
     * Sets the value to criteriaFlag field.
     * </p>
     *
     * @param criteriaFlag the value of criteriaFlag to set
     */
    public void setCriteriaFlag(Boolean criteriaFlag) {
        this.criteriaFlag = criteriaFlag;
    }

}