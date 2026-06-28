package org.jjophoven.driverstation.packets;

import java.io.DataOutputStream;
import java.io.IOException;

public interface Packet {
    byte getPacketType();
    void write(DataOutputStream out) throws IOException;

    byte TELEMETRY = 1;
    byte KEY = 2;
    byte STATE = 3;
    byte OPMODE = 4;
}