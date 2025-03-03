package org.example;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RandomWordServer {
    private static final int PORT = 60000;
    private static final int MAX_THREADS = 5;

    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newFixedThreadPool(MAX_THREADS);

        try (
                DatagramSocket socket = new DatagramSocket(PORT);
                ) {
            System.out.println("Random Word Server running...");

            while (true) {
                byte[] buffer = new byte[1024];

                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

                socket.receive(packet);

                threadPool.execute(new RandomWordClientHandler(socket, packet));
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
