package uk.ac.ebi.intenz.webapp.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.ServletException;

import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.PlugIn;
import org.apache.struts.config.ModuleConfig;

import uk.ac.ebi.intenz.webapp.utilities.EntryLockSingleton;
import uk.ac.ebi.xchars.SpecialCharacters;

/**
 * This plugin initialises necessary resources for the IntEnz web application.
 *
 * @author Michael Darsow
 * @version $Revision: 1.2 $ $Date: 2008/01/28 12:33:04 $
 */
public class IntEnzPlugInAction implements PlugIn {

//  private static final Logger LOGGER = Logger.getLogger(IntEnzPlugInAction.class);

  public void init(ActionServlet actionServlet, ModuleConfig moduleConfig) throws ServletException {
//    String path = IntEnzPlugInAction.class.getClassLoader().getResource("specialCharacters.xml").getPath();
//    try {
//      path = URLDecoder.decode(path, "ISO-8859-1");
//    } catch (UnsupportedEncodingException e) {                            
//      throw new ServletException("Error during initialisation.", e);
//    }
//    path = path.replaceFirst("specialCharacters.xml", "");
//    SpecialCharacters.class.getClassLoader().getResource("specialCharacters.dtd");
//    SpecialCharacters.class.getClassLoader().getResource("specialCharactersMapping.dtd");
    actionServlet.getServletContext().setAttribute("specialCharactersInstance", SpecialCharacters.getInstance(null));

    // Create object for locking entries in process.
    actionServlet.getServletContext().setAttribute("entryLock", EntryLockSingleton.getInstance());
  }

  public void destroy(){}

}
