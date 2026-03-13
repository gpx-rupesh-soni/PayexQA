package rcom.payex.service;

import java.net.URI;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import rcom.payex.service.UrlService.HttpGetWithEntity;
import rcom.payex.service.UrlService.HttpPostWithEntity;

@Service
public class UrlService {

	Logger logger = LogManager.getLogger(getClass());

	public JSONObject getLedgerData(String payload) {

    	System.out.println("getLedgerData2 called...");

    	String url = "https://freepay.co/PayexFP/export/getLedger";

        try (CloseableHttpClient client = HttpClients.createDefault()) {

            HttpGetWithEntity request = new HttpGetWithEntity(url);

            request.setHeader("Content-Type", "application/json");
            request.setEntity(new StringEntity(payload, ContentType.APPLICATION_JSON));
            //request.setHeader("Host", "localhost:8081");

            System.out.println("payload : " + payload);
            System.out.println("url : " + url);
            System.out.println("URI = " + request.getURI());

            try (CloseableHttpResponse response = client.execute(request)) {

                System.out.println("Status : " + response.getStatusLine());

                if (response.getEntity() != null) {
                    String responseBody = EntityUtils.toString(response.getEntity(), "UTF-8");
                    //System.out.println(responseBody);
                    JSONParser parser = new JSONParser();
                    JSONObject jsonObject = (JSONObject) parser.parse(responseBody);
                    return jsonObject;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

	public JSONArray getBuyerData(String payload) {

    	String url = "https://freepay.co/PayexFP/export/getBuyer";
		RestTemplate restTemplate = new RestTemplate();

		logger.info("payload : " + payload);
		logger.info("url : " + url);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<String> entity = new HttpEntity<>(payload, headers);

		try {
			ResponseEntity<String> response = restTemplate.exchange(
			        url,
			        HttpMethod.POST,
			        entity,
			        String.class
			);
			JSONParser parser = new JSONParser();
			JSONArray jsonObject = (JSONArray) parser.parse(response.getBody());
            return jsonObject;
		} catch (HttpClientErrorException e) {
			logger.info("Status: " + e.getStatusCode());
			logger.info("Body: " + e.getResponseBodyAsString());
		} catch (Exception e) {
            e.printStackTrace();
        }
		return null;
	}

	public JSONArray addBuyerData(String payload) {

    	System.out.println("payload : " + payload);
        String url = "http://localhost:8081/PayexFP/export/addBuyer";

        try (CloseableHttpClient client = HttpClients.createDefault()) {

        	HttpPostWithEntity request = new HttpPostWithEntity(url);

            request.setHeader("Content-Type", "application/json");
            request.setEntity(new StringEntity(payload, ContentType.APPLICATION_JSON));

            try (CloseableHttpResponse response = client.execute(request)) {

                System.out.println("Status : " + response.getStatusLine());

                if (response.getEntity() != null) {
                    String responseBody = EntityUtils.toString(response.getEntity(), "UTF-8");
                    System.out.println(responseBody);
                    JSONParser parser = new JSONParser();
                    JSONArray jsonObject = (JSONArray) parser.parse(responseBody);
                    return jsonObject;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

	public JSONObject addLedgerData(String payload) {

        String url = "http://localhost:8081/PayexFP/export/addLedger";

        try (CloseableHttpClient client = HttpClients.createDefault()) {

        	HttpPostWithEntity request = new HttpPostWithEntity(url);

            request.setHeader("Content-Type", "application/json");
            request.setEntity(new StringEntity(payload, ContentType.APPLICATION_JSON));

            try (CloseableHttpResponse response = client.execute(request)) {

                System.out.println("Status : " + response.getStatusLine());

                if (response.getEntity() != null) {
                    String responseBody = EntityUtils.toString(response.getEntity(), "UTF-8");
                    System.out.println(responseBody);
                    JSONParser parser = new JSONParser();
                    JSONObject jsonObject = (JSONObject) parser.parse(responseBody);
                    return jsonObject;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

	public class HttpGetWithEntity extends HttpEntityEnclosingRequestBase {

        public static final String METHOD_NAME = "GET";

        public HttpGetWithEntity(String uri) {
            setURI(URI.create(uri));
        }

        @Override
        public String getMethod() {
            return METHOD_NAME;
        }
    }

    public class HttpPostWithEntity extends HttpEntityEnclosingRequestBase {

        public static final String METHOD_NAME = "POST";

        public HttpPostWithEntity(String uri) {
            setURI(URI.create(uri));
        }

        @Override
        public String getMethod() {
            return METHOD_NAME;
        }
    }

}
