/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.hestia.warroom.services.impl;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.hestia.warroom.entities.BranchOverride;
import com.hestia.warroom.entities.CWPricingRecord;
import com.hestia.warroom.entities.NewFreightRate;
import com.hestia.warroom.entities.NewListAndMultiplier;
import com.hestia.warroom.entities.NewPPT;
import com.hestia.warroom.entities.PricingAdjustment;
import com.hestia.warroom.entities.PricingRecord;
import com.hestia.warroom.entities.User;
import com.hestia.warroom.services.dto.BranchCWPricingSheetTO;
import com.hestia.warroom.services.dto.InputPriceRecordDTO;
import com.hestia.warroom.services.dto.ModificationTO;
import com.hestia.warroom.services.dto.ModificationType;
import com.hestia.warroom.services.dto.PricingAdjustmentDTO;
import com.hestia.warroom.services.dto.PricingBranchOverrideDTO;
import com.hestia.warroom.services.dto.PricingFreightDTO;
import com.hestia.warroom.services.dto.ProductPriceRecordDTO;
import com.hestia.warroom.services.dto.RecalculatedPriceTO;

/**
 * This class contains utility functions used for price calculations.
 *
 * v1.1 - Search Enhancements - added New PPT support for non-CW products
 * v1.2 - Export and Search Filter - changed displayed product code to alt1 to match UI
 * v1.3 - Adjustments Freight and Branch Overrides - added support adjustments, freight and overrides
 * v1.4 - Hestia War Room Enhancements
 *      - added support for 'save all' and show correct data in table
 *
 * @author TCSASSEMBLER
 * @version 1.4
 * @since Price Calculations assembly.
 */
public class CalculationUtil {

    /**
     * The default format for displaying decimal numbers.
     */
    private static final String DEFAULT_PRICING_FORMAT = "#0.00";

    /**
     * The format for displaying integral numbers.
     */
    private static final String INTEGER_PRICING_FORMAT = "#0";

    /**
     * The default date format.
     */
    public static final String DEFAULT_DATE_FORMAT = "MM/dd/yyyy";

    /**
     * The date format instance.
     */
    public static final DateFormat DATE_FORMAT = new SimpleDateFormat(DEFAULT_DATE_FORMAT);

    /**
     * Private constructor.
     */
    private CalculationUtil() {
    }

    /**
     * Calculates the discount based on the 'number of fives' set as multiplier.
     *
     * if a discount is defined as 0.95^X, then X is the number of fives Example: One {five} would equal 0.95. Two
     * {fives} would equal 0.9025. Fifty-nine {fives} would equal 0.0485
     *
     * @param effectivePrice the effective price
     * @param effectiveMultiplier the multiplier value (in number of fives)
     * @return the new price with the multiplier factored in
     */
    public static BigDecimal applyMultiplier(BigDecimal effectivePrice, BigDecimal effectiveMultiplier) {
        BigDecimal discountFactor = new BigDecimal(0.95).pow(effectiveMultiplier.intValue());
        return effectivePrice.multiply(discountFactor);
    }

    /**
     * Transforms the given modification to an adjustment for the given CWPricing record. If there is an existing
     * adjustment, that is updated, otherwise a new one is created. If the value is empty, existing adjustments are
     * cleared.
     *
     * @param user the current user (for createdBy field auditing)
     * @param dto the record to be modified, it must be for a CW product
     * @param delta the change to be applied
     * @throws IllegalArgumentException if any of the arguments are null
     */
    public static void applyChangeToCWRecord(User user, InputPriceRecordDTO dto, ModificationTO delta) {
        if (user == null) {
            throw new IllegalArgumentException("user cannot be null.");
        }

        if (dto == null) {
            throw new IllegalArgumentException("dto cannot be null.");
        }

        if (delta == null) {
            throw new IllegalArgumentException("delta cannot be null.");
        }

        CWPricingRecord base = dto.getCwPricingRecord();
        if (base == null) {
            throw new IllegalArgumentException("dto must include the CW Pricing record.");
        }

        NewListAndMultiplier listPriceAdjustment = dto.getListPriceAdjustment();
        NewListAndMultiplier multiplierAdjustment = dto.getMultiplierAdjustment();

        boolean isClear = false;
        if (delta.getValue() == null || delta.getValue().trim().length() == 0) {
            isClear = true;
        }

        // list price modifications
        if (delta.getType() == ModificationType.REPLACE || delta.getType() == ModificationType.ADDER
            || delta.getType() == ModificationType.PERCENTAGE) {
            if (isClear) {
                if (dto.getInheritedPriceAdjustments().isEmpty()) {
                    dto.setListPriceAdjustment(null);
                } else {
                    // inherit the next newest overlapping record
                    dto.setListPriceAdjustment(dto.getInheritedPriceAdjustments().remove(0));
                }
            } else {
                if (listPriceAdjustment == null) {
                    listPriceAdjustment = new NewListAndMultiplier();
                    listPriceAdjustment.setPricingRecord(base);
                    listPriceAdjustment.setCreatedBy(user.getUsername());
                    listPriceAdjustment.setCreatedOn(new Date());
                }
                switch (delta.getType()) {
                case PERCENTAGE:
                    BigDecimal pctIncreased = CalculationUtil.applyPercentage(base, new BigDecimal(delta.getValue()));
                    listPriceAdjustment.setListPrice(pctIncreased);
                    break;
                case ADDER:
                    BigDecimal incremented = CalculationUtil.applyAdder(base, new BigDecimal(delta.getValue()));
                    listPriceAdjustment.setListPrice(incremented);
                    break;
                default:
                    BigDecimal replacement = new BigDecimal(delta.getValue());
                    listPriceAdjustment.setListPrice(replacement);
                }
                listPriceAdjustment.setComment(delta.getComments());
                listPriceAdjustment.setStartDate(delta.getStartDate());
                listPriceAdjustment.setEndDate(converterToDate(delta.getEndDate()));

                dto.setListPriceAdjustment(listPriceAdjustment);
            }
        } else if (delta.getType() == ModificationType.MULTIPLIER) { // multiplier modification
            if (isClear) {
                if (dto.getInheritedMultiplierAdjustments().isEmpty()) {
                    dto.setMultiplierAdjustment(null);
                } else {
                    // inherit the next newest overlapping record
                    dto.setMultiplierAdjustment(dto.getInheritedMultiplierAdjustments().remove(0));
                }
            } else {
                if (multiplierAdjustment == null) {
                    multiplierAdjustment = new NewListAndMultiplier();
                    multiplierAdjustment.setPricingRecord(base);
                    multiplierAdjustment.setCreatedBy(user.getUsername());
                    multiplierAdjustment.setCreatedOn(new Date());
                }
                multiplierAdjustment.setMultiplier(new BigDecimal(delta.getValue()));
                multiplierAdjustment.setComment(delta.getComments());
                multiplierAdjustment.setStartDate(delta.getStartDate());
                multiplierAdjustment.setEndDate(converterToDate(delta.getEndDate()));

                dto.setMultiplierAdjustment(multiplierAdjustment);
            }
        }
    }

    /**
     * Convert the string to date.
     *
     * @param date
     *            the date string
     * @return the date instance
     */
    private static Date converterToDate(String date) {
        try {
            return DATE_FORMAT.parse(date);
        } catch (ParseException e) {
            // Ignore
        }
        return new Date();
    }

    /**
     * Applies the percentage change to the CW pricing record.
     *
     * @param cwPricingRecord the pricing record to apply the percentage change to
     * @param bigDecimal the percentage to apply (where 1.0 = 100% or no change)
     * @return the list price modified by the given percentage
     */
    private static BigDecimal applyPercentage(CWPricingRecord cwPricingRecord, BigDecimal bigDecimal) {
        return cwPricingRecord.getInitialListPrice().multiply(bigDecimal).divide(new BigDecimal(100));
    }

    /**
     * Applies the increment/decrement change to the CW pricing record.
     *
     * @param cwPricingRecord the pricing record to apply the increment change to
     * @param bigDecimal the amount of change to add
     * @return the list price modified by the given change
     */
    private static BigDecimal applyAdder(CWPricingRecord cwPricingRecord, BigDecimal bigDecimal) {
        return cwPricingRecord.getInitialListPrice().add(bigDecimal);
    }

    /**
     * Recalculates the net prices given the given pricing record and adjustments.
     *
     * @param pricingRecord the CW pricing record to calculate the net prices for
     * @param listPriceAdjustment the list price adjustment (can be null)
     * @param multiplierAdjustment the multiplier adjustment (can be null)
     * @return the recalculated net prices, including some information regarding the adjustments used
     */
    public static RecalculatedPriceTO calculateCWPricing(CWPricingRecord pricingRecord,
        NewListAndMultiplier listPriceAdjustment, NewListAndMultiplier multiplierAdjustment) {
        RecalculatedPriceTO result = new RecalculatedPriceTO();

        if (pricingRecord == null) { // nothing to do
            return result;
        }

        result.setPricingRecordId(pricingRecord.getId());
        result.setCurrentListPrice(toDefaultPricingFormat(pricingRecord.getInitialListPrice()));
        result.setCurrentMultiplier(toDefaultPricingFormat(pricingRecord.getInitialMultiplier()));
        result.setCurrentNetPrice(toDefaultPricingFormat(applyMultiplier(pricingRecord.getInitialListPrice(),
            pricingRecord.getInitialMultiplier())));

        BigDecimal net = pricingRecord.getInitialListPrice();
        BigDecimal effectiveMultiplier = pricingRecord.getInitialMultiplier();

        if (listPriceAdjustment != null) {
            if (listPriceAdjustment.getListPrice() != null) {
                net = listPriceAdjustment.getListPrice();
                result.setAdjustedListPrice(toDefaultPricingFormat(listPriceAdjustment.getListPrice()));
                result.setAdjustedListPriceComment(listPriceAdjustment.getComment());
                result.setAdjustedListPriceCreatedBy(listPriceAdjustment.getCreatedBy());
                result.setAdjustedListPriceStartDate(toDefaultDateFormat(listPriceAdjustment.getStartDate()));
                result.setAdjustedListPriceEndDate(toDefaultDateFormat(listPriceAdjustment.getEndDate()));
            }
        }

        if (multiplierAdjustment != null) {
            if (multiplierAdjustment.getMultiplier() != null) {
                effectiveMultiplier = multiplierAdjustment.getMultiplier();
                result.setAdjustedMultiplier(toDefaultPricingFormat(effectiveMultiplier));
                result.setAdjustedMultiplierComment(multiplierAdjustment.getComment());
                result.setAdjustedMultiplierCreatedBy(multiplierAdjustment.getCreatedBy());
                result.setAdjustedMultiplierStartDate(toDefaultDateFormat(multiplierAdjustment.getStartDate()));
                result.setAdjustedMultiplierEndDate(toDefaultDateFormat(multiplierAdjustment.getEndDate()));
            }
        }

        if (net != null) {
            if (effectiveMultiplier != null) {
                net = CalculationUtil.applyMultiplier(net, effectiveMultiplier);
            }
            result.setAdjustedNetPrice(toDefaultPricingFormat(net));
        }

        return result;
    }

    /**
     * Converts the given date to a string using the default date format.
     *
     * @param value the date to be converted
     * @return the converted value
     */
    private static String toDefaultDateFormat(Date value) {
        return new SimpleDateFormat("MM/dd/yyyy").format(value);
    }

    /**
     * Converts the given number to a string using the default format.
     *
     * @param value the value to be converted
     * @return the formatted number, or empty string if the value is null
     */
    public static String toDefaultPricingFormat(BigDecimal value) {
        if (value == null) {
            return "";
        }

        return new DecimalFormat(DEFAULT_PRICING_FORMAT).format(value);
    }

    /**
     * Converts the given number to a string using the default integer format.
     *
     * @param value the value to be converted
     * @return the formatted number, or empty string if the value is null
     */
    public static String toDefaultIntegerFormat(BigDecimal value) {
        if (value == null) {
            return "";
        }

        return new DecimalFormat(INTEGER_PRICING_FORMAT).format(value);
    }

    /**
     * Calculates the final price for the given record.
     *
     * The following assumptions are made regarding units
     *
     * 1) for CW products, price record is already $/CFT, for Non-CW it is PPT
     * 2) Input price adjustments are in the same unit as the price record
     * 3) price adjustments are ALL in $/CFT
     * 4) freight rate is $/100 lbs, calculation is from ship point to the product.category.branch
     * 5) branch override is in $/CFT
     * 6) final price is in $/CFT
     *
     * @param pricingRecord the pricing record to calculate prices for
     * @return the final price holder, containing intermediate values
     */
    public static BranchCWPricingSheetTO calculateFinalPrice(ProductPriceRecordDTO pricingRecord) {
        BranchCWPricingSheetTO row = new BranchCWPricingSheetTO();
        row.setAdjustments(pricingRecord);

        InputPriceRecordDTO inputPrice = pricingRecord.getInputPrice();
        CWPricingRecord cwPricing = inputPrice.getCwPricingRecord();
        PricingRecord nonCWPricing = inputPrice.getPricingRecord();
        BigDecimal beforeAdjustmentPrice = null;
        if (cwPricing != null) { // CW product
            row.setPricingRecordId(cwPricing.getId());
            row.setProductId(cwPricing.getProduct().getId());
            row.setCategory(cwPricing.getProduct().getCategory().getName());
            row.setProductCode(cwPricing.getProduct().getAlternateCode1());
            row.setDescription(cwPricing.getProduct().getDescription());
            row.setShipPoint(cwPricing.getShipPoint().getName());
            row.setProductType(cwPricing.getProduct().getType().getName());
            if (cwPricing.getProduct().getCategory().getBranch() != null) {
                row.setBranchId(cwPricing.getProduct().getCategory().getBranch().getId());
                row.setBranch(cwPricing.getProduct().getCategory().getBranch().getProfitCenter());
                row.setMarket(cwPricing.getProduct().getCategory().getBranch().getMarket().getName());
            }
            row.setInitialListPrice(toDefaultPricingFormat(cwPricing.getInitialListPrice()));
            row.setFinalListPrice(toDefaultPricingFormat(cwPricing.getInitialListPrice()));
            row.setInitialMultiplier(toDefaultPricingFormat(cwPricing.getInitialMultiplier()));
            row.setFinalMultiplier(toDefaultPricingFormat(cwPricing.getInitialMultiplier()));

            row.setVendorId(cwPricing.getShipPoint().getVendor().getId());
            row.setShippointId(cwPricing.getShipPoint().getId());
            row.setCategoryId(cwPricing.getProduct().getCategory().getId());
            row.setBranchId(cwPricing.getProduct().getCategory().getBranch().getId());
            row.setProductTypeId(cwPricing.getProduct().getType().getId());
            row.setMarketId(cwPricing.getProduct().getCategory().getBranch().getMarket().getId());

            NewListAndMultiplier listPriceAdjustment = inputPrice.getListPriceAdjustment();
            row.setNewListPrice(toDefaultPricingFormat(cwPricing.getInitialListPrice()));
            if (listPriceAdjustment != null) {
                row.setNewListPrice(toDefaultPricingFormat(listPriceAdjustment.getListPrice()));
                row.setFinalListPrice(toDefaultPricingFormat(listPriceAdjustment.getListPrice()));
            }
            NewListAndMultiplier multiplierAdjustment = inputPrice.getMultiplierAdjustment();
            row.setNewMultiplier(toDefaultPricingFormat(cwPricing.getInitialMultiplier()));
            if (multiplierAdjustment != null) {
                row.setNewMultiplier(toDefaultPricingFormat(multiplierAdjustment.getMultiplier()));
                row.setFinalMultiplier(toDefaultPricingFormat(multiplierAdjustment.getMultiplier()));
            }

            // first, calculate the Net Price for the product
            RecalculatedPriceTO cwPrice = calculateCWPricing(cwPricing, listPriceAdjustment, multiplierAdjustment);
            BigDecimal netPrice = new BigDecimal(cwPrice.getCurrentNetPrice());
            beforeAdjustmentPrice = new BigDecimal(cwPrice.getAdjustedNetPrice());
            row.setInitialNetPrice(toDefaultPricingFormat(netPrice));
            row.setNewNetPrice(toDefaultPricingFormat(beforeAdjustmentPrice));
        } else { // non-CW
            row.setPricingRecordId(nonCWPricing.getId());
            row.setProductId(nonCWPricing.getProduct().getId());
            row.setCategory(nonCWPricing.getProduct().getCategory().getName());
            row.setProductCode(nonCWPricing.getProduct().getAlternateCode1());
            row.setProductType(nonCWPricing.getProduct().getType().getName());
            if (nonCWPricing.getProduct().getCategory().getBranch() != null) {
                row.setBranchId(nonCWPricing.getProduct().getCategory().getBranch().getId());
                row.setBranch(nonCWPricing.getProduct().getCategory().getBranch().getProfitCenter());
                row.setMarket(nonCWPricing.getProduct().getCategory().getBranch().getMarket().getName());
            }
            row.setDescription(nonCWPricing.getProduct().getDescription());
            row.setShipPoint(nonCWPricing.getShipPoint().getName());
            row.setInitialListPrice(toDefaultPricingFormat(nonCWPricing.getInitialPPT()));
            row.setFinalListPrice(toDefaultPricingFormat(nonCWPricing.getInitialPPT()));

            row.setVendorId(nonCWPricing.getShipPoint().getVendor().getId());
            row.setShippointId(nonCWPricing.getShipPoint().getId());
            row.setCategoryId(nonCWPricing.getProduct().getCategory().getId());
            row.setBranchId(nonCWPricing.getProduct().getCategory().getBranch().getId());
            row.setProductTypeId(nonCWPricing.getProduct().getType().getId());
            row.setMarketId(nonCWPricing.getProduct().getCategory().getBranch().getMarket().getId());

            NewPPT pptAdjustment = inputPrice.getNewPPT();
            row.setNewListPrice(toDefaultPricingFormat(nonCWPricing.getInitialPPT()));
            if (pptAdjustment != null) {
                row.setNewListPrice(toDefaultPricingFormat(pptAdjustment.getValue()));
                row.setFinalListPrice(toDefaultPricingFormat(pptAdjustment.getValue()));
            }

            // first, calculate the Net Price for the product
            RecalculatedPriceTO nonCWPrice = calculateNonCWPricing(nonCWPricing, pptAdjustment);
            beforeAdjustmentPrice = nonCWPricing.getInitialPPT();
            String adjustedListPrice = nonCWPrice.getAdjustedListPrice();
            if (adjustedListPrice != null) {
                beforeAdjustmentPrice = new BigDecimal(adjustedListPrice);
            }

            // convert from PPT to $/CFT
            double wtPerFoot = nonCWPricing.getProduct().getHundredWeight() / 100;
            beforeAdjustmentPrice = new BigDecimal(beforeAdjustmentPrice.doubleValue() / 20 * wtPerFoot);
            row.setNewNetPrice(toDefaultPricingFormat(beforeAdjustmentPrice));
        }

        /*
         * beforeAdjustmentPrice  is in $/CFT
         */

        // after the net price calculations all adjustments are fixed rates
        // http://apps.topcoder.com/forums/?module=Message&messageID=1726917
        PricingAdjustment pricingAdjustment = pricingRecord.getPricingAdjustment().getAdjustment();
        if (pricingAdjustment != null) {
            row.setPricingAdjustment(toDefaultPricingFormat(pricingAdjustment.getValue()));
            row.setPricingAdjustmentStartDate(pricingAdjustment.getStartDate());
            row.setPricingAdjustmentEndDate(pricingAdjustment.getEndDate());
            row.setPricingAdjustmentComment(pricingAdjustment.getComment());
            if (pricingAdjustment.getUpdatedBy() != null){
                row.setPricingAdjustmentCreatedBy(pricingAdjustment.getUpdatedBy());
            }else{
                row.setPricingAdjustmentCreatedBy(pricingAdjustment.getCreatedBy());
            }
        }

        /*
         * pricingAdjustment is in $/CFT
         * beforeFreight is in $/CFT
         */
        BigDecimal beforeFreight = applyPricingAdjustment(beforeAdjustmentPrice, pricingAdjustment);
        row.setBeforeFreightPrice(toDefaultPricingFormat(beforeFreight));

        PricingFreightDTO freightAdjustment = pricingRecord.getFreightAdjustment();
        BigDecimal totalPrice = beforeFreight;
        double weightPerFoot = 1;
        if (cwPricing != null) {
            weightPerFoot = cwPricing.getProduct().getHundredWeight() / 100;
        } else {
            weightPerFoot = nonCWPricing.getProduct().getHundredWeight() / 100;
        }

        /*
         * freightRate is in $/100 lbs
         */
        if (freightAdjustment != null) {
            row.setFreightRate(toDefaultPricingFormat(new BigDecimal(freightAdjustment.getFreightRate())));
            double freightRate = freightAdjustment.getFreightRate(); // freight rate is per cost unit
            /*
             * cost is in $/CFT
             */
            double cost = weightPerFoot *  freightRate;
            row.setFreightCost(toDefaultPricingFormat(new BigDecimal(cost)));
            row.setFreightAdjustment(toDefaultPricingFormat(new BigDecimal(cost)));
            totalPrice = beforeFreight.add(new BigDecimal(cost));

            NewFreightRate freightChange = freightAdjustment.getNewFreight();
            if (freightChange != null) {
                freightRate = freightChange.getValue().doubleValue();
                /*
                 * cost is in $/CFT
                 */
                cost = weightPerFoot * freightRate;
                row.setAdjustedFreightRate("" + freightRate);
                row.setAdjustedFreightCost(toDefaultPricingFormat(new BigDecimal(cost)));
                totalPrice = beforeFreight.add(new BigDecimal(cost));
                row.setFreightAdjustment(toDefaultPricingFormat(freightChange.getValue()));
                row.setNewFreightCost(toDefaultPricingFormat(freightChange.getValue()));
                row.setNewFreightCostComment(freightChange.getComment());
                row.setNewFreightCostCreatedBy(freightChange.getCreatedBy());
                row.setNewFreightCostEndDate(freightChange.getEndDate());
                row.setNewFreightCostStartDate(freightChange.getStartDate());
            }
        }

        /*
         * totalPrice is in $/CFT
         */
        row.setTotalPrice(toDefaultPricingFormat(totalPrice));

        PricingBranchOverrideDTO branchOverride = pricingRecord.getBranchOverride();
        /*
         * branchOverride is in $/CFT
         */
        if (branchOverride != null) {
            if (branchOverride.getBranchOverride() != null) {
                row.setBranchOverride(toDefaultPricingFormat(branchOverride.getBranchOverride().getValue()));
            }
        }

        BigDecimal finalPrice = applyBranchOverride(totalPrice, branchOverride);
        /*
         * finalPrice is in $/CFT
         */
        row.setFinalPrice(toDefaultPricingFormat(finalPrice));
        return row;
    }

    /**
     * Applies the branch override to the given price.
     *
     * @param totalPrice the current total price
     * @param branchOverride the branch override
     * @return the total price if there is no override, or the override
     */
    private static BigDecimal applyBranchOverride(BigDecimal totalPrice, PricingBranchOverrideDTO branchOverride) {
        if (branchOverride == null) {
            // nothing to add
            return totalPrice;
        }

        if (branchOverride.getBranchOverride() == null) {
            return totalPrice;
        }

        return branchOverride.getBranchOverride().getValue();
    }

    /**
     * Pricing adjustments are fixed rates already calculated in the UI, so this just replaces the net with the
     * adjustment.
     *
     * @param netAdjustedPrice the current calculated price
     * @param pricingAdjustment the pricing adjustment
     * @return the net price to be used
     */
    private static BigDecimal applyPricingAdjustment(BigDecimal netAdjustedPrice, PricingAdjustment pricingAdjustment) {
        if (pricingAdjustment == null) {
            return netAdjustedPrice;
        }

        return pricingAdjustment.getValue();
    }

    /**
     * Recalculates the new prices given the given pricing record and adjustments.
     *
     * @param pricingRecord the pricing record to calculate the net prices for
     * @param newPPT the price adjustment (can be null)
     * @return the recalculated prices, including some information regarding the adjustment used
     */
    public static RecalculatedPriceTO calculateNonCWPricing(PricingRecord pricingRecord, NewPPT newPPT) {
        RecalculatedPriceTO result = new RecalculatedPriceTO();

        if (pricingRecord == null) { // nothing to do
            return result;
        }

        result.setPricingRecordId(pricingRecord.getId());
        result.setCurrentListPrice(toDefaultIntegerFormat(pricingRecord.getInitialPPT()));

        if (newPPT != null) {
            if (newPPT.getValue() != null) {
                result.setAdjustedListPrice(toDefaultIntegerFormat(newPPT.getValue()));
                result.setAdjustedListPriceComment(newPPT.getComment());
                result.setAdjustedListPriceCreatedBy(newPPT.getCreatedBy());
                result.setAdjustedListPriceStartDate(toDefaultDateFormat(newPPT.getStartDate()));
                result.setAdjustedListPriceEndDate(toDefaultDateFormat(newPPT.getEndDate()));
            }
        }
        return result;
    }

    /**
     * Transforms the given modification to an adjustment for the given Pricing record. If there is an existing
     * adjustment, that is updated, otherwise a new one is created. If the value is empty, existing adjustments are
     * cleared.
     *
     * @param user the current user (for createdBy field auditing)
     * @param dto the record to be modified, it must be for a CW product
     * @param delta the change to be applied
     * @throws IllegalArgumentException if any of the arguments are null
     */
    public static void applyChangeToNonCWRecord(User user, InputPriceRecordDTO dto, ModificationTO delta) {
        if (user == null) {
            throw new IllegalArgumentException("user cannot be null.");
        }

        if (dto == null) {
            throw new IllegalArgumentException("dto cannot be null.");
        }

        if (delta == null) {
            throw new IllegalArgumentException("delta cannot be null.");
        }

        PricingRecord base = dto.getPricingRecord();
        if (base == null) {
            throw new IllegalArgumentException("dto must include the non-CW Pricing record.");
        }

        NewPPT ppt = dto.getNewPPT();

        boolean isClear = false;
        if (delta.getValue() == null || delta.getValue().trim().length() == 0) {
            isClear = true;
        }

        if (isClear) {
            if (dto.getInheritedPPTAdjustments().isEmpty()) {
                dto.setNewPPT(null);
            } else {
                // inherit the next newest overlapping record
                dto.setNewPPT(dto.getInheritedPPTAdjustments().remove(0));
            }
        } else {
            if (ppt == null) {
                ppt = new NewPPT();
                ppt.setPricingRecord(base);
                ppt.setCreatedBy(user.getUsername());
                ppt.setCreatedOn(new Date());
            }

            switch (delta.getType()) {
            case ADDER:
                BigDecimal incremented = CalculationUtil.applyAdder(base, new BigDecimal(delta.getValue()));
                ppt.setValue(incremented);
                break;
            default:
                BigDecimal replacement = new BigDecimal(delta.getValue());
                ppt.setValue(replacement);
            }

            ppt.setComment(delta.getComments());
            ppt.setStartDate(delta.getStartDate());
            ppt.setEndDate(converterToDate(delta.getEndDate()));
            dto.setNewPPT(ppt);
        }
    }

    /**
     * Applies the increment/decrement change to the pricing record.
     *
     * @param pricingRecord the pricing record to apply the increment change to
     * @param bigDecimal the amount of change to add
     * @return the list price modified by the given change
     */
    private static BigDecimal applyAdder(PricingRecord pricingRecord, BigDecimal bigDecimal) {
        return pricingRecord.getInitialPPT().add(bigDecimal);
    }

    /**
     * Applies pricing, freight and override adjustments to a pricing record
     *
     * @param currentUser the current user
     * @param record the record to modify
     * @param adjustmentType the adjustment type
     * @param delta the change
     * @return the recalculated change object
     */
    public static RecalculatedPriceTO applyAdjustmentToRecord(User currentUser, ProductPriceRecordDTO record,
        String adjustmentType, ModificationTO delta) {

        RecalculatedPriceTO dto = new RecalculatedPriceTO();
        BranchCWPricingSheetTO currentPricing = CalculationUtil.calculateFinalPrice(record);

        if ("Adjustments".equals(adjustmentType)) {
            PricingAdjustmentDTO pricingAdjustment = record.getPricingAdjustment();
            if (pricingAdjustment == null) {
                pricingAdjustment = new PricingAdjustmentDTO();
                record.setPricingAdjustment(pricingAdjustment);
            }
            PricingAdjustment adjustment = pricingAdjustment.getAdjustment();
            if (adjustment == null) {
                adjustment = new PricingAdjustment();
                pricingAdjustment.setAdjustment(adjustment);
            }

            double priceBeforeAdjustment = Double.parseDouble(currentPricing.getNewNetPrice());
            String newValue = getAdjustedValue(delta, priceBeforeAdjustment);
            adjustment.setValue(new BigDecimal(newValue));
            adjustment.setComment(delta.getComments());
            adjustment.setCreatedBy(currentUser.getUsername());
            adjustment.setCreatedOn(new Date());
            adjustment.setStartDate(new Date());
            adjustment.setEndDate(converterToDate(delta.getEndDate()));

            dto.setAdjustedListPrice(toDefaultPricingFormat(adjustment.getValue()));
            dto.setAdjustedListPriceComment(adjustment.getComment());
            dto.setAdjustedListPriceCreatedBy(adjustment.getCreatedBy());
            dto.setAdjustedListPriceStartDate(toDefaultDateFormat(adjustment.getStartDate()));
            dto.setAdjustedListPriceEndDate(toDefaultDateFormat(adjustment.getEndDate()));
        } else if ("Freight".equals(adjustmentType)) {

            PricingFreightDTO freightAdjustment = record.getFreightAdjustment();
            if (freightAdjustment == null) {
                freightAdjustment = new PricingFreightDTO();
                record.setFreightAdjustment(freightAdjustment);
            }
            NewFreightRate adjustment = freightAdjustment.getNewFreight();
            if (adjustment == null) {
                adjustment = new NewFreightRate();
                freightAdjustment.setNewFreight(adjustment);
            }

            double priceBeforeAdjustment = Double.parseDouble(currentPricing.getFreightRate());
            String newValue = getAdjustedValue(delta, priceBeforeAdjustment);

            adjustment.setValue(new BigDecimal(newValue));
            adjustment.setComment(delta.getComments());
            adjustment.setCreatedBy(currentUser.getUsername());
            adjustment.setCreatedOn(new Date());
            adjustment.setStartDate(new Date());
            adjustment.setEndDate(converterToDate(delta.getEndDate()));

            dto.setAdjustedListPrice(toDefaultPricingFormat(adjustment.getValue()));
            dto.setAdjustedListPriceComment(adjustment.getComment());
            dto.setAdjustedListPriceCreatedBy(adjustment.getCreatedBy());
            dto.setAdjustedListPriceStartDate(toDefaultDateFormat(adjustment.getStartDate()));
            dto.setAdjustedListPriceEndDate(toDefaultDateFormat(adjustment.getEndDate()));
        } else if ("Branch Overrides".equals(adjustmentType)) {

            PricingBranchOverrideDTO branchAdjustment = record.getBranchOverride();
            if (branchAdjustment == null) {
                branchAdjustment = new PricingBranchOverrideDTO();
                record.setBranchOverride(branchAdjustment);
            }
            BranchOverride adjustment = branchAdjustment.getBranchOverride();
            if (adjustment == null) {
                adjustment = new BranchOverride();
                branchAdjustment.setBranchOverride(adjustment);
            }

            double priceBeforeAdjustment = Double.parseDouble(currentPricing.getTotalPrice());
            String newValue = getAdjustedValue(delta, priceBeforeAdjustment);
            adjustment.setValue(new BigDecimal(newValue));
            adjustment.setComment(delta.getComments());
            adjustment.setCreatedBy(currentUser.getUsername());
            adjustment.setCreatedOn(new Date());
            adjustment.setStartDate(new Date());
            adjustment.setEndDate(converterToDate(delta.getEndDate()));

            dto.setAdjustedListPrice(toDefaultPricingFormat(adjustment.getValue()));
            dto.setAdjustedListPriceComment(adjustment.getComment());
            dto.setAdjustedListPriceCreatedBy(adjustment.getCreatedBy());
            dto.setAdjustedListPriceStartDate(toDefaultDateFormat(adjustment.getStartDate()));
            dto.setAdjustedListPriceEndDate(toDefaultDateFormat(adjustment.getEndDate()));
        }
        return dto;

    }

    /**
     * Retrieves the adjusted value for the given change.
     *
     * @param delta the change
     * @param price the price before
     * @return the adjusted price
     */
    private static String getAdjustedValue(ModificationTO delta, double price) {
        double newValue = 0;
        if (delta.getType() == ModificationType.ADDER) {
            newValue = price + Double.parseDouble(delta.getValue());
        } else if (delta.getType() == ModificationType.PERCENTAGE) {
            newValue = price * Double.parseDouble(delta.getValue()) / 100;
        } else {
            newValue = Double.parseDouble(delta.getValue());
        }
        return toDefaultPricingFormat(new BigDecimal(newValue));
    }
}
