import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Random;

public class PingServer {
    public static void main(String[] args) throws IOException {
        int port = 3333; // needed to be modified
        DatagramSocket serverSocket = new DatagramSocket(port); // bind port
        System.out.println("Server is running");
        while(true) {
           // System.out.println(1);
            byte[] data = new byte[1024]; // byte array
            DatagramPacket packet = new DatagramPacket(data, data.length);
            serverSocket.receive(packet);
           // System.out.println(packet.getAddress());
            Thread thread = new PingThread(serverSocket, packet);
            thread.start();
        }

    }
}
class PingThread extends Thread {
    DatagramSocket serverSocket;
    DatagramPacket packet;
    public PingThread(DatagramSocket serverSocket, DatagramPacket packet) {
        this.serverSocket = serverSocket;
        this.packet = packet;
    }
    @Override
    public void run() {
        int random_time = (int) (Math.random() * 1000);
        String receiveInformation = new String(packet.getData(), packet.getOffset(), packet.getLength());
        System.out.println("Reveive from IP:" + packet.getAddress() + " " + receiveInformation);

        byte[] data = new byte[1024];

        try {
            Thread.sleep(random_time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }  // stimulate the delay

        try {
            DatagramPacket sendPacket = new DatagramPacket(data, data.length);
            data = packet.getData();
            sendPacket = new DatagramPacket(data, data.length, packet.getAddress(), packet.getPort());
            serverSocket.send(sendPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}