package com.synechrone.message;

import org.json.JSONObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

public class SocketServer {

    public static final String SOCKET_PORT_PARAM = "socket.port";
//    public static final String SOCKET_SERVER_URL_PARAM = "socket.server.url";
    private static Integer serverPort;

    /**
     *
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {

        loadProperties();

        startServer(false);

    }

    public static void startServer(boolean supressMessage) {
        try (ServerSocket server = new ServerSocket(serverPort)) {
            while (true) {

                OutputStreamWriter out = null;
                BufferedReader in = null;

                System.out.print("Waiting for connection...");
                Socket client = server.accept();
                try {
                    System.out.print("Connection accepted.");

                    out = new OutputStreamWriter(client.getOutputStream());
                    System.out.println("Out stream created");

                    in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    System.out.println("In stream created");

                    while (!client.isClosed()) {
                        System.out.println("Server reading from channel");

                        String entry = in.readLine();
                        System.out.println("READ from client message - " + entry);

                        //Try to show message
                        if(!supressMessage) {
                            try {
                        /*
                         { title: "The amazing needed arcticle!", message : " Click here -> https://webengage.com/blog/web-push-notification-guide/", url : "https://webengage.com/blog/web-push-notification-guide/" }
                        */
                                JSONObject json = new JSONObject(entry);
                                String title = (String) json.get("title");
                                String message = (String) json.get("message");
                                String url = (String) json.get("url");
                                TrayIconDemo td = new TrayIconDemo();

                                td.showMessage(title, message, url);

                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }

                        System.out.println("Server try writing to channel");
                        if (entry != null && entry.equalsIgnoreCase("quit")) {
                            System.out.println("Client initialize connections suicide ...");
                            out.write("Server reply - " + entry + " - OK\n");
                            out.flush();
                            Thread.sleep(3000);
                            server.close();
                            return;
                        }

                        out.write("Server reply - " + entry + " - OK\n");
                        System.out.println("Server Wrote message to client.");
                        out.flush();
                    }
                } catch (Exception while_ex) {
                    while_ex.printStackTrace();
                } finally {
                    try {
                        if(in != null) in.close();
                        if(out != null) out.close();

                        if(client != null) client.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Closing connections & channels - DONE.");
                }
                System.out.println("Client disconnected");
                System.out.println("Closing connections & channels.");

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadProperties() {
        Properties prop = new Properties();
        InputStream input = null;

        try {

            input = SocketServer.class.getResourceAsStream("/config.properties");

            // load a properties file
            prop.load(input);

            serverPort = Integer.valueOf(prop.getProperty(SOCKET_PORT_PARAM));
//            serverURL = prop.getProperty(SOCKET_SERVER_URL_PARAM);

            System.out.println(prop.getProperty(SOCKET_PORT_PARAM));
//            System.out.println(prop.getProperty(SOCKET_SERVER_URL_PARAM));

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void setServerPort(Integer serverPort) {
        SocketServer.serverPort = serverPort;
    }
}