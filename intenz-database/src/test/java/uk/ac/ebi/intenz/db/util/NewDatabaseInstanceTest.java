package uk.ac.ebi.intenz.db.util;

import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.Connection;
import java.util.Properties;

import org.junit.Ignore;
import org.junit.Test;

public class NewDatabaseInstanceTest {
	@Test 
	public void testProperties() throws IOException {
		Properties properties = NewDatabaseInstance.loadProperties("ep-db-uzpdev");
		
		
		assertEquals("enzyme_portal", properties.getProperty("user"));
		assertEquals("epsilon", properties.getProperty("password"));
		assertEquals("ora-uzp-dev-hl.ebi.ac.uk", properties.getProperty("host"));
		assertEquals("1521", properties.getProperty("port"));
		assertEquals("Oracle", properties.getProperty("type"));
	//	assertEquals("VUZPDEV", properties.getProperty("instance"));
		assertEquals("UZPDEV", properties.getProperty("serviceName"));
	}
	
	@Test
	public void testCreateInstance() throws Exception {
		NewDatabaseInstance instance = NewDatabaseInstance.getInstance("ep-db-uzpdev");
		assertEquals("enzyme_portal", instance.getUser());
		assertEquals("epsilon",instance.getPassword());
		assertEquals("ora-uzp-dev-hl.ebi.ac.uk", instance.getHost());
		assertEquals("1521",instance.getPort());
		assertEquals("Oracle", instance.getDbType());
	//	assertEquals("VUZPDEV", instance.getInstance());
		assertEquals("UZPDEV", instance.getServiceName());
		assertEquals("enzyme_portal", instance.getSchema());
		
		Connection connection = instance.getConnection();
		assertNotNull(connection);
		connection.close();
	}
	@Ignore
	@Test
	public void testCreateChebiInstance() throws Exception {
		NewDatabaseInstance instance = NewDatabaseInstance.getInstance("chebi-db-prod");

		
		Connection connection = instance.getConnection();
		assertNotNull(connection);
		connection.close();
	}
}
