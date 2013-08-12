<%--
  - Author: stevenfrog
  - Version: 1.0
  - Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
  -
  - Description: Table fragment for freight.
  - since Adjustments Freight and Branch Overrides
--%>

<!-- table section -->
<div class="table-section table-CW table-sw-type-freights">

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
                    <li class="first"><a href="#" id="switchToBranchOverrides">Branch Overrides</a></li>
                </ul>
            </div>
            <div class="clear"></div>

        </div>
        <!-- End .pagination -->

        <!-- input prices -->
        <div class="input-prices">

            <!-- prices -->
            <div class="prices">
                <label>Freight:</label>
                <div class="column column-percentage">
                    <input type="radio" class="radio" value="PERCENTAGE" name="modificationType" />
                    <span>Percentage</span>
                    <input type="text" class="text" name="percentValue" value="" maxlength="8"/>
                </div>
                <div class="column column-increase">
                    <input type="radio" class="radio" value="ADDER" name="modificationType" />
                    <span>Increase/Decrease $</span>
                    <input type="text" class="text" name="incrementValue" maxlength="8"/>
                    <div class="icon">
                        <a href="javascript:;" class="btn-increase">INCREASE</a>
                        <a href="javascript:;" class="btn-decrease">DECREASE</a>
                    </div>
                </div>
            </div>
            <!-- End .prices -->

            <!-- date -->
            <div class="date">
                <div class="from-date">
                    <label>Start Date:</label>
                    <a id="_dummy_1" class="new-tip tooltip" rel="<h3>Help</h3><p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed eleifend ultricies sodales.</p>" href="javascript:;"></a>
                    <input type="text" class="text date-picker" readonly="readonly" name="startDate" />
                </div>
                <div class="to-date">
                    <label>End Date:</label>
                    <a id="_dummy_2" class="new-tip tooltip" rel="<h3>Help</h3><p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed eleifend ultricies sodales.</p>" href="javascript:;"></a>
                    <input type="text" class="text date-picker" readonly="readonly" name="endDate" />
                </div>
                <div class="comment">
                    <label>Comments:</label>
                    <a id="_dummy_3" class="new-tip tooltip" rel="<h3>Help</h3><p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed eleifend ultricies sodales.</p>" href="javascript:;"></a>
                    <input type="text" class="text" name="comments" maxlength="200"/>
                </div>
                <a href="javascript:;" class="btn-modify">MODIFY</a>
            </div>
            <!-- End .date -->

            <div class="button">
                <a href="javascript:;" class="btn-undo" id="btn_undo_freight">UNDO</a>
                <a href="javascript:;" class="btn-save-all" id="btn_save_all_freight">SAVE ALL</a>
            </div>

        </div>
        <!-- End .input-prices -->

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
<div class="table-section table-other-type table-other-type-freights">

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
                    <li class="first"><a href="#" id="switchToBranchOverrides">Branch Overrides</a></li>
                </ul>
            </div>
            <div class="clear"></div>

        </div>
        <!-- End .pagination -->

        <!-- input prices -->
        <div class="input-prices">

            <!-- prices -->
            <div class="prices">
                <label>Freight:</label>
                <div class="column column-percentage">
                    <input type="radio" class="radio" value="PERCENTAGE" name="modificationType" />
                    <span>Percentage</span>
                    <input type="text" class="text" name="percentValue" value="" maxlength="8"/>
                </div>
                <div class="column column-increase">
                    <input type="radio" class="radio" value="ADDER" name="modificationType" />
                    <span>Increase/Decrease $</span>
                    <input type="text" class="text" name="incrementValue" maxlength="8"/>
                    <div class="icon">
                        <a href="javascript:;" class="btn-increase">INCREASE</a>
                        <a href="javascript:;" class="btn-decrease">DECREASE</a>
                    </div>
                </div>
            </div>
            <!-- End .prices -->

            <!-- date -->
            <div class="date">
                <div class="from-date">
                    <label>Start Date:</label>
                    <a id="_dummy_4" class="new-tip tooltip" rel="<h3>Help</h3><p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed eleifend ultricies sodales.</p>" href="javascript:;"></a>
                    <input type="text" class="text date-picker" readonly="readonly" name="startDate" />
                </div>
                <div class="to-date">
                    <label>End Date:</label>
                    <a id="_dummy_5" class="new-tip tooltip" rel="<h3>Help</h3><p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed eleifend ultricies sodales.</p>" href="javascript:;"></a>
                    <input type="text" class="text date-picker" readonly="readonly" name="endDate" />
                </div>
                <div class="comment">
                    <label>Comments:</label>
                    <a id="_dummy_6" class="new-tip tooltip" rel="<h3>Help</h3><p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed eleifend ultricies sodales.</p>" href="javascript:;"></a>
                    <input type="text" class="text" name="comments" maxlength="200"/>
                </div>
                <a href="javascript:;" class="btn-modify">MODIFY</a>
            </div>
            <!-- End .date -->

            <div class="button">
                <a href="javascript:;" class="btn-undo" id="btn_undo_freight">UNDO</a>
                <a href="javascript:;" class="btn-save-all" id="btn_save_all_freight">SAVE ALL</a>
            </div>

        </div>
        <!-- End .input-prices -->

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