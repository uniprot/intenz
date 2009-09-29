package uk.ac.ebi.intenz.domain.enzyme;

/**
 * Interface for data objects to which a comment is applied.
 */
public interface Commented {

    public String getDataComment();

    public void setDataComment(String comment);

}
