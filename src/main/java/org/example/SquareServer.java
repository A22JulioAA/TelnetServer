package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SquareServer {
    private static final int PORT = 60000;

    public static void main(String[] args) {
        try (
                var listener =  new ServerSocket(PORT);
                ) {
            System.out.println("Square server is running...");

            while (true) {
                try (
                        Socket socket = listener.accept();
                        var out = new PrintWriter(socket.getOutputStream(), true);
                        var in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                ) {
                    out.println("Welcome to Square server!");
                    out.println("Enter a number to calculate the square: ");
                    String numberString = in.readLine();

                    if (numberString != null) {
                        try {
                            int number = Integer.parseInt(numberString);
                            int squared = number * number;

                            System.out.println("Received: " + number);
                            out.println("The square of " + number + " is: " + squared);
                        } catch (NumberFormatException e) {
                            out.println("Invalid input. Please enter a valid number.");
                        }
                    }
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
