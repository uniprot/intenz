package uk.ac.ebi.intenz.domain.enzyme;

import uk.ac.ebi.intenz.domain.constants.EnzymeSourceConstant;
import uk.ac.ebi.intenz.domain.constants.EnzymeViewConstant;

/**
 * This class represents a comment of an enzyme entry.
 * <p/>
 * Instances of this class are immutable.
 *
 * @author Michael Darsow
 * @version $Revision: 1.2 $ $Date: 2008/01/28 12:33:00 $
 */
public class EnzymeComment implements Viewable {

  private String commentText;

  private EnzymeSourceConstant source;

  private EnzymeViewConstant view;

  /**
   * Returns a <code>EnzymeComment</code> instance.
   *
   * @param commentText The actual comment.
   * @param source      The comment's source.
   * @param view        The view(s) where this comment will be displayed.
   * @throws NullPointerException     if <code>commentText</code> or <code>source</code> are <code>null</code>.
   * @throws IllegalArgumentException if <code>commentText</code> is empty.
   */
  public EnzymeComment(String commentText, EnzymeSourceConstant source, EnzymeViewConstant view) {
    if (commentText == null) throw new NullPointerException("Parameter 'commentText' must not be null.");
    if (source == null) throw new NullPointerException("Parameter 'source' must not be null.");
    if (view == null) throw new NullPointerException("Parameter 'view' must not be null.");
    if (commentText.equals("")) throw new IllegalArgumentException("Parameter 'commentText' must not be empty.");
    this.commentText = commentText;
    this.source = source;
    this.view = view;
  }

  /**
   * Standard equals method.
   *
   * @param o Object to be compared to this one.
   * @return <code>true</code> if the objects are equal.
   */
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof EnzymeComment)) return false;

    final EnzymeComment comment = (EnzymeComment) o;

    if (commentText != null ? !commentText.equals(comment.commentText) : comment.commentText != null) return false;
    if (source != null ? !source.equals(comment.source) : comment.source != null) return false;
    if (view != null ? !view.equals(comment.view) : comment.view != null) return false;

    return true;
  }

  /**
   * Returns the hash code of this object.
   *
   * @return the hash code of this object.
   */
  public int hashCode() {
    int result;
    result = (commentText != null ? commentText.hashCode() : 0);
    result = 29 * result + (source != null ? source.hashCode() : 0);
    result = 29 * result + (view != null ? view.hashCode() : 0);
    return result;
  }


  // ----------------  GETTER ------------------

  public String getCommentText() {
    return commentText;
  }

  public EnzymeSourceConstant getSource() {
    return source;
  }

  public EnzymeViewConstant getView() {
    return view;
  }

}
