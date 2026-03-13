package rcom.payex.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.logging.log4j.LogManager;import org.apache.logging.log4j.Logger;

public class CommonUrlUtility {
	
	static Logger logger = LogManager.getLogger(CommonUrlUtility.class);
	
	private static final String USER_AGENT = "Mozilla/5.0";
	
	// HTTP GET request
		public static String sendGet(String url) throws IOException {

			InputStreamReader inputStreamReader =null;
			BufferedReader rd =null;
			StringBuffer result=null;
			if(url.isEmpty())
				url = "http://www.google.com/search?q=developer";

			HttpClient client = HttpClientBuilder.create().build();
			HttpGet request = new HttpGet(url);

			// add request header
			request.addHeader("User-Agent", USER_AGENT);



			try{
				HttpResponse response = client.execute(request);
				logger.debug("\nSending 'GET' request to URL : " + url);
				logger.debug("Response Code : " + response.getStatusLine().getStatusCode());
			 inputStreamReader = new  InputStreamReader(response.getEntity().getContent()); 
			 rd = new BufferedReader(inputStreamReader);

			 result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			logger.debug(result.toString());
			}
			catch(Exception e)
			{
				logger.error(e.toString(),e);
			}
			finally
			{
				if(inputStreamReader != null){
				inputStreamReader.close();
				}
				if( rd != null)
				{
					rd.close();
				}
			}
			
			return result.toString();

		}

		// HTTP POST request
		public static void sendPost() throws IOException  {

			String url = "https://selfsolve.apple.com/wcResults.do";

			HttpClient client = HttpClientBuilder.create().build();
			HttpPost post = new HttpPost(url);
			StringBuffer result =null;
			InputStreamReader inputStreamReader = null;
			BufferedReader rd =null;
			// add header
			post.setHeader("User-Agent", USER_AGENT);

			List<NameValuePair> urlParameters = new ArrayList<>();
			urlParameters.add(new BasicNameValuePair("sn", "C02G8416DRJM"));
			urlParameters.add(new BasicNameValuePair("cn", ""));
			urlParameters.add(new BasicNameValuePair("locale", ""));
			urlParameters.add(new BasicNameValuePair("caller", ""));
			urlParameters.add(new BasicNameValuePair("num", "12345"));

			post.setEntity(new UrlEncodedFormEntity(urlParameters));

			try{
				
			HttpResponse response = client.execute(post);
			logger.debug("\nSending 'POST' request to URL : " + url);
			logger.debug("Post parameters : " + post.getEntity());
			logger.debug("Response Code : " +response.getStatusLine().getStatusCode());
			inputStreamReader = new InputStreamReader(response.getEntity().getContent());
			rd = new BufferedReader(inputStreamReader);

			 result = new StringBuffer();
			 String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			
			logger.debug(result.toString());
			}
			catch( Exception e)
			{
				logger.error(e.toString(), e);
			}
			finally
			{
				if(inputStreamReader != null){
					inputStreamReader.close();
					
					}
				if(rd != null)
				{
					rd.close();
				}
			}

		}

}
