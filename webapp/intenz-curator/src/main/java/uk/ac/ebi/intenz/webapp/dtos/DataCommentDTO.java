package uk.ac.ebi.intenz.webapp.dtos;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import java.util.Map;
import java.util.HashMap;

import uk.ac.ebi.intenz.domain.enzyme.DataComment;
import uk.ac.ebi.xchars.SpecialCharacters;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by IntelliJ IDEA.
 * User: rafalcan
 * Date: 30-Sep-2005
 * Time: 10:37:17
 * To change this template use File | Settings | File Templates.
 */
public class DataCommentDTO extends ActionForm {

    private String xmlComment;
    private String displayComment;
    private String id;

    public DataCommentDTO(){
        xmlComment = "";
        displayComment = "";
        id = "";
    }

    public DataCommentDTO(DataCommentDTO dataCommentDTO){
        this.xmlComment = dataCommentDTO.getXmlComment();
        this.displayComment = dataCommentDTO.getDisplayComment();
        this.id = dataCommentDTO.getId();
    }

    public ActionErrors validate(ActionMapping actionMapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        SpecialCharacters encoding = (SpecialCharacters) request.getSession().getAttribute("characters");
        if (xmlComment.trim().length() > 0){
            displayComment = encoding.xml2Display(xmlComment.trim());
        } else {
            displayComment = "";
        }
        return errors.isEmpty() ? null : errors;
    }

    public String getXmlComment() {
        return xmlComment;
    }

    public void setXmlComment(String xmlComment) {
        this.xmlComment = xmlComment;
    }

    public String getDisplayComment() {
        return displayComment;
    }

    public void setDisplayComment(String displayComment) {
        this.displayComment = displayComment;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
