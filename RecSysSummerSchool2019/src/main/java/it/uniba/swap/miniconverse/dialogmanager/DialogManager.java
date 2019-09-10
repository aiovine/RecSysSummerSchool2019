package it.uniba.swap.miniconverse.dialogmanager;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class DialogManager {
	
	private static List<RecEntity> recommendationList;
	private static int currentRecommendationIndex = -1;
	
	/**
	 * Invokes the appropriate components, given the intent
	 * @param userID ID of the user
	 * @param intent recognized intent
	 * @param text user utterance
	 * @return A system response if applicable, or null. In the latter case, the default Dialogflow fulfillment text is used.
	 * @throws UnsupportedOperationException
	 * @throws IOException
	 */
	public static String dispatchIntent(int userID, String intent, String text) throws UnsupportedOperationException, IOException {
		String responseText = null;
		/* The dispatchIntent method must handle the following intents:
		 * - preference: The user is expressing a preference (e.g. "I like The Matrix")
		 * - recommendation: The user is asking the system to receive a recommendation (e.g. "What can I watch tonight?")
		 * - explanation: After a recommendation, the user is asking the motivation behind it (e.g. "Why do you recommend this movie?")
		 * - reset: Reset the user profile (e.g. "Reset")
		 * Each intent may return a textual response, put it in the "responseText" variable.
		 * 
		 */
		//START WRITING CODE HERE
		
		//END CODE
		return responseText;
	}
	
	/**
	 * Add the preferences provided by the user
	 * @param userID ID of the user
	 * @param message user utterance
	 * @return Message containing a feedback of the operation
	 * @throws UnsupportedOperationException
	 * @throws IOException
	 */
	private static String addPreferences(int userID, String message) throws UnsupportedOperationException, IOException {
		String responseText = null;
		SentimentAnalyzerConnector sentimentConnector = new SentimentAnalyzerConnector();
		ServiceConnector serviceConnector = new ServiceConnector();
		List<Entity> preferences = null;
		boolean success = false;
		//START WRITING CODE HERE
		//Get the mentioned entities from the user's message and put them in the "preferences" list

		//Add the preferences to the user profile and put the outcome in the "success" variable
		
		//END CODE
		//Generate a feedback
		if (success) {
			responseText = "I added the following preferences:\n";
			for (Entity e: preferences) {
				responseText += getFeedbackLine(e);
			}
		} else {
			responseText = "An error occurred.";
		}
		return responseText;
	}
	
	/**
	 * Generates a list of recommended items
	 * @param userID ID of the user
	 * @return A list of recommended items.
	 */
	private static String getRecommendations(int userID) {
		String responseText = null;
		ServiceConnector serviceConnector = new ServiceConnector();
		
		if (recommendationList == null || currentRecommendationIndex == recommendationList.size() - 1) {
			//If the recommendation list is empty, or all items in the list have already been shown to the user
			//Generate a new recommendation list and put it in the "recommendationList" variable
			//START WRITING CODE HERE
			
			//END CODE
			if (recommendationList != null) {
				currentRecommendationIndex = 0;
				responseText = getRecommendationLine(recommendationList.get(currentRecommendationIndex));
			} else {
				responseText = "An error occurred.";
			}
		} else {
			//Else, present the next item in the recommendation list
			currentRecommendationIndex++;
			responseText = getRecommendationLine(recommendationList.get(currentRecommendationIndex));
		}
		return responseText;
	}
	
	/**
	 * Produces an explanation for the current recommended item
	 * @param userID ID of the current user
	 * @return A textual explanation for the recommended item
	 */
	private static String getExplanation(int userID) {
		String responseText = null;
		ServiceConnector serviceConnector = new ServiceConnector();
		
		if (recommendationList == null) {
			responseText = "No explanation is available.";
		} else {
			//Invoke the Service component to receive the explanation
			//START WRITING CODE HERE

			//END CODE
		}
		return responseText;
	}
	
	/**
	 * Resets the profile of the provided user
	 * @param userID
	 * @return
	 */
	private static String resetProfile(int userID) {
		boolean reset = new ServiceConnector().resetProfile(userID);
		if (reset) {
			return "The profile has been reset.";
		}
		return null;
	}
	
	private static String getFeedbackLine(Entity e) {
		String rating = "like";
		if (e.getRating() == 0) {
			rating = "dislike";
		}
		
		return "You " + rating + " " + e.getLabel() + "\n";
	}
	
	public static String getRecommendationLine(RecEntity e) {
		return "I suggest you watch " + e.getLabel() + "\n";
	}
	
	/*
	 * Main loop
	 */
	public static void main(String[] args) throws FileNotFoundException, IOException {
		IntentRecognizer dfConnector = new IntentRecognizer();
		Scanner s = new Scanner(System.in);
		
		int userID = 0; //TODO: Insert your birth date in the YYYYMMDD format

		while (true) {
			//Read the text message
			System.out.print(">");
			String text = s.nextLine();
			
			if (text != null && !text.equals("")) {
				QueryResult dfResult = null;
				//Get the intent from the IntentRecognizer
				//START WRITING CODE HERE

				//END CODE
				
				String intent = dfResult.getIntent();
				System.out.println("Detected intent: " + intent);
				
				String response = dispatchIntent(userID, intent, text);
				if (response == null) {
					response = dfResult.getFulfillmentText();
				}
				
				System.out.println("Fulfillment text: " + response);
			}
		}
		
		
		
		
	}
}
