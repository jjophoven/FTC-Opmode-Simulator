package org.jjophoven.driverstation.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OpModesPacket implements Packet {
    public List<OpModePacket> opmodes;

    public OpModesPacket(List<OpModePacket> opmodes) {
        this.opmodes = opmodes;
    }

    @Override
    public byte getPacketType() {
        return Packet.OPMODE;
    }

    @Override
    public void write(DataOutputStream output) throws IOException {
        try {
            output.writeInt(opmodes.size());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (OpModePacket opmode : opmodes) {
            opmode.write(output);
        }
    }

    public static OpModesPacket read(DataInputStream input) {
        List<OpModePacket> opmodes = new ArrayList<>();

        int autoCount = 0;
        try {
            autoCount = input.readInt();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < autoCount; i++) {
            opmodes.add(OpModePacket.read(input));
        }

        return new OpModesPacket(opmodes);
    }
}
