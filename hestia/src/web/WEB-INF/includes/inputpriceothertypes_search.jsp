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
	    <col width="178" />
	    <col width="92" />
	    <col width="72" />
	    <col width="88" />
	    <col width="94" />
	    <col width="90" />
	    <col width="116" />
	    <col width="80" />
	    <col width="78" />
	    <col width="134" />
	    <col width="114" />
	    <col width="100" />
    </colgroup>
    <thead>
        <tr>
            <th><div class="checkboxHeader"><input class="checkbox" type="checkbox" /></div></th>
            <th class="sortable"><div class="sortable-header">Category<a rel="product.category.name" href="javascript:;" class="arrow-up current">UP</a><a rel="product.category.name" href="javascript:;" class="arrow-down">DOWN</a></div></th>
            <th class="sortable"><div class="sortable-header">Product Code<a rel="product.alternateCode1" href="javascript:;" class="arrow-up current">UP</a><a rel="product.alternateCode1" href="javascript:;" class="arrow-down">DOWN</a></div></th>
            <th><div>Description</div></th>
            <th class="sortable"><div class="sortable-header">Vendor<a rel="shipPoint.name" href="javascript:;" class="arrow-up current">UP</a><a rel="shipPoint.name" href="javascript:;" class="arrow-down">DOWN</a></div></th>
            <th><div>PPT</div></th>
            <th><div>Adjustment</div></th>
            <th class="two-line"><div>Price before<br />Freight</div></th>
            <th><div>Freight</div></th>
            <th><div>Total Price</div></th>
            <th class="two-line"><div>Branch<br />Override</div></th>
            <th><div>Net Price</div></th>
            <th class="sortable"><div class="sortable-header">Type<a rel="product.type.name" href="javascript:;" class="arrow-up current">UP</a><a rel="product.type.name" href="javascript:;" class="arrow-down">DOWN</a></div></th>
            <th class="sortable"><div class="sortable-header">Market<a rel="product.category.branch.market.name" href="javascript:;" class="arrow-up current">UP</a><a rel="product.category.branch.market.name" href="javascript:;" class="arrow-down">DOWN</a></div></th>
            <th class="sortable"><div class="sortable-header">Branch<a rel="product.category.branch.profitCenter" href="javascript:;" class="arrow-up current">UP</a><a rel="product.category.branch.profitCenter" href="javascript:;" class="arrow-down">DOWN</a></div></th>
        </tr>
    </thead>
    <tbody>
       <s:iterator value="pricingSheet">
       <tr class="<s:if test="lowestNet">lownet</s:if>">
           <td><input type="checkbox" class="checkbox" value="productId"/></td>
           <td><s:property value="category"/></td>
           <td><s:property value="productCode"/></td>
           <td><s:property value="description"/></td>
           <td><s:property value="shipPoint"/></td>
           <td><s:property value="newListPrice"/></td>
           <td><s:property value="pricingAdjustment"/></td>
           <td><s:property value="beforeFreightPrice"/></td>
           <td><s:property value="freightAdjustment"/></td>
		   <td><s:property value="totalPrice"/></td>
           <td><s:property value="branchOverride"/></td>
           <td><s:property value="finalPrice"/></td>
           <td><s:property value="productType"/></td>
           <td><s:property value="market"/></td>
           <td><s:property value="branch"/></td>
       </tr>
       </s:iterator>
    </tbody>
</table>
</div>

<%@ include file="input_price_metadata.jsp" %>
