IntEnz-BioPAX
===============================================================================

The OWL file in this directory containst the latest IntEnz release in BioPAX
level 2 format.

IntEnz reactions are exported as Rhea (http://www.ebi.ac.uk/rhea) reactions
whenever possible.

Notes about enzyme names:

    * the systematic name is mapped to BioPAX' NAME.
    * the common/accepted/recommended name is mapped to BioPAX' SHORT-NAME.
    * BioPAX' SYNONYMS include both of them, following the specification.

Notes about xrefs:

    * GO xrefs are assigned to BioPAX catalysis.
    * PROSITE xrefs are assigned to BioPAX' CONTROLLER property, as
      relationshipXref.
    * Any other xrefs are assigned to BioPAX' CONTROLLER property, as
      unificationXrefs

IntEnz citations are exported as publicationXref assigned to the BioPAX'
catalysis.

The following BioPAX properties are currently ignored:

    * CONTROL-TYPE: ACTIVATION by default
    * PARTICIPANTS: defined in the enclosed reaction (CONTROLLED)
    * AVAILABILITY
    * EVIDENCE
    * INTERACTION-TYPE

Please contact us in <intenz-help@lists.sourceforge.net> with any problems or
suggestions regarding IntEnz BioPAX dumps.
