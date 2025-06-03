package chatappgui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class ChatClientGUI {
    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 12345;
    private static PrintWriter out;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Chat Client");
        JTextArea chatArea = new JTextArea(20, 50);
        chatArea.setEditable(false);
        JTextField inputField = new JTextField(40);
        JButton sendButton = new JButton("Send");

        JPanel panel = new JPanel();
        panel.add(inputField);
        panel.add(sendButton);

        frame.getContentPane().add(new JScrollPane(chatArea), BorderLayout.CENTER);
        frame.getContentPane().add(panel, BorderLayout.SOUTH);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        try {
            Socket socket = new Socket(SERVER_IP, SERVER_PORT);
            out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            sendButton.addActionListener(e -> {
                String message = inputField.getText();
                if (!message.isEmpty()) {
                    out.println(message);
                    inputField.setText("");
                }
            });

            inputField.addActionListener(e -> {
                sendButton.doClick();
            });

            new Thread(() -> {
                try {
                    String msg;
                    while ((msg = in.readLine()) != null) {
                        chatArea.append(msg + "\n");
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
