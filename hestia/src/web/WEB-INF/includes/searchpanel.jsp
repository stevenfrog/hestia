<%--
  - Author: stevenfrog
  - Version: 1.1
  - Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
  -
  - Description: Common user search filter rendering.
  - v1.1 - Export and Search Filter - applied UI updates, implemented initial rendering from a saved filter
--%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!-- filter -->
<div class="filter">

    <!-- header -->
    <div class="filter-header" id="${loadFilter ?  'search-on-startup' : 'filter-header'}">
        <div class="left">
            <div class="right">
             <a href="javascript:;">&nbsp;</a>
             <div class="headerTitle vendors">Vendors</div>
             <div class="headerTitle type">Type</div>
			 <div class="headerTitle category">Category<div class="select-all"></div></div>
		 	 <div class="headerTitle productCode">Product Code<div class="select-all"></div></div>
			 <div class="headerTitle market">Market<div class="select-all"></div></div>
			 <div class="headerTitle branch">Branch<div class="select-all"></div></div>
             <div class="clear"></div>
            </div>
        </div>
    </div>
    <!-- End .filter-header -->

    <!-- body -->
    <div class="filter-body">

        <!-- section -->
        <div class="filter-section">
            <div class="filter-list">

                <ul>
                    <s:form action="SelectVendors" id="FilterDependencyForm" namespace="/ajax" theme="simple">
                        <input type="hidden" value="placeholder"/>
                    </s:form>

                    <s:form action="SearchAction" id="searchForm" namespace="/">
                        <s:hidden name="newSearch" value="true" />
                        <s:hidden name="onlyLowestNetPrice" value="true" />
                        <s:hidden id="whichType" name="whichType" value="" />
                        <s:hidden name="criteria.page" value="1" />
                        <s:hidden name="criteria.pageSize" value="20" />
                        <s:hidden name="criteria.sortingColumn" value="shipPoint.name" />
                        <s:hidden name="criteria.sortingType" value="ASC" />

                        <!-- column -->
                        <li class="column vendors">

                            <div class="filter-search">
                                <input type="text" id="allVendorsId" class="text defalut" value="Search" />
                            </div>

                            <div id="allVendors_id" class="scroll">
                                <ul>
                                    <%-- Process Domestic Vendors --%>
                                    <li class="hasChildren">
                                        <span class="arrow"></span>
                                        <input name="vendorParent" type="checkbox" class="checkbox" />
                                        <label>DOMESTIC</label>
                                        <div class="clear"></div>
                                        <ul class="children">
                                            <s:iterator value="#session.allVendors" var="vendor" status="status">
                                                <s:iterator value="%{#vendor.shipPoints}" var="shipPoint">
                                                <c:if test="${shipPoint.typeId == 1 or shipPoint.typeId == 5 or shipPoint.typeId == 6}">
                                                <c:set var="isItemChecked" value="${false}"></c:set>
                                                <c:forEach var="userSelection" items="${userFilterRecord.shipPoints}">
                                                    <c:if test="${userSelection.id == shipPoint.id}">
                                                        <c:set var="isItemChecked" value="${true}"></c:set>
                                                    </c:if>
                                                </c:forEach>
                                                <li class="${isItemChecked ? 'selected' : ''}"><input name="vendor" type="checkbox" ${isItemChecked ? 'checked' : ''} value="<s:property value="%{#shipPoint.id}"/>" /><label id='<s:property value="%{#shipPoint.id}"/>'><s:property value="name" /></label><div class="clear" /></li>
                                                </c:if>
                                                </s:iterator>
                                            </s:iterator>
                                        </ul>
                                    </li>
                                    <%-- Process DC Vendors --%>
                                    <li class="hasChildren">
                                        <span class="arrow"></span>
                                        <input name="vendorParent" type="checkbox" class="checkbox" />
                                        <label>DISTRIBUTION CENTER</label>
                                        <div class="clear"></div>
                                        <ul class="children">
                                            <s:iterator value="#session.allVendors" var="vendor" status="status">
                                                <s:iterator value="%{#vendor.shipPoints}" var="shipPoint">
                                                <c:if test="${shipPoint.isADC}">
                                                <c:set var="isItemChecked" value="${false}"></c:set>
                                                <c:forEach var="userSelection" items="${userFilterRecord.shipPoints}">
                                                    <c:if test="${userSelection.id == shipPoint.id}">
                                                        <c:set var="isItemChecked" value="${true}"></c:set>
                                                    </c:if>
                                                </c:forEach>
                                                <li class="${isItemChecked ? 'selected' : ''}"><input name="vendor" type="checkbox" ${isItemChecked ? 'checked' : ''} value="<s:property value="%{#shipPoint.id}"/>" /><label id='<s:property value="%{#shipPoint.id}"/>'><s:property value="name" /></label><div class="clear" /></li>
                                                </c:if>
                                                </s:iterator>
                                            </s:iterator>
                                        </ul>
                                    </li>
                                    <%-- Process IMPORT Vendors --%>
                                    <li class="hasChildren">
                                        <span class="arrow"></span>
                                        <input name="vendorParent" type="checkbox" class="checkbox" />
                                        <label>IMPORT</label>
                                        <div class="clear"></div>
                                        <ul class="children">
                                            <s:iterator value="#session.allVendors" var="vendor" status="status">
                                                <s:iterator value="%{#vendor.shipPoints}" var="shipPoint">
                                                <c:if test="${shipPoint.typeId == 2 or shipPoint.typeId == 3 or shipPoint.typeId == 4 or shipPoint.typeId == 7 or shipPoint.typeId == 8}">
                                                <c:set var="isItemChecked" value="${false}"></c:set>
                                                <c:forEach var="userSelection" items="${userFilterRecord.shipPoints}">
                                                    <c:if test="${userSelection.id == shipPoint.id}">
                                                        <c:set var="isItemChecked" value="${true}"></c:set>
                                                    </c:if>
                                                </c:forEach>
                                                <li class="${isItemChecked ? 'selected' : ''}"><input name="vendor" type="checkbox" ${isItemChecked ? 'checked' : ''} value="<s:property value="%{#shipPoint.id}"/>" /><label id='<s:property value="%{#shipPoint.id}"/>'><s:property value="name" /></label> <div class="clear" /></li>
                                                </c:if>
                                                </s:iterator>
                                            </s:iterator>
                                        </ul>
                                    </li>
                                </ul>
                            </div>
                        </li>
                        <!-- End .filter-column -->

                        <!-- column -->
                        <li class="column type">

                            <div class="filter-search">
                                <input type="text" id="allTypesId" class="text defalut" value="Search" />
                            </div>

                            <div id="allTypes_id" class="scroll">
                                <ul>
                                    <s:iterator value="allTypes" var="type" status="status">
                                        <c:set var="isItemChecked" value="${false}"></c:set>
                                        <c:forEach var="userSelection" items="${userFilterRecord.productTypes}">
                                            <c:if test="${userSelection.id == type.id}">
                                                <c:set var="isItemChecked" value="${true}"></c:set>
                                            </c:if>
                                        </c:forEach>
                                        <li class="${isItemChecked ? 'selected' : ''}">
                                            <input type="checkbox" ${isItemChecked ? 'checked' : ''} class="checkbox" value="<s:property value="%{#type.id}"/>" />
                                            <label id='<s:property value="%{#type.id}"/>'><s:property value="name" /></label>
                                            <div class="clear"></div>
                                        </li>
                                    </s:iterator>
                                </ul>
                            </div>

                        </li>
                        <!-- End .filter-column -->



                        <!-- column -->
                        <li class="column category">
                            <div class="filter-search">
                                <input type="text" id="allCategoriesId" class="text defalut" value="Search" />
                            </div>

                            <div id="allCategories_id" class="scroll">
                                <ul>
                                    <s:iterator value="allCategories" var="category" status="status">
                                        <c:set var="isItemChecked" value="${false}"></c:set>
                                        <c:forEach var="userSelection" items="${userFilterRecord.categories}">
                                            <c:if test="${userSelection.id == category.id}">
                                                <c:set var="isItemChecked" value="${true}"></c:set>
                                            </c:if>
                                        </c:forEach>
                                        <li class="${isItemChecked ? 'selected' : ''}">
                                            <input type="checkbox" ${isItemChecked ? 'checked' : ''} class="checkbox" value="<s:property value="%{#category.id}"/>" />
                                            <label id='<s:property value="%{#category.id}"/>'><s:property value="name" /></label>
                                            <div class="clear"></div>
                                        </li>
                                    </s:iterator>
                                </ul>
                            </div>
                        </li>
                        <!-- End .filter-column -->

                        <!-- column -->
                        <li class="column productCode">

                            <div class="filter-search">
                                    <input type="text" id="allProductsId" class="text defalut" value="Search" />
                            </div>

                            <div id="allProducts_id" class="scroll">
                                <ul>
                                    <s:iterator value="allProducts" var="product">
                                        <c:set var="isItemChecked" value="${false}"></c:set>
                                        <c:forEach var="userSelection" items="${userFilterRecord.products}">
                                            <c:if test="${userSelection.id == product.id}">
                                                <c:set var="isItemChecked" value="${true}"></c:set>
                                            </c:if>
                                        </c:forEach>
                                        <li class="${isItemChecked ? 'selected' : ''}">
                                            <input type="checkbox" ${isItemChecked ? 'checked' : ''} class="checkbox" value="<s:property value="%{#product.id}"/>" />
                                            <label id='<s:property value="%{#product.id}"/>'><s:property value="alternateCode1" /></label>
                                            <div class="clear"></div>
                                        </li>
                                    </s:iterator>
                                </ul>
                            </div>
                        </li>
                        <!-- End .filter-column -->

                        <!-- column -->
                        <li class="column market">
                            <div class="filter-search">
                                <input type="text" id="allMarketsId" class="text defalut" value="Search" />
                            </div>

                            <div id="allMarkets_id" class="scroll">
                                <ul>
                                    <s:iterator value="allMarkets" var="market" status="status">
                                    	<c:set var="isItemChecked" value="${false}"></c:set>
                                        <c:forEach var="userSelection" items="${userFilterRecord.markets}">
                                            <c:if test="${userSelection.id == market.id}">
                                                <c:set var="isItemChecked" value="${true}"></c:set>
                                            </c:if>
                                        </c:forEach>
                                        <li class="${isItemChecked ? 'selected' : ''}">
                                            <input type="checkbox" ${isItemChecked ? 'checked' : ''} class="checkbox" value="<s:property value="%{#market.id}"/>" />
                                            <label id='<s:property value="%{#market.id}"/>'><s:property value="name" /></label>
                                            <div class="clear"></div>
                                        </li>
                                    </s:iterator>
                                </ul>
                            </div>

                        </li>
                        <!-- End .filter-column -->

                        <!-- column -->
                        <li class="column branch last-column">

                            <div class="filter-search">
                                <input type="text" id="allBranchesId" class="text defalut" value="Search" />
                            </div>

                            <div id="allBranches_id" class="scroll">
                                <ul>
                                    <s:iterator value="allBranches" var="branch" status="status">
                                    	<c:set var="isItemChecked" value="${false}"></c:set>
                                        <c:forEach var="userSelection" items="${userFilterRecord.branches}">
                                            <c:if test="${userSelection.id == branch.id}">
                                                <c:set var="isItemChecked" value="${true}"></c:set>
                                            </c:if>
                                        </c:forEach>
                                        <li class="${isItemChecked ? 'selected' : ''}">
                                            <input type="checkbox" ${isItemChecked ? 'checked' : ''} class="checkbox" value="<s:property value="%{#branch.id}"/>" />
                                            <label id='<s:property value="%{#branch.id}"/>'><s:property value="profitCenter" /></label>
                                            <div class="clear"></div>
                                        </li>
                                    </s:iterator>
                                </ul>
                            </div>

                        </li>
                        <!-- End .filter-column -->
                    </s:form>

                    <s:form action="ExportResultsAction" id="ExportResultsForm" namespace="/">
                        <input type="hidden" name="whichType" value="placeholder"/>
                        <input type="hidden" name="searchType" value="placeholder"/>
                    </s:form>

                </ul>

            </div>
        </div>
        <!-- End .filter-section -->

        <!-- button -->
        <div class="filter-button">
            <div class="left">
                <div class="right">
                    <a href="javascript:;" class="btn-search">SEARCH</a>
                </div>
            </div>
        </div>
        <!-- End .filter-button -->
    </div>
    <!-- End .filter-body -->

</div>
<!-- End .filter -->