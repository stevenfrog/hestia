/*
 * Copyright (C) 2012 TopCoder Inc., All Rights Reserved.
 */
package com.hestia.warroom.frontend.actions;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;

import com.hestia.warroom.entities.UserFilterRecord;
import com.hestia.warroom.services.BranchOverrideService;
import com.hestia.warroom.services.BranchService;
import com.hestia.warroom.services.CategoryService;
import com.hestia.warroom.services.HestiaWarRoomServiceConfigurationException;
import com.hestia.warroom.services.LocalBranchOverrideService;
import com.hestia.warroom.services.LocalBranchService;
import com.hestia.warroom.services.LocalCategoryService;
import com.hestia.warroom.services.LocalMarketService;
import com.hestia.warroom.services.LocalNewFreightRateService;
import com.hestia.warroom.services.LocalNewListAndMultiplierService;
import com.hestia.warroom.services.LocalNewPPTService;
import com.hestia.warroom.services.LocalPricingAdjustmentService;
import com.hestia.warroom.services.LocalPricingRecordsManagementService;
import com.hestia.warroom.services.LocalProductService;
import com.hestia.warroom.services.LocalProductTypeService;
import com.hestia.warroom.services.LocalUserFilterRecordService;
import com.hestia.warroom.services.LocalVendorService;
import com.hestia.warroom.services.MarketService;
import com.hestia.warroom.services.NewFreightRateService;
import com.hestia.warroom.services.NewListAndMultiplierService;
import com.hestia.warroom.services.NewPPTService;
import com.hestia.warroom.services.PricingAdjustmentService;
import com.hestia.warroom.services.PricingRecordsManagementService;
import com.hestia.warroom.services.ProductService;
import com.hestia.warroom.services.ProductTypeService;
import com.hestia.warroom.services.UserFilterRecordService;
import com.hestia.warroom.services.VendorService;
import com.hestia.warroom.services.dto.BaseCriteria;

/**
 * <p>
 * BaseGoToAction is used to hold the common fields for the sub classes to use.
 * </p>
 *
 * <p>
 * <b>Thread Safety: </b>It's mutable and not thread safe.
 * </p>
 *
 * <p>
 * <b>Sample configuration:</b><br>
 * Spring applicationContext.xml:
 * <pre>
 * &lt;?xml version=&quot;1.0&quot; encoding=&quot;UTF-8&quot;?&gt;
 * &lt;beans xmlns=&quot;http://www.springframework.org/schema/beans&quot;
 *        xmlns:xsi=&quot;http://www.w3.org/2001/XMLSchema-instance&quot;
 *        xsi:schemaLocation=&quot;http://www.springframework.org/schema/beans
 *        http://www.springframework.org/schema/beans/spring-beans.xsd&quot; &gt;
 *
 *     &lt;bean id=&quot;securityService&quot;
 *         class=&quot;com.hestia.warroom.services.impl.MockSecurityService&quot;/&gt;
 *
 *     &lt;bean id=&quot;pricingRecordsManagementService&quot;
 *         class=&quot;com.hestia.warroom.services.impl.MockPricingRecordsManagementService&quot;/&gt;
 *
 *     &lt;bean id=&quot;baseSecurityInterceptor&quot; abstract=&quot;true&quot;
 *         class=&quot;com.hestia.warroom.frontend.interceptors.BaseSecurityInterceptor&quot;&gt;
 *         &lt;property name=&quot;securityService&quot; ref=&quot;securityService&quot;/&gt;
 *     &lt;/bean&gt;
 *
 *     &lt;bean id=&quot;authenticationInterceptor&quot; parent=&quot;baseSecurityInterceptor&quot;
 *         class=&quot;com.hestia.warroom.frontend.interceptors.AuthenticationInterceptor&quot;/&gt;
 *
 *     &lt;bean id=&quot;authorizationInterceptor&quot; parent=&quot;baseSecurityInterceptor&quot;
 *         class=&quot;com.hestia.warroom.frontend.interceptors.AuthorizationInterceptor&quot;/&gt;
 *
 *     &lt;bean id=&quot;baseAction&quot; abstract=&quot;true&quot;
 *         class=&quot;com.hestia.warroom.frontend.actions.BaseAction&quot;&gt;
 *      &lt;property name=&quot;securityService&quot; ref=&quot;securityService&quot;/&gt;
 *     &lt;/bean&gt;
 *
 *     &lt;bean id=&quot;baseGoToAction&quot; abstract=&quot;true&quot; parent=&quot;baseAction&quot;
 *         class=&quot;com.hestia.warroom.frontend.actions.BaseGoToAction&quot;&gt;
 *         &lt;property name=&quot;pricingRecordsManagementService&quot;
 *             ref=&quot;pricingRecordsManagementService&quot;/&gt;
 *     &lt;/bean&gt;
 *
 *     &lt;bean id=&quot;loginAction&quot; parent=&quot;baseAction&quot;
 *         class=&quot;com.hestia.warroom.frontend.actions.LoginAction&quot;/&gt;
 *
 *     &lt;bean id=&quot;logoutAction&quot; parent=&quot;baseAction&quot;
 *         class=&quot;com.hestia.warroom.frontend.actions.LogoutAction&quot;/&gt;
 *
 *     &lt;bean id=&quot;goToInputPriceAction&quot; parent=&quot;baseGoToAction&quot;
 *         class=&quot;com.hestia.warroom.frontend.actions.GoToInputPriceAction&quot;/&gt;
 *
 *     &lt;bean id=&quot;goToAdjustmentsAction&quot; parent=&quot;baseGoToAction&quot;
 *         class=&quot;com.hestia.warroom.frontend.actions.GoToAdjustmentsAction&quot;/&gt;
 *
 *     &lt;bean id=&quot;goToFreightAction&quot; parent=&quot;baseGoToAction&quot;
 *         class=&quot;com.hestia.warroom.frontend.actions.GoToFreightAction&quot;/&gt;
 *
 *     &lt;bean id=&quot;goToBranchOverrideAction&quot; parent=&quot;baseGoToAction&quot;
 *         class=&quot;com.hestia.warroom.frontend.actions.GoToBranchOverrideAction&quot;/&gt;
 *
 *     &lt;bean id=&quot;calculatePriceAction&quot; parent=&quot;baseGoToAction&quot;
 *         class=&quot;com.hestia.warroom.frontend.actions.CalculatePriceAction&quot;/&gt;
 *
 * &lt;/beans&gt;
 * </pre>
 * Struts struts.xml:
 * <pre>
 * &lt;?xml version=&quot;1.0&quot; encoding=&quot;UTF-8&quot; ?&gt;
 * &lt;!DOCTYPE struts PUBLIC
 *     &quot;-//Apache Software Foundation//DTD Struts Configuration 2.0//EN&quot;
 *     &quot;http://struts.apache.org/dtds/struts-2.0.dtd&quot;&gt;
 * &lt;struts&gt;
 *     &lt;constant name=&quot;struts.objectFactory&quot; value=&quot;spring&quot;/&gt;
 *
 *     &lt;package name=&quot;default&quot; namespace=&quot;/&quot; extends=&quot;struts-default&quot;&gt;
 *
 *         &lt;interceptors&gt;
 *             &lt;interceptor name=&quot;authentication&quot; class=&quot;authenticationInterceptor&quot;/&gt;
 *          &lt;interceptor name=&quot;authorization&quot;  class=&quot;authorizationInterceptor&quot;/&gt;
 *             &lt;interceptor-stack name=&quot;securedStack&quot;&gt;
 *              &lt;interceptor-ref name=&quot;defaultStack&quot; /&gt;
 *                 &lt;interceptor-ref name=&quot;authentication&quot; /&gt;
 *                 &lt;interceptor-ref name=&quot;authorization&quot; /&gt;
 *             &lt;/interceptor-stack&gt;
 *         &lt;/interceptors&gt;
 *
 *         &lt;global-results&gt;
 *             &lt;result name=&quot;anonymous&quot; type=&quot;redirect&quot;&gt;
 *                 &lt;param name=&quot;location&quot;&gt;login.jsp&lt;/param&gt;
 *                 &lt;param name=&quot;parse&quot;&gt;false&lt;/param&gt;
 *             &lt;/result&gt;
 *         &lt;/global-results&gt;
 *
 *      &lt;action name=&quot;login&quot; class=&quot;loginAction&quot;&gt;
 *             &lt;result name=&quot;success&quot;&gt;searchInputPrice.jsp&lt;/result&gt;
 *             &lt;result name=&quot;input&quot;&gt;login.jsp&lt;/result&gt;
 *         &lt;/action&gt;
 *
 *         &lt;action name=&quot;goToInputPrice&quot; class=&quot;goToInputPriceAction&quot;&gt;
 *             &lt;result name=&quot;success&quot;&gt;searchInputPriceResult.jsp&lt;/result&gt;
 *         &lt;/action&gt;
 *     &lt;/package&gt;
 * &lt;/struts&gt;
 * </pre>
 * </p>
 * v1.2 - Hestia War Room Enhancements
 *      - added some service for persisting
 * @author woodjhon, hesibo, TCSASSEMBLER
 * @version 1.2
 */
@SuppressWarnings("serial")
public abstract class BaseGoToAction extends BaseAction {
    /**
     * <p>
     * The pricingRecordsManagementService used to represents the pricing records management service.
     * </p>
     * <p>
     * It's injected by Spring IOC and mutable. It can't be null after initialization.
     * </p>
     */
    @EJB(name="PricingRecordsManagementServiceBean", beanInterface=LocalPricingRecordsManagementService.class)
    private PricingRecordsManagementService pricingRecordsManagementService;

    /**
     * <p>
     * The vendorService used to represents the vendor Service.
     * </p>
     * <p>
     * It's injected by Spring IOC and mutable. It can't be null after initialization.
     * </p>
     */
    @EJB(name="VendorServiceBean", beanInterface=LocalVendorService.class)
    private VendorService vendorService;

    /**
     * <p>
     * The branchService used to represents the branch Service.
     * </p>
     * <p>
     * It's injected by Spring IOC and mutable. It can't be null after initialization.
     * </p>
     */
    @EJB(name="BranchServiceBean", beanInterface=LocalBranchService.class)
    private BranchService branchService;

    /**
     * <p>
     * The productTypeService used to represents the productType Service.
     * </p>
     * <p>
     * It's injected by Spring IOC and mutable. It can't be null after initialization.
     * </p>
     */
    @EJB(name="ProductTypeServiceBean", beanInterface=LocalProductTypeService.class)
    private ProductTypeService productTypeService;

    /**
     * <p>
     * The marketService used to represents the market Service.
     * </p>
     * <p>
     * It's injected by Spring IOC and mutable. It can't be null after initialization.
     * </p>
     */
    @EJB(name="MarketServiceBean", beanInterface=LocalMarketService.class)
    private MarketService marketService;

    /**
     * <p>
     * The categoryService used to represents the category Service.
     * </p>
     * <p>
     * It's injected by Spring IOC and mutable. It can't be null after initialization.
     * </p>
     */
    @EJB(name="CategoryServiceBean", beanInterface=LocalCategoryService.class)
    private CategoryService categoryService;

    /**
     * <p>
     * The productService used to represents the product Service.
     * </p>
     * <p>
     * It's injected by Spring IOC and mutable. It can't be null after initialization.
     * </p>
     */
    @EJB(name="ProductServiceBean", beanInterface=LocalProductService.class)
    private ProductService productService;

    /**
     * <p>
     * The userFilterRecordService used to represents the userFilterRecord Service.
     * </p>
     * <p>
     * It's injected by Spring IOC and mutable. It can't be null after initialization.
     * </p>
     */
    @EJB(name="UserFilterRecordServiceBean", beanInterface=LocalUserFilterRecordService.class)
    private UserFilterRecordService userFilterRecordService;

    /**
     * <p>
     * The newListAndMultiplierService used to represents the NewListAndMultiplier management service.
     * </p>
     * <p>
     * It's injected by Spring IOC and mutable. It can't be null after initialization.
     * </p>
     * @since Hestia War Room Enhancements
     */
    @EJB(name="NewListAndMultiplierServiceBean", beanInterface=LocalNewListAndMultiplierService.class)
    private NewListAndMultiplierService newListAndMultiplierService;

    /**
     * <p>
     * The newPPTService used to represents the NewPPT management service.
     * </p>
     * <p>
     * It's injected by Spring IOC and mutable. It can't be null after initialization.
     * </p>
     * @since Hestia War Room Enhancements
     */
    @EJB(name="NewPPTServiceBean", beanInterface=LocalNewPPTService.class)
    private NewPPTService newPPTService;

    /**
     * <p>
     * The pricingAdjustmentService used to represents the pricing adjustment management service.
     * </p>
     * <p>
     * It's injected by Spring IOC and mutable. It can't be null after initialization.
     * </p>
     * @since Hestia War Room Enhancements
     */
    @EJB(name="PricingAdjustmentServiceBean", beanInterface=LocalPricingAdjustmentService.class)
    private PricingAdjustmentService pricingAdjustmentService;

    /**
     * <p>
     * The newFreightRateService used to represents the newFreightRate management service.
     * </p>
     * <p>
     * It's injected by Spring IOC and mutable. It can't be null after initialization.
     * </p>
     * @since Hestia War Room Enhancements
     */
    @EJB(name="NewFreightRateServiceBean", beanInterface=LocalNewFreightRateService.class)
    private NewFreightRateService newFreightRateService;

    /**
     * <p>
     * The branchOverrideService used to represents the branch override management service.
     * </p>
     * <p>
     * It's injected by Spring IOC and mutable. It can't be null after initialization.
     * </p>
     * @since Hestia War Room Enhancements
     */
    @EJB(name="BranchOverrideServiceBean", beanInterface=LocalBranchOverrideService.class)
    private BranchOverrideService branchOverrideService;

    /**
     * <p>
     * The userFilterRecord used to represents the user filter record.
     * </p>
     * <p>
     * It's passed as the http input parameter for this action. It can't be null after initialization.
     * </p>
     */
    private UserFilterRecord userFilterRecord;

    /**
     * <p>
     * The targetDate used to represents the target date.
     * </p>
     * <p>
     * It's passed as the http input parameter for this action. It can be any value.
     * </p>
     */
    private Date targetDate;

    /**
     * <p>
     * The criteria used to represents the criteria.
     * </p>
     * <p>
     * It's passed as the http input parameter for this action. It can't be null after initialization.
     * </p>
     */
    private BaseCriteria criteria;

    /**
     * The filter id.
     * @since 1.1
     */
    private long filterId;

    /**
     * Flag indicating the filter must be loaded during execution.
     * @since 1.1
     */
    private boolean loadFilter = false;


    /**
     * <p>
     * Create the instance.
     * </p>
     */
    protected BaseGoToAction() {
    }

    /**
     * <p>
     * Check that the required fields are injected.
     * </p>
     *
     * @throws HestiaWarRoomServiceConfigurationException if pricingRecordsManagementService is null.
     */
    @PostConstruct
    protected void checkInit() {
        if (pricingRecordsManagementService == null) {
            throw new HestiaWarRoomServiceConfigurationException("pricingRecordsManagementService can't be null.");
        }
    }

    /**
     * <p>
     * Get user filter record.
     * </p>
     *
     * @return the user filter record.
     */
    public UserFilterRecord getUserFilterRecord() {
        return userFilterRecord;
    }

    /**
     * <p>
     * Set user filter record.
     * </p>
     *
     * @param userFilterRecord the user filter record.
     */
    public void setUserFilterRecord(UserFilterRecord userFilterRecord) {
        this.userFilterRecord = userFilterRecord;
    }

    /**
     * <p>
     * Get target date.
     * </p>
     *
     * @return the target date.
     */
    public Date getTargetDate() {
        return targetDate;
    }

    /**
     * <p>
     * Set target date.
     * </p>
     *
     * @param targetDate the target date.
     */
    public void setTargetDate(Date targetDate) {
        this.targetDate = targetDate;
    }

    /**
     * <p>
     * Get search criteria.
     * </p>
     *
     * @return the search criteria.
     */
    public BaseCriteria getCriteria() {
        return criteria;
    }

    /**
     * <p>
     * Set search criteria.
     * </p>
     *
     * @param criteria the search criteria.
     */
    public void setCriteria(BaseCriteria criteria) {
        this.criteria = criteria;
    }

    /**
     * <p>
     * Gets the pricing records management service.
     * </p>
     *
     * @return the pricing records management service.
     */
    protected PricingRecordsManagementService getPricingRecordsManagementService() {
        return pricingRecordsManagementService;
    }

    /**
     * <p>
     * Sets the pricing records management service.
     * </p>
     *
     * @param pricingRecordsManagementService the pricing records management service.
     */
    public void setPricingRecordsManagementService(PricingRecordsManagementService pricingRecordsManagementService) {
        this.pricingRecordsManagementService = pricingRecordsManagementService;
    }

    /**
     * <p>
     * Gets the vendorService.
     * </p>
     *
     * @return the vendorService.
     */
    public VendorService getVendorService() {
        return vendorService;
    }

    /**
     * <p>
     * Sets the vendorService.
     * </p>
     *
     * @param vendorService the vendorService.
     */
    public void setVendorService(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    /**
     * <p>
     * Gets the branchService.
     * </p>
     *
     * @return the branchService.
     */
    public BranchService getBranchService() {
        return branchService;
    }

    /**
     * <p>
     * Sets the branchService.
     * </p>
     *
     * @param branchService the branchService.
     */
    public void setBranchService(BranchService branchService) {
        this.branchService = branchService;
    }

    /**
     * <p>
     * Gets the productTypeService.
     * </p>
     *
     * @return the productTypeService.
     */
    public ProductTypeService getProductTypeService() {
        return productTypeService;
    }

    /**
     * <p>
     * Sets the productTypeService.
     * </p>
     *
     * @param productTypeService the productTypeService.
     */
    public void setProductTypeService(ProductTypeService productTypeService) {
        this.productTypeService = productTypeService;
    }

    /**
     * <p>
     * Gets the marketService.
     * </p>
     *
     * @return the marketService.
     */
    public MarketService getMarketService() {
        return marketService;
    }
    /**
     * <p>
     * Sets the marketService.
     * </p>
     *
     * @param marketService the marketService.
     */
    public void setMarketService(MarketService marketService) {
        this.marketService = marketService;
    }

    /**
     * <p>
     * Gets the categoryService.
     * </p>
     *
     * @return the categoryService.
     */
    public CategoryService getCategoryService() {
        return categoryService;
    }

    /**
     * <p>
     * Sets the categoryService.
     * </p>
     *
     * @param categoryService the categoryService.
     */
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * <p>
     * Gets the productService.
     * </p>
     *
     * @return the productService.
     */
    public ProductService getProductService() {
        return productService;
    }

    /**
     * <p>
     * Sets the productService.
     * </p>
     *
     * @param productService the productService.
     */
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    /**
     * <p>
     * Gets the userFilterRecordService.
     * </p>
     *
     * @return the userFilterRecordService.
     */
    public UserFilterRecordService getUserFilterRecordService() {
        return userFilterRecordService;
    }

    /**
     * <p>
     * Sets the userFilterRecordService.
     * </p>
     *
     * @param userFilterRecordService the userFilterRecord Service.
     */
    public void setUserFilterRecordService(
            UserFilterRecordService userFilterRecordService) {
        this.userFilterRecordService = userFilterRecordService;
    }

    /**
     * Gets the value of the field <code>filterId</code>.
     * @return the filterId
     */
    public long getFilterId() {
        return filterId;
    }

    /**
     * Sets the value of the field <code>filterId</code>.
     * @param filterId the filterId to set
     */
    public void setFilterId(long filterId) {
        this.filterId = filterId;
    }

    /**
     * Gets the value of the field <code>loadFilter</code>.
     * @return the loadFilter
     */
    public boolean isLoadFilter() {
        return loadFilter;
    }

    /**
     * Sets the value of the field <code>loadFilter</code>.
     * @param loadFilter the loadFilter to set
     */
    public void setLoadFilter(boolean loadFilter) {
        this.loadFilter = loadFilter;
    }

    /**
     * <p>
     * Retrieves the pricingAdjustmentService field.
     * </p>
     *
     * @return the value of pricingAdjustmentService
     */
    public PricingAdjustmentService getPricingAdjustmentService() {
        return pricingAdjustmentService;
    }

    /**
     * <p>
     * Sets the value to pricingAdjustmentService field.
     * </p>
     *
     * @param pricingAdjustmentService the value of pricingAdjustmentService to set
     */
    public void setPricingAdjustmentService(PricingAdjustmentService pricingAdjustmentService) {
        this.pricingAdjustmentService = pricingAdjustmentService;
    }

    /**
     * <p>
     * Retrieves the newFreightRateService field.
     * </p>
     *
     * @return the value of newFreightRateService
     */
    public NewFreightRateService getNewFreightRateService() {
        return newFreightRateService;
    }

    /**
     * <p>
     * Sets the value to newFreightRateService field.
     * </p>
     *
     * @param newFreightRateService the value of newFreightRateService to set
     */
    public void setNewFreightRateService(NewFreightRateService newFreightRateService) {
        this.newFreightRateService = newFreightRateService;
    }

    /**
     * <p>
     * Retrieves the branchOverrideService field.
     * </p>
     *
     * @return the value of branchOverrideService
     */
    public BranchOverrideService getBranchOverrideService() {
        return branchOverrideService;
    }

    /**
     * <p>
     * Sets the value to branchOverrideService field.
     * </p>
     *
     * @param branchOverrideService the value of branchOverrideService to set
     */
    public void setBranchOverrideService(BranchOverrideService branchOverrideService) {
        this.branchOverrideService = branchOverrideService;
    }

    /**
     * <p>
     * Retrieves the newListAndMultiplierService field.
     * </p>
     *
     * @return the value of newListAndMultiplierService
     */
    public NewListAndMultiplierService getNewListAndMultiplierService() {
        return newListAndMultiplierService;
    }

    /**
     * <p>
     * Sets the value to newListAndMultiplierService field.
     * </p>
     *
     * @param newListAndMultiplierService the value of newListAndMultiplierService to set
     */
    public void setNewListAndMultiplierService(NewListAndMultiplierService newListAndMultiplierService) {
        this.newListAndMultiplierService = newListAndMultiplierService;
    }

    /**
     * <p>
     * Retrieves the newPPTService field.
     * </p>
     *
     * @return the value of newPPTService
     */
    public NewPPTService getNewPPTService() {
        return newPPTService;
    }

    /**
     * <p>
     * Sets the value to newPPTService field.
     * </p>
     *
     * @param newPPTService the value of newPPTService to set
     */
    public void setNewPPTService(NewPPTService newPPTService) {
        this.newPPTService = newPPTService;
    }

}
