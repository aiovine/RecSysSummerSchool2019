package it.uniba.swap.miniconverse.dialogmanager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class SentimentAnalyzerConnector {
	private static final String url = "http://90.147.102.235:8080/movierecsys-sentimentextractor/sentiment";
	private boolean findEntities;
	private boolean findPropertyTypes;
	
	public SentimentAnalyzerConnector() {
		this.findEntities = true;
		this.findPropertyTypes = false;
	}
	
	private int sentimentToRating(int sentiment) {
		if (sentiment >= 2) {
			return 1;
		} else {
			return 0;
		}
	}
	
	public List<Entity> getEntities(String message) throws UnsupportedOperationException, IOException {
		JsonArray sentimentArray = sendRequest(url, message);
		List<Entity> entities = new ArrayList<>();
		
		for (int i = 0; i < sentimentArray.size(); i++) {
			JsonObject entityJson = sentimentArray.get(i).getAsJsonObject();
			int sentiment = entityJson.get("sentiment").getAsInt();
			entities.add(new Entity(entityJson.get("uriDBpedia").getAsString(),
					entityJson.get("label").getAsString(),
					sentimentToRating(sentiment)));
		}
		return entities;
	}
	
	private JsonArray sendRequest(String url, String message) throws UnsupportedOperationException, IOException {
		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(url + "?" 
				+ getParameterString("text", URLEncoder.encode(message, "UTF-8"))
				+ "&" + getParameterString("findEntities", findEntities)
				+ "&" + getParameterString("findPropertyTypes", findPropertyTypes));

		
		HttpResponse response = client.execute(request);
		
		BufferedReader rd = new BufferedReader(
				new InputStreamReader(response.getEntity().getContent()));

		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		
		System.out.println("Result from Sentiment Analyzer is:" + result.toString());
		JsonArray sentimentArray = new JsonParser().parse(result.toString()).getAsJsonArray();
		return sentimentArray;
	}
		
	private String getParameterString(String parameterName, String parameterValue) {
		return parameterName + "=" + parameterValue.replace(" ", "%20");
	}
	
	private String getParameterString(String parameterName, boolean parameterValue) {
		if (parameterValue) {
			return parameterName + "=" + "true";
		} else {
			return parameterName + "=" + "false";
		}
	}
}
