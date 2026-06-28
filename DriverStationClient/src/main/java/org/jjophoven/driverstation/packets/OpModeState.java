package org.jjophoven.driverstation.packets;

import java.io.*;

public enum OpModeState implements Packet {
    WAIT_FOR_INIT,
    INITIALIZING,
    RUNNING;

    @Override
    public byte getPacketType() {
        return Packet.STATE;
    }

    @Override
    public void write(DataOutputStream output) throws IOException {
        output.writeByte(ordinal());
    }

    public static OpModeState read(DataInputStream in) throws IOException {
        return values()[in.readByte()];
    }
}
