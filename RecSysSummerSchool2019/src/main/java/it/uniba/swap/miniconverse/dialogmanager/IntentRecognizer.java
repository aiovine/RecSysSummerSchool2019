package it.uniba.swap.miniconverse.dialogmanager;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.dialogflow.v2.DetectIntentResponse;
import com.google.cloud.dialogflow.v2.QueryInput;
import com.google.cloud.dialogflow.v2.QueryResult;
import com.google.cloud.dialogflow.v2.SessionName;
import com.google.cloud.dialogflow.v2.SessionsClient;
import com.google.cloud.dialogflow.v2.SessionsSettings;
import com.google.cloud.dialogflow.v2.TextInput;
import com.google.cloud.dialogflow.v2.TextInput.Builder;

/**
 * This class handles the communication with the DialogFlow agent
 * @author Andrea Iovine
 *
 */
public class IntentRecognizer {
	
	private String credentialsFileName = "miniconverseagent-rlyfcf-6da2e5af023f.json";
	private String agentName = "miniconverseagent-rlyfcf";
	
	/**
	 * Forwards the message to the DialogFlow agent
	 * @param userID The user that sent the message
	 * @param text Contents of the text message
	 * @return A QueryResult object that contains all data returned by DialogFlow, such as the recognized intent.
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public QueryResult getResponse(String userID, String text) throws FileNotFoundException, IOException {
		ClassLoader classLoader = this.getClass().getClassLoader();
		String credentialsPath = classLoader.getResource(credentialsFileName).getPath();
		GoogleCredentials cred = GoogleCredentials.fromStream(new FileInputStream(credentialsPath));
		SessionsSettings settings = SessionsSettings.newBuilder().setCredentialsProvider(FixedCredentialsProvider.create(cred)).build();
		try (SessionsClient sessionsClient = SessionsClient.create(settings)) {
			//String agentName = Configuration.getConfiguration().get("dialogFlowInfo").getAsJsonObject().get("agentName").getAsString();
			SessionName session = SessionName.of(agentName, userID);
			System.out.println("Session Path: " + session.toString());

			Builder textInput = TextInput.newBuilder().setText(text).setLanguageCode("it");
			QueryInput queryInput = QueryInput.newBuilder().setText(textInput).build();
		    DetectIntentResponse response = sessionsClient.detectIntent(session, queryInput);
		    QueryResult queryResult = response.getQueryResult();

	      	return queryResult;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
