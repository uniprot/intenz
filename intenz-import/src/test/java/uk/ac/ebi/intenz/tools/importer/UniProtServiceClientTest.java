package uk.ac.ebi.intenz.tools.importer;

import java.util.List;
import java.util.Map;

import org.junit.Test;

public class UniProtServiceClientTest {
	@Test
	public void testGetSwissProtEcAcc2IdMap() {
		
		Map<String, Map<String, String> >  result = UniProtServiceClient.getSwissProtEcAcc2IdMap();
		System.out.println(result.size());
		//System.out.println(result);
	}
	
	@Test
	public void test3() {
		String ecs = "4.2.3.131; 4.2.3.189; 4.2.3.190";
		List<String> result = UniProtServiceClient.split(ecs);
		System.out.println(result);
	}
	@Test
	public void test4() {
		String ecs = "4.2.3.131";
		List<String> result = UniProtServiceClient.split(ecs);
		System.out.println(result);
	}
}
