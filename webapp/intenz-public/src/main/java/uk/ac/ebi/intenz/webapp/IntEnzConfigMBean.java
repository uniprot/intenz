package uk.ac.ebi.intenz.webapp;

/**
 * Managed bean for IntEnz configuration.
 * @author rafalcan
 *
 */
public interface IntEnzConfigMBean {

	public abstract String getIntEnzDataSource();

	public abstract void setIntEnzDataSource(String intEnzDataSource);

	public abstract String getRssUrl();

	public abstract void setRssUrl(String rssUrl);

    public abstract long getRssRefresh();

    public abstract void setRssRefresh(long refresh);

	public abstract String getXmlUrl();

	public abstract void setXmlUrl(String xmlUrl);
	
    public String getSitemapUrl();

	public void setSitemapUrl(String sitemapUrl);

	public abstract String getMailSmtpHost();

	public abstract void setMailSmtpHost(String mailSmtpHost);

	public abstract String getMailFrom();

	public abstract void setMailFrom(String mailFrom);

	public abstract String getContactMailTo();

	public abstract void setContactMailTo(String mailTo);

	public abstract String getContactMailSubject();

	public abstract void setContactMailSubject(String mailSubject);

	public abstract String getErrorMailTo();

	public abstract void setErrorMailTo(String mailTo);

	public abstract String getErrorMailSubject();

	public abstract void setErrorMailSubject(String mailSubject);

	public abstract String getPageSize();

	public abstract void setPageSize(String pageSize);

	public abstract String getTemplatesUrl();
	
	public abstract void setTemplatesUrl(String templatesUrl); 
}
