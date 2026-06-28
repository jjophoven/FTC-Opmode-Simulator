package org.jjophoven.driverstation.packets;

import java.io.*;

public class OpModePacket implements Packet {
    public Type type;
    public String group;
    public String name;

    public OpModePacket(Type type, String name, String group) {
        this.type = type;
        this.group = group;
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public byte getPacketType() {
        return Packet.OPMODE;
    }

    public enum Type {
        TELEOP, AUTO
    }

    @Override
    public void write(DataOutputStream output) throws IOException {
        output.writeByte(type.ordinal());
        output.writeUTF(name);
        output.writeUTF(group);
    }

    public static OpModePacket read(DataInput input) {
        try {
            return new OpModePacket(
                    Type.values()[input.readByte()],
                    input.readUTF(),
                    input.readUTF()
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof OpModePacket) {
            OpModePacket other = (OpModePacket) obj;
            return other.type.equals(type) && other.name.equals(name) && other.group.equals(group);
        }
        return false;
    }
}