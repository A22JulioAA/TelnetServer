package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SquareServer {
    private static final int PORT = 60000;
    private static final int MAX_THREADS = 10;

    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newFixedThreadPool(MAX_THREADS);

        try (
                var listener =  new ServerSocket(PORT);
                ) {
            System.out.println("Square server is running...");

            while (true) {
                Socket clientSocket = listener.accept();
                threadPool.execute(new SquareClientHandler(clientSocket));
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            threadPool.shutdown();
        }
    }
}
