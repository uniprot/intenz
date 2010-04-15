<!-- MAIN CONTENT -->
<div>
    <p>
	IntEnz (<b>Int</b>egrated relational <b>Enz</b>yme database) is a freely available resource
	focused on enzyme nomenclature. IntEnz is created in collaboration with the
    <a href="http://www.isb-sib.ch/" target="_blank">Swiss Institute of Bioinformatics (SIB)</a>.
	This collaboration is responsible for the production of the
    <a href="http://www.expasy.org/enzyme/" target="_blank">ENZYME</a> resource.
    <br/>
    IntEnz contains the recommendations of the Nomenclature Committee of the
    <a href="http://www.iubmb.org/" target="_blank">International Union of Biochemistry and
	Molecular Biology</a> (NC-IUBMB) on the nomenclature and classification of
	enzyme-catalysed reactions.
    </p>
    <p>
		<b>All data in IntEnz is freely accessible and available for anyone to use</b>.
	</p>

</div>
<div>
	<h2>Search</h2>
	<br clear="all"/>
	<div style="text-align: center; min-width: 480px">
		<div style="text-align: left; margin-left: auto; margin-right: auto; width: 480px">
	        <form method="get" action="query">
	            <input type="hidden" name="cmd" value="Search"/>
	            <span style="white-space: nowrap">
	            <input type="text" size="40" maxsize="100" name="q"/>
	            <input type="submit" value="Search" class="submit_button" style="margin-left: 10px">
	            </span>
	            <p style="margin-top: 1em">
	            	You can use <b>%</b> as a wildcard, as well as some
	            	<a href="javascript:windowOpenWithSize('encoding.html', 340, 400)">
	            	special characters
	            	<img style="vertical-align:middle;"
	            		src="images/alpha_icon.gif" alt="Special characters"
	            		width="16" height="15" border="1"/>
	            	</a>
	            	<br/>
					Examples: <span title="Search by EC number">1.1.1.1</span>,
	                    <span title="Search using % as a wildcard">alcohol%</span>,
	                    <span title="Search using special characters">&#945;-glucosidase</span>
	                <br/>Or try the <a href="search.jsp">advanced search</a>.
	            </p>
	        </form>
		</div>
	</div>
</div>
<%--
<div class="spotlight">
	<h2>Enzyme spotlight</h2>
	<div style="margin: 0px 15px 15px 15px">
	  <jsp:include page="spotlight/1.14.13.39.html"/>
	</div>
	<div style="padding: 10px">Go to the <a href="spotlight.jsp"
		title="Selected enzyme spotlights from previous releases">gallery</a>
		of previous releases spotlights.</div>
</div>
--%>
<div>
	<h2>Acknowledgements</h2>
	<p>
	<img src="images/eu_flag.jpg" style="margin: 0em 1em 1em 1em; float: left;" alt="EU"/>
    IntEnz is funded by the European Commission under SLING, grant agreement
    number 226073 (Integrating Activity) within Research Infrastructures of the
    FP7 Capacities Specific Programme.
	</p>
</div>
