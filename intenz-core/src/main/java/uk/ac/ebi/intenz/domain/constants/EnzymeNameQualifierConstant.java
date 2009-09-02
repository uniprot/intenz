package uk.ac.ebi.intenz.domain.constants;

/**
 * This class represents an enumeration of all supported name qualifiers.
 * <p/>
 * Instances of this class are immutable.
 *
 * @author Michael Darsow
 * @version $Revision: 1.3 $ $Date: 2009/04/16 15:01:04 $
 */
public class EnzymeNameQualifierConstant {

  public static final EnzymeNameQualifierConstant UNDEF = new EnzymeNameQualifierConstant("NON");
  public static final EnzymeNameQualifierConstant MISLEADING = new EnzymeNameQualifierConstant("MIS");
  public static final EnzymeNameQualifierConstant AMBIGUOUS = new EnzymeNameQualifierConstant("AMB");
  public static final EnzymeNameQualifierConstant OBSOLETE = new EnzymeNameQualifierConstant("OBS");
  public static final EnzymeNameQualifierConstant MISPRINT = new EnzymeNameQualifierConstant("MPR");
  public static final EnzymeNameQualifierConstant INCORRECT = new EnzymeNameQualifierConstant("INC");

  private String qualifier;

  /**
   * Returns an <code>EnzymeNameQualifierConstant</code> instance of the given qualifier string.
   *
   * @param qualifier The qualifier string defining this instance.
   * @return The <code>EnzymeNameQualifierConstant</code> instance.
   * @throws NullPointerException     if <code>qualifier</code> is <code>null</code>.
   * @throws IllegalArgumentException if <code>qualifier</code> is unknown.
   */
  public static EnzymeNameQualifierConstant valueOf(String qualifier) {
    if (qualifier == null) throw new NullPointerException("Parameter 'qualifier' must not be null.");

    if (qualifier.equals(UNDEF.toString()) || qualifier.equals("")) return UNDEF;
    if (qualifier.equals(MISLEADING.toString())) return MISLEADING;
    if (qualifier.equals(AMBIGUOUS.toString())) return AMBIGUOUS;
    if (qualifier.equals(OBSOLETE.toString())) return OBSOLETE;
    if (qualifier.equals(MISPRINT.toString())) return MISPRINT;
    if (qualifier.equals(INCORRECT.toString())) return INCORRECT;
	throw new IllegalArgumentException();
  }

  /**
   * Object cannot be created outside this class.
   *
   * @param qualifier The name qualifier.
   */
  private EnzymeNameQualifierConstant(String qualifier) {
    this.qualifier = qualifier;
  }

  /**
   * Standard equals method.
   *
   * @param o Object to be compared to this one.
   * @return <code>true</code> if the objects are equal.
   */
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof EnzymeNameQualifierConstant)) return false;

    final EnzymeNameQualifierConstant enzymeNameQualifierConstant = (EnzymeNameQualifierConstant) o;

    if (qualifier != null ? !qualifier.equals(enzymeNameQualifierConstant.qualifier) : enzymeNameQualifierConstant.qualifier != null) return false;

    return true;
  }

  /**
   * Returns the hash code of this object.
   *
   * @return the hash code of this object.
   */
  public int hashCode() {
    return (qualifier != null ? qualifier.hashCode() : 0);
  }

  /**
   * Returns the name qualifier.
   *
   * @return the name qualifier.
   */
  public String toString() {
    return qualifier;
  }

   /**
   * TODO: put somewhere else.
   * Returns the human readable representation of the qualifier in ASCII flat file form.
   *
   * @param name The name is used to construct the URL for the ambiguous name search.
   * @return the human readable representation.
   * @deprecated
   */
  public String toASCII(EnzymeNameQualifierConstant name) {
    if(this == MISLEADING) return "[misleading]";
    if(this == OBSOLETE) return "[obsolete]";
    if(this == AMBIGUOUS) return "[ambiguous]";
    if(this == MISPRINT) return "[misprint]";
    if(this == INCORRECT) return "[incorrect]";
    return "";
  }

  /**
   * TODO: put somewhere else.
   * Returns the human readable representation of the qualifier in HTML.
   *
   * @param name The name is used to construct the URL for the ambiguous name search.
   * @return the human readable representation.
   * @deprecated MVC sin
   */
  public String toHTML(String name) {
    if(this == MISLEADING) return "[misleading]";
    if(this == OBSOLETE) return "[obsolete]";
    if(this == AMBIGUOUS) return "[<a href=\"search.do?q="+name+"\">ambiguous</a>]";
    if(this == MISPRINT) return "[misprint]";
    if(this == INCORRECT) return "[incorrect]";
    return "";
  }
  
  public String getLabel(){
	  if(this == MISLEADING) return "misleading";
	  if(this == OBSOLETE) return "obsolete";
	  if(this == AMBIGUOUS) return "ambiguous";
	  if(this == MISPRINT) return "misprint";
	  if(this == INCORRECT) return "incorrect";
	  return null;
  }

  /**
   * TODO: put somewhere else
   * Returns an editable representation of the qualifier in HTML.
   *
   * @return the human readable representation.
   * @deprecated MVC sin
   */
  public String toEditableHTML() {
    StringBuffer html = new StringBuffer();

    html.append("Additional characteristic:\n<select name=\"characteristics\" size=\"1\">\n");
    html.append(getEditableHTML());
    html.append("</select>\n");

    return html.toString();
  }

  /**
   * TODO: put somewhere else
   * Returns a string containing editable HTML.
   *
   * @return a string containing editable HTML.
   * @deprecated MVC sin
   */
  private String getEditableHTML() {
    StringBuffer html = new StringBuffer();

    if(this == UNDEF) {
      html.append("<option selected=\"selected\" value=\"NON\">&lt;NONE&gt;</option>");
      html.append("<option value=\"MIS\">misleading</option>");
      html.append("<option value=\"OBS\">obsolete</option>");
      html.append("<option value=\"AMB\">ambiguous</option>");
      html.append("<option value=\"MPR\">misprint</option>");
      html.append("<option value=\"INC\">incorrect</option>");
    }

    if(this == MISLEADING) {
      html.append("<option selected=\"selected\" value=\"MIS\">misleading</option>");
      html.append("<option value=\"NON\">&lt;NONE&gt;</option>");
      html.append("<option value=\"OBS\">obsolete</option>");
      html.append("<option value=\"AMB\">ambiguous</option>");
      html.append("<option value=\"MPR\">misprint</option>");
      html.append("<option value=\"INC\">incorrect</option>");
    }

    if(this == OBSOLETE) {
      html.append("<option selected=\"selected\" value=\"OBS\">obsolete</option>");
      html.append("<option value=\"NON\">&lt;NONE&gt;</option>");
      html.append("<option value=\"MIS\">misleading</option>");
      html.append("<option value=\"AMB\">ambiguous</option>");
      html.append("<option value=\"MPR\">misprint</option>");
      html.append("<option value=\"INC\">incorrect</option>");
    }

    if(this == AMBIGUOUS) {
      html.append("<option selected=\"selected\" value=\"AMB\">ambiguous</option>");
      html.append("<option value=\"NON\">&lt;NONE&gt;</option>");
      html.append("<option value=\"MIS\">misleading</option>");
      html.append("<option value=\"OBS\">obsolete</option>");
      html.append("<option value=\"MPR\">misprint</option>");
      html.append("<option value=\"INC\">incorrect</option>");
    }

    if(this == MISPRINT) {
      html.append("<option selected=\"selected\" value=\"AMB\">ambiguous</option>");
      html.append("<option value=\"NON\">&lt;NONE&gt;</option>");
      html.append("<option value=\"MIS\">misleading</option>");
      html.append("<option value=\"AMB\">ambiguous</option>");
      html.append("<option value=\"OBS\">obsolete</option>");
      html.append("<option value=\"INC\">incorrect</option>");
    }

    if(this == INCORRECT) {
      html.append("<option selected=\"selected\" value=\"AMB\">ambiguous</option>");
      html.append("<option value=\"NON\">&lt;NONE&gt;</option>");
      html.append("<option value=\"MIS\">misleading</option>");
      html.append("<option value=\"AMB\">ambiguous</option>");
      html.append("<option value=\"OBS\">obsolete</option>");
      html.append("<option value=\"MPR\">misprint</option>");
    }

    return html.toString();
  }
}


