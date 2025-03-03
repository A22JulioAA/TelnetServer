package org.example;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.List;

public class RandomWordClientHandler implements Runnable {
    private static final String API_URL = "https://random-word-api.herokuapp.com/word?length=";
    private final DatagramSocket socket;
    private final DatagramPacket packet;
    private static final int DEFAULT_LENGHT = 5;

    public RandomWordClientHandler (DatagramSocket socket, DatagramPacket packet) {
        this.socket = socket;
        this.packet = packet;
    }

    @Override
    public void run() {
        String command = new String(packet.getData(), 0, packet.getLength()).trim();

        if (command.equalsIgnoreCase("WORD")) {
            String apiUrl = API_URL + DEFAULT_LENGHT;
            fetchWord(apiUrl);
        } else if (command.startsWith("WORD")) {
            String[] commandParts = command.split(" ");

            if (commandParts.length == 2) {
                try {
                    int length = Integer.parseInt(commandParts[1]);

                    if (length > 0) {
                        String apiUrl = API_URL + length;
                        fetchWord(apiUrl);
                    } else {
                        sendResponse("The length must be a positive number.");
                    }
                } catch (NumberFormatException e) {
                    sendResponse("Invalid command. Use: WORD or WORD <length>");
                }
            } else {
                sendResponse("The command is incorect.");
            }
        }
    }

    public void fetchWord (String apiUrl) {
        try {
            URL url = new URI(apiUrl).toURL();

            URLConnection connection = url.openConnection();
            connection.setRequestProperty("Accept", "application/json");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            reader.close();

            Gson gson = new Gson();
            List<String> words = gson.fromJson(response.toString(), List.class);

            if (words != null && !words.isEmpty()) {
                String word = words.getFirst();
                sendResponse(word);
            } else {
                sendResponse("No words found.");
            }

        } catch (URISyntaxException | IOException e) {
            sendResponse("Error fetching word: " + e.getMessage());
        }
    }

    private void sendResponse (String response) {
        try {
            byte[] responseData = response.getBytes();
            InetAddress clientAddress = packet.getAddress();
            int clientPort = packet.getPort();
            DatagramPacket responsePacket = new DatagramPacket(responseData, responseData.length, clientAddress, clientPort);
            socket.send(responsePacket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
