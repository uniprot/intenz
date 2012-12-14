<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%-- 
<div id="intenz-rssNews">
    <div class="small_bold">
        <div id="intenz-rssDate"></div>
        <a id="intenz-rssTitle"></a>
    </div>
    <div id="intenz-rssDescription" class="psmall">
        Retrieving news...
    </div>
    <a id="intenz-rssMore"></a>
</div>

<script type="text/javascript">
getRss('http://sourceforge.net/export/rss2_projnews.php?group_id=94642&rss_fulltext=1', 'intenz');
</script>
--%>

<% request.setAttribute("now", System.currentTimeMillis()); %>
<c:if test="${empty lastFeed or (now - lastFeed gt intenzConfig.rssRefresh)}">
<c:catch var="rssException">
    <c:import var="newsXmlFull" url="${intenzConfig.rssUrl}"/>
    <x:parse doc="${newsXmlFull}" varDom="newsFull" scopeDom="application"/>
    <c:set var="lastFeed" value="${now}" scope="application" />
</c:catch>
</c:if>

<c:choose>
    <c:when test="${empty rssException}">
		<x:forEach var="item" select="$newsFull/rss/channel/item"
			begin="0" end="0">
		    <div class="small_bold">
		        <x:set var="pubDate" select="string($item/pubDate/text())"/>
		        <div style="white-space:nowrap">${fn:split(pubDate,' ,:')[1]}
		            ${fn:split(pubDate,' ,:')[2]} ${fn:split(pubDate,' ,:')[3]}
	            </div>
		        <x:set var="link" select="string($item/link/text())"/>
		        <a href="${link}">
		            <x:out select="string($item/title/text())"/>
		        </a>
		    </div>
		    <div class="psmall">
		        <x:out select="string($item/description/text())"
		            escapeXml="false"/>
		    </div>
		</x:forEach>
    </c:when>
    <c:otherwise>
        <div>Sorry, there are no news at the moment.</div>
    </c:otherwise>
</c:choose>

<div style="margin-top: 1ex; text-align: right">
	<a href="${intenzConfig.rssUrl}">More news...</a>
</div>
