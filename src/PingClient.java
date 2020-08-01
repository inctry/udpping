import java.io.IOException;
import java.math.BigInteger;
import java.net.*;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;



public class PingClient {

    public static void main(String[] args) throws IOException {
       if(args.length != 2) {
            System.out.println("Input error please retry.");
            return;
       }
        DatagramSocket clientSocket = new DatagramSocket();
        clientSocket.setSoTimeout(1000); // set reply time
        String ipAddress = args[0];
       // ipAddress = "192.168.0.100";
        int port = Integer.parseInt(args[1]);
        //port = 3333;
        System.out.println(ipAddress + " " + port);
        InetAddress Ip = InetAddress.getByName(ipAddress);
        clientSocket.connect(Ip, port); // build connection

        long RTT = 0; // caculate the time
        int count = 10;
        double RTT_average = 0.0;
        long RTT_maximum = 0, RTT_minimum = 999999999;
        for(int i = 0; i < 10; i++) {
            RTT = 0;
            byte[] sendData = new byte[1024];
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String payLoad = "PingUDP " + i + " " + sdf.format(date) + "\n";
            System.out.println(payLoad);
            sendData = payLoad.getBytes();
            DatagramPacket packet = new DatagramPacket(sendData, sendData.length);
            clientSocket.send(packet);
            RTT = System.currentTimeMillis();
            try {
                byte[] receiveData = new byte[1024];
                packet = new DatagramPacket(receiveData, receiveData.length);
                clientSocket.receive(packet); // receive packet
                RTT = System.currentTimeMillis() - RTT;
                String reply = new String(packet.getData(), packet.getOffset(), packet.getLength());
                System.out.println("Receive: " + reply);
                System.out.println("RTT is " + RTT + " ms.");

            } catch (java.net.SocketTimeoutException e) {
                String reply = "I don't receive any reply";
                count--;
                System.out.println(reply);
                continue;
            }
            RTT_average += RTT;
            RTT_maximum = Math.max(RTT_maximum, RTT);
            RTT_minimum = Math.min(RTT_minimum, RTT);

        }
        System.out.println("\nsend " + count + " packets successfully "+
                "\nRTT_average:" + RTT_average/(count * 1.0) +
                " ms.\nRTT_maximum:" + RTT_maximum +
                " ms.\nRTT_minimum:" + RTT_minimum + " ms.");

        clientSocket.close();

    }

}
