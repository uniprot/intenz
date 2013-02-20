<!DOCTYPE html>
<html>

<jsp:include page="head.jsp">
    <jsp:param name="title" value="IntEnz - FAQ"/>
</jsp:include>

<body class="level2">
<div id="wrapper" class="container_24">

<jsp:include page="headers.jsp">
    <jsp:param name="loc" value="docs"/>
</jsp:include>

<div class="grid_24 clearfix" id="content" role="main">

<section class="grid_18 alpha">
<h2>Frequently asked questions</h2>
<ol>
	<li><a href="#a1">What is IntEnz?</a></li>
	<li><a href="#a2">What is enzyme nomenclature?</a></li>
	<li><a href="#a3">Why do we need enzyme nomenclature as a relational database?</a></li>
	<li><a href="#a4">What is an EC number?</a></li>
	<li><a href="#a5">Incidentally, what does "EC" mean?</a></li>
	<li><a href="#a6">Isn't there an official web site of NC-IUBMB with the same data as IntEnz?</a></li>
	<li><a href="#a7">Why do we need IntEnz if there is already ENZYME?</a></li>
	<li><a href="#a8">Can I download the whole of the IntEnz data?</a></li>
</ol>
</section>

<section class="grid_6 omega">
    <%@ include file="docs.html" %>
</section>

<section>

<a name="a1"><h3>What is IntEnz?</h3></a>

<p>
	IntEnz (<b>Int</b>egrated relational <b>Enz</b>yme database) is a freely
	available resource focused on enzyme nomenclature.
</p>
	
<a name="a2"><h3>What is enzyme nomenclature?</h3></a>

<p>
	Enzyme nomenclature is a system of naming enzymes (protein catalysts).
	Enzyme nomenclature is closely linked to, but not identical with, 
	enzyme classification. Both the Classification and Nomenclature of Enzymes are
	described in our
	<a href="http://www.ebi.ac.uk/intenz/rules.jsp" title="IntEnz - Classification rules"><i>classification
	rules</i></a> page.
</p>
	
<a name="a3"><h3>Why do we need enzyme nomenclature as a relational database?</h3></a>
	
<p>
	A relational database provides better interoperability with other 
	bioinformatics resources. All the major biological databases at the EBI, such 
	as <a href="http://www.ebi.ac.uk/embl/" title="EMBL Nucleotide Sequence Database">EMBL
	Nucleotide Sequence Database</a>, 
	<a href="http://www.ebi.uniprot.org/" title="UniProt - Universal Protein Resource">UniProt</a> and 
	<a href="http://www.ebi.ac.uk/msd/" title="Macromolecular Structure Database">MSD</a>, are relational.
</p>
	
<a name="a4"><h3>What is an EC number?</h3></a>
	
<p>
	This is a string of digits and periods, such as EC
	<a href="http://www.ebi.ac.uk/intenz/query?cmd=SearchEC&ec=6.3.1.2">6.3.1.2</a>, that is 
	meant both to be an identifier of the enzyme and to provide a clue as to the 
	nature of the catalysed reaction, i.e. it is a part of enzyme classification. 
	Our example EC number consists of class number 6, subclass number 3, 
	sub-subclass number 1 and the serial number 2.
</p>
	
<a name="a5"><h3>Incidentally, what does "EC" mean?</h3></a>
	
<p>
	"EC" in the EC number means neither "Enzyme Classification" nor "European 
	Commission". It stands for "Enzyme Commission" which is a short name for the 
	International Commission on Enzymes, which was established more than 50 years 
	ago with an aim to develop a systematic approach to the nomenclature and 
	classification of enzymes. Since then, the "Enzyme Commission" has been 
	succeeded by a number of various bodies as described in
	<a href="http://www.chem.qmul.ac.uk/iubmb/enzyme/history.html" title="Historical Introduction to Enzyme Nomenclature" target="blank"><i>Historical introduction to Enzyme Nomenclature</i></a>.
	Currently the Nomenclature Committee of the 
	<a href="http://www.iubmb.org/" title="IUBMB web site" target="blank">International Union of Biochemistry and Molecular Biology</a> (NC-IUBMB) 
	is responsible for the nomenclature and classification of enzymes.
</p>
	
<a name="a6"><h3>Isn't there an official web site of NC-IUBMB with the same data as IntEnz?</h3></a>
	
<p>
	There is an <a href="http://www.chem.qmul.ac.uk/iubmb/enzyme/">official web 
	site for the Enzyme Nomenclature</a>.
	However it presents the whole of the Enzyme Nomenclature as a set of static HTML pages. IntEnz includes all of these data together with other data, originally from the ENZYME database.
</p>
	
<a name="a7"><h3>Why do we need IntEnz if there is already ENZYME?</h3></a>
	
<p>
	ENZYME started its life separately as a flat text file but now it is an 
	integral part of IntEnz. If you still want the good old <tt>ENZYME.dat</tt> file, it is
	made available for you at  
	<a href="ftp://ftp.ebi.ac.uk/pub/databases/intenz/enzyme">ftp://ftp.ebi.ac.uk/pub/databases/intenz/enzyme</a>. You also may want to see any IntEnz entry in 
	ENZYME format; for that, click on the tab named "ENZYME view", somewhere close
	to the top of the page.
</p>
	
<a name="a8"><h3>Can I download the whole of the IntEnz data?</h3></a>
	
<p>
	Sure: go to
	<a href="http://www.ebi.ac.uk/intenz/downloads.jsp">http://www.ebi.ac.uk/intenz/downloads.jsp</a>.
</p>

</section>

</div>

<%@ include file="footer.jsp" %>

</div>
</body>
</html>
