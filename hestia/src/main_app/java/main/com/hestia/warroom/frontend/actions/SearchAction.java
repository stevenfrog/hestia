/*
 * Copyright (C) 2012 TopCoder Inc., All Rights Reserved.
 */
package com.hestia.warroom.frontend.actions;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;

import com.hestia.warroom.entities.BasePricingRecord;
import com.hestia.warroom.entities.Branch;
import com.hestia.warroom.entities.CWPricingRecord;
import com.hestia.warroom.entities.Category;
import com.hestia.warroom.entities.FilterType;
import com.hestia.warroom.entities.IdentifiableEntity;
import com.hestia.warroom.entities.Market;
import com.hestia.warroom.entities.PricingRecord;
import com.hestia.warroom.entities.Product;
import com.hestia.warroom.entities.ProductType;
import com.hestia.warroom.entities.ShipPoint;
import com.hestia.warroom.entities.UserFilterRecord;
import com.hestia.warroom.entities.Vendor;
import com.hestia.warroom.frontend.Helper;
import com.hestia.warroom.services.HestiaWarRoomPersistenceException;
import com.hestia.warroom.services.HestiaWarRoomServiceException;
import com.hestia.warroom.services.dto.BaseCriteria;
import com.hestia.warroom.services.dto.BranchCWPricingSheetTO;
import com.hestia.warroom.services.dto.BranchSearchCriteria;
import com.hestia.warroom.services.dto.InputPriceRecordDTO;
import com.hestia.warroom.services.dto.PagedResult;
import com.hestia.warroom.services.dto.PricingAdjustmentDTO;
import com.hestia.warroom.services.dto.PricingBranchOverrideDTO;
import com.hestia.warroom.services.dto.PricingFreightDTO;
import com.hestia.warroom.services.dto.ProductPriceRecordDTO;
import com.hestia.warroom.services.dto.SortingType;
import com.hestia.warroom.services.impl.CalculationUtil;

/**
 * <p>
 * This action is called when "Search" button is clicked.
 * </p>
 *
 * <p>
 * <b>Thread Safety: </b> It's mutable and not thread safe. The struts framework will guarantee that it's used in the
 * thread safe model.
 * </p>
 *
 * <p>
 * <b>Sample configuration:</b><br>
 * Please refer {@link BaseGoToAction}
 * </p>
 *
 * v1.1 - Pricing Calculation Assembly - cleanup unused code and imports, prevent NPE on initial load
 * v1.2 - Search Enhancements - added paging and drill down support
 * v1.3 - Export and Search Filter - added additional column sort support
 * v1.4 - Adjustments Freight and Branch Overrides
 *      - added support for page Adjustments, Freight and Branch Overrides
 * v1.5 - Hestia War Room Enhancements
 *      - added support for lowest net checkbox
 *
 * @author woodjhon, DEVELOPER, stevenfrog, TCSASSEMBLIER
 * @version 1.5
 */
@SuppressWarnings("serial")
public class SearchAction extends BaseGoToAction {
    /**
     * Session holder for search results.
     */
    protected static final String USER_SEARCH_RESULTS = "USER_SEARCH_RESULTS";

    /**
     * Session holder for search results.
     */
    protected static final String USER_PRICING_SHEET = "USER_PRICING_SHEET";

    /**
     * Session holder for search results.
     */
    protected static final String USER_ONLY_LOWEST_PRICING_SHEET = "USER_ONLY_LOWEST_PRICING_SHEET";

    /**
     * Session holder for search results filter.
     */
    protected static final String USER_DD_TYPE = "USER_DD_TYPE";

    /**
     * Session holder for search results filter.
     */
    protected static final String USER_DD_VALUE = "USER_DD_VALUE";

    /**
     * Session holder for the flag whether need clear to be persisted data.
     */
    protected static final String NEED_CLEAR_TO_BE_PERSISTED_DATA = "NEED_CLEAR_TO_BE_PERSISTED_DATA";

    /**
     * <p>
     * Represents the class name used in logging.
     * </p>
     */
    private static final String CLASS_NAME = SearchAction.class.getName();

    /**
     * Flag indicating if this is a full search or just navigation from current results.
     */
    private boolean newSearch = true;

    /**
     * Flag indicating one should use the current session search values.
     */
    private boolean switchFromSession = false;

    /**
     * For drilldowns, the type of filter.
     */
    private String ddType;

    /**
     * For drilldowns, the value of filter.
     */
    private String ddValue;

    /**
     * The search type.
     */
    private String searchType;

    /**
     * The flag whether only lowest net price.
     */
    private boolean onlyLowestNetPrice;

    /**
     * Calculation results.
     */
    private List<BranchCWPricingSheetTO> pricingSheet = new ArrayList<BranchCWPricingSheetTO>();

    /**
     * <p>
     * Represents which type is submitted.
     * </p>
     */
    private String whichType;
    /**
     * <p>
     * The inputPriceRecords is used to represents the input price records.
     * </p>
     * <p>
     * It's set by the action methods, and consumed by the front end page. It can't be null when the page need it.
     * </p>
     */
    private PagedResult<InputPriceRecordDTO> inputPriceRecords;

    /**
     * The search result tree data.
     */
    private SearchResultTreeDTO searchResultTree;

    /**
     * The view result size.
     */
    private int viewResultSize;

    /**
     * <p>
     * Create the instance.
     * </p>
     */
    public SearchAction() {

    }

    /**
     * <p>
     * This action is called when "Search" button is clicked. It uses PricingRecordsManagementService to searches input
     * prices.
     * </p>
     *
     * updated in Adjustments Freight and Branch Overrides
     *      - added support for page Adjustments, Freight and Branch Overrides
     * updated in Hestia War Room Enhancements
 *      - added support for lowest net checkbox
     *
     * @return SUCCESS to indicate that the operation was successful
     *
     * @throws IllegalArgumentException if serviceContext, userFilterRecord or criteria is null, or criteria.page &lt;
     *             0, or (criteria.page &gt; 0 and criteria.pageSize &lt;= 0), or userFilterRecord.type is not
     *             INPUT_PRICE_FILTER.
     * @throws HestiaWarRoomServiceException if error occurs while searching input prices.
     */
    @SuppressWarnings("unchecked")
    @Override
    public String execute() throws HestiaWarRoomServiceException {
        String signature = CLASS_NAME + ".execute()";
        // logging entrance
        Helper.logEntrance(getLogger(), signature, new String[] {"userFilterRecord", "criteria", "targetDate"},
            new Object[] {getUserFilterRecord(), getCriteria(), getTargetDate()});

        UserFilterRecord filterRecord = getUserFilterRecord();// new UserFilterRecord();

        if (switchFromSession) { // changing tab
            ddType = (String) getSession().get(USER_DD_TYPE);
            ddValue = (String) getSession().get(USER_DD_VALUE);
            filterRecord = (UserFilterRecord) getSession().get(BaseAction.USER_SEARCH_FILTER);
            setCriteria((BaseCriteria) getSession().get(BaseAction.USER_SEARCH_CRITERIA));
            newSearch = false;
        }

        // start v.1.1 - initial load will always have this as null
        if (filterRecord == null) {
            filterRecord = new UserFilterRecord();
        }
        // end v.1.1

        filterRecord.setType(FilterType.INPUT_PRICE_FILTER);
        filterRecord.setName("filter1");

        // Only Search page need this flag
        if(!"Search".equalsIgnoreCase(searchType)){
            onlyLowestNetPrice = false;
        }

        BaseCriteria criteria = getCriteria();
        try {
            PagedResult<InputPriceRecordDTO> results = null;
            PagedResult<InputPriceRecordDTO> originalResults = new PagedResult<InputPriceRecordDTO>();
            if (switchFromSession || newSearch || getSession().get(USER_SEARCH_RESULTS) == null) {
                BaseCriteria newSearchCriteria = new BaseCriteria();
                newSearchCriteria.setSortingColumn(criteria.getSortingColumn());
                newSearchCriteria.setSortingType(criteria.getSortingType());

                List<Branch> branches = filterRecord.getBranches();
                List<Market> markets = filterRecord.getMarkets();
                filterRecord.setBranches(null);
                filterRecord.setMarkets(null);

                results = getPricingRecordsManagementService().searchInputPrices(getServiceContext(),
                    filterRecord, newSearchCriteria, getTargetDate());

                originalResults.setPage(results.getPage());
                originalResults.setTotal(results.getTotal());
                originalResults.setValues(results.getValues());

                filterRecord.setBranches(branches);
                filterRecord.setMarkets(markets);

                // Search - pricing * branch, lowest per product in yellow
                // Input Prices - pricing only
                // Adjustment - pricing only
                // Freight - pricing * branch
                // Branch Overrides - pricing * branch
                if ("Search".equalsIgnoreCase(searchType) || "Freight".equalsIgnoreCase(searchType)
                    || "Branch Overrides".equalsIgnoreCase(searchType)) {
                    List<InputPriceRecordDTO> tmp = crossJoinDestinations(results.getValues(), filterRecord);
                    results.setValues(tmp);
                    results.setTotal(tmp.size());
                }

                doSort(results, criteria.getSortingColumn(), criteria.getSortingType());

                // retain all changes for SAVE ALL support when just switching tabs
                if (!switchFromSession) {
                    Map<String, BranchCWPricingSheetTO> prices = new HashMap<String, BranchCWPricingSheetTO>();
                    getSession().put(USER_PRICING_SHEET, prices);
                }

                getSession().put(USER_SEARCH_RESULTS, results);

                searchResultTree = summarizeSearchResults(results.getValues());

                getSession().put(NEED_CLEAR_TO_BE_PERSISTED_DATA, true);
            } else {
                results = (PagedResult<InputPriceRecordDTO>) getSession().get(USER_SEARCH_RESULTS);
                doSort(results, criteria.getSortingColumn(), criteria.getSortingType());
            }

            getSession().put(USER_DD_TYPE, ddType);
            getSession().put(USER_DD_VALUE, ddValue);
            getSession().put(BaseAction.USER_SEARCH_FILTER, filterRecord);
            getSession().put(BaseAction.USER_SEARCH_CRITERIA, criteria);

            if (!onlyLowestNetPrice) {
                getSession().put(USER_ONLY_LOWEST_PRICING_SHEET, null);

                if (StringUtils.isNotBlank(ddType)) {
                    results = applyDrillDownFilter(results, ddType, ddValue);
                }

                // paginate
                inputPriceRecords = new PagedResult<InputPriceRecordDTO>();
                List<InputPriceRecordDTO> view = new ArrayList<InputPriceRecordDTO>();
                List<InputPriceRecordDTO> allResults = results.getValues();

                inputPriceRecords.setValues(view);
                inputPriceRecords.setTotal(results.getTotal());

                if (criteria.getPageSize() > 0) {
                    inputPriceRecords.setPage(criteria.getPage());
                    int fromIndex = (criteria.getPage() - 1) * criteria.getPageSize();
                    int toIndex = Math.min(allResults.size(), fromIndex + criteria.getPageSize());
                    view.addAll(allResults.subList(fromIndex, toIndex));
                } else if (results.getTotal() > 0) {
                    inputPriceRecords.setPage(1);
                    view.addAll(allResults);
                }
                viewResultSize = view.size();

                // get view port from session list
                List<InputPriceRecordDTO> viewResults = inputPriceRecords.getValues();
                pricingSheet = new ArrayList<BranchCWPricingSheetTO>();
                Map<String, BranchCWPricingSheetTO> sessionPricing = (Map<String, BranchCWPricingSheetTO>) getSession()
                    .get(USER_PRICING_SHEET);

                Set<Long> displayedProducts = new HashSet<Long>();
                for (InputPriceRecordDTO inputPriceRecordDTO : viewResults) {
                    String key = generatePricingKey(inputPriceRecordDTO);
                    if (inputPriceRecordDTO.getCwPricingRecord() != null) {
                        displayedProducts.add(inputPriceRecordDTO.getCwPricingRecord().getProduct().getId());
                    } else {
                        displayedProducts.add(inputPriceRecordDTO.getPricingRecord().getProduct().getId());
                    }
                    if (sessionPricing.containsKey(key)) {
                        pricingSheet.add(sessionPricing.get(key));
                    } else {
                        List<ProductPriceRecordDTO> adjustments = getPricingRecordsManagementService()
                            .populateProductAdjustments(getServiceContext(), Arrays.asList(inputPriceRecordDTO),
                                new Date());
                        for (ProductPriceRecordDTO productPriceRecordDTO : adjustments) {
                            BranchCWPricingSheetTO row = CalculationUtil
                                .calculateFinalPrice(productPriceRecordDTO);
                            sessionPricing.put(key, row);
                        }
                        pricingSheet.add(sessionPricing.get(key));
                    }
                }

                boolean useInputPrice = false;
                if ("Input Prices".equalsIgnoreCase(searchType)) {
                    useInputPrice = true;
                }

                // mark lowest net for all prices in view.
                markLowestNetPricesFromResults(allResults, sessionPricing, displayedProducts, useInputPrice,
                    inputPriceRecords.getValues());

            } else {

                List<BranchCWPricingSheetTO> tmpPricingSheet;
                if (switchFromSession || newSearch || getSession().get(USER_ONLY_LOWEST_PRICING_SHEET) == null) {
                    if (StringUtils.isNotBlank(ddType)) {
                        results = applyDrillDownFilter(originalResults, ddType, ddValue);
                    }

                    // get view port from session list
                    List<BranchCWPricingSheetTO> allPricingSheet = new ArrayList<BranchCWPricingSheetTO>();
                    Map<String, BranchCWPricingSheetTO> sessionPricing = new HashMap<String, BranchCWPricingSheetTO>();

                    for (InputPriceRecordDTO inputPriceRecordDTO : results.getValues()) {
                        String key = generatePricingKey(inputPriceRecordDTO);
                        List<ProductPriceRecordDTO> adjustments = getPricingRecordsManagementService()
                            .populateProductAdjustments(getServiceContext(), Arrays.asList(inputPriceRecordDTO),
                                new Date());
                        for (ProductPriceRecordDTO productPriceRecordDTO : adjustments) {
                            BranchCWPricingSheetTO row = CalculationUtil.calculateFinalPrice(productPriceRecordDTO);
                            sessionPricing.put(key, row);
                        }
                        allPricingSheet.add(sessionPricing.get(key));
                    }

                    // mark lowest net price for all prices.
                    markLowestNetPricesFromResults(results.getValues(), sessionPricing, null, false, null);

                    // just show the lowest one
                    tmpPricingSheet = new ArrayList<BranchCWPricingSheetTO>();
                    for (BranchCWPricingSheetTO branchCWPricingSheetTO : allPricingSheet) {
                        if (branchCWPricingSheetTO.isLowestNet()) {
                            tmpPricingSheet.add(branchCWPricingSheetTO);
                        }
                    }

                    // store the all lowest net price in session for paging
                    getSession().put(USER_ONLY_LOWEST_PRICING_SHEET, tmpPricingSheet);
                }else{
                    tmpPricingSheet = (List<BranchCWPricingSheetTO>)getSession().get(USER_ONLY_LOWEST_PRICING_SHEET);
                }

                List<InputPriceRecordDTO> view = new ArrayList<InputPriceRecordDTO>();

                inputPriceRecords = new PagedResult<InputPriceRecordDTO>();
                inputPriceRecords.setValues(view);
                inputPriceRecords.setTotal(tmpPricingSheet.size());

                pricingSheet = new ArrayList<BranchCWPricingSheetTO>();

                if (criteria.getPageSize() > 0) {
                    inputPriceRecords.setPage(criteria.getPage());
                    int fromIndex = (criteria.getPage() - 1) * criteria.getPageSize();
                    int toIndex = Math.min(tmpPricingSheet.size(), fromIndex + criteria.getPageSize());
                    pricingSheet.addAll(tmpPricingSheet.subList(fromIndex, toIndex));
                    viewResultSize = toIndex - fromIndex;
                } else if (results.getTotal() > 0) {
                    inputPriceRecords.setPage(1);
                    pricingSheet.addAll(tmpPricingSheet);
                    viewResultSize = inputPriceRecords.getTotal();
                }

            }

            String result = determineViewJSP(signature);

            // Log exit
            Helper.logExit(getLogger(), signature, result, getCurrentUser());
            return result;
        } catch (IllegalArgumentException e) {
            // log exception and re-throw
            throw Helper.logException(getLogger(), signature, e, "Argument error occurs.");
        } catch (HestiaWarRoomServiceException e) {
            // log exception and re-throw
            throw Helper.logException(getLogger(), signature, e, "Error occurs while searching input prices.");
        }
    }


    /**
     * Determines which JSP renders the result of this page.
     *
     * @return the jsp to show
     */
    private String determineViewJSP(String signature)throws HestiaWarRoomServiceException {
        String result = null;
        if ("Search".equalsIgnoreCase(searchType)) {

        result = whichType.equals("cwtype") ? "cwtype_search" : "othertype_search";
    } else if ("Input Prices".equalsIgnoreCase(searchType)) {
        result = whichType.equals("cwtype") ? "cwtype" : "othertype";
    } else if ("Adjustments".equalsIgnoreCase(searchType)) {

        result = whichType.equals("cwtype") ? "adjustment_cwtype" : "adjustment_othertype";
    } else if ("Freight".equalsIgnoreCase(searchType)) {

        result = whichType.equals("cwtype") ? "freight_cwtype" : "freight_othertype";
    } else if ("Branch Overrides".equalsIgnoreCase(searchType)) {

        result = whichType.equals("cwtype") ? "branch_overrides_cwtype" : "branch_overrides_othertype";
    } else {
        String message = "The search type is undefined[" + searchType + "]";
        throw Helper.logException(getLogger(), signature, new HestiaWarRoomServiceException(message),
            message);
    }
        return result;
    }

    /**
     * In memory sorting for already loaded results.
     *
     * @param results
     *            the results to sort
     * @param sortingColumn
     *            the sort column
     * @param sortingType
     *            the sort direction
     */
    @SuppressWarnings("unchecked")
    private <T> void doSort(PagedResult<T> results, final String sortingColumn, SortingType sortingType) {
        List<T> values = results.getValues();
        if (values.isEmpty()) {
            return;
        }
        if (values.get(0) instanceof InputPriceRecordDTO) {
            Collections.sort((List<InputPriceRecordDTO>) values, new Comparator<InputPriceRecordDTO>() {
                /**
                 * Compares the ship point names of the following objects.
                 *
                 * @param o1
                 *            one of objects to compare
                 * @param o2
                 *            one of objects to compare
                 * @return the ship point/vendor name comparison
                 */
                public int compare(InputPriceRecordDTO o1, InputPriceRecordDTO o2) {
                    BasePricingRecord b1 = o1.getCwPricingRecord() == null ? o1.getPricingRecord() : o1
                        .getCwPricingRecord();
                    BasePricingRecord b2 = o2.getCwPricingRecord() == null ? o2.getPricingRecord() : o2
                        .getCwPricingRecord();
                    try {
                        return BeanUtils.getNestedProperty(b1, sortingColumn).compareTo(
                            BeanUtils.getNestedProperty(b2, sortingColumn));
                    } catch (IllegalAccessException e) {
                        getLogger().warn("Unable to complete comparison for sorting.", e);
                        return 0;
                    } catch (InvocationTargetException e) {
                        getLogger().warn("Unable to complete comparison for sorting.", e);
                        return 0;
                    } catch (NoSuchMethodException e) {
                        getLogger().warn("Unable to complete comparison for sorting.", e);
                        return 0;
                    }
                }
            });
        }

        if (sortingType == SortingType.DESC) {
            Collections.reverse(values);
        }
    }

    /**
     * Supports drill down by filtering based on the clicked leaf.
     *
     * @param results the current results
     * @param entityType the drill down filter entity type
     * @param entityId the drill down filter entity ID
     * @return the filtered results
     */
    private <T> PagedResult<T> applyDrillDownFilter(final PagedResult<T> results,
        String entityType, String entityId) {
        try {
            PagedResult<T> filteredResults = new PagedResult<T>();
            List<T> filteredValues = new ArrayList<T>();
            filteredResults.setValues(filteredValues);

            long id = Long.parseLong(entityId);
            List<T> values = results.getValues();
            for (T value : values) {
                Product product = null;
                Vendor vendor = null;
                ShipPoint shipPoint = null;

                InputPriceRecordDTO inputPriceRecordDTO = (InputPriceRecordDTO) value;

                if (inputPriceRecordDTO.getCwPricingRecord() != null) {
                    product = inputPriceRecordDTO.getCwPricingRecord().getProduct();
                    vendor = inputPriceRecordDTO.getCwPricingRecord().getShipPoint().getVendor();
                    shipPoint = inputPriceRecordDTO.getCwPricingRecord().getShipPoint();
                } else if (inputPriceRecordDTO.getPricingRecord() != null) {
                    product = inputPriceRecordDTO.getPricingRecord().getProduct();
                    vendor = inputPriceRecordDTO.getPricingRecord().getShipPoint().getVendor();
                    shipPoint = inputPriceRecordDTO.getPricingRecord().getShipPoint();
                }

                Category category = product.getCategory();
                Branch branch = product.getCategory().getBranch();
                ProductType type = product.getType();
                Market market = branch.getMarket();


                if (entityType.equalsIgnoreCase("Market")) {
                    if (market.getId() == id) {
                        filteredValues.add(value);
                    }
                } else if (entityType.equalsIgnoreCase("Branch")) {
                    if (branch.getId() == id) {
                        filteredValues.add(value);
                    }
                } else if (entityType.equalsIgnoreCase("Product")) {
                    if (product.getId() == id) {
                        filteredValues.add(value);
                    }
                } else if (entityType.equalsIgnoreCase("ProductType")) {
                    if (type.getId() == id) {
                        filteredValues.add(value);
                    }
                } else if (entityType.equalsIgnoreCase("Category")) {
                    if (category.getId() == id) {
                        filteredValues.add(value);
                    }
                } else if (entityType.equalsIgnoreCase("Vendor")) {
                    if (vendor.getId() == id) {
                        filteredValues.add(value);
                    }
                } else if (entityType.equalsIgnoreCase("ShipPoint")) {
                    if (shipPoint.getId() == id) {
                        filteredValues.add(value);
                    }
                }
            }
            filteredResults.setTotal(filteredValues.size());

            return filteredResults;
        } catch (NumberFormatException nfe) {
            getLogger().warn("Invalid drill down entity Id is ignored." + entityId);
            return results;
        }
    }

    /**
     * Summarizes the search results for tree rendering.
     *
     * @param values the matched results
     * @return the tree DTO
     */
    private <T> SearchResultTreeDTO summarizeSearchResults(List<T> values) {
        SearchResultTreeDTO tree = new SearchResultTreeDTO();

        for (T value : values) {
            Product product = null;
            Vendor vendor = null;
            ShipPoint shipPoint = null;

            InputPriceRecordDTO inputPriceRecordDTO = null;
            if (value instanceof InputPriceRecordDTO){
                inputPriceRecordDTO = (InputPriceRecordDTO)value;
            }else if (value instanceof PricingAdjustmentDTO){
                inputPriceRecordDTO = ((PricingAdjustmentDTO)value).getInputPrice();
            } else if (value instanceof PricingFreightDTO) {
                inputPriceRecordDTO = ((PricingFreightDTO) value).getPricingAdjustment().getInputPrice();
            } else if (value instanceof PricingBranchOverrideDTO) {
                inputPriceRecordDTO = ((PricingBranchOverrideDTO) value).getPricingFreight()
                    .getPricingAdjustment().getInputPrice();
            }

            if (inputPriceRecordDTO.getPricingRecord() != null) {
                product = inputPriceRecordDTO.getPricingRecord().getProduct();
                vendor = inputPriceRecordDTO.getPricingRecord().getShipPoint().getVendor();
                shipPoint = inputPriceRecordDTO.getPricingRecord().getShipPoint();
            } else if (inputPriceRecordDTO.getCwPricingRecord() != null) {
                product = inputPriceRecordDTO.getCwPricingRecord().getProduct();
                vendor = inputPriceRecordDTO.getCwPricingRecord().getShipPoint().getVendor();
                shipPoint = inputPriceRecordDTO.getCwPricingRecord().getShipPoint();
            }

            Category category = product.getCategory();
            Branch branch = product.getCategory().getBranch();
            ProductType type = product.getType();
            Market market = branch.getMarket();

            tree.addVendor(vendor);
            tree.addProductType(type);
            tree.addMarket(market);
            tree.addBranch(branch);
            tree.addProduct(product);
            tree.addCategory(category);
            tree.addShipPoint(shipPoint);
        }
        return tree;
    }

    /**
     * Marks the product with the lowest net price from the current search (full) results.
     * @param allResults all result prices
     * @param sessionPricing the session price
     * @param displayedProducts the displayed product ids
     * @param useInputPrice flag for input price comparison
     * @param list
     * @throws HestiaWarRoomPersistenceException for any errors encountered
     */
    private void markLowestNetPricesFromResults(List<InputPriceRecordDTO> allResults,
        Map<String, BranchCWPricingSheetTO> sessionPricing, Set<Long> displayedProducts, boolean useInputPrice, List<InputPriceRecordDTO> inputPrices)
        throws HestiaWarRoomPersistenceException {
        Map<Long, String> lowestNetKeysPerProduct = new HashMap<Long, String>();
        for (InputPriceRecordDTO inputPriceRecordDTO : allResults) {
            long productId = 0;
            // if in view
            if (inputPriceRecordDTO.getCwPricingRecord() != null) {
                productId = inputPriceRecordDTO.getCwPricingRecord().getProduct().getId();
            } else {
                productId = inputPriceRecordDTO.getPricingRecord().getProduct().getId();
            }

            if (displayedProducts != null && !displayedProducts.contains(productId)) {
                continue;
            }

            // calculate price
            String key = generatePricingKey(inputPriceRecordDTO);
            BranchCWPricingSheetTO productPrice;
            if (sessionPricing.containsKey(key)) {
                productPrice = sessionPricing.get(key);
            } else {
                List<ProductPriceRecordDTO> adjustments = getPricingRecordsManagementService()
                    .populateProductAdjustments(getServiceContext(), Arrays.asList(inputPriceRecordDTO), new Date());
                for (ProductPriceRecordDTO productPriceRecordDTO : adjustments) {
                    BranchCWPricingSheetTO row = CalculationUtil.calculateFinalPrice(productPriceRecordDTO);
                    sessionPricing.put(key, row);
                }
                productPrice = sessionPricing.get(key);
            }

            if (lowestNetKeysPerProduct.get(productId) == null) {
                lowestNetKeysPerProduct.put(productId, key);
                productPrice.setLowestNet(true);
            } else {
                String currentLowest = lowestNetKeysPerProduct.get(productId);
                BranchCWPricingSheetTO lowestPrice = sessionPricing.get(currentLowest);
                if (productPrice == lowestPrice) {
                    continue;
                }

                if (useInputPrice) {
                    if (lowestPrice.getNewNetPrice() != null && lowestPrice.getNewNetPrice().trim().length() > 0) {
                        double dblCurrentPrice = Double.parseDouble(lowestPrice.getNewNetPrice());
                        double dblProductPricePrice = Double.parseDouble(productPrice.getNewNetPrice());
                        if (dblProductPricePrice < dblCurrentPrice) {
                            lowestNetKeysPerProduct.put(productId, key);
                            productPrice.setLowestNet(true);
                            lowestPrice.setLowestNet(false);
                        }
                    } else {
                        lowestPrice.setLowestNet(false);
                        productPrice.setLowestNet(true);
                        lowestNetKeysPerProduct.put(productId, key);
                    }
                } else {
                    if (lowestPrice.getFinalPrice() != null && lowestPrice.getFinalPrice().trim().length() > 0) {
                        double dblCurrentPrice = Double.parseDouble(lowestPrice.getFinalPrice());
                        double dblProductPricePrice = Double.parseDouble(productPrice.getFinalPrice());
                        if (dblProductPricePrice < dblCurrentPrice) {
                            lowestNetKeysPerProduct.put(productId, key);
                            productPrice.setLowestNet(true);
                            lowestPrice.setLowestNet(false);
                        }
                    } else {
                        lowestPrice.setLowestNet(false);
                        productPrice.setLowestNet(true);
                        lowestNetKeysPerProduct.put(productId, key);
                    }
                }
            }

        }
        if (useInputPrice) {
            for (InputPriceRecordDTO inputPrice : inputPrices) {
                long productId = 0;
                // if in view
                if (inputPrice.getCwPricingRecord() != null) {
                    productId = inputPrice.getCwPricingRecord().getProduct().getId();
                } else {
                    productId = inputPrice.getPricingRecord().getProduct().getId();
                }

                String key = generatePricingKey(inputPrice);
                if (key.equals(lowestNetKeysPerProduct.get(productId))) {
                    inputPrice.setLowestNet(true);
                } else {
                    inputPrice.setLowestNet(false);
                }
            }
        }
    }

    /**
     * Since the actual key for a pricing is a source + destination, we generate a key based on that.
     *
     * @param inputPriceRecordDTO the input price dto (base price)
     * @return the key for the base price + destination
     */
    public static String generatePricingKey(InputPriceRecordDTO inputPriceRecordDTO) {
        StringBuilder sb= new StringBuilder("pricingId=");
        BasePricingRecord basePricing = inputPriceRecordDTO.getCwPricingRecord();
        if (basePricing == null) {
            basePricing = inputPriceRecordDTO.getPricingRecord();
        }
        sb.append(basePricing.getId());
        sb.append(";branchId=").append(basePricing.getProduct().getCategory().getBranch().getId());
        return sb.toString();
    }

    /**
     * Cross joins the prices with the destinations.
     *
     * @param list the list to cross join
     * @param filterRecord the filtered records
     * @return the cross joined records
     * @throws HestiaWarRoomServiceException for any errors encountered
     */
    private List<InputPriceRecordDTO> crossJoinDestinations(List<InputPriceRecordDTO> list,
        UserFilterRecord filterRecord) throws HestiaWarRoomServiceException {
        List<InputPriceRecordDTO> tmp = new ArrayList<InputPriceRecordDTO>();
        // cross join with destinations to get rates
        List<Branch> allBranches = getBranchService().search(getServiceContext(), new BranchSearchCriteria()).getValues();
        List<Long> branchIds = getIds(filterRecord.getBranches());
        List<Long> marketIds = getIds(filterRecord.getMarkets());
        for (Branch branch : allBranches) {
            if (!branchIds.isEmpty() && !branchIds.contains(branch.getId())) {
                continue; // filters are selected and not matched
            }

            if (!marketIds.isEmpty() && !marketIds.contains(branch.getMarket().getId())) {
                continue; // filters are selected and not matched
            }

            // filters are empty (use ALL) or the branch/market is matched
            for (InputPriceRecordDTO inputPriceRecordDTO : list) {
                InputPriceRecordDTO clone = clone(inputPriceRecordDTO);
                CWPricingRecord cwPricingRecord = clone.getCwPricingRecord();
                if (cwPricingRecord != null) {
                    cwPricingRecord.getProduct().getCategory().setBranch(branch);
                }

                PricingRecord pricingRecord = clone.getPricingRecord();
                if (pricingRecord != null) {
                    pricingRecord.getProduct().getCategory().setBranch(branch);
                }

                tmp.add(clone);
            }
        }
        return tmp;
    }

    /**
     * <p>
     * Get input price records.
     * </p>
     *
     * @return the input price records.
     */
    public PagedResult<InputPriceRecordDTO> getInputPriceRecords() {
        return inputPriceRecords;
    }

    /**
     * <p>
     * Set input price records.
     * </p>
     *
     * @param inputPriceRecords the input price records.
     */
    public void setInputPriceRecords(PagedResult<InputPriceRecordDTO> inputPriceRecords) {
        this.inputPriceRecords = inputPriceRecords;
    }


    /**
     * <p>
     * Get whichType.
     * </p>
     *
     * @return the whichType.
     */
    public String getWhichType() {
        return whichType;
    }

    /**
     * <p>
     * Set whichType.
     * </p>
     *
     * @param whichType the whichType.
     */
    public void setWhichType(String whichType) {
        this.whichType = whichType;
    }

    /**
     * Gets the value of the field <code>newSearch</code>.
     *
     * @return the newSearch
     */
    public boolean isNewSearch() {
        return newSearch;
    }

    /**
     * Sets the value of the field <code>newSearch</code>.
     *
     * @param newSearch the newSearch to set
     */
    public void setNewSearch(boolean newSearch) {
        this.newSearch = newSearch;
    }

    /**
     * Gets the value of the field <code>searchResultTree</code>.
     * @return the searchResultTree
     */
    public SearchResultTreeDTO getSearchResultTree() {
        return searchResultTree;
    }

    /**
     * Sets the value of the field <code>searchResultTree</code>.
     * @param searchResultTree the searchResultTree to set
     */
    public void setSearchResultTree(SearchResultTreeDTO searchResultTree) {
        this.searchResultTree = searchResultTree;
    }

    /**
     * Gets the value of the field <code>ddType</code>.
     * @return the ddType
     */
    public String getDdType() {
        return ddType;
    }

    /**
     * Sets the value of the field <code>ddType</code>.
     * @param ddType the ddType to set
     */
    public void setDdType(String ddType) {
        this.ddType = ddType;
    }

    /**
     * Gets the value of the field <code>ddValue</code>.
     * @return the ddValue
     */
    public String getDdValue() {
        return ddValue;
    }

    /**
     * Sets the value of the field <code>ddValue</code>.
     * @param ddValue the ddValue to set
     */
    public void setDdValue(String ddValue) {
        this.ddValue = ddValue;
    }

    /**
     * Gets the value of the field <code>searchType</code>.
     * @return the searchType
     */
    public String getSearchType() {
        return searchType;
    }

    /**
     * Sets the value of the field <code>searchType</code>.
     * @param searchType the searchType to set
     */
    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    /**
     * Gets the value of the field <code>pricingSheet</code>.
     * @return the pricingSheet
     */
    public List<BranchCWPricingSheetTO> getPricingSheet() {
        return pricingSheet;
    }

    /**
     * Sets the value of the field <code>pricingSheet</code>.
     * @param pricingSheet the pricingSheet to set
     */
    public void setPricingSheet(List<BranchCWPricingSheetTO> pricingSheet) {
        this.pricingSheet = pricingSheet;
    }

    /**
     * Gets the value of the field <code>switchFromSession</code>.
     *
     * @return the switchFromSession
     */
    public boolean isSwitchFromSession() {
        return switchFromSession;
    }

    /**
     * Sets the value of the field <code>switchFromSession</code>.
     *
     * @param switchFromSession the switchFromSession to set
     */
    public void setSwitchFromSession(boolean switchFromSession) {
        this.switchFromSession = switchFromSession;
    }

    /**
     * <p>
     * Retrieves the onlyLowestNetPrice field.
     * </p>
     *
     * @return the value of onlyLowestNetPrice
     */
    public boolean isOnlyLowestNetPrice() {
        return onlyLowestNetPrice;
    }

    /**
     * <p>
     * Sets the value to onlyLowestNetPrice field.
     * </p>
     *
     * @param onlyLowestNetPrice the value of onlyLowestNetPrice to set
     */
    public void setOnlyLowestNetPrice(boolean onlyLowestNetPrice) {
        this.onlyLowestNetPrice = onlyLowestNetPrice;
    }

    /**
     * <p>
     * Retrieves the viewResultSize field.
     * </p>
     *
     * @return the value of viewResultSize
     */
    public int getViewResultSize() {
        return viewResultSize;
    }

    /**
     * <p>
     * Sets the value to viewResultSize field.
     * </p>
     *
     * @param viewResultSize the value of viewResultSize to set
     */
    public void setViewResultSize(int viewResultSize) {
        this.viewResultSize = viewResultSize;
    }

    /**
     * Gets ids of the entities.
     *
     * @param <T> the type.
     * @param entities the entities.
     *
     * @return the ids.
     */
    private static <T extends IdentifiableEntity> List<Long> getIds(List<T> entities) {
        List<Long> list = new ArrayList<Long>();
        if (entities == null) {
            return list;
        }

        for (T entity : entities) {
            list.add(entity.getId());
        }

        return list;
    }

    /**
     * Generic clone for serializable objects.
     * @param the object to clone
     * @return the cloned object
     * @throws HestiaWarRoomServiceException for any errors encountered
     */
    @SuppressWarnings("unchecked")
    private static <T extends Object> T clone(T obj) throws HestiaWarRoomServiceException {
        ObjectOutputStream out = null;
        ObjectInputStream in = null;
        try {
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            out = new ObjectOutputStream(bout);
            out.writeObject(obj);

            ByteArrayInputStream bin = new ByteArrayInputStream(bout.toByteArray());
            in = new ObjectInputStream(bin);
            Object copy = in.readObject();
            return (T) copy;
        } catch (IOException e) {
            throw new HestiaWarRoomServiceException("Cannnot clone result object.", e);
        } catch (ClassNotFoundException e) {
            throw new HestiaWarRoomServiceException("Cannnot clone result object.", e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    // ignore
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                 // ignore
                }
            }
        }
    }


}
