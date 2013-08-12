/*
 * Copyright (C) 2012 TopCoder Inc., All Rights Reserved.
 */
package com.hestia.warroom.frontend.actions;

import java.util.Map;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.hestia.warroom.entities.User;
import com.hestia.warroom.frontend.Helper;
import com.hestia.warroom.services.HestiaWarRoomServiceConfigurationException;
import com.hestia.warroom.services.LocalSecurityService;
import com.hestia.warroom.services.SecurityService;
import com.hestia.warroom.services.dto.ServiceContext;
import com.opensymphony.xwork2.ActionSupport;

/**
 * <p>
 * This is the base class for all actions defined in this module.
 * </p>
 *
 * It provides a logger to be used by subclasses to perform logging. it implements SessionAware and ServletRequestAware,
 * it holds references to the session map and servlet request and provide getters to retrieve it the by the subclasses.
 * This class is configured in the constructor by loading the configuration parameters from the PropsUtil class.
 *
 * <p>
 * <strong>Thread Safety: </strong> This class is thread safe as it is immutable.
 * </p>
 *
 * v1.1 -  Pricing Calculations Assembly - added get service context method
 * v1.2 - Hestia War Room Enhancements
 *      - added field USER_TO_PERSIST_MY_PRICING
 * @author Schpotsky, TCSDEVELOPER
 * @version 1.2
 */
@SuppressWarnings("serial")
public abstract class BaseAction extends ActionSupport implements ServletRequestAware, ServletResponseAware,
                SessionAware {

    /**
     * Session holder for search results filter.
     */
    public static final String USER_SEARCH_CRITERIA = "LAST_EXECUTED_CRITERIA";
    /**
     * Session holder for search results filter.
     */
    public static final String USER_SEARCH_FILTER = "LAST_EXECUTED_FILTER";

    /**
     * Session holder for changed my pricing.
     * @since 1.2 Hestia War Room Enhancements
     */
    public static final String USER_TO_PERSIST_MY_PRICING = "USER_TO_PERSIST_MY_PRICING";

    /**
     * <p>
     * The SecurityService EJB implementation to be used by subclasses to perform security operations.
     * </p>
     * It is injected using @EJB annotation from the Struts2 EJB 3 plugin. Can not be null after injection.
     * It has a setter and a getter.
     * <p>
     * It is used in the subclasses.
     * </p>
     */
    @EJB(name="SecurityServiceBean", beanInterface=LocalSecurityService.class)
    private SecurityService securityService;
    /**
     * <p>
     * The Log4j Logger instance to be used by subclasses of this action class to perform logging.
     * </p>
     * It is initialized in this class constructor, it can be re-initialized in the subclasses using the corresponding
     * protected setter. It has a getter and a setter. Can not be null after initialization.
     * <p>
     * It is used in subclasses to log errors and exceptions.
     * </p>
     */
    private Logger logger;
    /**
     * <p>
     * The url to which to redirect the user when he logs out from the application.
     * </p>
     * It is initialized in the constructor and never changed after that. It has a getter. Can not be null after
     * initialization.
     * <p>
     * It is used in subclasses to redirect the user when he logs out from the application.
     * </p>
     */
    private final String logoutUrl;
    /**
     * <p>
     * The key under which the current user is stored in the session map.
     * </p>
     * It is initialized in the constructor and never changed after that. Can not be null after initialization. It has a
     * getter.
     * <p>
     * It is used in getCurrentUser() and in the subclasses to access the user entity in the session map.
     * </p>
     */
    private final String userSessionKey;
    /**
     * <p>
     * Represents the name of the login page.
     * </p>
     * It is initialized in the constructor and never changed after that. Can not be null after initialization. It has a
     * getter.
     * <p>
     * It is used in subclasses to redirect the user to the login page.
     * </p>
     */
    private final String loginPageName;
    /**
     * <p>
     * The map holding the user session data.
     * </p>
     * It is initialized by Struts framework using the corresponding setter. It can not be null but can be empty, the
     * key can be any value, the contained values can be any value. It has a setter and a getter.
     * <p>
     * It is used in getCurrentUser() and in the subclasses to access the user session data.
     * </p>
     */
    private Map<String, Object> session;
    /**
     * <p>
     * The http servlet request. This is injected by Struts.
     * </p>
     * It has both getter and setter. Can not be null.
     * <p>
     * It is used in the subclasses actions to access the request data.
     * </p>
     */
    private HttpServletRequest servletRequest;
    /**
     * <p>
     * The http servlet response. This is injected by Struts. It is set through setter and retrieved by a getter.
     * </p>
     * It can not be null. It has a setter and a getter.
     * <p>
     * It is used in execute() method of subclasses.
     * </p>
     */
    private HttpServletResponse servletResponse;

    /**
     * Creates an instance of this class and loads the configuration parameters from the PropsUtil class.
     *
     * @throws HestiaWarRoomServiceConfigurationException
     *             if any error occurred during configuration.
     */
    protected BaseAction() {
        logoutUrl = Helper.checkParameter("logoutUrl");
        userSessionKey = Helper.checkParameter("userSessionKey");
        loginPageName = Helper.checkParameter("loginPageName");
        logger = Logger.getLogger(this.getClass().getSimpleName());
    }

    /**
     * Sets the url to which to redirect the user when he logs out from the application.
     *
     * @return the url to which to redirect the user when he logs out from the application.
     */
    protected String getLogoutUrl() {
        return logoutUrl;
    }

    /**
     * Gets the key under which the current user is stored in the session map.
     *
     * @return the key under which the current user is stored in the session map.
     */
    protected String getUserSessionKey() {
        return userSessionKey;
    }

    /**
     * Gets the name of the login page.
     *
     * @return the name of the login page.
     */
    protected String getLoginPageName() {
        return loginPageName;
    }

    /**
     * Gets the map holding the user session data.
     *
     * @return the map holding the user session data.
     */
    protected Map<String, Object> getSession() {
        return session;
    }

    /**
     * Sets the map holding the user session data.
     *
     * @param session
     *            the map holding the user session data.
     */
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }

    /**
     * Gets the http servlet request.
     *
     * @return the http servlet request.
     */
    protected HttpServletRequest getServletRequest() {
        return servletRequest;
    }

    /**
     * Sets the http servlet request.
     *
     * @param servletRequest
     *            the http servlet request.
     */
    public void setServletRequest(HttpServletRequest servletRequest) {
        this.servletRequest = servletRequest;
    }

    /**
     * Gets the currently logged in user.
     *
     * @return the user entity representing the currently logged-in user. (can be null).
     * @throws HestiaWarRoomServiceConfigurationException
     *             if user in session is invalid.
     */
    protected User getCurrentUser() {
        String signature = "BaseAction.getCurrentUser()";
        return Helper.getCurrentUser(signature, logger, getUserSessionKey(), session);
    }

    /**
     * Sets the Log4j Logger instance to be used by subclasses of this action class to perform logging.
     *
     * @param logger
     *            the Log4j Logger instance to be used by subclasses of this action class to perform logging.
     */
    protected void setLogger(Logger logger) {
        this.logger = logger;
    }

    /**
     * Gets the Log4j Logger instance to be used by subclasses of this action class to perform logging.
     *
     * @return the Log4j Logger instance to be used by subclasses of this action class to perform logging.
     */
    protected Logger getLogger() {
        return logger;
    }

    /**
     * Sets the http servlet response.
     *
     * @param servletResponse
     *            the http servlet response.
     */
    public void setServletResponse(HttpServletResponse servletResponse) {
        this.servletResponse = servletResponse;
    }

    /**
     * Gets the http servlet response.
     *
     * @return the http servlet response.
     */
    protected HttpServletResponse getServletResponse() {
        return servletResponse;
    }

    /**
     * Gets the SecurityService EJB implementation to be used by subclasses to perform security operations.
     *
     * @return the SecurityService EJB implementation to be used by subclasses to perform security operations.
     */
    protected SecurityService getSecurityService() {
        return securityService;
    }

    /**
     * Sets the SecurityService EJB implementation to be used by subclasses to perform security operations.
     *
     * @param securityService
     *            the SecurityService EJB implementation to be used by subclasses to perform security operations.
     */
    public void setSecurityService(SecurityService securityService) {
        this.securityService = securityService;
    }

    /**
     * Retrieves the current context variables.
     * @return the context variables
     * @since Price Calculations Assembly
     */
    protected final ServiceContext getServiceContext() {
    	ServiceContext ctx = new ServiceContext();
    	ctx.setUser(getCurrentUser());
		return ctx;
    }
}
