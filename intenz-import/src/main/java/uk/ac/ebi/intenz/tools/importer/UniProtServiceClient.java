package uk.ac.ebi.intenz.tools.importer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class UniProtServiceClient {
	private  static final String URL =
			"https://rest.uniprot.org/uniprotkb/stream?fields=accession%2Cid%2Cec&format=tsv&query=%28ec%3A%2A%29%20AND%20%28reviewed%3Atrue%29";
	

	public static Map<String, Map<String, String> > getSwissProtEcAcc2IdMap() {
		Map<String, Map<String, String>> ec2Acc2IdMap = new HashMap<>();
		String uri = URL;
		Reader reader = null;
		try {
			URL url = new URL(uri);

			URLConnection connection = url.openConnection();
			HttpURLConnection httpConnection = (HttpURLConnection) connection;

			// httpConnection.setRequestProperty("Accept", "application/xml");

			InputStream response = connection.getInputStream();
			int responseCode = httpConnection.getResponseCode();

			if (responseCode != 200) {
				return ec2Acc2IdMap;
			}

			String output;
			reader = new BufferedReader(new InputStreamReader(response, "UTF-8"));
			StringBuilder builder = new StringBuilder();
			char[] buffer = new char[8192];
			int read;
			while ((read = reader.read(buffer, 0, buffer.length)) > 0) {
				builder.append(buffer, 0, read);
			}
			output = builder.toString();

			String[] lines = output.split("\n");
			for(String line: lines) {
				line= line.trim();
				if(line.startsWith("Entry")) {
					continue;
				}
				String[] tokens =line.split("\t");
				if(tokens.length==3) {
					String ecs = tokens[2];
					String[] ecArrays = ecs.split("; ");
					for(String ec:ecArrays) {
						if(isValidEC(ec)) {
							Map<String, String> acc2Id = ec2Acc2IdMap.get(ec);
							if(acc2Id ==null) {
								acc2Id = new TreeMap<>();
								ec2Acc2IdMap.put(ec,  acc2Id);
							}
							acc2Id.put(tokens[0], tokens[1]);
							
						}
					}
					
				}
			}
		
			return ec2Acc2IdMap;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null)
				try {
					reader.close();
				} catch (IOException logOrIgnore) {
					logOrIgnore.printStackTrace();
				}
		}
		return ec2Acc2IdMap;
	}
	private static boolean isValidEC(String ec) {
		 return !ec.contains("-");
	}
	public static List<String> split(String ecs){
		String[] ecArrays = ecs.split("; ");
		return Arrays.asList(ecArrays);
	}
}
