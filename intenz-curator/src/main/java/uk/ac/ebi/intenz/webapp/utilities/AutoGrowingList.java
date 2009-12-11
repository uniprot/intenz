package uk.ac.ebi.intenz.webapp.utilities;

import org.apache.log4j.Logger;

import java.util.ArrayList;

/**
 * AutoGrowingList
 *
 * @author pmatos
 * @version $Revision: 1.2 $ $Date: 2008/01/28 12:33:09 $
 */
public class AutoGrowingList extends ArrayList {

  private Class clazz;
  Logger logger = Logger.getRootLogger();

  public AutoGrowingList(Class clazz) {
    super();
    this.clazz = clazz;
  }

  public Object get(int index) {
    Object obj = null;
    if (this.size() > index)
      obj = super.get(index);

    if (obj == null) {
      try {
        obj = clazz.newInstance();
      } catch (InstantiationException e) {
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }
      if (index > this.size()) {
        int indexCounter = this.size();
        while (indexCounter < index) {
          try {
            super.add(clazz.newInstance());
            indexCounter++;
          } catch (InstantiationException e) {
            logger.error("Error instantiating object of type " + clazz.getName());
          } catch (IllegalAccessException e) {
            logger.error(("Error in illegal access of the class " + clazz.getName()));
          }
        }
      }
      super.add(obj);
    }

    return obj;
  }

  public Object set(int index, Object obj) {
    if (this.size() > index)
      super.set(index, obj);
    else {
      int indexCounter = this.size();
      while (indexCounter < index) {
        try {
          super.add(clazz.newInstance());
          indexCounter++;
        } catch (InstantiationException e) {
          logger.error("Error instantiating object of type " + clazz.getName());
        } catch (IllegalAccessException e) {
          logger.error(("Error in illegal access of the class " + clazz.getName()));
        }
      }
      super.add(obj);
    }
    return obj;
  }
}
