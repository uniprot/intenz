package uk.ac.ebi.intenz.biopax.level2;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.biopax.paxtools.model.Model;
import org.biopax.paxtools.model.level2.biochemicalReaction;
import org.biopax.paxtools.model.level2.catalysis;
import org.biopax.paxtools.model.level2.dataSource;
import org.biopax.paxtools.model.level2.physicalEntity;
import org.biopax.paxtools.model.level2.physicalEntityParticipant;
import org.biopax.paxtools.model.level2.protein;
import org.biopax.paxtools.model.level2.publicationXref;
import org.biopax.paxtools.model.level2.relationshipXref;
import org.biopax.paxtools.model.level2.unificationXref;
import org.biopax.paxtools.model.level2.xref;

import uk.ac.ebi.biobabel.util.StringUtil;
import uk.ac.ebi.biobabel.util.collections.OperatorSet;
import uk.ac.ebi.intenz.domain.enzyme.Cofactor;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeComment;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeEntry;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeLink;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeName;
import uk.ac.ebi.intenz.domain.constants.View;
import uk.ac.ebi.intenz.domain.constants.XrefDatabaseConstant;
import uk.ac.ebi.intenz.domain.reference.Journal;
import uk.ac.ebi.intenz.domain.reference.Patent;
import uk.ac.ebi.intenz.domain.reference.Reference;
import uk.ac.ebi.rhea.domain.Database;
import uk.ac.ebi.rhea.domain.Reaction;
import uk.ac.ebi.rhea.biopax.level2.BiopaxBiochemicalReaction;
import uk.ac.ebi.rhea.biopax.level2.BiopaxPhysicalEntity;
import uk.ac.ebi.xchars.SpecialCharacters;
import uk.ac.ebi.xchars.domain.EncodingType;

/**
 * Adapter class to convert IntEnz <code>EnzymeEntry</code>s into BioPAX
 * <code>catalysys</code> objects and viceversa.<br/>
 * Note that one IntEnz <code>EnzymeEntry</code> may catalyze more than one
 * reaction. As BioPAX <code>catalysis</code> allows one CONTROLLED property
 * at most, several <code>catalysis</code> may be generated per IntEnz entry.
 */
public class BiopaxCatalysis {

    private EnzymeEntry enzymeEntry;

    private Collection<catalysis> cat;

    /**
     * Constructor from an IntEnz EnzymeEntry.
     * <br/>
     * The following BioPAX properties are currently ignored:
     * <ul>
     *  <li>CONTROL-TYPE: ACTIVATION by default</li>
     *  <li>PARTICIPANTS: defined in the enclosed reaction (CONTROLLED)</li>
     *  <li>AVAILABILITY</li>
     *  <li>EVIDENCE</li>
     *  <li>INTERACTION-TYPE</li>
     * </ul>
     * Notes about enzyme <b>names</b>:
     * <ul>
     *  <li>the systematic name is mapped to BioPAX' NAME.</li>
     *  <li>the common/accepted/recommended name is mapped to BioPAX' SHORT-NAME.</li>
     *  <li>BioPAX' SYNONYMS include both of them, following the specification.</li>
     * </ul>
     * Notes about <b>xrefs</b>:
     * <ul>
     *  <li>GO xrefs are assigned to BioPAX catalysis.</li>
     *  <li>PROSITE xrefs are assigned to BioPAX' CONTROLLER property, as
            relationshipXref.</li>
     *  <li>Any other xrefs are assigned to BioPAX' CONTROLLER property, as
            unificationXrefs</li>
     * </ul>
     * IntEnz <b>citations</b> are exported as publicationXref assigned to the
     * BioPAX' catalysis.
     * @param enzymeEntry an IntEnz enzyme.
     * @param model A BioPAX model to add the catalysis to.
     */
    public BiopaxCatalysis(EnzymeEntry enzymeEntry, Model model){
        this.enzymeEntry = enzymeEntry;
        cat = new HashSet<catalysis>();

        // bp:CONTROLLER
        String ec = enzymeEntry.getEc().toString();
        protein enzyme = model.addNew(protein.class,  ec);
        // bp:NAME - this will be the systematic name
        enzyme.setNAME(SpecialCharacters.getInstance(null)
            .xml2Display(enzymeEntry.getSystematicName().getName(), EncodingType.CHEBI_CODE));
        // bp:SHORT-NAME - common/accepted/recommended name
        enzyme.setSHORT_NAME(SpecialCharacters.getInstance(null)
            .xml2Display(enzymeEntry.getCommonName().getName(), EncodingType.CHEBI_CODE));
        // bp:SYNONYMS (BioPAX docs say NAME and SHORT-NAME should be included here)
        for (EnzymeName synonym : enzymeEntry.getSynonyms(View.INTENZ)){
            enzyme.addSYNONYMS(SpecialCharacters.getInstance(null)
                .xml2Display(synonym.getName(), EncodingType.CHEBI_CODE));
        }
        enzyme.addSYNONYMS(SpecialCharacters.getInstance(null)
            .xml2Display(enzymeEntry.getSystematicName().getName(), EncodingType.CHEBI_CODE));
        enzyme.addSYNONYMS(SpecialCharacters.getInstance(null)
            .xml2Display(enzymeEntry.getCommonName().getName(), EncodingType.CHEBI_CODE));

        // bp:COMMENT
        Set<String> bpComments = new HashSet<String>();
        for (EnzymeComment comment : enzymeEntry.getComments(View.INTENZ)){
            bpComments.add(SpecialCharacters.getInstance(null)
                .xml2Display(comment.getCommentText(), EncodingType.CHEBI_CODE));
        }

        // bp:XREF
        Set<xref> catXrefs = new HashSet<xref>();
        for (EnzymeLink link: enzymeEntry.getLinks()){
            if (link.getXrefDatabaseConstant().equals(XrefDatabaseConstant.GO)){
                catXrefs.add(getBpXref(link.getXrefDatabaseConstant().toString(),
                    link.getAccession(), model, null));
            } else if (link.getXrefDatabaseConstant().equals(XrefDatabaseConstant.PROSITE)){
                enzyme.addXREF(getBpXref(link.getXrefDatabaseConstant().toString(),
                    link.getAccession(), model, "Domains, families, functional sites"));
            } else if (link.getXrefDatabaseConstant().equals(XrefDatabaseConstant.SWISSPROT)){
                enzyme.addXREF(getBpXref(link.getXrefDatabaseConstant().toString(),
                    link.getAccession(), model, null));
            } else if (link.getXrefDatabaseConstant().equals(XrefDatabaseConstant.CAS)){
                enzyme.addXREF(getBpXref(link.getXrefDatabaseConstant().toString(),
                    link.getAccession(), model, null));
            } else if (link.getXrefDatabaseConstant().equals(XrefDatabaseConstant.MEROPS)){
                String meropsId = link.getSpecificUrl().substring(link.getSpecificUrl().indexOf("id=")+3);
                enzyme.addXREF(getBpXref(link.getXrefDatabaseConstant().toString(),
                    meropsId, model, null));
            }
        }

        // bp:XREF (publicationXref)
        Set<publicationXref> pubXrefs = new HashSet<publicationXref>();
        for (Reference citation : enzymeEntry.getReferences()){
            String pubXrefId = null;
            String xrefDb = null;
            String xrefAcc = null;
            if (citation instanceof Journal){
                xrefAcc = ((Journal) citation).getPubMedId();
                if (!StringUtil.isNullOrEmpty(xrefAcc)) xrefDb = "PUBMED";
            } else if (citation instanceof Patent){
                xrefAcc = ((Patent) citation).getPatentNumber();
                if (!StringUtil.isNullOrEmpty(xrefAcc)) xrefDb = "PATENT";
            }
            if (!StringUtil.isNullOrEmpty(xrefDb)){
                pubXrefId = xrefDb + "_" + xrefAcc;
            } else {
                pubXrefId = "PUB_" + citation.getPubId();
            }
            publicationXref pubXref = null;
            if (model.getIdMap().containsKey(pubXrefId)){
                pubXref = (publicationXref) model.getIdMap().get(pubXrefId);
            } else {
                pubXref = model.addNew(publicationXref.class, pubXrefId);
                if (!StringUtil.isNullOrEmpty(xrefDb)){
                    pubXref.setDB(xrefDb);
                    pubXref.setID(xrefAcc);
                }
                if (!StringUtil.isNullOrEmpty(citation.getAuthors())){
                    pubXref.addAUTHORS(SpecialCharacters.getInstance(null)
                        .xml2Display(citation.getAuthors(), EncodingType.CHEBI_CODE));
                }
                pubXref.addSOURCE(citation.getSource().toString());
                if (!StringUtil.isNullOrEmpty(citation.getTitle())){
                    pubXref.setTITLE(SpecialCharacters.getInstance(null)
                        .xml2Display(citation.getTitle(), EncodingType.CHEBI_CODE));
                }
                pubXref.setYEAR(Integer.valueOf(citation.getYear()));
            }
            pubXrefs.add(pubXref);
        }

        // bp:DATA-SOURCE
        dataSource bpDataSource =
            Biopax.getBpDataSource(Database.valueOf(enzymeEntry.getSource().toString()), model);

        // FIXME: some NPE (transferred/deleted entries) can be avoided here:
        for (Reaction r : enzymeEntry.getEnzymaticReactions().getReactions(View.INTENZ)){
            String catalysisId = Biopax.fixId(getBiopaxId(enzymeEntry, r));
            catalysis c = model.addNew(catalysis.class, catalysisId);
            // bp:CONTROLLER
            physicalEntityParticipant controller =
                    model.addNew(physicalEntityParticipant.class, "CTRLR_" + catalysisId);
            controller.setPHYSICAL_ENTITY(enzyme);
            c.addCONTROLLER(controller);
            // bp:COFACTOR
            for (Object o : enzymeEntry.getCofactors()){
                if (o instanceof Cofactor){
                    c.addCOFACTOR(getBiopaxCofactor((Cofactor) o, catalysisId, model));
                } else { // OperatorSet; XXX: here we loose operator information.
                    for (Object o2 : (OperatorSet) o){
                        c.addCOFACTOR(getBiopaxCofactor((Cofactor) o2, catalysisId, model));
                    }
                }
            }
            // bp:DATA-SOURCE
            c.addDATA_SOURCE(bpDataSource);
            // bp:CONTROLLED
            biochemicalReaction controlled =
                new BiopaxBiochemicalReaction(r, model, null/*FIXME*/, Biopax.RHEA_PREFIX).getBiopaxBiochemicalReaction();
            controlled.addEC_NUMBER(ec); // XXX: this is already done in the constructor, actually
            c.addCONTROLLED(controlled);
            // bp:DIRECTION; XXX: what about irreversible reactions?
            switch(r.getDirection()){
            case LR:
                c.setDIRECTION(org.biopax.paxtools.model.level2.Direction.PHYSIOL_LEFT_TO_RIGHT);
                break;
            case RL:
                c.setDIRECTION(org.biopax.paxtools.model.level2.Direction.PHYSIOL_RIGHT_TO_LEFT);
                break;
            case BI:
                c.setDIRECTION(org.biopax.paxtools.model.level2.Direction.REVERSIBLE);
                break;
            }
            // bp:COMMENT
            c.setCOMMENT(bpComments);
            // bp:XREF
            for (xref catXref: catXrefs){
                c.addXREF(catXref);
            }
            // bp:XREF (bibliography citations)
            for (publicationXref pubXref: pubXrefs){
                c.addXREF(pubXref);
            }
            cat.add(c);
        }
    }

    /**
     * Not yet implemented. Throws an exception.
     * @param cat
     */
    public BiopaxCatalysis(catalysis cat){
        throw new RuntimeException("Not yet implemented");
        //this.cat = Collections.singleton(cat);
    }

    /**
     * @return the IntEnz side of this apapter.
     */
    public EnzymeEntry getIntEnzEnzyme(){
        return enzymeEntry;
    }

    /**
     * @return the BioPAX side of this adapter.
     */
    public Collection<catalysis> getBiopaxCatalysis(){
        return cat;
    }

    private static String getBiopaxId(EnzymeEntry enzymeEntry, Reaction reaction){
 		return  enzymeEntry.getEc()
            + "_" + BiopaxBiochemicalReaction.getBiopaxId(reaction);
    }

    private physicalEntityParticipant getBiopaxCofactor(Cofactor cofactor, String catId, Model model){
        String cofactorId =  Biopax.fixId(SpecialCharacters.getInstance(null)
            .xml2Display(cofactor.getCompound().toString(), EncodingType.CHEBI_CODE))
            + "_" + catId;
        physicalEntityParticipant bpCofactor = null;
        bpCofactor = model.addNew(physicalEntityParticipant.class, cofactorId);
        physicalEntity bpCofactorCompound = null;
        String cofactorCompoundId = BiopaxPhysicalEntity.getBiopaxId(cofactor.getCompound());
        if (model.getIdMap().containsKey(cofactorCompoundId)){
            bpCofactorCompound = (physicalEntity) model.getIdMap().get(cofactorCompoundId);
        } else {
            bpCofactorCompound = new BiopaxPhysicalEntity(cofactor.getCompound(), model)
                .getBiopaxPhysicalEntity();
        }
        bpCofactor.setPHYSICAL_ENTITY(bpCofactorCompound);
        return bpCofactor;
    }

    private xref getBpXref(String db, String id, Model model, String relationship){
        xref x = null;
        String rdfId = Biopax.fixId(db + "_" + id);
        if (model.getIdMap().containsKey(rdfId)){
            x = (xref) model.getIdMap().get(rdfId);
        } else {
            x = relationship == null?
                model.addNew(unificationXref.class, rdfId):
                model.addNew(relationshipXref.class, rdfId);
            x.setDB(db);
            x.setID(id);
            if (relationship != null){
                ((relationshipXref) x).setRELATIONSHIP_TYPE(relationship);
            }
        }
        return x;
    }
}
