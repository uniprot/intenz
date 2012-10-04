package uk.ac.ebi.intenz.webapp.util;

import java.util.Map;
import java.util.NoSuchElementException;

import org.apache.commons.pool.BasePoolableObjectFactory;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.PoolUtils;
import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;

import uk.ac.ebi.intenz.tools.export.XmlExporter;
import uk.ac.ebi.intenz.tools.export.XmlExporter.Flavour;

/**
 * A pool of IntEnzXML exporters.
 * @author rafa
 *
 */
public class XmlExporterPool implements ObjectPool<XmlExporter> {

	private ObjectPool<XmlExporter> pool;

	public XmlExporterPool(int numOfExporters, final Map<String, Object> desc)
	throws IllegalArgumentException, Exception{
		pool = new GenericObjectPool<XmlExporter>(
				new BasePoolableObjectFactory<XmlExporter>(){
					@Override
					public XmlExporter makeObject() throws Exception {
						XmlExporter xmlExporter = new XmlExporter();
						xmlExporter.setDescriptions(desc);
						xmlExporter.setFlavour(Flavour.ASCII);
						return xmlExporter;
					}
				},
				numOfExporters);
		PoolUtils.prefill(pool, numOfExporters);
	}
	
	public void addObject() throws Exception, IllegalStateException,
			UnsupportedOperationException {
		pool.addObject();
	}

	public XmlExporter borrowObject() throws Exception, NoSuchElementException,
			IllegalStateException {
		return pool.borrowObject();
	}

	public void clear() throws Exception, UnsupportedOperationException {
		pool.clear();
	}

	public void close() throws Exception {
		pool.close();
	}

	public int getNumActive() throws UnsupportedOperationException {
		return pool.getNumActive();
	}

	public int getNumIdle() throws UnsupportedOperationException {
		return pool.getNumIdle();
	}

	public void invalidateObject(XmlExporter arg0) throws Exception {
		pool.invalidateObject(arg0);
	}

	public void returnObject(XmlExporter arg0) throws Exception {
		pool.returnObject(arg0);
	}

	@Deprecated
	public void setFactory(PoolableObjectFactory<XmlExporter> arg0)
	throws IllegalStateException, UnsupportedOperationException {
		pool.setFactory(arg0);
	}

}
