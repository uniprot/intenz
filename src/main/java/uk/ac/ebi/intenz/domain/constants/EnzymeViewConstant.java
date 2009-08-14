package uk.ac.ebi.intenz.domain.constants;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import uk.ac.ebi.biobabel.util.collections.OperatorSet;
import uk.ac.ebi.intenz.domain.enzyme.Viewable;


/**
 * This class represents an enumeration of all view codes.
 * <p/>
 * Instances of this class are immutable.
 *
 * @author Michael Darsow
 * @version $Revision: 1.4 $ $Date: 2009/04/16 15:01:04 $
 * @deprecated use enumeration {@link View} instead.
 */
public class EnzymeViewConstant {
  private final String viewCode;

  public static final EnzymeViewConstant UNDEF = new EnzymeViewConstant("UNDEF");
  public static final EnzymeViewConstant IUBMB = new EnzymeViewConstant("IUBMB");
  public static final EnzymeViewConstant SIB = new EnzymeViewConstant("SIB");
  public static final EnzymeViewConstant IUBMB_SIB = new EnzymeViewConstant("IUBMB_SIB");
  public static final EnzymeViewConstant IUBMB_INTENZ = new EnzymeViewConstant("IUBMB_INTENZ");
  public static final EnzymeViewConstant SIB_INTENZ = new EnzymeViewConstant("SIB_INTENZ");
  public static final EnzymeViewConstant INTENZ = new EnzymeViewConstant("INTENZ");

  private static final EnzymeViewConstant[] PRIVATE_ARRAY = {IUBMB, SIB, IUBMB_SIB, IUBMB_INTENZ, SIB_INTENZ, INTENZ};
  public static final List<EnzymeViewConstant> VIEW_CONSTANTS =
	  Collections.unmodifiableList(Arrays.asList(PRIVATE_ARRAY));

  /**
   * Object cannot be created outside this class.
   *
   * @param viewCode The line type code.
   */
  private EnzymeViewConstant(String viewCode) {
    this.viewCode = viewCode;
  }

  /**
   * Returns the corresponding instance of the given view code.
   * <p/>
   * If the view code does not match any code an exception is thrown.
   *
   * @param viewCode The view code.
   * @return the class constant corresponding to the given code.
   * @throws NullPointerException     if <code>viewCode</code> is <code>null</code>.
   * @throws IllegalArgumentException if the code is invalid.
   */
  public static EnzymeViewConstant valueOf(String viewCode) {
    if (viewCode == null) throw new NullPointerException("Parameter 'viewCode' must not be null.");
    if (viewCode.equals(IUBMB.toString())) return IUBMB;
    if (viewCode.equals(SIB.toString())) return SIB;
    if (viewCode.equals(IUBMB_SIB.toString())) return IUBMB_SIB;
    if (viewCode.equals(IUBMB_INTENZ.toString())) return IUBMB_INTENZ;
    if (viewCode.equals(SIB_INTENZ.toString())) return SIB_INTENZ;
    if (viewCode.equals(INTENZ.toString())) return INTENZ;
	throw new IllegalArgumentException("The given view code is not supported.");
  }

  public boolean isInSIBView() {
    return this == SIB || this == IUBMB_SIB || this == SIB_INTENZ || this == INTENZ;
  }

  public boolean isInIUBMBView() {
    return this == IUBMB || this == IUBMB_SIB || this == IUBMB_INTENZ || this == INTENZ;
  }

  public boolean isInIntEnzView() {
    return this == INTENZ || this == IUBMB_INTENZ || this == SIB_INTENZ;
  }

  public static boolean isInSIBView(String viewCode) {
    return viewCode.equals(SIB.viewCode) || viewCode.equals(IUBMB_SIB.viewCode) ||
           viewCode.equals(SIB_INTENZ.viewCode) || viewCode.equals(INTENZ.viewCode);
  }

  public static boolean isInIUBMBView(String viewCode) {
    return viewCode.equals(IUBMB.viewCode) || viewCode.equals(IUBMB_SIB.viewCode) ||
           viewCode.equals(IUBMB_INTENZ.viewCode) || viewCode.equals(INTENZ.viewCode);
  }

  public static boolean isInIntEnzView(String viewCode) {
    return viewCode.equals(INTENZ.viewCode) || viewCode.equals(IUBMB_INTENZ.viewCode) ||
           viewCode.equals(SIB_INTENZ.viewCode);
  }
  
  public static boolean isInView(EnzymeViewConstant theView, Object o){
	  EnzymeViewConstant view = null;
	  if (o instanceof Viewable){
		  view = ((Viewable) o).getView();
	  } else if (o instanceof OperatorSet){
		  // Take first one to extract view info, as all of them must be the same:
  		Viewable v = null;
		OperatorSet loopOs = (OperatorSet) o;
		while (v == null){
			Object obj = loopOs.iterator().next();
			if (obj instanceof Viewable) v = (Viewable) obj;
			else loopOs = (OperatorSet) obj;
		}
		view = v.getView();
	  }
	  return view.toString().contains(theView.toString()) || EnzymeViewConstant.INTENZ.equals(view);
  }

  /**
   * Standard equals method.
   *
   * @param o Object to be compared to this one.
   * @return <code>true</code> if the objects are equal.
   */
    @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof EnzymeViewConstant)) return false;

    final EnzymeViewConstant enzymeSource = (EnzymeViewConstant) o;

    if (!viewCode.equals(enzymeSource.viewCode)) return false;

    return true;
  }

  /**
   * Returns the hash code of this object.
   *
   * @return the hash code of this object.
   */
    @Override
  public int hashCode() {
    return viewCode.hashCode();
  }

  /**
   * Returns the database code.
   *
   * @return the database code.
   */
    @Override
  public String toString() {
    return viewCode;
  }

  /**
   * @deprecated
   * @param view
   * @return
   */
  public static String toDisplayString(String view) {
    EnzymeViewConstant viewConstant = EnzymeViewConstant.valueOf(view);
    if(viewConstant == UNDEF) return "undefined";
    if(viewConstant == IUBMB) return "NC-IUBMB";
    if(viewConstant == SIB) return "ENZYME";
    if(viewConstant == INTENZ) return "all views";
    if(viewConstant == IUBMB_SIB) return "NC-IUBMB & ENZYME";
    if(viewConstant == IUBMB_INTENZ) return "NC-IUBMB & IntEnz";
    return "ENZYME & IntEnz";
  }

  /**
   * @deprecated
   * @return
   */
  public String toDisplayString() {
    if(this == UNDEF) return "undefined";
    if(this == IUBMB) return "NC-IUBMB";
    if(this == SIB) return "ENZYME";
    if(this == INTENZ) return "all views";
    if(this == IUBMB_SIB) return "NC-IUBMB & ENZYME";
    if(this == IUBMB_INTENZ) return "NC-IUBMB & IntEnz";
    return "ENZYME & IntEnz";
  }

  /**
   * @deprecated MVC sin
   * @param view
   * @return
   */
  public static String toDisplayImage(String view) {
    EnzymeViewConstant viewConstant = EnzymeViewConstant.valueOf(view);
    if(viewConstant == UNDEF) return "undefined";
    if(viewConstant == IUBMB) return "<img src=\"images/green_bullet.gif\"/>";
    if(viewConstant == SIB) return "<img src=\"images/red_bullet.gif\"/>";
    if(viewConstant == INTENZ) return "<img src=\"images/blue_bullet.gif\"/><img src=\"images/green_bullet.gif\"/><img src=\"images/red_bullet.gif\"/>";
    if(viewConstant == IUBMB_SIB) return "<img src=\"images/green_bullet.gif\"/><img src=\"images/red_bullet.gif\"/>";
    if(viewConstant == IUBMB_INTENZ) return "<img src=\"images/blue_bullet.gif\"/><img src=\"images/green_bullet.gif\"/>";
    return "<img src=\"images/blue_bullet.gif\"/><img src=\"images/red_bullet.gif\"/>";
  }

  /**
   * @deprecated MVC sin
   * @return
   */
  public String toDisplayImage() {
    if(this == UNDEF) return "undefined";
    if(this == IUBMB) return "<img src=\"images/green_bullet.gif\"/>";
    if(this == SIB) return "<img src=\"images/red_bullet.gif\"/>";
    if(this == INTENZ) return "<img src=\"images/blue_bullet.gif\"/><img src=\"images/green_bullet.gif\"/><img src=\"images/red_bullet.gif\"/>";
    if(this == IUBMB_SIB) return "<img src=\"images/green_bullet.gif\"/><img src=\"images/red_bullet.gif\"/>";
    if(this == IUBMB_INTENZ) return "<img src=\"images/blue_bullet.gif\"/><img src=\"images/green_bullet.gif\"/>";
    return "<img src=\"images/blue_bullet.gif\"/><img src=\"images/red_bullet.gif\"/>";
  }
}


