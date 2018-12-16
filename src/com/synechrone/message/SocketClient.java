package com.synechrone.message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketClient {

    /**
     *
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        startClient();
    }

    public static void startClient() throws InterruptedException {
        try(Socket socket = new Socket("localhost", 3534);
            BufferedReader br =new BufferedReader(new InputStreamReader(System.in));
            //DataOutputStream oos = new DataOutputStream(socket.getOutputStream());
            OutputStreamWriter oos = new OutputStreamWriter(socket.getOutputStream());
            BufferedReader ois =  new BufferedReader(new InputStreamReader(socket.getInputStream())); )
        {

            System.out.println("Client connected to socket.");
            System.out.println();
            System.out.println("Client writing channel = oos & reading channel = ois initialized.");

            while(!socket.isOutputShutdown()){

                if(br.ready()){

                    System.out.println("Client start writing in channel...");
                    Thread.sleep(1000);
                    String clientCommand = br.readLine() + "\n";

                    oos.write(clientCommand);
                    oos.flush();
                    System.out.println("Clien sent message " + clientCommand + " to server.");
                    Thread.sleep(1000);
                    if(clientCommand.equalsIgnoreCase("quit")){

                        System.out.println("Client kill connections");
                        Thread.sleep(2000);

                        if(ois.read() > -1)     {
                            System.out.println("reading...");
                            String in = ois.readLine();
                            System.out.println(in);
                        }

                        break;
                    }

                    System.out.println("Client sent message & start waiting for data from server...");
                    Thread.sleep(2000);

                    if(ois.read() > -1)     {

                        System.out.println("reading...");
                        String in = ois.readLine();
                        System.out.println(in);
                    }
                }
            }
            System.out.println("Closing connections & channels on clentSide - DONE.");

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}