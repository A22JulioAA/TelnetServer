package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class GuessNumberClient {
    private static final int PORT = 60000;
    private static final String HOST = "localhost";

    public static void main(String[] args) {
        try (
                var socket = new Socket(HOST, PORT);
                var scanner = new Scanner(System.in);
                var in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                var out = new PrintWriter(socket.getOutputStream(), true);
                ) {
            System.out.println(in.readLine());

            String command;
            while (true) {
                command = scanner.nextLine();
                out.println(command);

                String response = in.readLine();

                if (response == null) {
                    return;
                }

                if ("QUIT".equalsIgnoreCase(response)) {
                    return;
                }
            }

        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
