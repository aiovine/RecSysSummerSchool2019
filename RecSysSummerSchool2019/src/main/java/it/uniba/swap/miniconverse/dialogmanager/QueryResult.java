package it.uniba.swap.miniconverse.dialogmanager;

import com.google.gson.JsonObject;

public class QueryResult {
	private String intent;
	private String fulfillmentText;
	public String getIntent() {
		return intent;
	}
	public String getFulfillmentText() {
		return fulfillmentText;
	}
	
	public QueryResult(JsonObject dialogflowResponse) {
		if (dialogflowResponse.has("fulfillmentText")) {
			this.fulfillmentText = dialogflowResponse.get("fulfillmentText").getAsString();
		}
		this.intent = dialogflowResponse.get("intent").getAsJsonObject().get("displayName").getAsString();
	}
}
