<%--
  - Author: stevenfrog
  - Version: 1.0
  - Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
  -
  - Description: common pagination fragment for multiple tab pages like the user home.
--%>

<!-- pagination -->
<div class="pagination">
    <div class="per-page-show">
        <div class="total-data">Displaying 0 to 0 of 0</div>
        <div class="last">
            <strong>Display</strong>
            <select class="pageSize" disabled="disabled">
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

    <div class="page-trun" style="display: none;">
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
