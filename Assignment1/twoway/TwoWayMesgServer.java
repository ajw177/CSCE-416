/**
 * Implementation of a two-way message server in java
 * @author Alec Williams
 */

// Package for I/O related stuff
import java.io.*;

// Package for socket related stuff
import java.net.*;

/*
 * This class does all the server's job
 * It receives the connection from client
 * and prints messages sent from the client
 * Then it accepts input from the server user and sends it back to the client
 */
public class TwoWayMesgServer {

    /*
     * The server program starts from here
     */
    public static void main(String args[]) {

        // Server needs the port number to listen on and a name for itself
        if (args.length != 2) {
            System.out.println("usage: java TwoWayMesgServer <port> <server name>");
            System.exit(1);
        }

        // Get the port on which server should listen and the server name
        int serverPort = Integer.parseInt(args[0]);
        String serverName = args[1];

        // Be prepared to catch socket related exceptions
        try {
            // Create a server socket with the given port
            ServerSocket serverSock = new ServerSocket(serverPort);
            System.out.println("Waiting for a client ...");

            // Wait to receive a connection request
            Socket clientSock = serverSock.accept();

            InetSocketAddress remote =
                (InetSocketAddress) clientSock.getRemoteSocketAddress();

            System.out.println("Connected to a client at ('" +
                remote.getAddress().getHostAddress() + "', '" +
                remote.getPort() + "')");

            // No other clients, close the server socket
            serverSock.close();

            // Prepare to read from client
            BufferedReader fromClientReader =
                new BufferedReader(new InputStreamReader(clientSock.getInputStream()));

            // Prepare to write to client with auto flush on
            PrintWriter toClientWriter =
                new PrintWriter(clientSock.getOutputStream(), true);

            // Prepare to read from server keyboard (server user's input)
            BufferedReader fromUserReader =
                new BufferedReader(new InputStreamReader(System.in));

            // Keep serving the client
            while (true) {
                // Read a message from the client
                String message = fromClientReader.readLine();

                // If we get null, it means client sent EOF / closed connection
                if (message == null) {
                    System.out.println("Client closed connection");
                    break;
                }

                // Display the message
                System.out.println(message);

                // Read a reply from the server user and send it back
                String reply = fromUserReader.readLine();

                // If server user hits EOF, still send an empty reply and continue/exit
                if (reply == null) {
                    reply = "";
                }

                toClientWriter.println(serverName + ": " + reply);
            }

            // Close socket and exit
            toClientWriter.close();
            clientSock.close();

        } catch (Exception e) {
            // Print the exception message
            System.out.println(e);
        }
    }
}