package uk.ac.ebi.intenz.tools.sib.sptr_enzyme;

import uk.ac.ebi.interfaces.Factory;
import uk.ac.ebi.interfaces.uniref.*;
import uk.ac.ebi.interfaces.uniparc.UniParcEntry;
import uk.ac.ebi.interfaces.uniparc.UniParcException;
import uk.ac.ebi.interfaces.uniparc.UniParcCrossReferenceMetaData;
import uk.ac.ebi.interfaces.uniparc.UniParcCrossReference;
import uk.ac.ebi.interfaces.clustr.CluSTrEntry;
import uk.ac.ebi.interfaces.clustr.CluSTrException;
import uk.ac.ebi.interfaces.go.*;
import uk.ac.ebi.interfaces.interpro.*;
import uk.ac.ebi.interfaces.pythia.PythiaEntry;
import uk.ac.ebi.interfaces.pythia.PythiaException;
import uk.ac.ebi.interfaces.pythia.PredictiveInstance;
import uk.ac.ebi.interfaces.integr8.*;
import uk.ac.ebi.interfaces.citation.*;
import uk.ac.ebi.interfaces.enzyme.EnzymeEntry;
import uk.ac.ebi.interfaces.feature.Feature;
import uk.ac.ebi.interfaces.feature.FeatureException;
import uk.ac.ebi.interfaces.feature.FeatureLocation;
import uk.ac.ebi.interfaces.sptr.*;
import uk.ac.ebi.interfaces.sptr.Gene;
import uk.ac.ebi.interfaces.sptr.SPTREntry;
import uk.ac.ebi.interfaces.sptr.spin.SPINSubmission;

/**
 * This class is a simple extension of the abstract {@link uk.ac.ebi.interfaces.Factory Factory},
 * which allows to create {@link uk.ac.ebi.intenz.tools.sib.sptr_enzyme.EnzymeCrossReference EnzymeCrossReference} instances only.
 *
 * @author Michael Darsow
 * @version preliminary - $Revision: 1.2 $ $Date: 2008/01/28 11:43:22 $
 */
public class EnzymeXrefFactory extends Factory {

  public SPTREntry newSPTREntryInstance(int entryType) throws SPTRException {
    return null;
  }

  public SPTREntry newSPTREntryInstance() throws SPTRException {
    return null;
  }

  public SPTREntry copySPTREntryInstance(SPTREntry entry) throws SPTRException {
    return null;
  }

  public EnzymeEntry newEnzymeEntryInstance() throws SPTRException {
    return null;
  }

  public EnzymeEntry copyEnzymeEntryInstance(EnzymeEntry entry) throws SPTRException {
    return null;
  }

  public Feature newFeatureInstance(String featureType) throws FeatureException {
    return null;
  }

  public Feature copyFeatureInstance(Feature feature) throws FeatureException {
    return null;
  }

  public FeatureLocation newFeatureLocationInstance() throws FeatureException {
    return null;
  }

  public FeatureLocation copyFeatureLocationInstance(FeatureLocation location) throws FeatureException {
    return null;
  }

  public Author newAuthorInstance() throws CitationException {
    return null;
  }

  public Author copyAuthorInstance(Author author) throws CitationException {
    return null;
  }

  public Book newBookInstance() throws CitationException {
    return null;
  }

  public Book copyBookInstance(Book book) throws CitationException {
    return null;
  }

  public ElectronicArticle newElectronicArticleInstance() throws CitationException {
    return null;
  }

  public ElectronicArticle copyElectronicArticleInstance(ElectronicArticle electronicArticle) throws CitationException {
    return null;
  }

  public JournalArticle newJournalArticleInstance() throws CitationException {
    return null;
  }

  public JournalArticle copyJournalArticleInstance(JournalArticle journalArticle) throws CitationException {
    return null;
  }

  public Patent newPatentInstance() throws CitationException {
    return null;
  }

  public Patent copyPatentInstance(Patent patent) throws CitationException {
    return null;
  }

  public Submission newSubmissionInstance() throws CitationException {
    return null;
  }

  public Submission copySubmissionInstance(Submission submission) throws CitationException {
    return null;
  }

  public Thesis newThesisInstance() throws CitationException {
    return null;
  }

  public Thesis copyThesisInstance(Thesis thesis) throws CitationException {
    return null;
  }

  public UnpublishedObservations newUnpublishedObservationsInstance() throws CitationException {
    return null;
  }

  public UnpublishedObservations copyUnpublishedObservationsInstance(UnpublishedObservations unpublishedObservations) throws CitationException {
    return null;
  }

  public UnpublishedResults newUnpublishedResultsInstance() throws CitationException {
    return null;
  }

  public UnpublishedResults copyUnpublishedResultsInstance(UnpublishedResults unpublishedResults) throws CitationException {
    return null;
  }

  public SPTRComment newSPTRCommentInstance(String type) throws SPTRException {
    return null;
  }

  public SPTRComment copySPTRCommentInstance(SPTRComment sptrComment) throws SPTRException {
    return null;
  }

  public Isoform newIsoformInstance() throws SPTRException {
    return null;
  }

  public Isoform copyIsoformInstance(Isoform isoform) throws SPTRException {
    return null;
  }

  /**
   * <em class="text">
   * Returns a new instance of an EnzymeCrossReference.
   * </em>
   * @return A new instance of an EnzymeCrossReference.
   * @throws uk.ac.ebi.interfaces.sptr.SPTRException Needs to be implemented by individual application.
   */
  public SPTRCrossReference newEnzymeCrossReference(String type) throws SPTRException {
    // Check database name (type).
    if(!type.equals(EnzymeCrossReference.SWISSPROT) &&
       !type.equals(EnzymeCrossReference.PROSITE) &&
       !type.equals(EnzymeCrossReference.MIM)) throw new EnzymeCrossReferenceException("The database name given is not supported.");

    return new EnzymeCrossReference(type);
  }

  public SPTRCrossReference newSPTRCrossReference(String type) throws SPTRException {
    return null;
  }

  public SPTRCrossReference copySPTRCrossReference(SPTRCrossReference sptrCrossReference) throws SPTRException {
    return null;
  }

  public ProteomeEntry copyProteomeEntryInstance(ProteomeEntry proteomeEntry) throws Integr8Exception {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public SPINSubmission newSPINSubmissionInstance() throws CitationException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public SPINSubmission copySPINSubmissionInstance(SPINSubmission spinSubmission) throws CitationException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public SPTRCrossReference newSPTRCrossReferenceInstance(String s) throws SPTRException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public SPTRCrossReference copySPTRCrossReferenceInstance(SPTRCrossReference sptrCrossReference) throws SPTRException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public PythiaEntry newPythiaEntryInstance(int i) throws PythiaException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public PythiaEntry copyPythiaEntryInstance(PythiaEntry pythiaEntry) throws PythiaException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public PredictiveInstance newPredictiveInstance(String s) throws PythiaException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public PredictiveInstance copyPredictiveInstance(PredictiveInstance predictiveInstance) throws PythiaException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public InterProProteinData newInterProProteinData(SPTREntry sptrEntry) throws InterProException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public InterProProteinData copyInterProProteinData(InterProProteinData interProProteinData) throws InterProException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public InterProStructure newInterProStructure(InterProEntry interProEntry) throws InterProException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public InterProStructure copyInterProStructure(InterProStructure interProStructure) throws InterProException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public InterProEntry newInterProEntry() throws InterProException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public InterProEntry copyInterProEntry(InterProEntry interProEntry) throws InterProException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public SignatureDBHit newSignatureDBHit() throws InterProException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public SignatureDBHit copySignaturDBHit(SignatureDBHit signatureDBHit) throws InterProException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public SignatureDBEntry newSignatureDBEntry(int i) throws InterProException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public SignatureDBEntry copySignatureDBEntry(SignatureDBEntry signatureDBEntry) throws InterProException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

   public Annotation newAnnotationInstance () throws InterProException {
      return null;  //To change body of implemented methods use File | Settings | File Templates.
   }

   public Annotation copyAnnotationInstance (Annotation annotation) throws InterProException {
      return null;  //To change body of implemented methods use File | Settings | File Templates.
   }

   public InterProCrossReference newInterProCrossReferenceInstance (String s) throws InterProException {
      return null;  //To change body of implemented methods use File | Settings | File Templates.
   }

   public InterProCrossReference copyInterProCrossReferenceInstance (InterProCrossReference interProCrossReference) throws InterProException {
      return null;  //To change body of implemented methods use File | Settings | File Templates.
   }

   public GOTerm newGOTerm() throws GOException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public GOTerm copyGOTerm(GOTerm goTerm) throws GOException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public GOEvidence newGOEvidence() throws GOException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public GOEvidence copyGOEvidence(GOEvidence goEvidence) throws GOException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public GOProteinData newGOProteinData(SPTREntry sptrEntry) throws GOException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public GOProteinData copyGOProteinData(GOProteinData goProteinData) throws GOException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public GOStructure newGOStructure() throws GOException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public GOStructure copyGOStructure(GOStructure goStructure) throws GOException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public CluSTrEntry newCluSTrEntry() throws CluSTrException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public CluSTrEntry copyCluSTrEntry(CluSTrEntry cluSTrEntry) throws CluSTrException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public UniParcEntry newUniParcEntry() throws UniParcException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public UniParcEntry newUniParcEntry(SPTREntry sptrEntry, String s,
                                      UniParcCrossReferenceMetaData uniParcCrossReferenceMetaData) throws UniParcException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public UniParcEntry copyUniParcEntry(UniParcEntry uniParcEntry) throws UniParcException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public UniParcCrossReference newUniParcCrossReference(String s) throws UniParcException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public UniParcCrossReference copyUniParcCrossReference(UniParcCrossReference uniParcCrossReference) throws UniParcException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public UniParcCrossReferenceMetaData newUniParcCrossReferenceMetaData() throws UniParcException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public UniParcCrossReferenceMetaData copyUniParcCrossReferenceMetaData(
          UniParcCrossReferenceMetaData uniParcCrossReferenceMetaData) throws UniParcException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public UniRef100Entry newUniRef100Entry() throws UniRefException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public UniRef100Entry newUniRef100Entry(SPTREntry sptrEntry) throws UniRefException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public UniRef90Entry newUniRef90Entry() throws UniRefException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public UniRef90Entry newUniRef90Entry(SPTREntry sptrEntry) throws UniRefException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public UniRef50Entry newUniRef50Entry() throws UniRefException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public UniRef50Entry newUniRef50Entry(SPTREntry sptrEntry) throws UniRefException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public ClusterMember newClusterMember(String s) throws UniRefException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public Gene newGeneInstance() throws SPTRException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public Gene copyGeneInstance(Gene gene) throws SPTRException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public uk.ac.ebi.interfaces.integr8.SPTREntry newIntegr8SPTREntryInstance(int i) throws Integr8Exception {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public uk.ac.ebi.interfaces.integr8.SPTREntry newIntegr8SPTREntryInstance(SPTREntry sptrEntry) throws Integr8Exception {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public uk.ac.ebi.interfaces.integr8.SPTREntry copyIntegr8SPTREntryInstance(uk.ac.ebi.interfaces.integr8.SPTREntry sptrEntry) throws Integr8Exception {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public uk.ac.ebi.interfaces.integr8.Gene newIntegr8GeneInstance() throws Integr8Exception {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public uk.ac.ebi.interfaces.integr8.Gene copyIntegr8GeneInstance(Gene gene) throws Integr8Exception {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public uk.ac.ebi.interfaces.integr8.Gene copyIntegr8GeneInstance(uk.ac.ebi.interfaces.integr8.Gene gene) throws Integr8Exception {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public ProteomeEntry newProteomeEntryInstance() throws Integr8Exception {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public Absorption newAbsorptionInstance() throws SPTRException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public Absorption copyAbsorptionInstance(Absorption absorption) throws SPTRException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public KineticParameter newKineticParameterInstance() throws SPTRException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public KineticParameter copyKineticParameterInstance(KineticParameter kineticParameter) throws SPTRException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public MaximumVelocity newMaximumVelocityInstance() throws SPTRException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public MaximumVelocity copyMaximumVelocityInstance(MaximumVelocity maximumVelocity) throws SPTRException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public MichaelisConstant newMichaelisConstantInstance() throws SPTRException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public MichaelisConstant copyMichaelisConstantInstance(MichaelisConstant michaelisConstant) throws SPTRException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public MassSpectrometryRange newMassSpectrometryRangeInstance() throws SPTRException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public MassSpectrometryRange copyMassSpectrometryRangeInstance(MassSpectrometryRange massSpectrometryRange) throws SPTRException {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }
}
