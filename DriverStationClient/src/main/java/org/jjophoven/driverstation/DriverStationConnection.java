package org.jjophoven.driverstation;

import org.jjophoven.driverstation.packets.*;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class DriverStationConnection {
    private final Consumer<TelemetryPacket> telemetryConsumer;
    private final Runnable connectedCallback;
    private final Runnable disconnectedCallback;
    private final Consumer<OpModesPacket> opModeListCallback;

    private DataInputStream input;
    private DataOutputStream output;
    private Socket socket;
    private final AtomicBoolean running = new AtomicBoolean(true);

    public DriverStationConnection(
            int port,
            Consumer<TelemetryPacket> telemetryConsumer,
            Consumer<OpModesPacket> opModeListCallback,
            Runnable connectedCallback,
            Runnable disconnectedCallback
    ) {
        this.telemetryConsumer = telemetryConsumer;
        this.connectedCallback = connectedCallback;
        this.disconnectedCallback = disconnectedCallback;
        this.opModeListCallback = opModeListCallback;

        Thread thread = new Thread(() -> connect(port), "DriverStationConnection");
        thread.setDaemon(true);
        thread.start();
    }

    private void connect(int port) {
        try {
            socket = new Socket("127.0.0.1", port);
            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());

            SwingUtilities.invokeLater(connectedCallback);

            readLoop();
        } catch (IOException e) {
            System.out.println("Could not connect to server: " + e.getMessage());
            close();
        }
    }

    private void readLoop() {
        try {
            while (running.get()) {
                byte type = input.readByte();
                switch (type) {
                    case Packet.TELEMETRY:
                        TelemetryPacket packet = TelemetryPacket.read(input);
                        SwingUtilities.invokeLater(() -> telemetryConsumer.accept(packet));
                        break;
                    case Packet.OPMODE:
                        OpModesPacket opmodes = OpModesPacket.read(input);
                        System.out.print("RECEIVED OPMODES: ");
                        for (OpModePacket info : opmodes.opmodes) {
                            System.out.print(", " + info.name);
                        }
                        SwingUtilities.invokeLater(() -> opModeListCallback.accept(opmodes));
                        break;
                }
            }
        } catch (IOException ignored) {
            close();
        }
    }

    public void send(Packet packet) {
        try {
            output.writeByte(packet.getPacketType());
            packet.write(output);
            output.flush();
        } catch (IOException e) {
            System.out.println("Error sending packet: " + e.getMessage());
            close();
        }
    }

    public void close() {

        running.set(false);

        try { if (socket != null) socket.close(); } catch (IOException ignored) {}

        SwingUtilities.invokeLater(disconnectedCallback);

        System.out.println("Closed client connection");
    }
}