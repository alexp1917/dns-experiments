package names;

import lombok.SneakyThrows;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;


public class SocketServers {
    static final byte[] endBytes = "end".getBytes(StandardCharsets.UTF_8);
    
    public static void main(String[] args) throws IOException, InterruptedException {
        SocketServer socketServer = new SocketServer(8053);
        socketServer.start();

        DatagramSocket client = new DatagramSocket(8054);
        client.connect(InetAddress.getLocalHost(), 8053);
        client.send(new DatagramPacket(endBytes, 0, endBytes.length));

        socketServer.join();
        System.out.println("server exited");
    }

    public static class SocketServer extends Thread {
        private final DatagramPacket packet;
        private final DatagramSocket datagramSocket;
        private boolean running = true;

        public SocketServer(int port) throws SocketException {
            datagramSocket = new DatagramSocket(port);
            byte[] buf = new byte[10_000];
            packet = new DatagramPacket(buf, buf.length);
        }

        @SneakyThrows
        @Override
        public void run() {
            while (running) {
                datagramSocket.receive(packet);

                if ("end".equals(tryGetString())) {
                    running = false;
                    System.out.println("exiting due to having read end");
                }
            }
        }

        private String tryGetString() {
            try {
                String s = new String(packet.getData(),
                        packet.getOffset(),
                        packet.getLength(),
                        StandardCharsets.UTF_8);
                System.out.println("Server got message '" + s +
                        "' on port: " +
                        datagramSocket.getLocalPort() +
                        " from: " +
                        datagramSocket.getPort());
                return s;
            } catch (Exception ignored) {
                return null;
            }
        }
    }
}
