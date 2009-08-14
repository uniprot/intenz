package uk.ac.ebi.intenz.domain.enzyme;

import uk.ac.ebi.intenz.domain.DomainObject;
import uk.ac.ebi.intenz.domain.exceptions.EcException;

import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class represents an Enzyme Commission (EC) number.
 * <p/>
 * The general valid format looks as follows:<p/>
 * <code>\d+(?:\.\d+(?:\.\d+(?:\.\d+){0,1}){0,1}){0,1}</code>
 * <p/>
 * <p/>
 * For instance, the first enzyme has the EC <code>1.1.1.1</code>.
 * <p/>
 * The first digit is the number of the class the enzyme belongs to.<br>
 * The second digit is the number of the subclass the enzyme belongs to.<br>
 * The third digit is the number of the sub-subclass the enzyme belongs to.<br>
 * The last digit is the number of the enzyme within the sub-subclass.
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
  public final static EnzymeCommissionNumber UNDEF = new EnzymeCommissionNumber(-1, -1, -1, -1);

  /**
   * The EC number is undefined.
   */
  public static final int TYPE_UNDEF = 0;

  /**
   * Only the first digit is > 0 which means that this EC is a class EC.
   */
  public static final int TYPE_CLASS = 1;

  /**
   * Only the first two digits are > 0 which means that this EC is a subclass EC.
   */
  public static final int TYPE_SUBCLASS = 2;

  /**
   * Only the first three digits are > 0 which means that this EC is a sub-subclass EC.
   */
  public static final int TYPE_SUBSUBCLASS = 3;

  /**
   * All digits are > 0 which means that this EC is an enzyme EC.
   */
  public static final int TYPE_ENZYME = 4;

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
   * Object cannot be created outside this class.
   *
   * @param ec1 The enzyme's class number.
   * @param ec2 The enzyme's subclass number.
   * @param ec3 The enzyme's sub-subclass number.
   * @param ec4 The enzyme's number.
   */
  private EnzymeCommissionNumber(int ec1, int ec2, int ec3, int ec4) {
    super();
    this.ec1 = ec1;
    this.ec2 = ec2;
    this.ec3 = ec3;
    this.ec4 = ec4;
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
    return new EnzymeCommissionNumber(ec1, -1, -1, -1);
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

    return new EnzymeCommissionNumber(ec1, ec2, -1, -1);
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

    return new EnzymeCommissionNumber(ec1, ec2, ec3, -1);
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
  public static EnzymeCommissionNumber valueOf(int ec1, int ec2, int ec3, int ec4) throws EcException {
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

    return new EnzymeCommissionNumber(ec1, ec2, ec3, ec4);
  }

  /**
   * Returns an <code>EnzymeCommissionNumber</code> instance defined by the given string.
   * <p/>
   * Calls {@link EnzymeCommissionNumber#valueOf(int, int, int, int)}.
   *
   * @param ecString A string representing an EC number.
   * @throws NullPointerException  if <code>ecString</code> is <code>null</code>.
   * @throws EcException           if one of the parameters is invalid.
   * @throws NumberFormatException if <code>ecString</code> contained invalid characters.
   */
  public static EnzymeCommissionNumber valueOf(String ecString) throws EcException, NumberFormatException {
    if (ecString == null) throw new NullPointerException("Parameter 'ecString' must not be null.");
    int ec1 = -1, ec2 = -1, ec3 = -1, ec4 = -1;

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
          ec4 = Integer.parseInt(ecPart);
          break;
      }
      iii++;
    }

    return EnzymeCommissionNumber.valueOf(ec1, ec2, ec3, ec4);
  }

  /**
   * Creates a copy of the given <code>EnzymeCommissionNumber</code>.
   *
   * @param ecToCopy The EC to be copied.
   * @return a copy of the given <code>EnzymeCommissionNumber</code>.
   * @throws NullPointerException if <code>ecToCopy</code> is <code>null</code>.
   */
  public static EnzymeCommissionNumber copy(EnzymeCommissionNumber ecToCopy) {
    if (ecToCopy == null) throw new NullPointerException("Parameter 'ecToCopy' must not be null.");
    return new EnzymeCommissionNumber(ecToCopy.ec1, ecToCopy.ec2, ecToCopy.ec3, ecToCopy.ec4);
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

    return ec4 - ec.ec4;
  }

  /**
   * Returns the type of this EC number.
   * <p/>
   * See class constants for more information of the supported types.
   *
   * @return the type code.
   */
  public int getType() {
    if (ec1 == -1) return TYPE_UNDEF;
    if (ec2 == -1) return TYPE_CLASS;
    if (ec3 == -1) return TYPE_SUBCLASS;
    if (ec4 == -1) return TYPE_SUBSUBCLASS;
    return TYPE_ENZYME;
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
    int ec1 = 0, ec2 = 0, ec3 = 0, ec4 = 0;
    if (ecString == null) return false;
    if (!Pattern.matches("(\\d+)(?:\\.(\\d+)(?:\\.(\\d+)(?:\\.(\\d+)){0,1}){0,1}){0,1}", ecString)) return false;
    Pattern ecPattern = Pattern.compile("(\\d+)(?:\\.(\\d+)(?:\\.(\\d+)(?:\\.(\\d+)){0,1}){0,1}){0,1}");
    Matcher ecMatcher = ecPattern.matcher(ecString);
    try {
      if (ecMatcher.find()) {
        ec1 = Integer.parseInt(ecMatcher.group(1));
        if (ec1 < 0) return false;
        if (ecMatcher.group(2) != null) {
          ec2 = Integer.parseInt(ecMatcher.group(2));
          if (ec2 < 0) return false;
          if (ecMatcher.group(3) != null) {
            ec3 = Integer.parseInt(ecMatcher.group(3));
            if (ec3 < 0) return false;
            if (ecMatcher.group(4) != null) {
              ec4 = Integer.parseInt(ecMatcher.group(4));
              if (ec4 < 0) return false;
            }
          }
        }
      }
    } catch (NumberFormatException e) {
      return false;
    }

    if (ec1 == 0) {
      if (ec2 > 0 || ec3 > 0 || ec4 > 0) return false;
    }
    if (ec2 == 0) {
      if (ec3 > 0 || ec4 > 0) return false;
    }
    if (ec3 == 0) {
      if (ec4 > 0) return false;
    }

    return true;
  }

  /**
   * Checks whether two EC numbers are equal or not.
   *
   * @param o object to be compared with this one
   * @return <code>true</code>, if the two EC instances are equal.
   */
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof EnzymeCommissionNumber)) return false;

    final EnzymeCommissionNumber enzymeCommissionNumber = (EnzymeCommissionNumber) o;

    if (ec1 != enzymeCommissionNumber.ec1) return false;
    if (ec2 != enzymeCommissionNumber.ec2) return false;
    if (ec3 != enzymeCommissionNumber.ec3) return false;
    if (ec4 != enzymeCommissionNumber.ec4) return false;

    return true;
  }

  /**
   * Creates a unique hash code derived from the EC number.
   *
   * @return the hash code.
   */
  public int hashCode() {
    int result;
    result = ec1;
    result = 29 * result + ec2;
    result = 29 * result + ec3;
    result = 29 * result + ec4;
    return result;
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
            ecString.append(ec4);
          }
        }
      }
    }

    return ecString.toString();
  }

  // --------------------  GETTER & SETTER -----------------------

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
