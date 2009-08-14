package uk.ac.ebi.intenz.domain.enzyme;


/**
 * Created by IntelliJ IDEA.
 * User: rafalcan
 * Date: 29-Sep-2005
 * Time: 15:19:01
 * To change this template use File | Settings | File Templates.
 */
public class DataComment {

	/** @deprecated */
    private Long id;

    private String comment = "";

	/** @deprecated */
    public DataComment(Long id, String comment){
        this.id = id;
        this.comment = comment;
    }
    
    public DataComment(String comment){
        this.comment = comment;
    }

    /** Convenience constructor.
     * @param id
     * @param comment
     * @deprecated
     */
    private DataComment(String id, String comment){
        try {
            this.id = new Long(id);
        } catch (NumberFormatException e){}
        this.comment = comment;
    }

	/** @deprecated */
    public static DataComment valueOf(String id, String comment){
        boolean noId = (id == null || id.trim().equals(""));
        boolean noComment = (comment == null || comment.trim().equals(""));
        if ( noId && noComment) return null;
        return new DataComment(id, comment);
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DataComment)) return false;

        final DataComment dataComment = (DataComment) o;

        if (comment != null ? !comment.equals(dataComment.comment) : dataComment.comment != null) return false;
        if (id != null ? !id.equals(dataComment.id) : dataComment.id != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (comment != null ? comment.hashCode() : 0);
        result = 29 * result + (id != null ? id.hashCode() : 0);
        return result;
    }

    public String getComment(){
        return comment;
    }

	/** @deprecated */
    public Long getId(){
        return id;
    }

	/** @deprecated */
    public void setId(long l){
        this.id = new Long(l);
    }

}
