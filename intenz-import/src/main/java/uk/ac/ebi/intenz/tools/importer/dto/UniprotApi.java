package uk.ac.ebi.intenz.tools.importer.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
//@JsonInclude(JsonInclude.Include.NON_NULL)
//@JsonFormat(shape=JsonFormat.Shape.ANY)
//@JsonPropertyOrder({
//    "accession",
//    "id",
//    "proteinExistence",
//    "info",
//    "organism",
//    "secondaryAccession",
//    "protein",
//    "gene",
//    "comments",
//    "features",
//    "dbReferences",
//    "keywords",
//    "references",
//    "sequence"
//})
/**
 *
 * @author joseph
 */
//@Data
public class UniprotApi {

    @JsonProperty("accession")
    private String accession;
    @JsonProperty("id")
    private String id;
//    @JsonProperty("proteinExistence")
//    private String proteinExistence;
//    @JsonProperty("info")
//    private Info info;
//    @JsonProperty("organism")
//    private Organism organism;
//    @JsonProperty("secondaryAccession")
//    private List<String> secondaryAccession = null;
//    @JsonProperty("protein")
//    private Protein protein;
//    @JsonProperty("gene")
//    private List<Gene> gene = null;
    //@JsonProperty("comments")
    //private List<Comment> comments;
//    @JsonProperty("features")
//    private List<Feature> features = null;
//    @JsonProperty("dbReferences")
//    private List<DbReference_> dbReferences = null;
//    @JsonProperty("keywords")
//    private List<Keyword> keywords = null;
//    @JsonProperty("references")
//    private List<Reference> references = null;
//    @JsonProperty("sequence")
//    private Sequence sequence;
//    @JsonIgnore
//    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("accession")
    public String getAccession() {
        return accession;
    }

    @JsonProperty("accession")
    public void setAccession(String accession) {
        this.accession = accession;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }
//
//    @JsonProperty("proteinExistence")
//    public String getProteinExistence() {
//        return proteinExistence;
//    }
//
//    @JsonProperty("proteinExistence")
//    public void setProteinExistence(String proteinExistence) {
//        this.proteinExistence = proteinExistence;
//    }
//
//    @JsonProperty("info")
//    public Info getInfo() {
//        return info;
//    }
//
//    @JsonProperty("info")
//    public void setInfo(Info info) {
//        this.info = info;
//    }
//
//    @JsonProperty("organism")
//    public Organism getOrganism() {
//        return organism;
//    }
//
//    @JsonProperty("organism")
//    public void setOrganism(Organism organism) {
//        this.organism = organism;
//    }
//
//    @JsonProperty("secondaryAccession")
//    public List<String> getSecondaryAccession() {
//        return secondaryAccession;
//    }
//
//    @JsonProperty("secondaryAccession")
//    public void setSecondaryAccession(List<String> secondaryAccession) {
//        this.secondaryAccession = secondaryAccession;
//    }
//
//    @JsonProperty("protein")
//    public Protein getProtein() {
//        return protein;
//    }
//
//    @JsonProperty("protein")
//    public void setProtein(Protein protein) {
//        this.protein = protein;
//    }
//
//    @JsonProperty("gene")
//    public List<Gene> getGene() {
//        return gene;
//    }
//
//    @JsonProperty("gene")
//    public void setGene(List<Gene> gene) {
//        this.gene = gene;
//    }
//    @JsonProperty("comments")
//    public List<Comment> getComments() {
//        if (comments == null) {
//            comments = new ArrayList<>();
//        }
//        return comments;
//    }
//
//    @JsonProperty("comments")
//    public void setComments(List<Comment> comments) {
//        this.comments = comments;
//    }

//    @JsonProperty("features")
//    public List<Feature> getFeatures() {
//        return features;
//    }
//
//    @JsonProperty("features")
//    public void setFeatures(List<Feature> features) {
//        this.features = features;
//    }
//
//    @JsonProperty("dbReferences")
//    public List<DbReference_> getDbReferences() {
//        return dbReferences;
//    }
//
//    @JsonProperty("dbReferences")
//    public void setDbReferences(List<DbReference_> dbReferences) {
//        this.dbReferences = dbReferences;
//    }
//
//    @JsonProperty("keywords")
//    public List<Keyword> getKeywords() {
//        return keywords;
//    }
//
//    @JsonProperty("keywords")
//    public void setKeywords(List<Keyword> keywords) {
//        this.keywords = keywords;
//    }
//
//    @JsonProperty("references")
//    public List<Reference> getReferences() {
//        return references;
//    }
//
//    @JsonProperty("references")
//    public void setReferences(List<Reference> references) {
//        this.references = references;
//    }
//
//    @JsonProperty("sequence")
//    public Sequence getSequence() {
//        return sequence;
//    }
//
//    @JsonProperty("sequence")
//    public void setSequence(Sequence sequence) {
//        this.sequence = sequence;
//    }
//    @JsonAnyGetter
//    public Map<String, Object> getAdditionalProperties() {
//        return this.additionalProperties;
//    }
//
//    @JsonAnySetter
//    public void setAdditionalProperty(String name, Object value) {
//        this.additionalProperties.put(name, value);
//    }
}
