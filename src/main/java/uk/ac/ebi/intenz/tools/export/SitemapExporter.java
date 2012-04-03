package uk.ac.ebi.intenz.tools.export;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.sitemaps.ObjectFactory;
import org.sitemaps.Url;
import org.sitemaps.Urlset;
import org.xml.sax.SAXException;

/**
 * Exports IntEnz as a
 * <a href="http://www.google.com/webmasters/sitemaps">sitemap XML file</a>
 * @author rafalcan
 *
 */
public class SitemapExporter {

	private Marshaller marshaller;
    private ObjectFactory of;
    private final String INTENZ_URL = "http://www.ebi.ac.uk/intenz/";

    public SitemapExporter() throws JAXBException, SAXException{
        JAXBContext context = JAXBContext.newInstance("org.sitemaps");
        marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		Schema sitemapXsd = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
			.newSchema(SitemapExporter.class.getClassLoader().getResource("sitemap.xsd"));
		marshaller.setSchema(sitemapXsd);
        of = new ObjectFactory();
	}

    /**
     * Dumps a list of URLs as sitemap (see {@link http://www.sitemaps.org})
     * @param urls List of indexed URLs as Strings.
     * @param os
     * @throws JAXBException in case of marshalling error
     */
	public void export(Collection<String> urls, OutputStream os) throws JAXBException {
		final String CHANGE_FREQ = "weekly";
		Urlset urlset = of.createUrlset();
		Url intenzUrl = of.createUrl();
		intenzUrl.setLoc(INTENZ_URL);
		intenzUrl.setLastmod(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		intenzUrl.setChangefreq(CHANGE_FREQ);
		intenzUrl.setPriority(new BigDecimal(0.8).setScale(1,BigDecimal.ROUND_DOWN));
		urlset.getUrl().add(intenzUrl);
		for (String url : urls) {
			Url pageUrl = of.createUrl();
			pageUrl.setLoc(url);
			pageUrl.setChangefreq(CHANGE_FREQ);
			urlset.getUrl().add(pageUrl);
		}
		marshaller.marshal(urlset, os);
	}

}
