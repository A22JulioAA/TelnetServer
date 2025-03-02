package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SquareClientHandler implements Runnable {
    private final Socket clientSocket;

    public SquareClientHandler (Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try (
                var out = new PrintWriter(clientSocket.getOutputStream(), true);
                var in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                ) {

            out.println("Welcome to Square server!");
            out.println("Enter a number to calculate the square: ");
            String numberString;

            while ((numberString = in.readLine()) != null) {
                if (numberString.equalsIgnoreCase("exit")) {
                    out.println("Bye!");
                    break;
                }

                try {
                    int number = Integer.parseInt(numberString);
                    int squared = number * number;

                    System.out.println("Received: " + number);
                    out.println("The square of " + number + " is: " + squared);
                } catch (NumberFormatException e) {
                    out.println("Invalid input. Please enter a valid number.");
                }

            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
