package krainyk.hardware.emulation;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class DisplayEmulator extends JFrame {

    private JPanel mainPanel;
    private JLabel initStatusLabel;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private DisplayComponent displayComponent = new DisplayComponent();

    public DisplayEmulator() throws IOException {
        this.setContentPane(mainPanel);
        this.setPreferredSize(new Dimension(300, 300));
        mainPanel.add(displayComponent);
        mainPanel.setPreferredSize(new Dimension(300, 300));
        serverSocket = new ServerSocket(55555);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int available;
                    clientSocket = serverSocket.accept();
                    InputStream inputStream = clientSocket.getInputStream();
                    OutputStream outputStream = clientSocket.getOutputStream();
                    byte[] buffer = new byte[1500];
                    while (clientSocket.isConnected()) {
                        if ((available=inputStream.available()) > 0) {
                            inputStream.read(buffer, 0, available);
                            System.out.println("Received " + available + " bytes");
                            // send information to the screen emulator component so it can apply necessary activities
                            displayComponent.receiveData(buffer, available);
                            //displayComponent.clearImage(0);
                            mainPanel.repaint();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void main(String[] args) throws IOException {
        JFrame frame = new DisplayEmulator();
        //frame.setContentPane(new DisplayEmulator().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
