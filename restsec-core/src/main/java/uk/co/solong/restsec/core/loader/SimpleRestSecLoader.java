package uk.co.solong.restsec.core.loader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class SimpleRestSecLoader extends AbstractRestSecLoader {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String url;

    @Override
    public List<String> load(String userId) throws Exception {
        ParameterizedTypeReference<List<String>> typeRef = new ParameterizedTypeReference<List<String>>() {
        };
        Map<String, String> map = new HashMap<String, String>();
        map.put("userId", userId);
        ResponseEntity<List<String>> response = restTemplate.exchange(url, HttpMethod.GET, null, typeRef, map);
        return response.getBody();
    }

    public SimpleRestSecLoader(String url) {
        this.url = url;
    }
}
