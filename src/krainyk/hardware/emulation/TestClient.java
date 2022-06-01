package krainyk.hardware.emulation;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class TestClient {
    public static void main(String[] args) throws IOException {
        Socket displayConnection = new Socket();
        SocketAddress address = new InetSocketAddress("localhost", 55555);
        displayConnection.connect(address);
        byte[] data = new byte[128*2];
        if (displayConnection.isConnected()) {
            OutputStream outputStream = displayConnection.getOutputStream();
            //send 1 pixel value as 2 integers
            //outputStream.write(0);
            //outputStream.write(0);
            // send 1 line as an array
            outputStream.write(data);
            outputStream.flush();
            outputStream.close();
        }
        displayConnection.close();
    }
}
