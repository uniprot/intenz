<!DOCTYPE html>
<html>

<jsp:include page="head.jsp">
    <jsp:param name="title" value="IntEnz - Advice on enzyme submission"/>
</jsp:include>

<body class="level2">
<div id="wrapper" class="container_24">

<jsp:include page="headers.jsp">
    <jsp:param name="loc" value="docs"/>
</jsp:include>

<div class="grid_24 clearfix" id="content" role="main">

<section class="grid_18 alpha">
    <h2>EC guidelines</h2>
	<div class="iconboxcontents tocBody" id="rulesContent">
      <ul>
        <li><a href="#class">The enzyme classification system</a></li>
        <li><a href="#list">An enzyme should be listed if...</a></li>
        <li><a href="#notlisted">An enzyme is not classified as a new enzyme if...</a></li>
        <li><a href="#multifun">Multienzymes</a></li>
        <li><a href="#listing">To obtain a listing for a previously unlisted enzyme</a></li>
        <li><a href="#info">Information needed in order to draft an enzyme entry</a></li>
        <li><a href="#then">What happens next?</a></li>
        <li><a href="#ammend">Amendments to existing entries</a></li>
        <li><a href="#ECnumber">Use of EC numbers</a></li>
      </ul>
	</div>

<p>
This is a brief guide to the enzyme list and principles of classification.
</p>

</section>

<section class="grid_6 omega">
    <%@ include file="docs.html" %>
</section>

<section>

      <a name="class"></a><h3 id="class">The enzyme classification system</h3>
      <p>
        Each enzyme is assigned a recommended name; usually at the suggestion of the person who submits the details.
        The rules used to classify enzymes can be found under <a href="rules.jsp">Classification rules</a>.
        Each enzyme is allocated a four-digit EC number, the first three digits of which define the reaction catalysed
        and the fourth of which is a unique identifier (serial number). Each enzyme is also assigned a systematic name
        that uniquely defines the reaction catalysed.
      </p>
      <p>
        The recommended names and EC number should be referred to in publications. This has a number of benefits:
        it will eliminate some of the ambiguities in the literature that have been caused by people using the same
        name for different enzymes and will also make the searching of literature databases more efficient. The EC
        number can also be used to find ancillary information, such as genes, sequences, properties and structures
        in other databases.
      </p>

        <a name="list"></a><h3 id="list">An enzyme should be listed <b>if</b>:</h3>

        <ul>
          <li>there is published evidence that it catalyses a distinct reaction that has not been listed previously.</li>
          <li>the reaction is similar to one already listed, but with a distinctly different substrate specificity,
           e.g. D- and L-amino-acid oxidases are classified as separate enzymes based on their different substrate specificities.</li>
        </ul>

        <a name="notlisted"></a><h3 id="notlisted">An enzyme is not classified as a new enzyme <b>if</b>:</h3>

        <ul>
          <li>the reaction is the same as that of an enzyme already listed, but the enzyme differs in source, structure
           or mechanism. Such differences may be mentioned in the "Comments" section and we would welcome your
           suggestions for such additions.</li>
          <li>the reaction represents an intermediate step in a reaction mechanism. Enzymes are classified based on the
           overall reaction that they catalyse.</li>
          <li>the enzyme is one that is already known to have a broad specificity and the reaction catalysed is just
           another specific example of the general reaction. In this case, the reaction is given a more general
           description (example: the reaction for alcohol dehydrogenase, EC 1.1.1.1, is written as
           "an alcohol + NAD = an aldehyde or ketone + NADH<small><sub>2</sub></small>").</li>
        </ul>

      <p>
        There are exceptions to these guidelines, for example, among the peptidases and ATPases/GTPases.
      </p>

        <a name="multifun"></a><h3 id="multifun">Multienzymes:</h3>

      <p>
        An enzyme system, consisting of a number of proteins with different catalytic activities is named a
        <i>system</i> (e.g. fatty acid synthase system). The overall reaction is not normally given an EC number
        but each individual enzyme is given a number, and its involvement in the system is described in the comments.
      </p>

        <a name="listing"></a><h3 id="listing">To obtain a listing for a previously unlisted enzyme:</h3>

      <p>
        The procedure for recommending a new enzyme for inclusion in the enzyme database is straightforward.
        Complete the form available under <a class="externalLink" target="_blank"
        href="http://www.expasy.ch/enzyme/enz_new_form.html">New Enzyme</a>, which will
        be sent to <a class="icon_email" href="mailto:ktipton@tcd.ie; sboyce@tcd.ie">Professor
        K.F. Tipton and Dr S. Boyce</a>, of Trinity College Dublin.
        They are responsible for assignment of EC numbers and maintenance of the enzyme
        list for the nomenclature committee of IUBMB. Enzymes that have been approved are
        displayed on this website as well as those awaiting approval.
      </p>

        <a name="info"></a><h3 id="info">Information you are requested to provide:</h3>

        <ul>
          <li>The overall reaction equation, including substrate(s), product(s), donor, acceptor, stoichiometry etc.</li>
          <li>Suggested name for the enzyme.</li>
          <li>Other names by which the enzyme is known (if any).</li>
          <li>Full literature references (preferably journals, but books are also acceptable. To aid the classification
           process, please send reprints to Dr. Sin&eacute;ad Boyce, Department of Biochemistry, Trinity College, Dublin
            2, Ireland), describing the properties of the enzyme. General review(s) about the enzyme are also helpful,
            if available.</li>
          <li>Any information that you know of, on the source of the enzyme, structure, cofactors, etc., to be included
           in the comments section.</li>
          <li>Any information that you know of, on the source of the enzyme, structure, cofactors, etc., as it may be
          suitable for inclusion in the Comments section.</li>
        </ul>

      <a name="then"></a><h3 id="then">Then what happens:</h3>

      <ul>
        <li>A draft entry for the enzyme, including a provisional EC number, reaction, systematic name, recommended
        name etc., is prepared and sent to the nomenclature committee for refinement and approval.</li>
        <li>The enzyme entry is then displayed on the web under <i>Proposed enzymes</i> for a period of two months,
        to enable the biochemical community to make comments and suggest changes, if appropriate.</li>
        <li>The enzyme is then incorporated in the enzyme list.</li>
      </ul>

        <a name="ammend"></a><h3 id="ammend">Amendments to existing entries:</h3>

      <p>
        You can also provide information about errors or updates in existing enzyme entries by completing the
        <a class="externalLink" target="_blank"
        href="http://www.expasy.ch/enzyme/enz_update_form.html">Update form</a>. If an error necessitates
        changing the classification of an enzyme, i.e., its EC number, then the old entry will be marked as having
        been deleted or transferred and a link will be provided to the new entry.
      </p>

        <a name="ECnumber"></a><h3 id="ECnumber">Use of EC numbers</h3>

      <p>
        Enzyme entries, including EC numbers, may be quoted in any publication without permission. The copyright
        of the enzyme list rests with the IUBMB.
	</p>
	<span class="linktotop"><a href="javascript: toTop()">Top</a></span>

<%@ include file="footer.jsp" %>

</div>
</body>
</html>
