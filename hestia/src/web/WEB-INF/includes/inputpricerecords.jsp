<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="pricing" uri="PricingTags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="table-data-container">
<table border="0" cellpadding="0" cellspacing="0" class="data" id="sw-input-prices-table">
    <colgroup>
        <col width="32" />
        <col width="100" />
        <col width="178" />
        <col width="168" />
        <col width="90" />
        <col width="84" />
        <col width="70" />
        <col width="70" />
        <col width="106" />
        <col width="84" />
        <col width="60" />
        <col width="98" />
    </colgroup>
    <thead>
        <tr>
            <th><div class="checkboxHeader"><input type="checkbox" class="checkbox" /></div></th>
            <th><div>Category</div></th>
            <th class="sortable"><div class="sortable-header">Product Code<a rel="product.alternateCode1" href="javascript:;" class="arrow-up current">UP</a><a rel="product.alternateCode1" href="javascript:;" class="arrow-down">DOWN</a></div></th>
            <th><div>Description</div></th>
            <th class="sortable"><div class="sortable-header">Vendor<a rel="shipPoint.name" href="javascript:;" class="arrow-up current">UP</a><a rel="shipPoint.name" href="javascript:;" class="arrow-down">DOWN</a></div></th>
            <th class="two-line"><div>Current List<br />($/CFT)</div></th>
            <th class="two-line"><div>Current<br />Multiplier</div></th>
            <th class="two-line"><div>Current<br />Net</div></th>
            <th><div>New List ($/CFT)</div></th>
            <th class="two-line"><div>New<br />Multiplier</div></th>
            <th><div>New Net</div></th>
            <th class="sortable"><div class="sortable-header">Type<a rel="product.type.name" href="javascript:;" class="arrow-up current">UP</a><a rel="product.type.name" href="javascript:;" class="arrow-down">DOWN</a></div></th>
        </tr>
    </thead>
    <tbody>
        <s:iterator value="inputPriceRecords.values">
          <tr id="cw_price_record_${cwPricingRecord.id}" >
            <td><input name="pricingRecordId" type="checkbox" value="${cwPricingRecord.id}" class="checkbox"/></td>
            <td><s:property value="cwPricingRecord.product.category.name"/></td>
            <td><s:property value="cwPricingRecord.product.alternateCode1"/></td>
            <td><s:property value="cwPricingRecord.product.description"/></td>
            <td><s:property value="cwPricingRecord.shipPoint.name"/></td>
            <td><s:property value="cwPricingRecord.initialListPrice"/></td>
            <td><s:property value="cwPricingRecord.initialMultiplier"/></td>
            <td class="net-price-col"><pricing:CWNetPrice pricingRecord="${cwPricingRecord}" /> </td>
            <td class="adjusted-list-price-col tip new-list modify" abbr="New Pricing">
            	<span class="adjustment-text"><s:property value="listPriceAdjustment.listPrice" /></span>
            	<input name="adjustmentValue" type="hidden" value="${listPriceAdjustment.listPrice}"/>
            	<input name="adjustmentStartDate" type="hidden" value="<s:date name="listPriceAdjustment.startDate" format="MM/dd/yyyy"/>"/>
            	<input name="adjustmentEndDate" type="hidden" value="<s:date name="listPriceAdjustment.endDate" format="MM/dd/yyyy"/>"/>
            	<input name="adjustmentComment" type="hidden" value="${listPriceAdjustment.comment}"/>
            	<input name="adjustmentCreatedBy" type="hidden" value="${listPriceAdjustment.createdBy}"/>
            </td>
            <td class="adjusted-multiplier-col tip new-multiplier modify" abbr="New Multiplier">
            	<span class="adjustment-text"><s:property value="multiplierAdjustment.multiplier" /></span>
            	<input name="adjustmentValue" type="hidden" value="${multiplierAdjustment.multiplier}"/>
            	<input name="adjustmentStartDate" type="hidden" value="<s:date name="multiplierAdjustment.startDate" format="MM/dd/yyyy"/>"/>
            	<input name="adjustmentEndDate" type="hidden" value="<s:date name="multiplierAdjustment.endDate" format="MM/dd/yyyy"/>"/>
            	<input name="adjustmentComment" type="hidden" value="${multiplierAdjustment.comment}"/>
            	<input name="adjustmentCreatedBy" type="hidden" value="${multiplierAdjustment.createdBy}"/>
            </td>
            <td class="new-net-price-col new-net modify" abbr="New Net">
	       		<pricing:CWNetAdjustedPrice pricingRecord="${cwPricingRecord}" listPriceAdjustment="${listPriceAdjustment}" multiplierAdjustment="${multiplierAdjustment}"/>
            </td>
            <td><s:property value="cwPricingRecord.product.type.name"/></td>
          </tr>
        </s:iterator>
    </tbody>
</table>
</div>

<%@ include file="input_price_metadata.jsp" %>
