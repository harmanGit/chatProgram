package project.pkg7.pkgfinal.copy;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.StringTokenizer;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * The UI Class, Creates UI for users to use for the chat client.
 *
 * @author harmanjeetdhillon
 */
public class UI
{

    private Stage mainStage = null; // mainStage to add all scenes
    private Scene sceneBase = null; // base scene
    private Scene newLoginScene = null; // new user login scene
    private Scene loginScene = null; // login scene
    private Scene chatScene = null; // chat scene
    private String userNameL; // username for the client
    private String passwordL; // username for the client
    private Socket clientSocket = null; // connected to the server
    private boolean canSend; // checks if a message can be sent
    private HashMap<String, ArrayList> dataBank; // where all user info is stored
    private PrintWriter toClient; // sending the message to the server
    private String returnMessage; // message recieved from the server
    private String displayMessage; // message to display
    private String usersonline; // users online
    private String userSelect; // users to send the message to
    private TextArea ta; // the chat text area
    private TextArea taUser; // the online users text area
    private String Message; // message to send
    private ArrayList onlineList; // arraylist of people online
    private InputThread it; // the input thread

    UI(Stage primaryStage)
    {
        canSend = false;
        dataBank = new HashMap<String, ArrayList>();
        onlineList = new ArrayList<String>();
        mainStage = primaryStage;
        loginScene = new Scene(LoginPane(), 600, 600); // login scene
        newLoginScene = new Scene(NewLoginPane(), 600, 600);// new login in scene
        chatScene = new Scene(ChatPane(), 900, 600); // chat scene
        sceneBase = new Scene(BasePane(), 600, 600); // sceneBase

    }

    /**
     * Private method adds all user entered information into the hashMap.
     *
     * @param n String name
     * @param UN String username
     * @param p String password
     * @param ip String ip address
     * @param pt String port number
     * @return true if the user was added, and false if the user was not added
     */
    private boolean addUser(String n, String UN, String p, String ip, String pt)
    {
        // Places all information other than the username into a arraylist,
        // to be placed into the databank hash map
        ArrayList inputData = new ArrayList();
        inputData.add(n);
        inputData.add(p);
        inputData.add(ip);
        inputData.add(pt);
        System.out.println(inputData);

        // Inserting into databank, which stores all user info
        if (dataBank.get(UN) == null)
        {
            // Username is the key, the arraylist is the value
            dataBank.put(UN, inputData); // Adds info to the hashMap
            try
            {
                clientSocket = new Socket(ip, (Integer.valueOf(pt)));
                System.out.println("ClientSocket");
                // Creating the OutputStream(), to the server
                toClient = new PrintWriter(clientSocket.getOutputStream(), true);
                it = new InputThread(clientSocket); // creates the input thread
            } catch (IOException ex)
            {
                ex.printStackTrace();
            }
            return true;
        }
        return false;
    }

    /**
     * Creates the Base pane, with a login and new login button, that lead to
     * other login in pane or new login pane
     *
     * @return
     */
    private Pane BasePane()
    {
        StackPane sPane = new StackPane(); // Base pane to add everything to

        // Getting the image to use as the background
        ImageView i = new ImageView(new Image("file:Irislux Logo 1.0.jpg"));
        i.setPreserveRatio(true);

        // Creates a login and new login button 
        Button newLogin = new Button("New Login");
        newLogin.setStyle("-fx-font: 15 Courier; -fx-base: #20B2AA;");
        newLogin.setTranslateX(147);
        newLogin.setTranslateY(250);
        Button login = new Button("Login");
        login.setStyle("-fx-font: 15 Courier; -fx-base: #20B2AA;");
        login.setTranslateX(-115);
        login.setTranslateY(250);

        // Login button pressed expression
        login.setOnAction((ActionEvent event) ->
        {
            // When button is pressed it moves to the next login pane
            mainStage.setScene(loginScene);
        });

        // New Login button pressed expression
        newLogin.setOnAction((ActionEvent event) ->
        {
            // When button is pressed it moves to the next new login pane
            mainStage.setScene(newLoginScene);
        });

        //adds everything to the pane
        sPane.setAlignment(i, Pos.CENTER);
        sPane.getChildren().add(i);
        sPane.getChildren().add(newLogin);
        sPane.getChildren().add(login);
        return sPane;
    }

    /**
     * Creates the new login pane, that allows users to enter info and create a
     * user.
     *
     * @return a Pane, the new login in pane
     */
    private Pane NewLoginPane()
    {
        // Base pane
        StackPane sPane = new StackPane();
        Color orange = Color.rgb(235, 127, 13); // color for the text
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.BOTTOM_CENTER);// adding the gridpane to the bottom
        // Added gaps in between slots
        gridPane.setHgap(5);
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.setVgap(5);

        // Background image
        ImageView i = new ImageView(new Image("file:2015-10-05 09.59.25 1.jpg", 600, 600, false, false));
        i.setPreserveRatio(true);

        // save button
        Button save = new Button("Save");
        save.setStyle("-fx-font: 16 Courier; -fx-base: #EBB00D;");
        // back button
        Button back = new Button("Back");
        back.setStyle("-fx-font: 16 Courier; -fx-base: #EBB00D;");

        // labels
        Label name = new Label("Legal Name");
        name.setStyle("-fx-font: 15 Courier;");
        name.setTextFill(orange);

        Label user = new Label("User Name");
        user.setStyle("-fx-font: 15 Courier;");
        user.setTextFill(orange);

        Label password = new Label("Password");
        password.setStyle("-fx-font: 15 Courier;");
        password.setTextFill(orange);

        Label ip = new Label("IP Address");
        ip.setStyle("-fx-font: 15 Courier;");
        ip.setTextFill(orange);

        Label port = new Label("Port Number");
        port.setStyle("-fx-font: 15 Courier;");
        port.setTextFill(orange);

        Label output = new Label("Waiting");
        output.setStyle("-fx-font: 15 Courier;");
        output.setTextFill(orange);

        // Textfields to enter the user input
        TextField nameEntered = new TextField();
        TextField userEntered = new TextField();
        TextField passwordEntered = new TextField();
        TextField ipEntered = new TextField();
        TextField portEntered = new TextField();

        //adding everything to the gridpane
        gridPane.add(nameEntered, 1, 0);
        gridPane.add(userEntered, 1, 1);
        gridPane.add(passwordEntered, 1, 2);
        gridPane.add(ipEntered, 1, 3);
        gridPane.add(portEntered, 1, 4);
        gridPane.add(name, 0, 0);
        gridPane.add(user, 0, 1);
        gridPane.add(password, 0, 2);
        gridPane.add(ip, 0, 3);
        gridPane.add(port, 0, 4);
        gridPane.add(save, 0, 5);
        gridPane.add(back, 1, 5);

        // adding the the girdpane and image to the main sPane, that is returned
        sPane.setAlignment(i, Pos.CENTER);
        sPane.getChildren().add(i);
        sPane.getChildren().add(gridPane);

        // Code for the back button pressed
        back.setOnAction((ActionEvent e) ->
        {

            output.setText("Waiting");
            mainStage.setScene(sceneBase); // sets the primary stage to the base scene
        });

        // Code for the save button pressed
        save.setOnAction((ActionEvent e) ->
        {

            // getting the name from the name textField
            String n = nameEntered.getText();
            // getting the username from the username textField
            String un = userEntered.getText();
            // setting the username globe for the class
            userNameL = un;
            System.out.println("Username Client side" + un);
            // getting the password from the password textField
            String p = passwordEntered.getText();
            // setting the password globe for the class
            passwordL = p;
            // getting the ip from the ip textField
            String ipi = ipEntered.getText();
            // getting the port number from the port number textField
            String pN = portEntered.getText();

            // adding constraints to the username
            if (!dataBank.containsKey(un))
            {
                if (userNameL.matches("[a-zA-Z]+")
                        && !userNameL.contains(" ") && userNameL.length() < 15)
                {
                    addUser(n, un, p, ipi, pN);
                    dataDump();
                    mainStage.setScene(loginScene);
                } else
                {
                    gridPane.add(output, 1, 6);
                    output.setText("Invalid Username, Use only letters less then 15 Chars");
                }
            }
        });
        return sPane; // the return pane
    }

    /**
     * Creates the login pane, that allows users login and continue to the
     * chatpane
     *
     * @return
     */
    private Pane LoginPane()
    {
        StackPane sPane = new StackPane(); // main pane
        Color purple = Color.rgb(154, 123, 181); // text color
        GridPane gridPane = new GridPane(); // girdpane for buttons
        gridPane.setAlignment(Pos.BOTTOM_CENTER);
        gridPane.setHgap(5);
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.setVgap(5);

        // background image
        ImageView i = new ImageView(new Image("file:2015-09-26 06.44.18 1.jpg", 600, 600, false, false));
        i.setPreserveRatio(true);

        //buttons
        Button back = new Button("Back");
        back.setStyle("-fx-font: 15 Courier; -fx-base: #9A7BB5;");
        Button enter = new Button("Login");
        enter.setStyle("-fx-font: 15 Courier; -fx-base: #9A7BB5;");

        //Labels
        Label user = new Label("User Name");
        user.setStyle("-fx-font: 15 Courier;");
        user.setTextFill(purple);
        Label password = new Label("Password");
        password.setStyle("-fx-font: 15 Courier;");
        password.setTextFill(purple);
        Label loginL = new Label("Login In ->");
        loginL.setStyle("-fx-font: 15 Courier;");
        loginL.setTextFill(purple);

        // Text fields for user input
        TextField userEntered = new TextField();
        TextField passwordEntered = new TextField();

        // adding all the buttons, labels, textfields to the gridpane
        gridPane.add(userEntered, 2, 1);
        gridPane.add(passwordEntered, 2, 2);
        gridPane.add(user, 1, 1);
        gridPane.add(password, 1, 2);
        gridPane.add(enter, 1, 3);
        gridPane.add(back, 2, 3);
        gridPane.add(loginL, 0, 1);

        // Adding the girdpane and the background image to the "sPane" the main pane
        sPane.setAlignment(i, Pos.CENTER);
        sPane.getChildren().add(i);
        sPane.getChildren().add(gridPane);

        // Code for the back button pressed
        back.setOnAction((ActionEvent e) ->
        {
            loginL.setText("Login In ->");
            //going back to the base scene
            mainStage.setScene(sceneBase);
        });

        // Code for the enter button pressed
        enter.setOnAction((ActionEvent e) ->
        {
            // getting the username fron the  username textfield
            String un = userEntered.getText();
            // getting the passowrd from the password textfield
            String p = passwordEntered.getText();

            // checking if the username and password match before continuing 
            if (dataBank.containsKey(un) && p.equals(passwordL))
            {
                System.out.println("Login pressed");
                mainStage.setScene(chatScene); // moving to the next scene if info matches
                // sending test message being sent
                toClient.println(userNameL + "%" + "CONNECT TO THE LUX SERVER*" + userNameL + "/");
                // starts the input thread so it starts looking for data from the server
                it.start();
            } else
            {
                loginL.setText("Incorrect Login, Try Again!");
            }
        });
        return sPane;

    }

    /**
     * Creates the chat pane, that allows users chat with one another
     *
     * @return
     */
    private Pane ChatPane()
    {
        Message = ""; // initializing the globe var Message
        userSelect = ""; // initializing the globe var userSelect
        // base pane for the buttons and textfields
        GridPane gPane = new GridPane();
        gPane.setAlignment(Pos.BOTTOM_CENTER);
        gPane.setHgap(5);
        gPane.setVgap(5);
        gPane.setHgap(5);
        gPane.setVgap(5);

        // Main pane that everything is added to, including the textfield
        StackPane sPane = new StackPane();

        // Background image
        ImageView i = new ImageView(new Image("file:IMG_2740.JPG", 900, 600, false, false));
        i.setPreserveRatio(true);

        // Textfield
        TextField userEntered = new TextField();
        userEntered.setStyle("-fx-font: 12 Courier; -fx-text-fill: #DF7DCC;");
        TextField toSend = new TextField();
        toSend.setStyle("-fx-font: 12 Courier; -fx-text-fill: #DF7DCC;");

        // Labels
        Label online = new Label("Online Users");

        // Buttons
        Button send = new Button("Send ->");
        send.setStyle("-fx-font: 15 Courier; -fx-text-fill:E5D3E1; -fx-base: #DF7DCC;");
        Button addUsers = new Button("Add Users");
        addUsers.setStyle("-fx-font: 15 Courier; -fx-text-fill:E5D3E1; -fx-base: #DF7DCC;");

        // textArea that contains the online users
        taUser = new TextArea();
        taUser.setPrefColumnCount(2);
        taUser.setPrefRowCount(10);
        taUser.setEditable(false);
        taUser.setWrapText(true);
        taUser.setPrefHeight(150);
        taUser.setPrefWidth(150);
        taUser.setMaxWidth(100);
        taUser.setMaxHeight(300);
        taUser.setStyle("-fx-font: 15 Courier; -fx-text-fill: #DF7DCC;");

        // textArea that contains the user sent message, and message recieved
        ta = new TextArea();
        ta.setPrefColumnCount(2);
        ta.setPrefRowCount(10);
        ta.setEditable(false);
        ta.setWrapText(true);
        ta.setPrefHeight(150);
        ta.setPrefWidth(150);
        ta.setMaxWidth(665);
        ta.setMaxHeight(450);
        ta.setStyle("-fx-font: 12 Courier; -fx-text-fill: #DF7DCC;");

        // Adding the the instructions to the "ta" textArea
        ta.appendText("*** 1) Enter an username first, then press the add user button***");
        ta.appendText("\n*** 2) After a username is added then type your Message,"
                + " and press send-> to send the message ***");
        ta.appendText("\n*** 3) To send to multily enter the username/username/...***");

        // Code for send button pressed
        send.setOnAction((ActionEvent e) ->
        {
            if (canSend)// candSend is set to true in the userSend method
            {
                //Sending the string to the server
                Message = userNameL + "%" + userEntered.getText() + "*" + userSelect;
                toClient.println(Message);// sending the message itself
                System.out.println("Message sent ------------------------_> from Client");
                // Adding the message to the textarea
                ta.appendText("\n" + userEntered.getText() + "\n");
            } else
            {
                ta.appendText("\n" + "Add User or Users Before Sending!" + "\n");
            }
        });

        // Code for the send button pressed
        addUsers.setOnAction((ActionEvent e) ->
        {
            taUser.clear(); // clearing people online

            // adding the current users online again, back up way to add users online
            taUser.appendText("\n" + usersonline);
            System.out.println("PEOPLE THE CLIENT THINKS ARE ONLINE : "
                    + usersonline);

            // getting the user entered reciepients from the textField
            userSelect = toSend.getText();
            // Using the userSend method to see if the person is online, 
            // before sending the message to the person
            userSend(userSelect);
        });

        // Adding textfeilds and buttons to the gridpane
        gPane.add(send, 0, 1);
        gPane.add(addUsers, 0, 2);
        gPane.add(userEntered, 1, 1);
        gPane.add(toSend, 1, 2);

        // Adding everything to the pane, the pane that is returned
        sPane.setAlignment(i, Pos.CENTER);
        sPane.getChildren().add(i);
        sPane.setAlignment(ta, Pos.CENTER_RIGHT);
        sPane.setAlignment(online, Pos.BASELINE_LEFT);
        sPane.setAlignment(taUser, Pos.CENTER_LEFT);
        sPane.getChildren().add(taUser);
        sPane.getChildren().add(ta);
        sPane.setAlignment(gPane, Pos.BOTTOM_RIGHT);
        sPane.getChildren().add(gPane);

        return sPane;
    }

    /**
     * Method returns the base scene so the javaFX App has something to start from
     * @return base pane, with the login and new login buttons
     */
    public Scene basePaneUI()
    {
        return sceneBase;
    }

    /**
     * Method used to print out user data for debugging
     */
    private void dataDump()
    {
        System.out.println(dataBank.keySet());
        System.out.println(dataBank.values());
    }

    /**
     * Class is inputThread that will keep running and looking for incoming
     * messages and out update strings contain the online thread.
     */
    private class InputThread extends Thread
    {

        private Socket client; //socket connected to the server
        private Scanner fromClient; //used to recieve messages as strings

        public InputThread(Socket c) throws IOException
        {
            client = c;
            fromClient = new Scanner(client.getInputStream());

        }

        // Main code for the thread
        @Override
        public void run()
        {
            // initializing the returnMessage, which is the raw message from
            // the server
            returnMessage = ""; 
            // initializing the actual message to display to the user, recieved
            // from the Reciepient
            displayMessage = "";
            boolean dontRunOnce = false; // used to skip the if statement
                                        // the first run of the while loop
            
            System.out.println("InputThread Thread Running");
            try
            {
                System.out.println("Try");
                while (true) // while loop that keeps checking whos online
                {
                    System.out.println("First while IT");
                    returnMessage = fromClient.nextLine(); // getting the raw message
                    // Checks if the first char is a "!", if it is that means 
                    // That the server sent update of users online, not a text 
                    // message
                    if (returnMessage.charAt(0) == '!')
                    {
                        System.out.println("HEYYYY ONLINE" + returnMessage);
                        int ls = returnMessage.length();
                        // Getting rid of brackets form the set and "!"
                        usersonline = returnMessage.substring(2, ls - 1);
                        onlineList = onlineUsers(usersonline);
                        // clearing people online
                        taUser.clear();
                        // adding the current users online to the textArea
                        taUser.appendText("\n" + usersonline);

                    } else if (returnMessage.length() > 0) // if not update then 
                                                            //its a message
                    {
                        displayMessage = returnMessage;
                        if (dontRunOnce)
                        {
                            // appending the recieved message to the textArea
                            ta.appendText("\n" + displayMessage);
                            ta.appendText("\n");
                        }
                        dontRunOnce = true;
                    }
                    System.out.println("INPUT THREAD MESSAGE RECIEVED: " + displayMessage);
                }
            } catch (NoSuchElementException e)
            {
                System.out.println("InputThread : Disconnected");
            }
        }
    }

    /**
     * Method takes the string which is a list of users online and adds each user
     * to a arrayList. Then returns the arrayList
     * @param us a String which contains all online users
     * @return a array list contain online users
     */
    private ArrayList<String> onlineUsers(String us)
    {
        ArrayList<String> rAL = new ArrayList<String>();
        StringTokenizer uTon = new StringTokenizer(us, ", ");
        // adding to the arraylist while there are more user names
        while (uTon.hasMoreTokens())
        {
            String singleUser = "EMPTY";
            singleUser = uTon.nextToken();
            rAL.add(singleUser);
        }
        return rAL;
    }

    /**
     * Method checks if the users the client wants to send are online. Method can
     * handle multi users split by a "/".
     * @param send a string of users the client wants to send to
     */
    private void userSend(String send)
    {
        // Arraylist contains all users to send the message too
        ArrayList<String> returnArray = new ArrayList<String>();
        // getting all users to send too
        StringTokenizer uTon = new StringTokenizer(send, "/");
        //Reciepient Storage
        String rUserName = "";
        // while there are more users to send too, add them to the arrayList
        while (uTon.hasMoreTokens())
        {
            rUserName = uTon.nextToken();
            System.out.println("User being added " + rUserName);
            returnArray.add(rUserName);
        }
        // Checking if the people sending the message to online are in fact online
        for (int i = 0; i < returnArray.size(); i++)
        {
            for (int j = 0; j < onlineList.size(); j++)
            {
                // checks if the person is in the onlineList array
                if (onlineList.contains(returnArray.get(i)))
                {
                    canSend = true; // sets globe var to true if person is online
                                    // and it can send the message. 
                } else
                {
                    // The person is not online the it sets the canSend globe
                    // var to false, prints the client users a message and 
                    // breaks out of the method. Becasue the username entered
                    // to send the message is invalid.
                    canSend = false;
                    ta.appendText("\n" + "Reciepient Entered Incorrectly" + "\n");
                    return;
                }
            }
        }
    }
}
