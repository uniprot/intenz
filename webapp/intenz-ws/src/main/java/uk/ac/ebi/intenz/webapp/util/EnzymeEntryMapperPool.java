package uk.ac.ebi.intenz.webapp.util;

import java.util.NoSuchElementException;

import org.apache.commons.pool.BasePoolableObjectFactory;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.PoolUtils;
import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;

import uk.ac.ebi.intenz.mapper.EnzymeEntryMapper;


/**
 * A pool of EnzymeEntryMappers.
 * @author rafa
 *
 */
public class EnzymeEntryMapperPool implements ObjectPool<EnzymeEntryMapper> {

		private ObjectPool<EnzymeEntryMapper> pool;

		public EnzymeEntryMapperPool(int numOfObjs)
		throws IllegalArgumentException, Exception{
			pool = new GenericObjectPool<EnzymeEntryMapper>(
					new BasePoolableObjectFactory<EnzymeEntryMapper>(){
						@Override
						public EnzymeEntryMapper makeObject() throws Exception {
							return new EnzymeEntryMapper();
						}
					},
					numOfObjs);
			PoolUtils.prefill(pool, numOfObjs);
		}
		
		public void addObject() throws Exception, IllegalStateException,
				UnsupportedOperationException {
			pool.addObject();
		}

		public EnzymeEntryMapper borrowObject()
		throws Exception, NoSuchElementException, IllegalStateException {
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

		public void invalidateObject(EnzymeEntryMapper arg0) throws Exception {
			pool.invalidateObject(arg0);
		}

		public void returnObject(EnzymeEntryMapper arg0) throws Exception {
			pool.returnObject(arg0);
		}

		@Deprecated
		public void setFactory(PoolableObjectFactory<EnzymeEntryMapper> arg0)
		throws IllegalStateException, UnsupportedOperationException {
			pool.setFactory(arg0);
		}

	}
