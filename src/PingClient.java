import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PingClient {

    public static void main(String[] args) throws Exception {
        DatagramSocket clientSocket = new DatagramSocket();
        clientSocket.setSoTimeout(1000); // set reply time
        String ipAddress = "";
        int port = 3333;
        InetAddress Ip = InetAddress.getByName(ipAddress);
        clientSocket.connect(Ip, port); // build connection

        for(int i = 0; i < 10; i++) {
            byte[] sendData = new byte[1024];
            Date date = new Date();
            var sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String payLoad = "PingUDP " + i + " " + sdf.format(date) + "\r\n";
            sendData = payLoad.getBytes();
            DatagramPacket packet = new DatagramPacket(sendData, sendData.length);
            try {
                clientSocket.send(packet);

                byte[] receiveData = new byte[1024];
                packet = new DatagramPacket(receiveData, receiveData.length);
                clientSocket.receive(packet); // receive packet
                String reply = new String(packet.getData(), packet.getOffset(), packet.getLength());
                System.out.println(reply);

            } catch (java.net.SocketTimeoutException e) {
                String reply = "I don't receive any reply";
                System.out.println(reply);
            }
        }
        clientSocket.close();



    }

}
