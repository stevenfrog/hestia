<%--
  - Author: pinoydream
  - Date: 2013-01-20
  - Version: 1.0
  - Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
  -
  - Description: Default home screen for Users.

--%>
<%@ include file="/WEB-INF/includes/taglibs.jsp" %>
<%@ taglib prefix="pricing" uri="PricingTags" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<sj:head jqueryui="true" jquerytheme="cupertino"/>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Hestia</title>
<link href="css/screen.css" rel="stylesheet" type="text/css" />
<!--[if lt IE 8]>
<link href="${cssctx}/css/screen-ie7.css" rel="stylesheet" type="text/css"  />
<![endif]-->
<script type="text/javascript" src="js/jquery.form.js"></script>
<script type="text/javascript" src="js/tooltip.js"></script>
<script type="text/javascript" src="js/jquery.ui.datepicker.js"></script>
<script type="text/javascript" src="js/scripts.js"></script>
<script type="text/javascript">
    var datactx = '${jsctx}';
</script>
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

					<c:set var="currentTab" value="${1}"></c:set>
					<%@ include file="/WEB-INF/includes/mainnavuser.jsp" %>

				</div>

			</div>
			<!-- End #header -->

			<!-- main container -->
			<div id="main-container" class="my-war-room">

				<div class="inner">

					<h3>MY WAR ROOM</h3>

					<!-- alert section -->
					<c:if test="${not empty latestUserFilterRecord or not empty latestCriteriaRecord}">
					<div class="comfirm">
						<c:if test="${not empty latestCriteriaRecord}">
						<div>
							<p id="notification-filter-${latestCriteriaRecord.id}">Pricing <a href="OpenFilter.action?filterId=${latestCriteriaRecord.id}"><c:out value="${latestCriteriaRecord.name}" /></a> was saved in your last login on <s:date name="latestCriteriaRecord.updatedOn" format="dd/MM/yyyy, HH:mm"/></p>
							<a href="OpenFilter.action?filterId=${latestCriteriaRecord.id}" class="resume-link">Resume</a>
							<div class="clear"></div>
						</div>
						</c:if>
						<c:if test="${not empty latestUserFilterRecord}">
						<div>
							<p id="notification-filter-${latestUserFilterRecord.id}"><pricing:FilterType type="${latestUserFilterRecord.type}" /><a href="OpenFilter.action?filterId=${latestUserFilterRecord.id}"><c:out value="${latestUserFilterRecord.name}"></c:out></a> was saved in your last login on <s:date name="latestUserFilterRecord.updatedOn" format="dd/MM/yyyy, HH:mm"/></p>
							<a href="OpenFilter.action?filterId=${latestUserFilterRecord.id}" class="execute-link">Execute</a>
							<div class="clear"></div>
						</div>
						</c:if>
						<div class="corner tl"></div>
						<div class="corner tr"></div>
						<div class="corner bl"></div>
						<div class="corner br"></div>
					</div>
					</c:if>
					<!-- End .alert -->

					<!-- tab -->
					<div class="tab">

						<!-- tab list -->
						<div class="tab-list">

							<ul>
								<li><a href="javascript:;" class="active"><span><span>My Pricings</span></span></a></li>
								<li><a href="javascript:;" class="${param['tab'] eq 'my-filters' ? 'activate-on-startup' : ''}"><span><span>My Saved Filters</span></span></a></li>
							</ul>

						</div>
						<!-- End .tab-list -->

						<!-- tab container -->
						<div class="tab-container">

							<!-- tab section -->
							<div class="tab-section my-pricings">

								<s:form action="SearchMyPricingAndFiltersAction" namespace="/ajax" id="SearchMyPricingForm">
							        <s:hidden name="myPricingSearchCriteria.page"></s:hidden>
							        <s:hidden name="myPricingSearchCriteria.pageSize"></s:hidden>
							        <s:hidden name="myPricingSearchCriteria.sortingType"></s:hidden>
							        <s:hidden name="myPricingSearchCriteria.sortingColumn"></s:hidden>
								</s:form>

								<div class="tabel-data">

									<%@ include file="/WEB-INF/includes/multi_tab_pagination.jsp" %>

									<div class="loading"><img src="${imgctx}/i/loadingAnimation.gif" alt="" /></div>
									<div class="no-data">No data available</div>

									<table border="0" cellpadding="0" cellspacing="0" class="data" id="my-pricings-table">
										<colgroup>
											<col width="150" />
											<col width="172" />
											<col width="172" />
											<col width="160" />
											<col width="118" />
											<col width="96" />
											<col width="90" />
										</colgroup>
										<thead>
											<tr>
												<th class="first sortable myPrcingName"><div class="sortable-header">Pricing Name<a rel="name" href="javascript:;" class="arrow-up current">UP</a><a rel="name" href="javascript:;" class="arrow-down">DOWN</a></div></th>
												<th><div>Search Criteria</div></th>
												<th><div>Created On</div></th>
												<th><div>Last Saved on</div></th>
												<th><div>Progress</div></th>
												<th><div>Status</div></th>
												<th><div>Actions</div></th>
											</tr>
										</thead>
										<tbody>
											<td colspan="7"></td>
										</tbody>
									</table>

								</div>

								<%@ include file="/WEB-INF/includes/multi_tab_pagination.jsp" %>
							</div>
							<!-- End .tab-section -->

							<!-- tab section -->
							<div class="tab-section my-saved-filter">

 								<s:form action="SearchMyPricingAndFiltersAction" namespace="/ajax" id="SearchMyFiltersForm">
							        <s:hidden name="userFilterRecordCriteria.page"></s:hidden>
							        <s:hidden name="userFilterRecordCriteria.pageSize"></s:hidden>
							        <s:hidden name="userFilterRecordCriteria.sortingType"></s:hidden>
							        <s:hidden name="userFilterRecordCriteria.sortingColumn"></s:hidden>
							        <s:hidden name="userFilterRecordCriteria.criteriaFlag" value="false" />
								</s:form>

								<div class="table-data">

									<%@ include file="/WEB-INF/includes/multi_tab_pagination.jsp" %>

									<div class="loading"><img src="${imgctx}/i/loadingAnimation.gif" alt="" /></div>
									<div class="no-data">No data available</div>

									<table border="0" cellpadding="0" cellspacing="0" class="data" id="my-saved-filter-table">
										<colgroup>
											<col width="150" />
											<col width="172" />
											<col width="396" />
											<col width="160" />
											<col width="90" />
										</colgroup>
										<thead>
											<tr>
												<th class="first sortable myFilterName"><div class="sortable-header">Filter Name<a rel="name" href="javascript:;" class="arrow-up current">UP</a><a rel="name" href="javascript:;" class="arrow-down">DOWN</a></div></th>
												<th class="myFilterType"><div>Type</div></th>
												<th class="myFilterDesc"><div>Description</div></th>
												<th><div>Last Saved on</div></th>
												<th><div>Actions</div></th>
											</tr>
										</thead>
										<tbody>
											<tr>
												<td colspan="7"></td>
											</tr>
										</tbody>
									</table>

								</div>

								<%@ include file="/WEB-INF/includes/multi_tab_pagination.jsp" %>
							</div>
							<!-- End .tab-section -->

						</div>
						<!-- End .tab-container -->

					</div>

				</div>

			</div>
			<!-- End #main-container -->

		</div>

	</div>
	<!-- End #wrapper -->

	<%@ include file="/WEB-INF/includes/footer.jsp" %>

	<!-- Modal -->
	<div id="modal-background"></div>
	<div id="modal">

		<!-- Delete Confirmation -->
		<div class="modal" id="modal-delete-confirmation">

			<!-- header -->
			<div class="modal-header">
				<div>
					<div>
						<a href="javascript:;" class="close-modal">Close</a>
						<h1>DELETE CONFIRMATION</h1>
					</div>
				</div>
			</div>
			<!-- End .modal-header -->

			<!-- Container -->
			<div class="modal-container">
				<p>Are you sure to delete the active list? All saved progress to the list will be deleted.</p>
			</div>
			<!-- End .container -->

			<!-- footer -->
			<div class="modal-footer">
				<div>
					<div>
						<a href="javascript:;" class="btn-yes">YES</a>
						<a href="javascript:;" class="btn-no close-modal">NO</a>
					</div>
				</div>
			</div>
			<!-- End .modal-footer -->

		</div>

		<!-- Delete -->
		<div class="modal" id="modal-delete">

			<!-- header -->
			<div class="modal-header">
				<div>
					<div>
						<a href="javascript:;" class="close-modal">Close</a>
						<h1>DELETED</h1>
					</div>
				</div>
			</div>
			<!-- End .modal-header -->

			<!-- Container -->
			<div class="modal-container">

				<p>The list has been successfully deleted</p>

			</div>
			<!-- End .container -->

			<!-- footer -->
			<div class="modal-footer">
				<div>
					<div>
						<a href="javascript:;" class="btn-ok close-modal">OK</a>
					</div>
				</div>
			</div>
			<!-- End .modal-footer -->

		</div>
		<div id="escapeelement" style="display: none;">&nbsp;</div>
	</div>

</body>
</html>
