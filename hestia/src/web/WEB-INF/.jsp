<%--
  - Author: stevenfrog
  - Date: 2013-07-19
  - Version: 1.0
  - Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
  -
  - Description: Page of Adjustment search
--%>
<%@ include file="/WEB-INF/includes/taglibs.jsp" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<sj:head jqueryui="true" jquerytheme="cupertino"/>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Hestia</title>
<%@ include file="/WEB-INF/includes/user_html_head.jsp" %>
</head>

<body>

    <!-- wrapper -->
    <div id="wrapper">

        <div id="main">

            <!-- header -->
            <div id="header">

                <div class="inner">
					<%@ include file="/WEB-INF/includes/topnav.jsp" %>

					<%@ include file="/WEB-INF/includes/headertitle.jsp" %>

					<c:set var="currentTab" value="${4}"></c:set>
					<%@ include file="/WEB-INF/includes/mainnavuser.jsp" %>

                </div>

            </div>
            <!-- End #header -->

            <!-- main container -->
            <div id="main-container" class="admin">

                <div class="inner" id="input_prices">

                    <h3>ADJUSTMENTS</h3>

                    <%@ include file="/WEB-INF/includes/searchpanel.jsp" %>

                    <!-- comfirm -->
                    <div class="comfirm comfirm-filter">
                        <p>Please Select the criteria to Search for the Products from the Vendor or <br />Choose the previously saved filter to see the Products.</p>
                        <div class="corner tl"></div>
                        <div class="corner tr"></div>
                        <div class="corner bl"></div>
                        <div class="corner br"></div>
                    </div>
                    <!-- End .comfirm -->


                    <%@ include file="/WEB-INF/includes/adjustment_results_section.jsp" %>

                    <!-- tip -->
                    <div class="tip-save-search">
                        <div class="top">
                            <div class="bottom">
                                <div class="middle">
                                    <div class="tip-inner">
                                        <div class="tip-input">
                                            <label>Search Name:</label>
                                            <input type="text" class="text" maxlength="50"/>
                                        </div>
                                        <div class="buttons">
                                            <a href="javascript:;" class="btn-save">SAVE</a>
                                            <a href="javascript:;">Cancel</a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- tip for hover -->
                    <div class="tip-for-prices-hover">
                        <div class="start-date">
                            <strong>Start Date</strong>
                            <span>3/1/2012</span>
                        </div>
                        <div class="end-date">
                            <strong>End Date</strong>
                            <span>3/1/2012</span>
                        </div>
                        <div class="clear"></div>
                        <div class="comment">
                            <strong>Comment by Username:</strong>
                            <span>Additional cost for this SKU due to limited availability</span>
                        </div>
                    </div>

                    <!-- tip for click -->
                    <div class="tip-for-prices-click">
                        <div class="left">
                            <div class="right">
                                <div class="middle">
                                    <div class="tip-inner">
                                        <div class="header-text">
                                            <h2>Enter New Value</h2>
                                        </div>
                                        <div class="body">
                                            <div class="start-date">
                                                <label>Value:<strong>*</strong></label>
                                                <input type="text" name="adjustmentValue" class="text" maxlength="8"/>
                                                <input type="hidden" name="modificationType" />
                                                <input type="hidden" name="pricingRecordId" />
                                            </div>
                                            <div class="end-date">
                                                <label>End Date:<strong>*</strong></label>
                                                <input type="text" name="adjustmentEndDate" class="text date-picker" readonly="readonly" />
                                            </div>
                                            <div class="clear"></div>
                                            <div class="comment">
                                                <label>Comments:<strong>*</strong></label>
                                                <textarea rows="3" cols="20" name="adjustmentComment" class="textarea"></textarea>
                                            </div>
                                        </div>
                                        <div class="button">
                                            <a href="javascript:;" class="btn-modify">MODIFY</a>
                                            <a href="javascript:;">Cancel</a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                </div>

            </div>
            <!-- End #main-container -->
            <div class="hide" id="ajax-fragment-container">

            </div>

            <div id="escapeelement" style="display: none;">&nbsp;</div>
        </div>

    </div>
    <!-- End #wrapper -->

	<%@ include file="/WEB-INF/includes/footer.jsp" %>

</body>
</html>
