package it.uniba.swap.miniconverse.dialogmanager;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * This class handles the communication with the DialogFlow agent
 * @author Andrea Iovine
 *
 */
public class IntentRecognizer {
	
	private static final String url = "http://90.147.102.235:8080/dialogflowbridge/api/detectIntent";
	
	/**
	 * Forwards the message to the DialogFlow agent
	 * @param userID The user that sent the message
	 * @param text Contents of the text message
	 * @return A QueryResult object that contains all data returned by DialogFlow, such as the recognized intent.
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public QueryResult getResponse(String userID, String text) throws FileNotFoundException, IOException {
		
		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(url + "?userID=" + userID + "&message=" + URLEncoder.encode(text, "UTF-8"));
		HttpResponse response = client.execute(request);
		
		return new QueryResult(responseToJson(response).getAsJsonObject());
	}
	
	private String responseToString(HttpResponse response) throws UnsupportedOperationException, IOException {
		BufferedReader rd = new BufferedReader(
				new InputStreamReader(response.getEntity().getContent()));

		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line + "\n");
		}
		return result.toString();
	}
	
	private JsonElement responseToJson(HttpResponse response) throws UnsupportedOperationException, IOException {
		JsonElement responseJson = new JsonParser().parse(responseToString(response));
		return responseJson;
	}

}
