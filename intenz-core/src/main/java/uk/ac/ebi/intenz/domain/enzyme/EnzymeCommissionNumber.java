package uk.ac.ebi.intenz.domain.enzyme;

import java.util.StringTokenizer;

import uk.ac.ebi.biobabel.validator.DbIdentifierValidator;
import uk.ac.ebi.intenz.domain.DomainObject;
import uk.ac.ebi.intenz.domain.exceptions.EcException;

/**
 * This class represents an Enzyme Commission (EC) number.
 * <p/>
 * The general valid format looks as follows:<p/>
 * <code>\d+(?:\.\d+(?:\.\d+(?:\.n?\d+){0,1}){0,1}){0,1}</code>
 * <p/>
 * <p/>
 * For instance, the first enzyme has the EC <code>1.1.1.1</code>.
 * <p/>
 * The first digit is the number of the class the enzyme belongs to.<br>
 * The second digit is the number of the subclass the enzyme belongs to.<br>
 * The third digit is the number of the sub-subclass the enzyme belongs to.<br>
 * The last digit is the number of the enzyme within the sub-subclass. In case
 * of preliminary EC numbers, it is prefixed with <code>n</code>.
 * <p/>
 * Instances of this class are immutable.
 *
 * @author Michael Darsow
 * @version $Revision: 1.2 $ $Date: 2008/01/28 12:33:00 $
 */
public class EnzymeCommissionNumber extends DomainObject implements Comparable<EnzymeCommissionNumber> {
  /**
   * Constant for an undefined EC which can be used for new enzyme suggestions where the EC number is not known (yet).
   */
  public final static EnzymeCommissionNumber UNDEF =
	  new EnzymeCommissionNumber(-1, -1, -1, -1, false);

  /**
   * Type of EC number.
   * @author rafalcan
   */
  public static enum Type {
	  /** The EC number is undefined. */
	  UNDEF,
	  /** Only the first digit is > 0 which means that this EC is a class EC. */
	  CLASS,
	  /** Only the first two digits are > 0 which means that this EC is a subclass EC. */
	  SUBCLASS,
	  /** Only the first three digits are > 0 which means that this EC is a sub-subclass EC. */
	  SUBSUBCLASS,
	  /** All digits are > 0 which means that this EC is an enzyme EC. */
	  ENZYME,
	  /** Like {@link #ENZYME}, but not provided by NC-IUBMB. */
	  PRELIMINARY
  }

  /**
   * The enzyme's class number.
   */
  private int ec1;

  /**
   * The enzyme's subclass number.
   */
  private int ec2;

  /**
   * The enzyme's sub-subclass number.
   */
  private int ec3;

  /**
   * The enzyme's number.
   */
  private int ec4;
  
  /**
   * This EC number's type.
   */
  private Type type;


  /**
   * Object cannot be created outside this class.
   *
   * @param ec1 The enzyme's class number.
   * @param ec2 The enzyme's subclass number.
   * @param ec3 The enzyme's sub-subclass number.
   * @param ec4 The enzyme's number.
   * @param preliminary is this a preliminary EC number?
   */
  private EnzymeCommissionNumber(int ec1, int ec2, int ec3, int ec4, boolean preliminary) {
    super();
    this.ec1 = ec1;
    this.ec2 = ec2;
    this.ec3 = ec3;
    this.ec4 = ec4;
    this.type = (ec1 == -1)? Type.UNDEF:
    	(ec2 == -1)? Type.CLASS:
    		(ec3 == -1)? Type.SUBCLASS:
    			(ec4 == -1)? Type.SUBSUBCLASS:
    				preliminary? Type.PRELIMINARY : Type.ENZYME;
  }

  /**
   * Returns an <code>EnzymeCommissionNumber</code> instance defined by the given integer.
   * <p/>
   * If this requirement is met the EC number is valid. However, it does not implicitly mean, that
   * this EC number is a valid number of the official Enzyme List. This has to be checked separately, outside this class.
   *
   * @param ec1 The enzyme's class number (must be a pos. integer).
   * @return an <code>EnzymeCommissionNumber</code> instance.
   * @throws EcException if one of the parameters is invalid.
   */
  public static EnzymeCommissionNumber valueOf(int ec1) throws EcException {
    if (ec1 < 1) throw new EcException("The class number is invalid.");
    return new EnzymeCommissionNumber(ec1, -1, -1, -1, false);
  }

  /**
   * Returns an <code>EnzymeCommissionNumber</code> instance defined by the given integers.
   * <p/>
   * All parameters have to be positive integers. A parameter can only be 0 if the succeeding parameter in
   * the formal parameters list is > 0.
   * <p/>
   * Creating a class by giving 1,0 as parameters is alright, but it is invalid to create an EC number as follows:
   * 0,1.<br/>
   * If this requirement is met the EC number is valid. However, it does not implicitly mean, that
   * this EC number is a valid number of the official Enzyme List. This has to be checked separately, outside this class.
   *
   * @param ec1 The enzyme's class number.
   * @param ec2 The enzyme's subclass number.
   * @return an <code>EnzymeCommissionNumber</code> instance.
   * @throws EcException if one of the parameters is invalid.
   */
  public static EnzymeCommissionNumber valueOf(int ec1, int ec2) throws EcException {
    if (ec1 < 1) throw new EcException("The class number is invalid.");
    if (ec2 < 0) throw new EcException("The subclass number is invalid.");

    return new EnzymeCommissionNumber(ec1, ec2, -1, -1, false);
  }

  /**
   * Returns an <code>EnzymeCommissionNumber</code> instance defined by the given integers.
   * <p/>
   * All parameters have to be positive integers. A parameter can only be 0 if none of the succeeding parameters in
   * the formal parameters list is > 0.
   * <p/>
   * Creating a class by giving 1,0,0 as parameters is alright, but it is invalid to create an EC number as follows:
   * 0,1,1.<br/>
   * If this requirement is met the EC number is valid. However, it does not implicitly mean, that
   * this EC number is a valid number of the official Enzyme List. This has to be checked separately, outside this class.
   *
   * @param ec1 The enzyme's class number.
   * @param ec2 The enzyme's subclass number.
   * @param ec3 The enzyme's sub-subclass number.
   * @return an <code>EnzymeCommissionNumber</code> instance.
   * @throws EcException if one of the parameters is invalid.
   */
  public static EnzymeCommissionNumber valueOf(int ec1, int ec2, int ec3) throws EcException {
    if (ec1 < 1) throw new EcException("The class number is invalid.");
    if (ec2 < 0) throw new EcException("The subclass number is invalid.");
    if (ec3 < 0) throw new EcException("The sub-subclass number is invalid.");
    if (ec1 == 0) {
      if (ec2 > 0 || ec3 > 0) throw new EcException("Invalid EC.");
    }
    if (ec2 == 0) {
      if (ec3 > 0) throw new EcException("Invalid EC.");
    }

    return new EnzymeCommissionNumber(ec1, ec2, ec3, -1, false);
  }

  /**
   * Returns an <code>EnzymeCommissionNumber</code> instance defined by the given integers.
   * <p/>
   * A parameter can only be 0 if none of the succeeding parameters in the formal parameters list is > 0.
   * <p/>
   * Creating a class by giving 1,0,0,0 as parameters is alright, but it is invalid to create an EC number as follows:
   * 0,1,1,1.<br/>
   * If this requirement is met the EC number is valid. However, it does not implicitly mean, that
   * this EC number is a valid number of the official Enzyme List. This has to be checked separately, outside this class.
   *
   * @param ec1 The enzyme's class number.
   * @param ec2 The enzyme's subclass number.
   * @param ec3 The enzyme's sub-subclass number.
   * @param ec4 The enzyme's number.
   * @return an <code>EnzymeCommissionNumber</code> instance.
   * @throws EcException if one of the parameters is invalid.
   */
  public static EnzymeCommissionNumber valueOf(int ec1, int ec2, int ec3, int ec4)
  throws EcException {
    return valueOf(ec1, ec2, ec3, ec4, false);
  }
  
  /**
   * Constructor allowing preliminary EC numbers.
   * @param ec1
   * @param ec2
   * @param ec3
   * @param ec4
   * @param preliminary is this a preliminary EC number?
   * @return An EC number instance.
   * @throws EcException if the EC number is not valid.
   */
  public static EnzymeCommissionNumber valueOf(int ec1, int ec2, int ec3, int ec4,
		  boolean preliminary) throws EcException{
	    if (ec1 < 1) throw new EcException("The class number is invalid.");
	    if (ec2 == 0) {
	      if (ec3 > 0 || ec4 > 0) throw new EcException("Invalid EC.");
	    }
	    if (ec2 < 0) {
	      if (ec3 > -1 || ec4 > -1) throw new EcException("Invalid EC.");
	    }
	    if (ec3 == 0) {
	      if (ec4 > 0) throw new EcException("Invalid EC.");
	    }
	    if (ec3 < 0) {
	      if (ec4 > -1) throw new EcException("Invalid EC.");
	    }
	    if (preliminary && (ec1 < 1 || ec2 < 1 || ec3 < 1 || ec4 < 1))
	    	throw new EcException("Invalid preliminary EC");
	    return new EnzymeCommissionNumber(ec1, ec2, ec3, ec4, preliminary);
  }

  /**
   * Returns an <code>EnzymeCommissionNumber</code> instance defined by the given string.
   * <p/>
   * Calls {@link EnzymeCommissionNumber#valueOf(int, int, int, int, boolean)}.
   *
   * @param ecString A string representing an EC number.
   * @throws NullPointerException  if <code>ecString</code> is <code>null</code>.
   * @throws EcException           if one of the parameters is invalid.
   * @throws NumberFormatException if <code>ecString</code> contained invalid characters.
   */
  public static EnzymeCommissionNumber valueOf(String ecString)
  throws EcException, NumberFormatException {
    if (ecString == null) 
    	throw new NullPointerException("Parameter 'ecString' must not be null.");
    int ec1 = -1, ec2 = -1, ec3 = -1, ec4 = -1;
    boolean preliminary = false;
    int iii = 1;
    for (StringTokenizer stringTokenizer = new StringTokenizer(ecString, "."); stringTokenizer.hasMoreTokens();) {
      String ecPart = stringTokenizer.nextToken();
      switch (iii) {
        case 1:
          ec1 = Integer.parseInt(ecPart);
          break;
        case 2:
          ec2 = Integer.parseInt(ecPart);
          break;
        case 3:
          ec3 = Integer.parseInt(ecPart);
          break;
        case 4:
        	while (!Character.isDigit(ecPart.charAt(0))){
        		preliminary = true;
        		ecPart = ecPart.substring(1);
        	}
          ec4 = Integer.parseInt(ecPart);
          break;
      }
      iii++;
    }
    return EnzymeCommissionNumber.valueOf(ec1, ec2, ec3, ec4, preliminary);
  }

  /**
   * Creates a copy of the given <code>EnzymeCommissionNumber</code>.
   *
   * @param ecToCopy The EC to be copied.
   * @return a copy of the given <code>EnzymeCommissionNumber</code>.
   * @throws NullPointerException if <code>ecToCopy</code> is <code>null</code>.
   */
  public static EnzymeCommissionNumber copy(EnzymeCommissionNumber ecToCopy) {
    if (ecToCopy == null)
    	throw new NullPointerException("Parameter 'ecToCopy' must not be null.");
    return new EnzymeCommissionNumber(ecToCopy.ec1, ecToCopy.ec2, ecToCopy.ec3,
    		ecToCopy.ec4, Type.PRELIMINARY.equals(ecToCopy.getType()));
  }

  /**
   * Makes instances of this class comparable.
   *
   * @param ec The object to be compared to this instance.
   * @return a pos. integer if this instance is greater than, a neg. integer if it is less than or 0 if it equals o.
   */
  public int compareTo(EnzymeCommissionNumber ec) {
    int ec1Diff = ec1 - ec.ec1;
    if (ec1Diff != 0) return ec1Diff;

    int ec2Diff = ec2 - ec.ec2;
    if (ec2Diff != 0) return ec2Diff;

    int ec3Diff = ec3 - ec.ec3;
    if (ec3Diff != 0) return ec3Diff;

    int ec4Diff = ec4 - ec.ec4;
    if (ec4Diff != 0) return ec4Diff;

    return type.ordinal() - ec.type.ordinal();
  }

  /**
   * Returns the type of this EC number.
   * @return the type.
   */
  public Type getType() {
	return type;
  }

  /**
   * Checks whether a given EC number is valid.
   *
   * A valid EC number must be of the following format:<br>
   *
   * <code>\d+(?:\.\d+(?:\.\d+(?:\.\d+){0,1}){0,1}){0,1}</code>
   *
   * Examples:<br>
   *
   * <ul>
   *   <li>1</li>
   *   <li>2.3</li>
   *   <li>4.2.14</li>
   *   <li>1.2.2.126</li>
   * </ul>
   *
   * @param ecString The EC string to be checked.
   * @return <code>true</code>, if the EC is valid.
   */
  public static boolean isValid(String ecString) {
	  return DbIdentifierValidator.getInstance()
	  	.validate(ecString, DbIdentifierValidator.EC_NUMBER);
  }

  /**
   * Returns a string representation of this EC number.
   *
   * @return the EC number as a string.
   */
  public String toString() {
    StringBuffer ecString = new StringBuffer();

    if (ec1 == 0) {
      ecString.append(ec1);
      ecString.append(".");
      ecString.append(ec2);
      ecString.append(".");
      ecString.append(ec3);
      ecString.append(".");
      ecString.append(ec4);
    } else {
      ecString.append(ec1);
      if (ec2 > 0) {
        ecString.append(".");
        ecString.append(ec2);
        if (ec3 > 0) {
          ecString.append(".");
          ecString.append(ec3);
          if (ec4 > 0) {
            ecString.append(".");
            if (Type.PRELIMINARY.equals(type))
            	ecString.append('n');
            ecString.append(ec4);
          }
        }
      }
    }

    return ecString.toString();
  }

  @Override
public int hashCode() {
	final int prime = 31;
	int result = super.hashCode();
	result = prime * result + ec1;
	result = prime * result + ec2;
	result = prime * result + ec3;
	result = prime * result + ec4;
	result = prime * result + ((type == null) ? 0 : type.hashCode());
	return result;
}

@Override
public boolean equals(Object obj) {
	if (this == obj)
		return true;
	if (!super.equals(obj))
		return false;
	if (getClass() != obj.getClass())
		return false;
	EnzymeCommissionNumber other = (EnzymeCommissionNumber) obj;
	if (ec1 != other.ec1)
		return false;
	if (ec2 != other.ec2)
		return false;
	if (ec3 != other.ec3)
		return false;
	if (ec4 != other.ec4)
		return false;
	if (type == null) {
		if (other.type != null)
			return false;
	} else if (!type.equals(other.type))
		return false;
	return true;
}

/**
   * Returns the enzyme's class number.
   *
   * @return the enzyme's class number.
   */
  public int getEc1() {
    return ec1;
  }

  /**
   * Returns the enzyme's subclass number.
   *
   * @return the enzyme's subclass number.
   */
  public int getEc2() {
    return ec2;
  }

  /**
   * Returns the enzyme's sub-subclass number.
   *
   * @return the enzyme's sub-subclass number.
   */
  public int getEc3() {
    return ec3;
  }

  /**
   * Returns the enzyme's number.
   *
   * @return the enzyme's number.
   */
  public int getEc4() {
    return ec4;
  }

  // ------------------- PRIVATE METHODS ------------------------

}
