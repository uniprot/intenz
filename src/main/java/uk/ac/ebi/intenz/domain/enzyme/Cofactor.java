package uk.ac.ebi.intenz.domain.enzyme;

import uk.ac.ebi.intenz.domain.constants.EnzymeSourceConstant;
import uk.ac.ebi.intenz.domain.constants.EnzymeViewConstant;
import uk.ac.ebi.rhea.domain.Compound;


/**
 * Cofactors are compounds participating in the reaction and are represented by this class.
 * <p/>
 * At the moment this class stores the cofactor string from ENZYME only. In future this string should
 * be broken down into its components, i.e. compounds and the binary operators (AND/OR) between them.
 * <p/>
 * Instances of this class are immutable.
 *
 * @author Michael Darsow
 * @version $Revision: 1.2 $ $Date: 2008/01/28 12:33:00 $
 */
public class Cofactor implements Viewable {

	public static enum Operators {
 		/** Only used in complex nestings */
 		AND("AND", "AND"),
	    /** For cofactors which can be used indiscriminately by an enzyme. */
		OR_OPTIONAL("OR", "OR1"),
	    /** For cofactors which are used by different families of enzymes within the same EC number. */
 		OR_FAMILIES("OR", "OR2"),
	    /** For cofactors which may be used by an enzyme, but scientists aren't sure which one. */
		OR_UNKNOWN("OR", "OR3");
		private String text;
		private String code;
		private Operators(String t, String c){ this.text = t; this.code = c; }
        @Override
		public String toString(){ return text; }
		public String getCode(){ return code; }
		public static String[] getCodes(){
			String[] result = new String[values().length];
			for (int i = 0; i < result.length; i++) {
				result[i] = values()[i].getCode();
			}
			return result;
		}
	}
    
  /**
   * The string representation of the cofactor(s).
   * @deprecated use compound.getName() instead
   */
  private String cofactorValue;

  private EnzymeSourceConstant source;

  private EnzymeViewConstant view;
  
  private Compound compound;

  /**
   * Returns a <code>Cofactor</code> instance defined by <code>cofactorValue</code>.
   *
   * @param cofactorValue The string representation of the cofactor(s).
   * @throws NullPointerException     if <code>cofactorValue</code> is <code>null</code>.
   * @throws IllegalArgumentException if <code>cofactorValue</code> is empty.
   * @deprecated use the valueOf method instead to call the private constructor
   */
  public Cofactor(String cofactorValue, EnzymeSourceConstant source, EnzymeViewConstant view) {
    if (cofactorValue == null) throw new NullPointerException("Parameter 'cofactorValue' must not be null.");
    if (cofactorValue.equals("")) throw new IllegalArgumentException("Parameter 'cofactorValue' must not be empty.");
    if (source == null) throw new NullPointerException("Parameter 'source' must not be null.");
    if (view == null) throw new NullPointerException("Parameter 'view' must not be null.");
    this.cofactorValue = cofactorValue;
    this.source = source;
    this.view = view;
  }
  
  private Cofactor(Compound compound, EnzymeSourceConstant source,
          EnzymeViewConstant view) {
      this.compound = compound;
      this.source = source;
      this.view = view;
  }
  
  /**
   * Builds a cofactor from the passed parameters.
   * @param compound
   * @param source
   * @param view
   * @return a new cofactor object
   */
    public static Cofactor valueOf(Compound compound, EnzymeSourceConstant source,
            EnzymeViewConstant view) {
        if (compound == null)
            throw new NullPointerException("null compound");
        if (source == null)
            throw new NullPointerException("null source");
        if (view == null)
            throw new NullPointerException("null view");
        return new Cofactor(compound, source, view);
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
    if (!(o instanceof Cofactor)) return false;

    final Cofactor cofactor = (Cofactor) o;

    if (!compound.equals(cofactor.compound))
        return false;
    if (source != null ? !source.equals(cofactor.source) : cofactor.source != null)
        return false;
    if (view != null ? !view.equals(cofactor.view) : cofactor.view != null) 
       return false;

    return true;
  }

  /**
   * Returns the hash code of this object.
   *
   * @return the hash code of this object.
   */
    @Override
  public int hashCode() {
    int result;
    result = (compound != null ? compound.hashCode() : 0);
    result = 29 * result + (source != null ? source.hashCode() : 0);
    result = 29 * result + (view != null ? view.hashCode() : 0);
    return result;
  }

  
  // ----------------  GETTER -----------------------------------

  /**
   * Returns the cofactor string.
   *
   * @return the cofactor string.
   * @deprecated use {@link #getCompound()}.getName() or {@link #toString()} instead
   */
  public String getCofactorValue() {
    return cofactorValue;
  }

  public EnzymeSourceConstant getSource() {
      return source;
  }

  public EnzymeViewConstant getView() {
    return view;
  }

    public Compound getCompound() {
        return compound;
    }

	@Override
	public String toString() {
		return compound.toString();
	}

}
