//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.KeyEvent;
//import java.io.DataInputStream;
//import java.io.DataOutputStream;
//import java.io.IOException;
//import java.net.Socket;
//
//public class Signal {
//
//    private void connect(int port) {
//        new Thread(() -> {
//            try {
//                System.out.println("Connecting on port " + port);
//                socket = new Socket("127.0.0.1", port);
//                in  = new DataInputStream(socket.getInputStream());
//                out = new DataOutputStream(socket.getOutputStream());
//                SwingUtilities.invokeLater(() -> {
//                    connectionLabel.setText("● CONNECTED");
//                    connectionLabel.setForeground(ACCENT_GREEN);
//                });
//                readLoop();
//            } catch (IOException e) {
//                System.out.println("Connection error: " + e.getMessage());
//                closeConnection();
//            }
//        }).start();
//    }
//
//    public static final byte INPUT_TELEMETRY = 1;
//    public static final byte KEY_PACKET = 1;
//    public static final byte STATE_PACKET = 2;
//
//    private void readLoop() {
//        try {
//            while (running.get()) {
//                if (in.readByte() == INPUT_TELEMETRY) {
//                    String text = in.readUTF();
//                    SwingUtilities.invokeLater(() -> telemetryArea.setText(text));
//                }
//            }
//        } catch (IOException e) {
//            closeConnection();
//        }
//    }
//
//    private void closeConnection() {
//        running.set(false);
//        try { if (in  != null) in.close();  } catch (IOException ignored) {}
//        try { if (out != null) out.close(); } catch (IOException ignored) {}
//        try { if (socket != null && !socket.isClosed()) socket.close(); } catch (IOException ignored) {}
//        SwingUtilities.invokeLater(() -> {
//            connectionLabel.setText("● DISCONNECTED");
//            connectionLabel.setForeground(ACCENT_RED);
//        });
//    }
//
//    public void sendKey(int keyCode, boolean down) {
//        try {
//            if (out != null) {
//                out.writeByte(KEY_PACKET);
//                out.writeInt(keyCode);
//                out.writeBoolean(down);
//                out.flush();
//            }
//        } catch (IOException e) {
//            System.out.println("Send error: " + e.getMessage());
//        }
//    }
//
//    public void sendState(OpModeState state) {
//        try {
//            if (out != null) {
//                out.writeByte(STATE_PACKET);
//                out.writeByte(state.ordinal());
//                out.flush();
//            }
//        } catch (IOException e) {
//            System.out.println("Send error: " + e.getMessage());
//        }
//    }
//
//}
