package com.javatechie;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Map;

@SpringBootApplication
@RestController
public class ChatgptBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChatgptBotApplication.class, args);
	}
	/*
	 *
	 *  CHATBOT
	 *
	 * */
	@PostMapping("/chat")
	public String chatWithBot(@RequestBody Map<String, String> requestBody) throws JSONException {
		// Set up your OpenAI API key
		String apiKey = "YOUR API KEY";

		// Set up your API endpoint for the chatbot
		String apiUrl = "https://api.openai.com/v1/chat/completions";

		// Set up your request headers
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + apiKey);
		headers.setContentType(MediaType.APPLICATION_JSON);

		// Set up your request body
		JSONObject requestJson = new JSONObject();
		requestJson.put("model", "gpt-4");
		JSONArray messages = new JSONArray();
		messages.put(new JSONObject().put("role", "user").put("content", requestBody.get("message")));
		requestJson.put("messages", messages);

		// Create the HTTP entity with headers and body
		HttpEntity<String> requestEntity = new HttpEntity<>(requestJson.toString(), headers);

		// Send the POST request to the API
		ResponseEntity<String> response = new RestTemplate().postForEntity(apiUrl, requestEntity, String.class);

		// Extract and return the chatbot's reply from the response
		String botReply = extractChatbotReplyFromResponse(response.getBody());
		return botReply;
	}

	// Method to extract the chatbot's reply from the API response
	private String extractChatbotReplyFromResponse(String responseBody) {
		try {
			// Parse the JSON object
			JSONObject jsonObject = new JSONObject(responseBody);

			// Get the "choices" array from the object
			JSONArray choicesArray = jsonObject.getJSONArray("choices");

			// Get the first object from the array
			JSONObject firstObject = choicesArray.getJSONObject(0);

			// Extract the "message" object from the first choice
			JSONObject messageObject = firstObject.getJSONObject("message");

			// Extract the "content" from the message object
			return messageObject.getString("content");
		} catch (JSONException e) {
			// Log the error or handle it appropriately
			e.printStackTrace();
			return null; // Return null or an appropriate default value
		}
	}


	/*
	 *
	 *  TEXT TO IMAGE
	 *
	 * */
	@PostMapping("/generateImage")
	public String generateImage(@RequestBody Map<String, String> requestBody) {
		// Set up your OpenAI API key
		String apiKey = "YOUR API KEY";

		// Set up your API endpoint for image generation
		String apiUrl = "https://api.openai.com/v1/images/generations";


		// Set up your request headers
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + apiKey);
		headers.setContentType(MediaType.APPLICATION_JSON);

		// Create the HTTP entity with headers and body
		HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

		// Send the POST request to the API
		ResponseEntity<String> response = new RestTemplate().postForEntity(apiUrl, requestEntity, String.class);

		// Extract and return the generated image URL from the response
		String imageUrl = extractImageUrlFromResponse(response.getBody());
		return imageUrl;
	}

	// Method to extract the image URL from the API response
	private String extractImageUrlFromResponse(String responseBody) {
		try {
			// Parse the JSON object
			JSONObject jsonObject = new JSONObject(responseBody);

			// Get the "data" array from the object
			JSONArray dataArray = jsonObject.getJSONArray("data");

			// Get the first object from the array
			JSONObject firstObject = dataArray.getJSONObject(0);

			// Extract the URL from the object
			return firstObject.getString("url");
		} catch (JSONException e) {
			// Log the error or handle it appropriately
			e.printStackTrace();
			return null; // Return null or an appropriate default value
		}
	}


	/*
	 *
	 *  TEXT TO SPEECH
	 *
	 * */

	@PostMapping("/generateAudio")
	public byte[] generateAudio(@RequestBody Map<String, Object> requestBody) {
		// Set up your OpenAI API key
		String apiKey = "YOUR API KEY";

		// Set up your API endpoint for audio generation
		String apiUrl = "https://api.openai.com/v1/audio/speech";

		// Set up your request headers
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + apiKey);
		headers.setContentType(MediaType.APPLICATION_JSON);

		// Add any additional parameters to the request body
//        requestBody.put("model", "tts-1"); // Choose your desired model
//        requestBody.put("voice", "nova"); // Choose your desired voice
//        requestBody.put("response_format", "mp3"); // Choose your desired audio format
//        requestBody.put("speed", 1.0); // Set the speed of the generated audio

		// Create the HTTP entity with headers and body
		HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

		// Send the POST request to the API
		ResponseEntity<byte[]> response = new RestTemplate().postForEntity(apiUrl, requestEntity, byte[].class);

		// Return the audio file content
		return response.getBody();
	}


	/*
	 *
	 *  SPEECH TO TEXT
	 *
	 *
	 * */

	@PostMapping("/transcribe")
	public String transcribeAudio(@RequestParam("file") MultipartFile file) throws IOException {
		// Set up your OpenAI API key
		String apiKey = "YOUR API KEY";

		// Set up your API endpoint for speech-to-text transcription
		String apiUrl = "https://api.openai.com/v1/audio/transcriptions";

		// Set up request headers
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + apiKey);
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);

		// Set up request body
		MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
		requestBody.add("file", file.getResource());
		requestBody.add("model", "whisper-1");
		requestBody.add("language", "en");

		// Create the HTTP entity with headers and body
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

		// Send the POST request to the API
		ResponseEntity<String> response = new RestTemplate().postForEntity(apiUrl, requestEntity, String.class);

		// Return the transcription result
		return response.getBody();
	}

}