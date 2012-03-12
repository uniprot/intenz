package uk.ac.ebi.intenz.tools.export;

import java.io.OutputStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.xml.sax.SAXException;

import uk.ac.ebi.biobabel.util.collections.OperatorSet;
import uk.ac.ebi.intenz.domain.constants.EnzymeNameQualifierConstant;
import uk.ac.ebi.intenz.domain.constants.EnzymeViewConstant;
import uk.ac.ebi.intenz.domain.constants.XrefDatabaseConstant;
import uk.ac.ebi.intenz.domain.enzyme.Cofactor;
import uk.ac.ebi.intenz.domain.enzyme.EnzymaticReactions;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeClass;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeComment;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeCommissionNumber;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeEntry;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeLink;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeName;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeSubSubclass;
import uk.ac.ebi.intenz.domain.enzyme.EnzymeSubclass;
import uk.ac.ebi.intenz.domain.reference.Book;
import uk.ac.ebi.intenz.domain.reference.Journal;
import uk.ac.ebi.intenz.domain.reference.Patent;
import uk.ac.ebi.intenz.domain.reference.Reference;
import uk.ac.ebi.intenz.tools.sib.translator.XCharsASCIITranslator;
import uk.ac.ebi.intenz.xml.jaxb.BookType;
import uk.ac.ebi.intenz.xml.jaxb.CofactorType;
import uk.ac.ebi.intenz.xml.jaxb.DatabaseType;
import uk.ac.ebi.intenz.xml.jaxb.EcClassType;
import uk.ac.ebi.intenz.xml.jaxb.EcSubclassType;
import uk.ac.ebi.intenz.xml.jaxb.EcSubsubclassType;
import uk.ac.ebi.intenz.xml.jaxb.Editorial;
import uk.ac.ebi.intenz.xml.jaxb.EntryType;
import uk.ac.ebi.intenz.xml.jaxb.EnzymeNameQualifierType;
import uk.ac.ebi.intenz.xml.jaxb.EnzymeNameType;
import uk.ac.ebi.intenz.xml.jaxb.EnzymeType;
import uk.ac.ebi.intenz.xml.jaxb.Intenz;
import uk.ac.ebi.intenz.xml.jaxb.JournalType;
import uk.ac.ebi.intenz.xml.jaxb.LinkType;
import uk.ac.ebi.intenz.xml.jaxb.ObjectFactory;
import uk.ac.ebi.intenz.xml.jaxb.PatentType;
import uk.ac.ebi.intenz.xml.jaxb.ReactionType;
import uk.ac.ebi.intenz.xml.jaxb.ReferenceType;
import uk.ac.ebi.intenz.xml.jaxb.ViewType;
import uk.ac.ebi.intenz.xml.jaxb.ViewableType;
import uk.ac.ebi.intenz.xml.jaxb.XmlContentType;
import uk.ac.ebi.rhea.domain.Database;
import uk.ac.ebi.rhea.domain.Reaction;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

/**
 * Exporter of IntEnz data in XML format.
 * Currently, the following <i>flavours</i> of XML are available:
 * <ul>
 * 		<li>ASCII: the IntEnz fields with XML markup are translated
 * 			into ASCII.</li>
 * 		<li>XCHARS: those fields are left as are, but within <code>CDATA</code>
 * 			sections.</li>
 * </ul>
 * The flavour can be changed using the
 * {@link #setFlavour(uk.ac.ebi.intenz.tools.export.XmlExporter.Flavour) setFlavour}
 * method.<br>
 * Note that the <code>export</code> methods are synchronized in order to avoid changing
 * the flavour in the middle of a dump.
 * @author rafalcan
 */
public class XmlExporter {

    static enum Flavour { ASCII, XCHARS }
    
    /**
     * Elements known to (possibly) contain XML markup.
     */
    static final String[] XML_CONTENT_ELEMENTS = new String[]{
            "http://www.ebi.ac.uk/intenz^accepted_name",
            "http://www.ebi.ac.uk/intenz^authors",
            "http://www.ebi.ac.uk/intenz^cofactor",
            "http://www.ebi.ac.uk/intenz^comment",
            "http://www.ebi.ac.uk/intenz^description",
            "http://www.ebi.ac.uk/intenz^editor",
            "http://www.ebi.ac.uk/intenz^link",
            "http://www.ebi.ac.uk/intenz^name",
            "http://www.ebi.ac.uk/intenz^note",
            "http://www.ebi.ac.uk/intenz^reaction",
            "http://www.ebi.ac.uk/intenz^synonym",
            "http://www.ebi.ac.uk/intenz^systematic_name",
            "http://www.ebi.ac.uk/intenz^title"
    };
    
    /**
     * Pre-defined namespaces prefixes to be declared in the root element.
     */
    private final static Map<String, String> NS_PREFIXES =
    	new Hashtable<String, String>();
    /**
     * Schema locations for predefined namespaces.
     */
    private final static Map<String, String> NS_SCHEMA_LOCATIONS =
    	new Hashtable<String, String>();
    static {
    	NS_PREFIXES.put("http://www.w3.org/2001/XMLSchema-instance", "xsi");
		NS_PREFIXES.put("http://www.ebi.ac.uk/xchars", "x");
    }

    private Map<EnzymeNameQualifierConstant, EnzymeNameQualifierType> NAME_QUALIFIERS;
    private Map<EnzymeViewConstant, ViewType> VIEWS;
    private Map<XrefDatabaseConstant, DatabaseType> DATABASES;
    private Map<String, Object> DESCRIPTIONS;
    private Marshaller marshaller;
    private ObjectFactory of;
	private Flavour flavour;
    
    XmlExporter() throws JAXBException, SAXException{
		this.DESCRIPTIONS = null;
		this.flavour = Flavour.XCHARS;
        this.of = new ObjectFactory();
        buildNameQualifiersMap();
        buildViewMap();
        buildDbMap();
        JAXBContext context = JAXBContext.newInstance("uk.ac.ebi.intenz.xml.jaxb");
        marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION,
        		"http://www.ebi.ac.uk/intenz ftp://ftp.ebi.ac.uk/pub/databases/intenz/xml/intenz.xsd");
        marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper",
        		new NamespacePrefixMapper(){
					@Override
					public String getPreferredPrefix(String arg0, String arg1, boolean arg2) {
						return NS_PREFIXES.get(arg0);
					}
					@Override
					public String[] getPreDeclaredNamespaceUris() {
						return NS_PREFIXES.keySet().toArray(
								new String[NS_PREFIXES.size()]);
					}
				});
		Schema intenzXsd = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
			.newSchema(XmlExporter.class.getClassLoader().getResource("intenz.xsd"));
		marshaller.setSchema(intenzXsd);
    }
    
    private void buildNameQualifiersMap(){
        NAME_QUALIFIERS = new HashMap<EnzymeNameQualifierConstant, EnzymeNameQualifierType>();
        NAME_QUALIFIERS.put(EnzymeNameQualifierConstant.AMBIGUOUS, EnzymeNameQualifierType.AMBIGUOUS);
        NAME_QUALIFIERS.put(EnzymeNameQualifierConstant.INCORRECT, EnzymeNameQualifierType.INCORRECT);
        NAME_QUALIFIERS.put(EnzymeNameQualifierConstant.MISLEADING, EnzymeNameQualifierType.MISLEADING);
        NAME_QUALIFIERS.put(EnzymeNameQualifierConstant.MISPRINT, EnzymeNameQualifierType.MISPRINT);
        NAME_QUALIFIERS.put(EnzymeNameQualifierConstant.OBSOLETE, EnzymeNameQualifierType.OBSOLETE);
    }
    
    private void buildViewMap(){
        VIEWS = new HashMap<EnzymeViewConstant, ViewType>();
        VIEWS.put(EnzymeViewConstant.INTENZ, ViewType.INTENZ_IUBMB_SIB);
        VIEWS.put(EnzymeViewConstant.IUBMB, ViewType.IUBMB);
        VIEWS.put(EnzymeViewConstant.SIB, ViewType.SIB);
        VIEWS.put(EnzymeViewConstant.IUBMB_INTENZ, ViewType.INTENZ_IUBMB);
        VIEWS.put(EnzymeViewConstant.SIB_INTENZ, ViewType.INTENZ_SIB);
        VIEWS.put(EnzymeViewConstant.IUBMB_SIB, ViewType.IUBMB_SIB);
    }
    
    private void buildDbMap(){
        DATABASES = new HashMap<XrefDatabaseConstant, DatabaseType>();
        DATABASES.put(XrefDatabaseConstant.BRENDA,DatabaseType.BRENDA);
        DATABASES.put(XrefDatabaseConstant.CAS,DatabaseType.CAS);
        DATABASES.put(XrefDatabaseConstant.DIAGRAM,DatabaseType.DIAGRAM);
        DATABASES.put(XrefDatabaseConstant.ERGO,DatabaseType.ERGO);
        DATABASES.put(XrefDatabaseConstant.ENZYME,DatabaseType.EX_PA_SY);
        DATABASES.put(XrefDatabaseConstant.GO,DatabaseType.GO);
        DATABASES.put(XrefDatabaseConstant.KEGG,DatabaseType.KEGG_ENZYME);
        DATABASES.put(XrefDatabaseConstant.MEROPS,DatabaseType.MEROPS);
        DATABASES.put(XrefDatabaseConstant.MIM,DatabaseType.MIM);
        DATABASES.put(XrefDatabaseConstant.NIST74,DatabaseType.NIST_74);
        DATABASES.put(XrefDatabaseConstant.PROSITE,DatabaseType.PROSITE);
        DATABASES.put(XrefDatabaseConstant.UMBBD,DatabaseType.UM_BBD);
        DATABASES.put(XrefDatabaseConstant.SWISSPROT,DatabaseType.UNI_PROT);
        DATABASES.put(XrefDatabaseConstant.WIT,DatabaseType.WIT);
		DATABASES = Collections.unmodifiableMap(DATABASES);
    }

    public void setDescriptions(Map<String, Object> descriptions){
		this.DESCRIPTIONS = Collections.unmodifiableMap(descriptions);
	}

	public void setFlavour(Flavour flavour){
		this.flavour = flavour;
	}

    public void export(EnzymeEntry entry, String release,
            String relDate, OutputStream os) throws Exception {
        export(Collections.singletonList(entry), release, relDate, os);
    }
    
    public void export(List<EnzymeEntry> entries, String release,
            String relDate, OutputStream os) throws Exception{
        Intenz intenz = of.createIntenz();
        intenz.setRelease(BigInteger.valueOf(Long.valueOf(release)));
        intenz.setDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(relDate));
        for (EnzymeEntry entry : entries) {
            int ec1 = entry.getEc().getEc1();
            int ec2 = entry.getEc().getEc2();
            int ec3 = entry.getEc().getEc3();
            
            EcClassType clazz = getClazz(intenz, ec1);
            EcSubclassType subClazz = getSubClazz(clazz, ec1, ec2);
            EcSubsubclassType subSubClazz = getSubSubClazz(subClazz, ec1, ec2, ec3);
            
            EntryType jaxbEntry = getJaxbEntry(entry);
            subSubClazz.getEnzyme().add(jaxbEntry);
        }
        //marshaller.marshal(intenz, os);
        marshaller.marshal(intenz, getXMLSerializer(os));
    }
    
    private EcClassType getClazz(Intenz intenz, int ec1) {
        for (EcClassType c : intenz.getEcClass()) {
            if (c.getEc1().intValue() == ec1) return c;
        }
        EcClassType clazz = of.createEcClassType();
        clazz.setEc1(BigInteger.valueOf(ec1));
        // Only if we have (need) descriptions:
        if (DESCRIPTIONS != null){
            EnzymeClass c = (EnzymeClass) DESCRIPTIONS.get(String.valueOf(ec1));
            if (c.getName() != null && c.getName().length() > 0){
                XmlContentType name = of.createXmlContentType();
				name.getContent().add(getFlavoured(c.getName()));
                clazz.setName(name);
            }
            if (c.getDescription() != null && c.getDescription().length() > 0){
                XmlContentType cDescription = of.createXmlContentType();
				cDescription.getContent().add(getFlavoured(c.getDescription().trim()));
                clazz.setDescription(cDescription);
            }
        }
        intenz.getEcClass().add(clazz);
        return clazz;
    }
    
    private EcSubclassType getSubClazz(EcClassType clazz, int ec1, int ec2){
        for (EcSubclassType sc : clazz.getEcSubclass()){
            if (sc.getEc2().intValue() == ec2) return sc;
        }
        EcSubclassType subClazz = of.createEcSubclassType();
        subClazz.setEc2(BigInteger.valueOf(ec2));
        // Only if we have (need) descriptions:
        if (DESCRIPTIONS != null){
            EnzymeSubclass sc = (EnzymeSubclass) DESCRIPTIONS.get(ec1+"."+ec2);
            if (sc.getName() != null && sc.getName().length() > 0){
                XmlContentType name = of.createXmlContentType();
                name.getContent().add(getFlavoured(sc.getName()));
                subClazz.setName(name);
            }
            if (sc.getDescription() != null && sc.getDescription().length() > 0){
                XmlContentType scDescription = of.createXmlContentType();
                scDescription.getContent().add(getFlavoured(sc.getDescription().trim()));
                subClazz.setDescription(scDescription);
            }
        }
        clazz.getEcSubclass().add(subClazz);
        return subClazz;
    }
    
    private EcSubsubclassType getSubSubClazz(EcSubclassType subClazz, int ec1, int ec2, int ec3){
        for (EcSubsubclassType ssc : subClazz.getEcSubSubclass()){
            if (ssc.getEc3().intValue() == ec3) return ssc;
        }
        EcSubsubclassType subSubClazz = of.createEcSubsubclassType();
        subSubClazz.setEc3(BigInteger.valueOf(ec3));
        // Only if we have (need) descriptions:
        if (DESCRIPTIONS != null){
            EnzymeSubSubclass ssc = (EnzymeSubSubclass) DESCRIPTIONS.get(ec1+"."+ec2+"."+ec3);
            if (ssc.getName() != null && ssc.getName().length() > 0){
                XmlContentType name = of.createXmlContentType();
                name.getContent().add(getFlavoured(ssc.getName()));
                subSubClazz.setName(name);
            }
            if (ssc.getDescription() != null && ssc.getDescription().length() > 0){
                XmlContentType sscDescription = of.createXmlContentType();
                sscDescription.getContent().add(getFlavoured(ssc.getDescription().trim()));
                subSubClazz.setDescription(sscDescription);
            }
        }
        subClazz.getEcSubSubclass().add(subSubClazz);
        return subSubClazz;
    }
	
    /**
	 * Processes a <code>String</code> to adapt it to the particular flavour
	 * of this exporter. This method should be used when populating the
	 * elements listed in {@link #XML_CONTENT_ELEMENTS}.
	 *
	 * @param s a <code>String</code> value
	 * @return a <code>String</code> value
	 */
	private String getFlavoured(String s){
		String flavoured = null;
		switch (flavour){
		case ASCII:
			flavoured = XCharsASCIITranslator.getInstance().toASCII(s, true, true);
			break;
		case XCHARS:
            Pattern p = Pattern.compile(" <?[?=]>? ");
            Matcher m = p.matcher(s);
            flavoured = s;
            while (m.find()){
            	String dirSign = m.group(0);
            	String escaped = dirSign.replaceAll("<","&lt;").replaceAll(">","&gt;");
            	flavoured = flavoured.replace(dirSign, escaped);
            }
			// Add xchars namespace prefix:
			flavoured = flavoured.replaceAll("(</?)(?! |-|/)","$1x:");
			break;
		}
		return flavoured;
	}

    private XMLSerializer getXMLSerializer(OutputStream os) {
        OutputFormat outFormat = new OutputFormat();
		switch (flavour){
		case ASCII:
			break;
		case XCHARS:
			// If we want CDATA:
//			outFormat.setCDataElements(XML_CONTENT_ELEMENTS);
			// If we don't want CDATA:
			outFormat.setNonEscapingElements(XML_CONTENT_ELEMENTS);
			break;
		}
// 		outFormat.setPreserveSpace(true);
        outFormat.setIndenting(true);
//      outFormat.setIndent(4);
		outFormat.setOmitDocumentType(false);
        
        XMLSerializer serializer = new XMLSerializer(outFormat);
        serializer.setOutputByteStream(os);
        serializer.setNamespaces(true);
        
        return serializer;
    }

    private JAXBElement<EnzymeType> getJaxbEnzyme(EnzymeEntry entry, Map<?, ?> descriptions)
    throws DatatypeConfigurationException {
        JAXBElement<EnzymeType> jaxbEnzyme = of.createEnzyme(of.createEnzymeType());
        // EC number:
        jaxbEnzyme.getValue().setEc("EC ".concat(entry.getEc().toString()));
        
        if (entry.getHistory().isDeletedRootNode()){
            jaxbEnzyme.getValue().setDeleted(of.createInactiveStatusType());
            String note = getInactiveEntryNote(entry);
            if (note.length() > 1)
                jaxbEnzyme.getValue().getDeleted().setNote(getFlavoured(note));
        } else if (entry.getHistory().isTransferredRootNode()){
            jaxbEnzyme.getValue().setTransferred(of.createInactiveStatusType());
            String note = getInactiveEntryNote(entry);
            if (note.length() > 1)
                jaxbEnzyme.getValue().getTransferred().setNote(getFlavoured(note));
        } else {
            setNames(entry, jaxbEnzyme.getValue(), of);
            setReactions(entry, jaxbEnzyme.getValue(), of);
            setCofactors(entry, jaxbEnzyme.getValue(), of);
            setComments(entry, jaxbEnzyme.getValue(), of);
            setLinks(entry, jaxbEnzyme.getValue(), of);
            setReferences(entry, jaxbEnzyme.getValue(), of);
        }
        
        // History:
        jaxbEnzyme.getValue().setHistory(entry.getHistory().getRootNode().getHistoryLine());
        
        return jaxbEnzyme;
    }
    
    private EntryType getJaxbEntry(EnzymeEntry entry)
    throws DatatypeConfigurationException {
        EntryType jaxbEntry = of.createEntryType();
        // EC number:
        jaxbEntry.setEc("EC ".concat(entry.getEc().toString()));
        jaxbEntry.setEc4(BigInteger.valueOf(entry.getEc().getEc4()));
        jaxbEntry.setPreliminary(EnzymeCommissionNumber.isPreliminary(
        		entry.getEc().toString()));
        
        if (entry.getHistory().isDeletedRootNode()){
            jaxbEntry.setDeleted(of.createInactiveStatusType());
            String note = getInactiveEntryNote(entry);
            if (note.length() > 1)
                jaxbEntry.getDeleted().setNote(getFlavoured(note));
        } else if (entry.getHistory().isTransferredRootNode()){
            jaxbEntry.setTransferred(of.createInactiveStatusType());
            String note = getInactiveEntryNote(entry);
            if (note.length() > 1)
                jaxbEntry.getTransferred().setNote(getFlavoured(note));
        } else {
            setNames(entry, jaxbEntry, of);
            setReactions(entry, jaxbEntry, of);
            setCofactors(entry, jaxbEntry, of);
            setComments(entry, jaxbEntry, of);
            setLinks(entry, jaxbEntry, of);
            setReferences(entry, jaxbEntry, of);
        }
        
        // History:
        jaxbEntry.setHistory(entry.getHistory().getRootNode().getHistoryLine());
        
        return jaxbEntry;
    }

    /**
     * @param entry
     * @param jaxbEnzyme
     * @param of
     * @throws DatatypeConfigurationException
     */
    private void setReferences(EnzymeEntry entry, EnzymeType jaxbEnzyme, ObjectFactory of) throws DatatypeConfigurationException {
        // References:
        for (Object o : entry.getReferences()) {
            if (jaxbEnzyme.getReferences() == null)
                jaxbEnzyme.setReferences(of.createReferences());
            Reference ref = (Reference) o;
            ReferenceType jaxbRef = null;
            if (ref instanceof Book){
                Book book = (Book) ref;
                BookType jaxbBook = of.createBookType();
                Editorial editorial = of.createEditorial();
                editorial.setContent(book.getPublisher());
                editorial.setPlace(book.getPublisherPlace());
                jaxbBook.setEditorial(editorial);
                jaxbBook.setEdition(book.getEdition(true));
                jaxbBook.setEditor(getFlavoured(book.getEditor(true)));
                jaxbBook.setName(getFlavoured(book.getPubName()));
                jaxbBook.setVolume(book.getVolume());
                jaxbBook.setFirstPage(book.getFirstPage());
                jaxbBook.setLastPage(book.getLastPage());
                jaxbRef = jaxbBook;
            } else if (ref instanceof Journal){
                Journal journal = (Journal) ref;
                JournalType jaxbJournal = of.createJournalType();
                jaxbJournal.setName(getFlavoured(journal.getPubName()));
                jaxbJournal.setVolume(journal.getVolume());
                jaxbJournal.setFirstPage(journal.getFirstPage());
                jaxbJournal.setLastPage(journal.getLastPage());
                if (journal.getMedlineId() != null && journal.getMedlineId().length() > 0)
                    jaxbJournal.setMedline(journal.getMedlineId());
                if (journal.getPubMedId() != null && journal.getPubMedId().length() > 0)
                    jaxbJournal.setPubmed(journal.getPubMedId());
                jaxbRef = jaxbJournal;
            } else if (ref instanceof Patent){
                Patent patent = (Patent) ref;
                PatentType jaxbPatent = of.createPatentType();
                jaxbPatent.setNumber(patent.getPatentNumber());
                jaxbRef = jaxbPatent;
            }
            jaxbRef.setAuthors(getFlavoured(ref.getAuthors()));
            jaxbRef.setTitle(getFlavoured(ref.getTitle()));
            jaxbRef.setYear(DatatypeFactory.newInstance().newXMLGregorianCalendar(ref.getYear()));
            jaxbRef.setView(VIEWS.get(ref.getView()));
            jaxbEnzyme.getReferences().getBookOrJournalOrPatent().add(jaxbRef);
        }
    }

    /**
     * @param entry
     * @param jaxbEnzyme
     * @param of
     */
    private void setLinks(EnzymeEntry entry, EnzymeType jaxbEnzyme, ObjectFactory of) {
        // Links:
        for (Object o : entry.getLinks()) {
            EnzymeLink link = (EnzymeLink) o;
            if (EnzymeLink.isStaticLink(link.getXrefDatabaseConstant()))
                continue;
            if (jaxbEnzyme.getLinks() == null)
                jaxbEnzyme.setLinks(of.createLinks());
            LinkType jaxbLink = of.createLinkType();
            jaxbLink.setDb(DATABASES.get(link.getXrefDatabaseConstant()));
            if (link.getXrefDatabaseConstant().equals(XrefDatabaseConstant.DIAGRAM)){
            	jaxbLink.setHref(link.getSpecificUrl());
            } else if (link.getXrefDatabaseConstant().equals(XrefDatabaseConstant.MEROPS)){
                String meropsId = link.getSpecificUrl().substring(link.getSpecificUrl().indexOf("id=")+3);
                jaxbLink.setAccessionNumber(meropsId);
            	jaxbLink.setHref(link.getSpecificUrl());
            } else {
                jaxbLink.setAccessionNumber(link.getAccession());
                if (link.getXrefDatabaseConstant().getUrl().length() > 0){
                    jaxbLink.setHref(link.getXrefDatabaseConstant().getUrl()+link.getAccession());
                }
            }
            if (link.getDataComment() != null)
                jaxbLink.setComment(link.getDataComment());
            jaxbLink.getContent().add(link.getName());
            jaxbLink.setView(VIEWS.get(link.getView()));
            jaxbEnzyme.getLinks().getLink().add(jaxbLink);
        }
        // Cross-references to Rhea:
        EnzymaticReactions er = entry.getEnzymaticReactions();
        if (er != null && er.size() > 0){
            for (int i = 0; i < er.size(); i++) {
                Reaction reaction = er.getReaction(i);
                if (reaction.getId().equals(Reaction.NO_ID_ASSIGNED)
                		|| !reaction.getStatus().isPublic()){
                	continue;
                }
                if (jaxbEnzyme.getLinks() == null)
                    jaxbEnzyme.setLinks(of.createLinks());
                LinkType rheaLink = of.createLinkType();
                rheaLink.setDb(DatabaseType.RHEA);
                rheaLink.setAccessionNumber(reaction.getId().toString());
                rheaLink.setView(ViewType.INTENZ);
                rheaLink.setHref(Database.RHEA.getEntryUrl(reaction.getId().toString()));
                rheaLink.getContent().add(getFlavoured(reaction.getTextualRepresentation()));
                jaxbEnzyme.getLinks().getLink().add(rheaLink);
            }
        }
    }

    /**
     * @param entry
     * @param jaxbEnzyme
     * @param of
     */
    private void setComments(EnzymeEntry entry, EnzymeType jaxbEnzyme, ObjectFactory of) {
        // Comments:
        for (Object o : entry.getComments()) {
            if (jaxbEnzyme.getComments() == null)
                jaxbEnzyme.setComments(of.createComments());
            EnzymeComment comment = (EnzymeComment) o;
            ViewableType jaxbComment = of.createViewableType();
            jaxbComment.getContent().add(getFlavoured(comment.getCommentText()));
            jaxbComment.setView(VIEWS.get(comment.getView()));
            jaxbEnzyme.getComments().getComment().add(jaxbComment);
        }
    }

    /**
     * @param entry
     * @param jaxbEnzyme
     * @param of
     */
    private void setCofactors(EnzymeEntry entry, EnzymeType jaxbEnzyme, ObjectFactory of) {
        // Cofactors:
    	
        for (Iterator<?> it = entry.getCofactors().iterator(); it.hasNext();){
        	Object cofactor = it.next();
            if (jaxbEnzyme.getCofactors() == null)
                jaxbEnzyme.setCofactors(of.createCofactors());
			if (cofactor instanceof Cofactor){
				JAXBElement<CofactorType> jaxbCofactor =
						cofactor2jaxb(of, (Cofactor) cofactor);
	            jaxbEnzyme.getCofactors().getContent().add(jaxbCofactor);
			} else {
				OperatorSet os = (OperatorSet) cofactor;
				jaxbEnzyme.getCofactors().getContent()
						.addAll(cofactors2jaxb(of, os));
			}
			if (it.hasNext()){
				jaxbEnzyme.getCofactors().getContent().add(" and ");
			}
        }
    }

	/**
	 * Creates a JAXB cofactor from an IntEnz cofactor.
	 * @param of an object factory to create JAXB objects.
	 * @param cofactor an IntEnz cofactor.
	 * @return a JAXB cofactor.
	 */
	private JAXBElement<CofactorType> cofactor2jaxb(ObjectFactory of, Cofactor cofactor) {
		CofactorType cofactorType = of.createCofactorType();
		cofactorType.getContent().add(getFlavoured(
				((Cofactor) cofactor).getCompound().getName()));
		cofactorType.setView(VIEWS.get(
				((Cofactor) cofactor).getView()));
		cofactorType.setDb(((Cofactor) cofactor).getCompound()
				.getXref().getDatabaseName());
		cofactorType.setAccession(((Cofactor) cofactor).getCompound()
				.getAccession());
		return of.createCofactor(cofactorType);
	}

    private Collection<Serializable> cofactors2jaxb(ObjectFactory of, OperatorSet os) {
    	Collection<Serializable> jaxbCofactors =
    			new ArrayList<Serializable>();
    	jaxbCofactors.add("(");
		for (Iterator<?> it = os.iterator(); it.hasNext();){
			Object o = it.next();
			if (o instanceof Cofactor){
				Cofactor cofactor = (Cofactor) o;
				jaxbCofactors.add(cofactor2jaxb(of, cofactor));
			} else {
				jaxbCofactors.addAll(cofactors2jaxb(of, (OperatorSet) o));
			}
			if (it.hasNext()) jaxbCofactors.add(" "
					+ os.getOperator().toLowerCase().replaceAll("\\d", "")
					+ " ");
		}
    	jaxbCofactors.add(")");
		return jaxbCofactors;
	}

	/**
     * @param entry
     * @param jaxbEnzyme
     * @param of
     */
    private void setReactions(EnzymeEntry entry, EnzymeType jaxbEnzyme, ObjectFactory of) {
        // Reactions:
        EnzymaticReactions er = entry.getEnzymaticReactions();
        for (int i = 0; i < er.size(); i++) {
            Reaction reaction = er.getReaction(i);
			// Rhea-ctions won't appear in IntEnzXML except for preliminary ECs:
			if (reaction.getId() > Reaction.NO_ID_ASSIGNED
					/*&& !reaction.getStatus().equals(Status.OK)*/
					&& !EnzymeCommissionNumber.isPreliminary(entry.getEc().toString()))
				continue;
            if (jaxbEnzyme.getReactions() == null)
                jaxbEnzyme.setReactions(of.createReactions());

            ReactionType jaxbReaction = of.createReactionType();
            jaxbReaction.getContent().add(getFlavoured(reaction.getTextualRepresentation()));
            jaxbReaction.setView(VIEWS.get(er.getReactionView(i)));
            jaxbReaction.setIubmb(reaction.isIubmb());
            jaxbEnzyme.getReactions().getReaction().add(jaxbReaction);
        }
    }

    /**
     * @param entry
     * @param jaxbEnzyme
     * @param of
     */
    private void setNames(EnzymeEntry entry, EnzymeType jaxbEnzyme, ObjectFactory of) {
        // Names:
        for (Object o : entry.getCommonNames()) {
            EnzymeName commonName = (EnzymeName) o;
            EnzymeNameType jaxbComName = getJaxbEnzymeName(of, commonName);
            jaxbEnzyme.getAcceptedName().add(jaxbComName);
        }
        EnzymeName sysName = entry.getSystematicName();
        if (sysName != null && sysName.getName().length() > 0){
            EnzymeNameType jaxbSysName = getJaxbEnzymeName(of, sysName);
            jaxbEnzyme.setSystematicName(jaxbSysName);
        }
        for (Object o : entry.getSynonyms()){
            if (jaxbEnzyme.getSynonyms() == null)
                jaxbEnzyme.setSynonyms(of.createSynonyms());
            EnzymeName synonym = (EnzymeName) o;
            EnzymeNameType jaxbSynonym = getJaxbEnzymeName(of, synonym);
            jaxbEnzyme.getSynonyms().getSynonym().add(jaxbSynonym);
        }
    }

    /**
     * @param entry
     * @return
     */
    private String getInactiveEntryNote(EnzymeEntry entry) {
        StringBuffer note = new StringBuffer();
        String entryName = entry.getCommonName(EnzymeViewConstant.INTENZ).getName();
        String historyNote = entry.getHistory().getLatestHistoryEventOfRoot().getNote();
        note.append(entryName);
        if (entryName.length() > 0 && historyNote.length() > 0) note.append(". ");
        note.append(historyNote);
        return note.toString();
    }

    /**
     * @param of
     * @param name
     * @return
     */
    private EnzymeNameType getJaxbEnzymeName(ObjectFactory of, EnzymeName name) {
        EnzymeNameType jaxbName = of.createEnzymeNameType();
        jaxbName.getContent().add(getFlavoured(name.getName()));
        jaxbName.setQualifier(NAME_QUALIFIERS.get(name.getQualifier()));
        jaxbName.setView(VIEWS.get(name.getView()));
        return jaxbName;
    }

}
