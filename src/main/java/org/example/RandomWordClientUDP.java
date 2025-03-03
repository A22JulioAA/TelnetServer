package org.example;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class RandomWordClientUDP {
    private static final int PORT = 60000;
    private static final String HOST = "localhost";

    public static void main(String[] args) {
        try (
                DatagramSocket socket = new DatagramSocket();
                Scanner scanner = new Scanner(System.in);
                ) {
            InetAddress serverAddress = InetAddress.getByName(HOST);

            while (true) {
                String userInput = scanner.nextLine();

                DatagramPacket sendPacket = new DatagramPacket(userInput.getBytes(), userInput.length(), serverAddress, PORT);
                socket.send(sendPacket);

                System.out.println(sendPacket);

                byte[] buffer = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
                socket.receive(receivePacket);

                String response = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println(response);
            }

        } catch (SocketException e) {
            throw new RuntimeException(e);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
