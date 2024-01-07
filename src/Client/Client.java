package Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client extends JFrame {
    BufferedReader br; // Receiving the data
    PrintWriter out; // Sending the data
    Socket socket;

    /** Declare Components **/
    private JLabel heading = new JLabel("Client Area");
    private JTextArea messageArea = new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new Font("Roboto",Font.PLAIN,20);


    // Constructor
    public Client() {
        try {
            System.out.println("Sending Request to Server");
            socket = new Socket("localhost", 7777);
            System.out.println("Connection established");

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

            createGUI();
            handleEvents();

            startReading();
        //    startWriting();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Method to handle the Events **/
    private void handleEvents() {
        messageInput.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {

               // System.out.println("Key Released " +e.getKeyCode());
                if (e.getKeyCode()==10){
                   // System.out.println("You Pressed enter Button");
                    String contentToSend = messageInput.getText();
                    messageArea.append("Me : " +contentToSend + "\n");
                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();
                }
            }
        });
    }

    private void createGUI(){
        /** GUI Code **/

      this.setTitle("Client Messenger End");
      this.setSize(500,500);
      this.setLocationRelativeTo(null);
      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // frame ka layout
        this.setLayout(new BorderLayout());

        // adding the components to frame
        this.add(heading,BorderLayout.NORTH);
        JScrollPane jScrollPane = new JScrollPane(messageArea);
        this.add(jScrollPane,BorderLayout.CENTER);
        this.add(messageInput,BorderLayout.SOUTH);


        this.setVisible(true);

      // Coding for components

        heading.setSize(1366,565);
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);

        heading.setIcon(new ImageIcon(getClass().getResource("icons8-chat.gif")));

        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
      //  heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        messageArea.setEditable(false);
        heading.setBackground(Color.red);




    }

    /** Method to Receive the data **/
    public void startReading() {

        // Thread --> read karke deta rahega

        Runnable r1 = () -> {   // Creating thread using lambda

            System.out.println("Reader started..");
            try {
            while (true) {
                    String msg;// = br.readLine();
                    msg = br.readLine();

                    if (msg.equals("exit")) {
                        System.out.println("Server terminated the chat");
                        JOptionPane.showMessageDialog(this,"Server terminated the chat");
                        messageInput.setEnabled(false);
                        break;
                    }
                    System.out.println("Server : " + msg);
           //     messageArea.append("Server : " + msg + "\n");
                messageArea.append("Server : " + msg + "\n"); // Add the emoji to the message
              }
            }catch (Exception e) {
             //   e.printStackTrace();
                System.out.println("Connection is Closed!!!");

            }
        };
        new Thread(r1).start();
    }

    /** Method to send the data **/
        public void startWriting () {

            // Thread --> data user lega and send karega client tak

            Runnable r2 = () -> {   // Creating thread using lambda

                System.out.println("Writer Started...");
                try {

                while (!socket.isClosed() ) {
                        BufferedReader br1 = new BufferedReader(new InputStreamReader((System.in)));
                        String content = br1.readLine();
                        out.println(content);
                        out.flush();

                    if (content.equals("exit")){
                        socket.close();
                        break;
                    }

                }
                //    System.out.println("Connection is Closed!!!");

                } catch (Exception e) {
                   // e.printStackTrace();
                    System.out.println("Connection is Closed!!!");
                }

            };
            new Thread(r2).start();
        }
        public static void main (String[]args){
            System.out.println("This is client");
            new Client();


        }
}
