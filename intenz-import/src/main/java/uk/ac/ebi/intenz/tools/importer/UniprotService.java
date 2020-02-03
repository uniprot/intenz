/*
 * Copyright 2020 EMBL-EBI.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.ac.ebi.intenz.tools.importer;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import uk.ac.ebi.intenz.tools.importer.dto.UniprotApi;

/**
 *
 * @author joseph
 */
public class UniprotService {

    private static final String FORMAT = "&format=json";
    private static final String EC_ENDPOINT = "/api/proteins?offset=0&size=1000&reviewed=true&ec=";
    private static final String API_BASE_URL = "https://www.ebi.ac.uk/proteins";

    private URI buildURI(String ec) {

        String baseUrl = String.format("%s%s%s%s", API_BASE_URL, EC_ENDPOINT, ec, FORMAT);

        return UriComponentsBuilder.fromUriString(baseUrl).build().toUri();
    }

    private RestTemplate restTemplate() {
        return new RestTemplate();
    }

    private List<UniprotApi> responseEntity(String ec) {
        URI uri = buildURI(ec);
        ResponseEntity<List<UniprotApi>> data
                = restTemplate().exchange(uri,
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<UniprotApi>>() {
                });

        if (data.getBody() != null && data.hasBody()) {
            return data.getBody();
        }
        return Collections.EMPTY_LIST;
    }

    public List<UniprotApi> uniprotApiByEc(String ec) {

        return responseEntity(ec);

    }
}
