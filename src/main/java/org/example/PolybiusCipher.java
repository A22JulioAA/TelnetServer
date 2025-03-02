package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class PolybiusCipher {
    private static final int PORT = 60000;
    private static final char[][] POLYBIUS_SQUARE = {
            {'A', 'B', 'C', 'D', 'E', 'F'},
            {'G', 'H', 'I', 'K', 'L', 'M'},
            {'N', 'O', 'P', 'Q', 'R', 'S'},
            {'T', 'U', 'V', 'W', 'X', 'Y'},
            {'Z', '0', '1', '2', '3', '4'},
            {'5', '6', '7', '8', '9', '.'}
    };

    private static final Map<Character, String> polybiusMap = new HashMap<>();

    static {
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                polybiusMap.put(POLYBIUS_SQUARE[row][col], (row + 1) + "" + (col + 1));
            }
        }
        polybiusMap.put('J', "24");
    }

    public static void main(String[] args) throws IOException {
        try (
                var listener = new ServerSocket(PORT);
                ) {
            System.out.println("Polybius Cipher server is running...");

            while (true) {
                try (
                        var socket = listener.accept();
                        var out = new PrintWriter(socket.getOutputStream(), true);
                        var in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        ) {

                    out.println("Write a message to encrypt: ");

                    String messageToEncrypt = in.readLine();
                    String encryptedMessage = encryptMessage(messageToEncrypt);

                    out.println(encryptedMessage);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private static String encryptMessage (String message) {
        System.out.println("Message to encrypt: " + message);
        System.out.println("Encrypting...");

        message = message.toUpperCase().replace("J", "I");
        StringBuilder encryptedText = new StringBuilder();

        for (char c : message.toCharArray()) {
            if (Character.isLetter(c)) {
                encryptedText.append(polybiusMap.get(c)).append(" ");
            }
        }

        System.out.println("Encrypted message: " + encryptedText);
        return encryptedText.toString().trim();

    }
}
