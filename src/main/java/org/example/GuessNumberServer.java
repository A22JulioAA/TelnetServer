package org.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GuessNumberServer {
    private static final int PORT = 60000;
    private static final int MAX_THREADS = 5;
    private static final Logger logger = Logger.getLogger(GuessNumberServer.class.getName());

    public static void main(String[] args) {
        logger.setLevel(Level.INFO);

        ExecutorService threadPool = Executors.newFixedThreadPool(MAX_THREADS);

        try (
                ServerSocket listener = new ServerSocket(PORT);
                ) {
            System.out.println("Guess Game server running...");

            while (true) {
                Socket clientSocket = listener.accept();

                threadPool.execute(new GuessNumberClientHandler(clientSocket));
            }

        } catch (IOException e) {
            logger.severe("Something went wrong...");
        }
    }
}
