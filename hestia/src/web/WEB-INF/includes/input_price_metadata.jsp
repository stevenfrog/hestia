<%--
  - Author: stevenfrog
  - Version: 1.0
  - Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
  -
  - Description: Renders additional information for the input price search.
--%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="result-metadata-container">
	<span class="total-matches">${inputPriceRecords.total}</span>
	<span class="view-result-size">${viewResultSize}</span>
	<span class="current-page">${inputPriceRecords.page}</span>
	<span class="current-sort-column">${criteria.sortingColumn}</span>
	<span class="current-sort-direction">${criteria.sortingType}</span>
</div>

<c:if test="${newSearch}">
<div class="result-tree-container">
	<div class="tree-panel">
		<div class="tree-inner">
			<div class="scroll-pane">
				<!-- tree -->
				<div class="tree">
					<ul>
						<li class="vendorDrillDown">
							<a href="javascript:;">Vendor (${fn:length(searchResultTree.shipPoints)})</a>
							<ul>
								<c:forEach var="item" items="${searchResultTree.shipPoints}" varStatus="status">
									<li class="drilldownItem ${status.last ? 'bottom' : ''}"><a href="javascript:;" rel="${item.id}">${item.name}</a></li>
								</c:forEach>
							</ul>
						</li>
						<li class="productTypeDrillDown">
							<a href="javascript:;" class="unfolder">Type (${fn:length(searchResultTree.productTypes)})</a>
							<ul class="hidden">
								<c:forEach var="item" items="${searchResultTree.productTypes}" varStatus="status">
									<li class="drilldownItem ${status.last ? 'bottom' : ''}"><a href="javascript:;" rel="${item.id}">${item.name}</a></li>
								</c:forEach>
							</ul>
						</li>
						<li class="marketDrillDown">
							<a href="javascript:;" class="unfolder">Market (${fn:length(searchResultTree.markets)})</a>
							<ul class="hidden">
								<c:forEach var="item" items="${searchResultTree.markets}" varStatus="status">
									<li class="drilldownItem ${status.last ? 'bottom' : ''}"><a href="javascript:;" rel="${item.id}">${item.name}</a></li>
								</c:forEach>
							</ul>
						</li>
						<li class="branchDrillDown">
							<a href="javascript:;" class="unfolder">Branch (${fn:length(searchResultTree.branches)})</a>
							<ul class="hidden">
								<c:forEach var="item" items="${searchResultTree.branches}" varStatus="status">
									<li class="drilldownItem ${status.last ? 'bottom' : ''}"><a href="javascript:;" rel="${item.id}">${item.profitCenter}</a></li>
								</c:forEach>
							</ul>
						</li>
						<li class="categoryDrillDown">
							<a href="javascript:;">Category (${fn:length(searchResultTree.categories)})</a>
							<ul>
								<c:forEach var="item" items="${searchResultTree.categories}" varStatus="status">
									<li class="drilldownItem ${status.last ? 'bottom' : ''}"><a href="javascript:;" rel="${item.id}">${item.name}</a></li>
								</c:forEach>
							</ul>
						</li>
						<li class="productDrillDown">
							<a href="javascript:;" class="unfolder">Product (${fn:length(searchResultTree.products)})</a>
							<ul class="hidden">
								<c:forEach var="item" items="${searchResultTree.products}" varStatus="status">
									<li class="drilldownItem ${status.last ? 'bottom' : ''}"><a href="javascript:;" rel="${item.id}">${item.alternateCode1}</a></li>
								</c:forEach>
							</ul>
						</li>
					</ul>

				</div>
				<!-- End .tree -->

			</div>

		</div>

	</div>

	<div class="toggle">
		<a href="javascript:;">TOGGLE</a>
	</div>
</div>
</c:if>