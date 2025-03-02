package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class SquareClient {
    private static final String HOST = "localhost";
    private static final int PORT = 60000;

    public static void main(String[] args) {
        try (
                var socket = new Socket(HOST, PORT);
                var in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                var out = new PrintWriter(socket.getOutputStream(), true);
                var scanner = new Scanner(System.in);
                ) {

            System.out.println("Connected to Square server!");

            System.out.println(in.readLine());
            System.out.println(in.readLine());

            String number = scanner.nextLine();
            out.println(number);

            System.out.println(in.readLine());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
