package project.pkg7.pkgfinal.copy;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * Server Class runs and keep running the server. Always looking for new
 * clients. Also the server makes each client its own thread.
 *
 * @author harmanjeetdhillon
 */
public class ChatServer
{

   // private static String username = "X"; // user that sent the message
    private static HashMap<String, Socket> chatData; // Stores users, with there sockets

    public static void main(String[] args)
    {
        chatData = new HashMap<String, Socket>();

        try
        {
            ServerSocket server = new ServerSocket(9999); // new socket

            // never ending while loop thats always looking for new clients
            // And also once a client is connect the client theard starts 
            while (true)
            {
                System.out.println("While loop For Server ----------------------------------");
                Socket client = server.accept();
                System.out.println("Server is up And Running");
                ClientThread t = new ClientThread(client); // new client
                t.start();
            }

        } catch (Exception c)
        {
            c.printStackTrace();
        }
    }

    /*
    * This class is a thread for the client. Also sends and recieves messages form
    * client to client, either single to single of as many as the user likes.
     */
    public static class ClientThread extends Thread
    {
        private String username = "X"; // user that sent the message
        private final Socket client; // user that connect socket
        private final Scanner fromClient; // used to send back
        private String message; // message to be sent
        private String rUserName; // Reciepient
        private String usersToSend; // All Reciepients

        public ClientThread(Socket c) throws IOException
        {
            client = c;
            fromClient = new Scanner(client.getInputStream());
        }

        @Override
        public void run()
        {
            System.out.println("Running Thread");
            try
            {

                while (true)
                {
                    //Making sure someone is logged on before sending a update
                    if (!username.equals("X"))
                    {
                        update();// sending a update of whos online
                    }
                    message = "";
                    message = fromClient.nextLine();//Getting the message
                    //Getting the user name from the message
                    StringTokenizer USERtokenizer = new StringTokenizer(message, "%");
                    username = USERtokenizer.nextToken();
                    System.out.println("UN : " + username); // Testing
                    chatData.put(username, client);
                    //Getting the rest of the message
                    message = USERtokenizer.nextToken();
                    // Getting just the text from the user
                    StringTokenizer tokenizer = new StringTokenizer(message, "*");
                    message = tokenizer.nextToken();
                    System.out.println("Message --------> " + message); // Testing
                    //Getting all users to send to
                    usersToSend = tokenizer.nextToken();
                    System.out.println("While 1"); // Testing
                    System.out.println("usersToSend ---------> " + usersToSend); // testing
                    // getting each Reciepient
                    StringTokenizer uTon = new StringTokenizer(usersToSend, "/");

                    //Reciepient Storage
                    ArrayList usersToSendTo = new ArrayList();
                    while (uTon.hasMoreTokens())
                    {
                        rUserName = uTon.nextToken();
                        System.out.println("Recep " + rUserName);
                        usersToSendTo.add(rUserName);
                    }

                    // Sending to each Reciepient
                    for (int i = 0; i < usersToSendTo.size(); i++)
                    {
                        System.out.println(usersToSendTo.get(i));
                        Socket theOtherPerson = chatData.get(usersToSendTo.get(i));
                        PrintWriter toClientR = new PrintWriter(theOtherPerson.getOutputStream(), true);
                        //Sending the message to the users
                        toClientR.println(username + "->" + message);
                        System.out.println("Message sent");
                        update();// sending a update of whos online
                    }
                    
                    
                }
            } catch (NoSuchElementException e)
            {
                System.out.println("Disconnected DUE TO :::> No Such Element Exception");
                chatData.remove(username); // removing the user after they have closed out of the app
                update(); // sending a update of whos online
            } catch (IOException ex)
            {
                System.out.println("Disconnected DUE TO :::> IO Exception");
            }
        }

    }

    /**
     * Method sends a update of all online users to all the users connected to 
     * the server. The update is sent as a string contain everyone online.
     */
    public static void update()
    {
        String us = ""; // list of all users online message
        {
            try
            {
                String rUsers = chatData.keySet().toString();
                System.out.println("rUsers: " + rUsers);
                System.out.println(rUsers);
                ArrayList<String> rAL = new ArrayList<String>();
                //Getting rid of the brackets in the set recived from the hashMap
                int ls = rUsers.length();
                us = rUsers.substring(1, ls - 1);
                StringTokenizer uTon = new StringTokenizer(us, ", ");
                // Tokenizing the users online into a arrayList
                while (uTon.hasMoreTokens())
                {
                    String singleUser = "EMPTY";
                    singleUser = uTon.nextToken();
                    rAL.add(singleUser);
                }
                System.out.println("********************" + rAL.toString());

                // Sending the update to each person in the arrayList
                for (int i = 0; i < rAL.size(); i++)
                {
                    System.out.println("SIZE: " + rAL.size());
                    Socket theOtherPerson = chatData.get(rAL.get(i));
                    PrintWriter toClientR = new PrintWriter(theOtherPerson.getOutputStream(), true);
                    //Updating everyone online
                    String allUsers = chatData.keySet().toString();
                    toClientR.println("!" + allUsers);
                }

            } catch (IOException ex)
            {
                System.out.println("IOEXCEPTION");
                ex.printStackTrace();
            }
        }
    }
}
