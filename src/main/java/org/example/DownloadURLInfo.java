package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DownloadURLInfo {
    private static final String API_URL = "https://api.slingacademy.com/v1/sample-data/blog-posts/";
    private static final String TEMPLATE_PATH = "src/main/resources/index.html";
    private static final String OUTPUT_PATH = "src/main/article";
    private static final Logger logger = Logger.getLogger(DownloadURLInfo.class.getName());

    public static void main(String[] args) {
        logger.setLevel(Level.INFO);

        int postID = requestNumber();
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();

        try {
            URL url = new URI(API_URL + postID).toURL();

            URLConnection connection = urlConnect(url);
            connection.setRequestProperty("Accept", "application/json");

            String jsonResponse = readResponse(connection);

            if (jsonResponse == null || jsonResponse.isEmpty()) {
                logger.warning("Received an empty response.");
                return;
            }

            JsonObject response = gson.fromJson(jsonResponse, JsonObject.class);

            JsonObject blog = response.has("blog") ? response.getAsJsonObject("blog") : new JsonObject();

            String title = blog.has("title") ? blog.get("title").getAsString() : "No title";
            String image = blog.has("photo_url") ? blog.get("photo_url").getAsString() : "No photo";
            String created_at = blog.has("created_at") ? blog.get("created_at").getAsString() : "No created at";
            String user_id = blog.has("user_id") ? blog.get("user_id").getAsString() : "No user";
            String content = blog.has("content_text") ? blog.get("content_text").getAsString() : "No content";

            generateHTML(postID, title, image, content, user_id, created_at);
            System.out.println("Generated: article" + postID + ".html");

        } catch (MalformedURLException e) {
            logger.severe("URL format is invalid");
        } catch (URISyntaxException e) {
            logger.severe("URI format is invalid");
        }
    }

    private static void generateHTML (int postID, String title, String image, String content, String userId, String createdAt) {
        try {
            String template = new String(Files.readAllBytes(Paths.get(TEMPLATE_PATH)));

            template = template
                    .replace("POSTTITLE", title)
                    .replace("POSTIMAGE", "<img src=\"" + image + "\" alt=\"article\">")
                    .replace("POSTBODY", content)
                    .replace("POSTWRITTENBY", userId)
                    .replace("POSTTIME", createdAt);

            String outputFilePath = OUTPUT_PATH + postID + ".html";
            Files.write(Paths.get(outputFilePath), template.getBytes());

            logger.info("File created: " + outputFilePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static int requestNumber () {
        int postID;

        try (
                Scanner scanner = new Scanner(System.in);
        ) {
            System.out.print("Welcome! Enter a ID number to create a post: ");
            postID = scanner.nextInt();
        }

        return postID;
    }

    public static URLConnection urlConnect (URL url) {
        try {
            return url.openConnection();
        } catch (IOException e) {
            logger.severe("Something went wrong getting into the URL...");
        }

        return null;
    }

    public static String readResponse (URLConnection connection) {
        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                ) {
            StringBuilder jsonRequest = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonRequest.append(line);
            }

            return jsonRequest.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
