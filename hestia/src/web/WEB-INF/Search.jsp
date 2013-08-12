<%--
  - Author: stevenfrog
  - Version: 1.0
  - Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
  -
  - Description: Search screen for Users.
--%>
<%@ include file="/WEB-INF/includes/taglibs.jsp" %>
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

<script type="text/javascript" src="js/jquery.jscrollpane.js"></script>
<script type="text/javascript" src="js/jquery.ui.datepicker.js"></script>
<script type="text/javascript" src="js/jquery-ui.min.js"></script>
<script type="text/javascript" src="js/jquery.ui.autocomplete.min.js"></script>
<script type="text/javascript" src="js/jquery.form.js"></script>

<script type="text/javascript" src="js/tooltip.js"></script>
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

					<c:set var="currentTab" value="${2}"></c:set>
					<%@ include file="/WEB-INF/includes/mainnavuser.jsp" %>
				</div>

			</div>
			<!-- End #header -->

			<!-- main container -->
			<div id="main-container" class="admin">

				<div class="inner" id="search_products">

					<h3>SEARCH</h3>

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

					<!-- table section -->
					<div class="table-section table-CW">

						<div class="table-data">

							<!-- pagination -->
							<div class="pagination">
								<div class="per-page-show">
									<div class="total-data">Displaying 1 to 10 of 0 Products</div>
									<div class="last">
										<strong>Display</strong>
										<select>
                                            <option value="10">10</option>
                                            <option value="20" selected="selected">20</option>
                                            <option value="40">40</option>
                                            <option value="50">50</option>
                                            <option value="100">100</option>
                                            <option value="200">200</option>
                                            <option value="500">500</option>
                                            <option value="0">All</option>
										</select>
									</div>
								</div>
								<div class="button-panel">
									<a href="javascript:;" class="btn-save-search">SAVE SEARCH</a>
									<a href="javascript:;" class="btn-export">EXPORT</a>
								</div>
								<div class="link-ul">
									<ul>
										<li class="first"><a href="#" id="switchToInputPrice">Input Price</a></li>
										<li><a href="#" id="switchToAdjustment">Adjustments</a></li>
										<li><a href="#" id="switchToFreight">Freight</a></li>
										<li><a href="#" id="switchToBranchOverrides">Branch Overrides</a></li>
									</ul>
								</div>
								<div class="clear"></div>

							</div>
							<!-- End .pagination -->

							<!-- result section -->
							<div class="result-section">

								<div class="loading"><img src="i/loadingAnimation.gif" alt="" /></div>
								<div class="no-data">No data available</div>

								<table border="0" cellpadding="0" cellspacing="0" class="scroll-section">
									<tr>
										<td class="tree-wrapper">

											<!-- tree -->
											<div class="result-tree">

												<!-- title -->
												<div class="result-tree-title">
													<h4>Search Result</h4>
												</div>
												<!-- End .result-tree-title -->

												<!-- section -->
												<div class="result-tree-section">

													<div class="tree-panel">

														<div class="tree-inner">

															<div class="scroll-pane">

																<!-- tree -->
																<div class="tree">

																	<ul>
																		<li>
																			<a href="javascript:;">Vendor (3)</a>
																			<ul>
																				<li><a href="javascript:;">Vendor2 (3)</a></li>
																				<li><a href="javascript:;">Vendor1 (3)</a></li>
																				<li class="bottom"><a href="javascript:;">Vendor3 (3)</a></li>
																			</ul>
																		</li>
																		<li>
																			<a href="javascript:;" class="unfolder">Type (1)</a>
																			<ul class="hidden">
																				<li class="bottom"><a href="javascript:;">Type1 (3)</a></li>
																			</ul>
																		</li>
																		<li>
																			<a href="javascript:;" class="unfolder">Market (2)</a>
																			<ul class="hidden">
																				<li><a href="javascript:;">Market1 (3)</a></li>
																				<li class="bottom"><a href="javascript:;">Market2 (3)</a></li>
																			</ul>
																		</li>
																		<li>
																			<a href="javascript:;" class="unfolder">Branch (1)</a>
																			<ul class="hidden">
																				<li class="bottom"><a href="javascript:;">Branch1 (3)</a></li>
																			</ul>
																		</li>
																		<li>
																			<a href="javascript:;">Category (1)</a>
																			<ul>
																				<li class="bottom"><a href="javascript:;">Orlando (9)</a></li>
																			</ul>
																		</li>
																		<li>
																			<a href="javascript:;">Product (12)</a>
																			<ul>
																				<li><a href="javascript:;">Product (1)</a></li>
																				<li><a href="javascript:;">Product (1)</a></li>
																				<li><a href="javascript:;">Product (1)</a></li>
																				<li><a href="javascript:;">Product (1)</a></li>
																				<li><a href="javascript:;">Product (1)</a></li>
																				<li><a href="javascript:;">Product (1)</a></li>
																				<li><a href="javascript:;">Product (1)</a></li>
																				<li><a href="javascript:;">Product (1)</a></li>
																				<li class="bottom"><a href="javascript:;">Product (1)</a></li>
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
												<!-- End .result-tree-section -->

											</div>
											<!-- End .result-tree -->

										</td>

										<td>

											<!-- table -->
											<div id="cwResulttable" class="result-table">

											</div>
											<!-- End .result-table -->

										</td>
									</tr>
								</table>

							</div>
							<!-- End .result-section -->

						</div>

						<!-- pagination -->
						<div class="pagination">
							<div class="per-page-show">
								<div class="total-data">Displaying 1 to 10 of 0 Products</div>
								<div class="last">
									<strong>Display</strong>
									<select>
                                        <option value="10">10</option>
                                        <option value="20" selected="selected">20</option>
                                        <option value="40">40</option>
                                        <option value="50">50</option>
                                        <option value="100">100</option>
                                        <option value="200">200</option>
                                        <option value="500">500</option>
                                        <option value="0">All</option>
									</select>
								</div>
								<div class="dot">
								    <input id='lowestNetPrice' class="lowestNetPrice" type="checkbox" checked="checked" />
									<span>Lowest Net Price</span>
								</div>
							</div>
							<div class="page-trun">
								<div class="page-jump">
									<span>Jump to :<input type="text" class="text default" value="Page #" />of <em>20</em></span>
									<a href="javascript:;" class="btn-go">GO</a>
								</div>
                                <div class="pageLinks">
                                    <a href="javascript:;" class="first-link">FIRST</a>
                                    <a href="javascript:;" class="prev-link">PREV</a>
                                    <a href="javascript:;" class="page">11</a>
                                    <a href="javascript:;" class="page">12</a>
                                    <a href="javascript:;" class="page current">13</a>
                                    <a href="javascript:;" class="page">14</a>
                                    <a href="javascript:;" class="page">15</a>
                                    <a href="javascript:;" class="next-link">NEXT</a>
                                    <a href="javascript:;" class="last-link">LAST</a>
                                </div>
							</div>
							<div class="clear"></div>
						</div>
						<!-- End .pagination -->

						<div class="corner tl"></div>
						<div class="corner tr"></div>

					</div>
					<!-- End .table-section -->

					<!-- table section -->
					<div class="table-section table-other-type">

						<div class="table-data">

							<!-- pagination -->
							<div class="pagination">
								<div class="per-page-show">
									<div class="total-data">Displaying 1 to 10 of 0 Products</div>
									<div class="last">
										<strong>Display</strong>
                                        <select>
	                                        <option value="10">10</option>
	                                        <option value="20" selected="selected">20</option>
	                                        <option value="40">40</option>
	                                        <option value="50">50</option>
	                                        <option value="100">100</option>
	                                        <option value="200">200</option>
	                                        <option value="500">500</option>
	                                        <option value="0">All</option>
                                        </select>
									</div>
								</div>
								<div class="button-panel">
									<a href="javascript:;" class="btn-save-search">SAVE SEARCH</a>
									<a href="javascript:;" class="btn-export">EXPORT</a>
								</div>
								<div class="link-ul">
									<ul>
										<li class="first"><a href="#" id="switchToInputPrice">Input Price</a></li>
										<li><a href="#" id="switchToAdjustment">Adjustments</a></li>
										<li><a href="#" id="switchToFreight">Freight</a></li>
										<li><a href="#" id="switchToBranchOverrides">Branch Overrides</a></li>
									</ul>
								</div>
								<div class="clear"></div>
							</div>
							<!-- End .pagination -->

							<!-- result section -->
							<div class="result-section">

								<div class="loading"><img src="i/loadingAnimation.gif" alt="" /></div>
								<div class="no-data">No data available</div>

								<table border="0" cellpadding="0" cellspacing="0" class="scroll-section">
									<tr>
										<td class="tree-wrapper">

											<!-- tree -->
											<div class="result-tree">

												<!-- title -->
												<div class="result-tree-title">
													<h4>Search Result</h4>
												</div>
												<!-- End .result-tree-title -->

												<!-- section -->
												<div class="result-tree-section">

													<div class="tree-panel">

														<div class="tree-inner">

															<div class="scroll-pane">

																<!-- tree -->
																<div class="tree">

																	<ul>
																		<li>
																			<a href="javascript:;">Vendor (3)</a>
																			<ul>
																				<li><a href="javascript:;">Vendor2 (3)</a></li>
																				<li><a href="javascript:;">Vendor1 (3)</a></li>
																				<li class="bottom"><a href="javascript:;">Vendor3 (3)</a></li>
																			</ul>
																		</li>
																		<li>
																			<a href="javascript:;" class="unfolder">Type (1)</a>
																			<ul class="hidden">
																				<li class="bottom"><a href="javascript:;">Type1 (3)</a></li>
																			</ul>
																		</li>
																		<li>
																			<a href="javascript:;" class="unfolder">Market (2)</a>
																			<ul class="hidden">
																				<li><a href="javascript:;">Market1 (3)</a></li>
																				<li class="bottom"><a href="javascript:;">Market2 (3)</a></li>
																			</ul>
																		</li>
																		<li>
																			<a href="javascript:;" class="unfolder">Branch (1)</a>
																			<ul class="hidden">
																				<li class="bottom"><a href="javascript:;">Branch1 (3)</a></li>
																			</ul>
																		</li>
																		<li>
																			<a href="javascript:;">Category (1)</a>
																			<ul>
																				<li class="bottom"><a href="javascript:;">Orlando (9)</a></li>
																			</ul>
																		</li>
																		<li>
																			<a href="javascript:;">Product (12)</a>
																			<ul>
																				<li><a href="javascript:;">Product (1)</a></li>
																				<li><a href="javascript:;">Product (1)</a></li>
																				<li><a href="javascript:;">Product (1)</a></li>
																				<li><a href="javascript:;">Product (1)</a></li>
																				<li><a href="javascript:;">Product (1)</a></li>
																				<li><a href="javascript:;">Product (1)</a></li>
																				<li><a href="javascript:;">Product (1)</a></li>
																				<li><a href="javascript:;">Product (1)</a></li>
																				<li class="bottom"><a href="javascript:;">Product (1)</a></li>
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
												<!-- End .result-tree-section -->

											</div>
											<!-- End .result-tree -->

										</td>

										<td>

											<!-- table -->
                                            <div id="resulttable" class="result-table">

											</div>
											<!-- End .result-table -->

										</td>
									</tr>
								</table>

							</div>
							<!-- End .result-section -->

						</div>

						<!-- pagination -->
						<div class="pagination">
							<div class="per-page-show">
								<div class="total-data">Displaying 1 to 10 of 0 Products</div>
								<div class="last">
									<strong>Display</strong>
									<select>
                                        <option value="10">10</option>
                                        <option value="20" selected="selected">20</option>
                                        <option value="40">40</option>
                                        <option value="50">50</option>
                                        <option value="100">100</option>
                                        <option value="200">200</option>
                                        <option value="500">500</option>
                                        <option value="0">All</option>
									</select>
								</div>
								<div class="dot">
								    <input id='lowestNetPrice' class="lowestNetPrice" type="checkbox" checked="checked" />
									<span>Lowest Net Price</span>
								</div>
							</div>
							<div class="page-trun">
								<div class="page-jump">
									<span>Jump to :<input type="text" class="text default" value="Page #" />of <em>20</em></span>
									<a href="javascript:;" class="btn-go">GO</a>
								</div>
                                <div class="pageLinks">
                                    <a href="javascript:;" class="first-link">FIRST</a>
                                    <a href="javascript:;" class="prev-link">PREV</a>
                                    <a href="javascript:;" class="page">11</a>
                                    <a href="javascript:;" class="page">12</a>
                                    <a href="javascript:;" class="page current">13</a>
                                    <a href="javascript:;" class="page">14</a>
                                    <a href="javascript:;" class="page">15</a>
                                    <a href="javascript:;" class="next-link">NEXT</a>
                                    <a href="javascript:;" class="last-link">LAST</a>
                                </div>
							</div>
							<div class="clear"></div>
						</div>
						<!-- End .pagination -->

						<div class="corner tl"></div>
						<div class="corner tr"></div>

					</div>
					<!-- End .table-section -->

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
