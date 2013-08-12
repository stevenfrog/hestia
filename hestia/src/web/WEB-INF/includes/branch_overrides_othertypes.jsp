<%--
  - Author: stevenfrog
  - Version: 1.0
  - Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
  -
  - Description: Table fragment for other records.
  - since Adjustments Freight and Branch Overrides
--%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="pricing" uri="PricingTags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="table-data-container">
	<table border="0" cellpadding="0" cellspacing="0" class="data" id="other-input-prices-table">
		<colgroup>
			<col width="32">
			<col width="100">
			<col width="178">
			<col width="184">
			<col width="94">
			<col width="54">
			<col width="80">
			<col width="84">
			<col width="68">
			<col width="82">
			<col width="68">
			<col width="80">
			<col width="74">
			<col width="106">
			<col width="100">
			<col width="90">
		</colgroup>
		<thead>
			<tr>
				<th><div><input type="checkbox" class="checkbox" /></div></th>
                <th><div>Category</div></th>
				<th class="sortable"><div class="sortable-header">Product Code<a rel="product.alternateCode1" href="javascript:;" class="arrow-up current">UP</a><a rel="product.alternateCode1" href="javascript:;" class="arrow-down">DOWN</a></div></th>
                <th><div>Description</div></th>
				<th class="sortable"><div class="sortable-header">Vendor<a rel="shipPoint.name" href="javascript:;" class="arrow-up current">UP</a><a rel="shipPoint.name" href="javascript:;" class="arrow-down">DOWN</a></div></th>
				<th><div>PPT</div></th>
				<th><div>Adjustment</div></th>
				<th class="two-line"><div>Price Before<br />Freight</div></th>
				<th><div>Freight</div></th>
				<th><div>Total Price</div></th>
				<th class="two-line"><div>Branch<br />Override</div></th>
				<th class="two-line"><div>New Branch<br />Override</div></th>
				<th><div>Net Price</div></th>
				<th class="sortable"><div class="sortable-header">Type<a rel="product.type.name" href="javascript:;" class="arrow-up current">UP</a><a rel="product.type.name" href="javascript:;" class="arrow-down">DOWN</a></div></th>
				<th class="sortable"><div class="sortable-header">Market<a rel="market.name" href="javascript:;" class="arrow-up current">UP</a><a rel="market.name" href="javascript:;" class="arrow-down">DOWN</a></div></th>
				<th class="sortable"><div class="sortable-header">Branch<a rel="branch.name" href="javascript:;" class="arrow-up current">UP</a><a rel="branch.name" href="javascript:;" class="arrow-down">DOWN</a></div></th>
			</tr>
		</thead>
	    <tbody>
		    <s:iterator value="pricingSheet">
		       <tr id='branch_override_record_<s:property value="pricingRecordId"/>_<s:property value="branchId"/>' >
		           <td><input type="checkbox" class="checkbox" value='<s:property value="pricingRecordId"/>_<s:property value="branchId"/>' name="productId"/></td>
		           <td><s:property value="category"/></td>
		           <td><s:property value="productCode"/></td>
		           <td><s:property value="description"/></td>
		           <td><s:property value="shipPoint"/></td>
		           <td><s:property value="newListPrice"/></td>
		           <td class="tip no-click-tip modify" abbr="Adgustment" >
	                   <span class="adjustment-text"><s:property value="pricingAdjustment"/></span>
		               <input name="adjustmentValue" type="hidden" value='<s:property value="pricingAdjustment"/>'/>
		               <input name="adjustmentStartDate" type="hidden" value="<s:date name="pricingAdjustmentStartDate" format="MM/dd/yyyy"/>"/>
		               <input name="adjustmentEndDate" type="hidden" value="<s:date name="pricingAdjustmentEndDate" format="MM/dd/yyyy"/>"/>
		               <input name="adjustmentComment" type="hidden" value='<s:property value="pricingAdjustmentComment"/>'/>
		               <input name="adjustmentCreatedBy" type="hidden" value='<s:property value="pricingAdjustmentCreatedBy"/>'/>
		           </td>
		           <td><s:property value="beforeFreightPrice"/></td>
		           <td id="freight-cost"><s:property value="freightAdjustment"/></td>
		           <td><s:property value="totalPrice"/></td>
		           <td id="branch-override"><s:property value="branchOverride"/></td>
		           <td class="tip new-branch-overrides modify" abbr="New Branch Overrides">
		               <span class="adjustment-text"><s:property value="newBranchOverride" /></span>
		               <input name="adjustmentValue" type="hidden" value='<s:property value="newBranchOverride"/>'/>
		               <input name="adjustmentStartDate" type="hidden" value="<s:date name="newBranchOverrideStartDate" format="MM/dd/yyyy"/>"/>
		               <input name="adjustmentEndDate" type="hidden" value="<s:date name="newBranchOverrideEndDate" format="MM/dd/yyyy"/>"/>
		               <input name="adjustmentComment" type="hidden" value='<s:property value="newBranchOverrideComment"/>'/>
		               <input name="adjustmentCreatedBy" type="hidden" value='<s:property value="newBranchOverrideCreatedBy"/>'/>
		           </td>
		           <td id="final-price"><s:property value="finalPrice"/></td>
		           <td><s:property value="productType"/></td>
		           <td><s:property value="market"/></td>
		           <td><s:property value="branch"/></td>
		       </tr>
	        </s:iterator>
		</tbody>
	</table>
</div>

<%@ include file="adjustment_metadata.jsp" %>