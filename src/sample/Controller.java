package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Controller {
    @FXML Button connectToServerButton;
    @FXML TextArea outputTextArea;
    @FXML TextField serverAddressInput;
    @FXML TextField serverPortInput;
    @FXML TextField inputText;

    //instance variables
    private String serverAddress;
    private int serverPort;

    private PrintWriter out;
    private InputStream in;

    public Controller() {
    }

    //Connecting to the server and creating a thread for incoming and
    //outgoing data.
    public void connectToServer() {
        serverAddress = serverAddressInput.getText();
        serverPort = Integer.parseInt(serverPortInput.getText());

        new Thread(() -> {
            //Trying to create a connection to the server and then creates a scanner
            //for handling all incoming data.
            try {
                Socket socket = new Socket(serverAddress, serverPort);
                in  = socket.getInputStream();
                out = new PrintWriter(socket.getOutputStream(), true);
                Scanner scanner = new Scanner(in);

                //Takes incoming data and print to text area.
                while (scanner.hasNextLine()) {
                    outputTextArea.appendText(scanner.nextLine() + "\n");
                }
                socket.close();

            //If connection were not possible do make, tell the user.
            } catch (IOException e) {
                System.out.println(e);
                outputTextArea.appendText("No server connection: \n" + e + "\n");
            }
        }).start();
    }

    //Listening for Enter press and send button pressed.
    //Take input data from text field and send it to the server.
    public void onEnter(){
        out.println(inputText.getText());
        inputText.setText("");
    }
}
