package rcom.payex.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import rcom.payex.service.UrlService;

public class Utils {
	
	static Logger logger = LogManager.getLogger(Utils.class);

	public static String getStackTrace(Exception e) {
		final StringWriter sw = new StringWriter();
		final PrintWriter pw = new PrintWriter(sw, true);
		e.printStackTrace(pw);
		return sw.getBuffer().toString();
	}
	
	public static String sendPost(String URL,String msg, String contentType, Map<String,String> headers) throws ClientProtocolException, IOException {
		final String USER_AGENT = "Mozilla/5.0";
		logger.info("URL : "+URL);
		if(contentType.isEmpty())
			contentType = "application/json";
		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(URL);

		// add header
		if(headers != null) {
			for (Map.Entry<String, String> entry: headers.entrySet()){
				post.setHeader(entry.getKey(),entry.getValue());
			}
		}
		post.setHeader("User-Agent", USER_AGENT);
		post.setHeader("Accept", "*/*");
		post.setHeader("Content-Type", contentType);
		
		post.setEntity(new StringEntity(msg));

		HttpResponse response = client.execute(post);
		
		StringBuffer result = new StringBuffer();
		try(InputStreamReader streamReader = new InputStreamReader(response.getEntity().getContent());){
			BufferedReader rd = new BufferedReader(streamReader);				
			String line;
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
		}catch (Exception e) {
			logger.info("sendPost > "+Utils.getStackTrace(e));
		}		
		
		if(response.getStatusLine().getStatusCode()>=200 && response.getStatusLine().getStatusCode()<=299){
			logger.info("sendPost > responce : "+response.getStatusLine().getStatusCode()+":"+result.toString() );
			return result.toString();
		}else {			
			logger.info("sendPost > Response code is not 200 > "+response.getStatusLine().getStatusCode()+":" +result.toString());
			return "";
		}
	}

}
