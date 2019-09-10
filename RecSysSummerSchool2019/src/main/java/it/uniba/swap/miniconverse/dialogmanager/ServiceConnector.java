package it.uniba.swap.miniconverse.dialogmanager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ServiceConnector {
	private static final String url = "http://90.147.102.235:8080/miniconverse/restService";
	
	public boolean addPreferences(int userID, List<Entity> entities) {
		try {
			HttpClient client = HttpClientBuilder.create().build();
			HttpPost post = new HttpPost(url + "/addPreferences");
			JsonObject body = new JsonObject();
			body.addProperty("userID", userID + ""); //Cast to string
			body.add("preferences", new Gson().toJsonTree(entities));
			StringEntity bodyEntity = new StringEntity(body.toString());
			post.addHeader("content-type", "application/json");
			post.setEntity(bodyEntity);
			HttpResponse response = client.execute(post);
			if (response.getStatusLine().getStatusCode() == 200) {
				JsonObject responseJson = responseToJson(response).getAsJsonObject();
				return responseJson.get("success").getAsBoolean();
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
	
	public boolean resetProfile(int userID) {
		try {
			HttpClient client = HttpClientBuilder.create().build();
			HttpGet request = new HttpGet(url + "/resetProfile?userID=" + userID);
			HttpResponse response = client.execute(request);
			if (response.getStatusLine().getStatusCode() == 200) {
				JsonObject responseJson = responseToJson(response).getAsJsonObject();
				return responseJson.get("success").getAsBoolean();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public List<RecEntity> getRecommendations(int userID) {
		try {
			HttpClient client = HttpClientBuilder.create().build();
			HttpGet request = new HttpGet(url + "/getRecommendations?userID=" + userID);
			HttpResponse response = client.execute(request);
			if (response.getStatusLine().getStatusCode() == 200) {
				JsonArray responseJson = responseToJson(response).getAsJsonArray();
				List<RecEntity> entities = recommendationsToList(responseJson);
				return entities;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public String getExplanation(int userID, String entityURI) {
		try {
			HttpClient client = HttpClientBuilder.create().build();
			HttpGet request = new HttpGet(url + "/explanation?userID=" + userID + "&entityURI=" + entityURI);
			HttpResponse response = client.execute(request);
			if (response.getStatusLine().getStatusCode() == 200) {
				return responseToString(response);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private List<RecEntity> recommendationsToList(JsonArray recommendations) {
		Gson gson = new Gson();
		List<RecEntity> entities = new ArrayList<RecEntity>();
		for (JsonElement recJson: recommendations) {
			JsonObject recJsonObject = recJson.getAsJsonObject();
			RecEntity e = gson.fromJson(recJsonObject, RecEntity.class);
			entities.add(e);
		}
		return entities;
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
