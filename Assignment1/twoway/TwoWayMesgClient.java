/**
 * Implementation of the two-way message client in java
 * @author Alec Williams
 */

// Package for I/O related stuff
import java.io.*;

// Package for socket related stuff
import java.net.*;

/*
 * This class does all the client's job
 * It connects to the server at the given address
 * and sends messages typed by the user to the server
 * Then it waits for and prints the reply sent back by the server
 */
public class TwoWayMesgClient {

    /*
     * The client program starts from here
     */
    public static void main(String args[]) {

        // Client needs server's contact information and a name for itself
        if (args.length != 3) {
            System.out.println("usage: java TwoWayMesgClient <server name> <server port> <client name>");
            System.exit(1);
        }

        // Get server's whereabouts and client name
        String serverName = args[0];
        int serverPort = Integer.parseInt(args[1]);
        String clientName = args[2];

        // Be prepared to catch socket related exceptions
        try {
            // Connect to the server at the given host and port
            Socket sock = new Socket(serverName, serverPort);
            System.out.println("Connected to server at ('" + serverName + "', '" + serverPort + "')");

            // Prepare to write to server with auto flush on
            PrintWriter toServerWriter =
                new PrintWriter(sock.getOutputStream(), true);

            // Prepare to read from server
            BufferedReader fromServerReader =
                new BufferedReader(new InputStreamReader(sock.getInputStream()));

            // Prepare to read from keyboard
            BufferedReader fromUserReader =
                new BufferedReader(new InputStreamReader(System.in));

            // Keep doing till we get EOF from user
            while (true) {
                // Read a line from the keyboard
                String line = fromUserReader.readLine();

                // If we get null, it means user is done (EOF)
                if (line == null) {
                    System.out.println("Closing connection");
                    break;
                }

                // Send the line to the server, include client name
                toServerWriter.println(clientName + ": " + line);

                // Wait for server's reply and print it
                String reply = fromServerReader.readLine();

                // If reply is null, server closed connection
                if (reply == null) {
                    System.out.println("Server closed connection");
                    break;
                }

                System.out.println(reply);
            }

            // close the socket and exit
            toServerWriter.close();
            sock.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}