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
    @FXML
    Button connectToServerButton;

    @FXML
    TextArea outputTextArea;

    @FXML
    TextField serverAddressInput;

    @FXML
    TextField serverPortInput;

    @FXML
    TextField inputText;

    private String serverAddress;
    private int serverPort;

    private PrintWriter out;
    private InputStream in;

    public Controller() {
    }

    public void connectToServer() {

        serverAddress = serverAddressInput.getText();
        serverPort = Integer.parseInt(serverPortInput.getText());

        new Thread(() -> {
            try {
                Socket socket = new Socket(serverAddress, serverPort);
                in  = socket.getInputStream();
                out = new PrintWriter(socket.getOutputStream(), true);
                Scanner scanner = new Scanner(in);

                while (scanner.hasNextLine()) {
                    outputTextArea.appendText(scanner.nextLine() + "\n");
                }
                socket.close();
            } catch (IOException e) {
                System.out.println(e);
                outputTextArea.appendText("No server connection: \n" + e + "\n");
            }
        }).start();
    }
    public void onEnter(){
        out.println(inputText.getText());
        inputText.setText("");
    }
}
