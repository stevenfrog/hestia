<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<div class="table-data-container">
<table border="0" cellpadding="0" cellspacing="0" class="data" id="other-input-prices-table">
    <colgroup>
        <col width="32" />
        <col width="100" />
        <col width="178" />
        <col width="164" />
        <col width="94" />
        <col width="108" />
        <col width="140" />
        <col width="124" />
        <col width="100" />
        <col width="92" />
    </colgroup>
    <thead>
        <tr>
            <th><div class="checkboxHeader"><input type="checkbox" class="checkbox" /></div></th>
            <th><div>Category</div></th>
            <th class="sortable"><div class="sortable-header">Product Code<a rel="product.alternateCode1" href="javascript:;" class="arrow-up current">UP</a><a rel="product.alternateCode1" href="javascript:;" class="arrow-down">DOWN</a></div></th>
            <th><div>Description</div></th>
            <th class="sortable"><div class="sortable-header">Vendor<a rel="shipPoint.name" href="javascript:;" class="arrow-up current">UP</a><a rel="shipPoint.name" href="javascript:;" class="arrow-down">DOWN</a></div></th>
            <th><div>Current PPT</div></th>
            <th><div>New PPT</div></th>
            <th class="sortable"><div class="sortable-header">Type<a rel="product.type.name" href="javascript:;" class="arrow-up current">UP</a><a rel="product.type.name" href="javascript:;" class="arrow-down">DOWN</a></div></th>
            <th class="sortable"><div class="sortable-header">Market<a rel="product.category.branch.market.name" href="javascript:;" class="arrow-up current">UP</a><a rel="product.category.branch.market.name" href="javascript:;" class="arrow-down">DOWN</a></div></th>
            <th class="sortable"><div class="sortable-header">Branch<a rel="product.category.branch.profitCenter" href="javascript:;" class="arrow-up current">UP</a><a rel="product.category.branch.profitCenter" href="javascript:;" class="arrow-down">DOWN</a></div></th>
        </tr>
    </thead>
    <tbody>
        <s:iterator value="inputPriceRecords.values">
          <tr id="price_record_${pricingRecord.id}" >
            <td><input name="pricingRecordId" type="checkbox" value="${pricingRecord.id}"  class="checkbox"/></td>
            <td><s:property value="pricingRecord.product.category.name"/></td>
            <td><s:property value="pricingRecord.product.alternateCode1"/></td>
            <td><s:property value="pricingRecord.product.description"/></td>
            <td><s:property value="pricingRecord.shipPoint.name"/></td>
            <td><s:property value="pricingRecord.initialPPT"/></td>
            <td class="tip new-PPT modify adjusted-list-price-col" abbr="Additional cost for this SKU due to limited availability">
            	<span class="adjustment-text"><s:property value="newPPT.value" /></span>
            	<input name="adjustmentValue" type="hidden" value="${newPPT.value}"/>
            	<input name="adjustmentStartDate" type="hidden" value="<s:date name="newPPT.startDate" format="MM/dd/yyyy"/>"/>
            	<input name="adjustmentEndDate" type="hidden" value="<s:date name="newPPT.endDate" format="MM/dd/yyyy"/>"/>
            	<input name="adjustmentComment" type="hidden" value="${newPPT.comment}"/>
            	<input name="adjustmentCreatedBy" type="hidden" value="${newPPT.createdBy}"/>
            </td>
            <td><s:property value="pricingRecord.product.type.name"/></td>
            <td><s:property value="pricingRecord.product.category.branch.market.name"/></td>
            <td><s:property value="pricingRecord.product.category.branch.city"/></td>
          </tr>
        </s:iterator>
    </tbody>
</table>
</div>

<%@ include file="input_price_metadata.jsp" %>
