package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class GuessNumberClientHandler implements Runnable {
    private final Socket clientSocket;
    private static boolean isPlaying = false;



    public GuessNumberClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (
                var in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                var out = new PrintWriter(clientSocket.getOutputStream(), true);
                ) {
            // Game server ready message.
            out.println("10 Number game server ready");

            // Read client command.
            String command;
            while ((command = in.readLine()) != null) {
                switch (command.toUpperCase()) {
                    case "NEW":
                        startGame(out);
                        break;
                    case "NUM":
                        System.out.println("Numero");
                        break;
                    case "HELP":
                        System.out.println("Ayuda");
                        break;
                    case "QUIT":
                        out.println("QUIT");
                        exitServer(clientSocket);
                        return;
                    default:
                        System.out.println("Comando no reconocido");
                        break;
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

    private static void exitServer (Socket clientSocket) {
        try {
            if (!clientSocket.isClosed()) {
                clientSocket.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void startGame (PrintWriter out) {
        if (isPlaying) {
            out.println("80 ERR");
            return;
        }

    }
}
