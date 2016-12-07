<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://ajaxtags.sourceforge.net/tags/ajaxtags" prefix="ajax"%>

<script type="text/javascript" src="js/prototype.js"></script>
<script type="text/javascript" src="js/scriptaculous/scriptaculous.js"></script>
<script type="text/javascript" src="js/scriptaculous/controls.js"></script>
<script type="text/javascript" src="js/ajaxtags.js"></script>
<script type="text/javascript" src="js/overlibmws/overlibmws.js"></script>
<script type="text/javascript" src="js/rhea-view.js"></script>
<script type="text/javascript" src="js/rhea-search.js"></script>
<script type="text/javascript" src="js/rhea-search-for-intenz.js"></script>

<bean:define id="horizontalCompoundSearch" value="true"/>

<style>
<!--
@import "css/rhea-view.css";
@import "css/rhea-search.css";
@import "css/rheaSelector.css";
@import "css/intenz.css";
-->
</style>

<script type="text/javascript">
var searchCompoundHelp = 'Search by <ul>'
    + '<li>ChEBI ID: number <i>prefixed by \"CHEBI:\"</i> (letter case does not matter)</li>'
    + '<li>compound name: can include space and the wildcards \"_\" or \"?\"'
    + '(= one character) and \"%\" or \"*\" (= several characters)</li></ul>';
</script>

<div style="display: table-row">

    <div style="${horizontalCompoundSearch?
        'display: table-cell; min-width: 37em; max-width: 37em' : ''}">

        <table style="border: none; margin-left: 1em; ${horizontalCompoundSearch? 'width: 100%' : ''}">
        <tbody><tr>
            <td><input type="text" id="compoundNameSearch"/></td>
            <td><img src="img/Help16.gif"
                onmouseover="return overlib(searchCompoundHelp, WRAP, WRAPMAX, 300, HAUTO, FGCOLOR, '#ffffee')"
                onmouseout="nd()"/></td>
            <td>
                <button type="button" id="searchButton" style="white-space: nowrap;">
                    Search <img src="img/indicator.gif"
                        id="compoundSearchResultsIndicator"
                        style="display: none; position: absolute"/>
                    <img src="img/Down16.gif"/>
                </button>
                <ajax:htmlContent
                    baseUrl="searchCofactor.do"
                    parameters="q={compoundNameSearch}"
                    source="searchButton"
                    target="compoundSearchResults"
                    preFunction="startSearchCompound"
                    postFunction="endSearchCompound"
                    errorFunction="hideIndicator" />
            </td>
        </tr>
        </tbody>
        </table>

        <div id="compoundSearchResults">
        <logic:notEmpty name="compoundSearchResults" scope="session">
        <%@ include file="compoundsList.jsp" %>
        </logic:notEmpty>
        </div>

    </div>

    <div style="${horizontalCompoundSearch? 'display: table-cell; width: 50%' : ''}">
        <div id="compoundDataIndicator" class="indicator"
            style="display: none; position: absolute; text-align: center;"><img
                src="img/indicator.gif"/></div>

        <div id="compoundData">(No compound selected)</div>
    </div>

</div>
