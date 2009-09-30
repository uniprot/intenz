IntEnz XML
=============================================================================

Thanks for your interest in IntEnz XML!

This directory contains IntEnz data exported in XML format, according to the
supplied schemas (intenz.xsd and enzyme.xsd).

Directory structure:

  |- IntEnzXML-README.txt: this file.
  |- IntEnzXML.zip: a zip file containing this whole directory.
  |- enzyme.xsd: XML schema for a single entry.
  |- intenz.xsd: XML schema for a tree of enzyme entries. Requires enzyme.xsd.
  |- XCHARS: IntEnz data in XCHARS 'flavour'.
  |   |- intenz.xml: the whole IntEnz data set in XCHARS 'flavour'.
  |   \- (directory tree for single EC numbers)
  \- ASCII: IntEnz data in ASCII 'flavour'.
      |- intenz.xml: the whole IntEnz data set in ASCII 'flavour'.
      \- (directory tree for single EC numbers)


The dump is available in two 'flavours':

    * XCHARS (for eXtended CHARacterS) {http://intenz.sf.net/xchars}:
      data which includes XML markup in the IntEnz database is exported
      /as is/. Example: EC 1.1.1.100 has an accepted name
      '3-oxoacyl-<protein>acyl-carrier-protein</protein> reductase'. The
      corresponding XML will be:

      <accepted_name view="INTENZ,IUBMB,SIB">
      3-oxoacyl-<protein>acyl-carrier-protein</protein> reductase
      </accepted_name>

      For more information about xchars, please visit its sourceforge
      website {http://intenz.sf.net/xchars}.

    * ASCII: those mentioned fields are translated into the ASCII version.
      Following the same example, the ASCII flavour of EC 1.1.1.100's accepted
      name will be:

      <accepted_name view="INTENZ,IUBMB,SIB">
      3-oxoacyl-[acyl-carrier-protein] reductase
      </accepted_name>


Each XML file in the directory trees contains all the data related to an EC
number, for all three views - IntEnz, IUBMB and ENZYME (SIB) - available in
the IntEnz website {http://www.ebi.ac.uk/intenz}.
Note that cross-references based on EC numbers as identifiers have not been
included.


Using XSLT
-----------------------------------------------------------------------------

Extracting data for just one of those views should be straightforward using XSL
stylesheets, including the ones available at {http://tinyurl.com/76go2h}. For
example, to get the systematic name of EC 1.1.1.1 in its default formatting:

<xsl:stylesheet
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
    xmlns:i="http://www.ebi.ac.uk/intenz">
    <!-- You have to download the package and extract an XSL: --> 
    <xsl:import href="xchars2default.xsl"/>
    <xsl:output method="text"/>
    <xsl:template match="/">
        <xsl:apply-templates
            select="/i:intenz/i:ec_class[@ec1='1']/i:ec_subclass[@ec2='1']/i:ec_sub-subclass[@ec3='1']/i:enzyme/i:systematic_name"/>
    </xsl:template>
</xsl:stylesheet>

Or, to get any EC numbers with reactions involving proteins:

<xsl:stylesheet
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
    xmlns:i="http://www.ebi.ac.uk/intenz"
    xmlns:x="http://www.ebi.ac.uk/xchars">
    <xsl:import href="xchars2default.xsl"/>
    <xsl:output method="text"/>
    <xsl:template match="/">
        <xsl:apply-templates select="//i:reaction/x:protein/parent::*"/>
    </xsl:template>
    <xsl:template match="i:reaction">
        <xsl:value-of select="../../i:ec"/><xsl:text>
        </xsl:text>
        <xsl:apply-templates/><xsl:text>
</xsl:text>
    </xsl:template>
</xsl:stylesheet>


Please contact us in <intenz-help@lists.sourceforge.net> with any problems or
suggestions regarding IntEnz XML dumps.

