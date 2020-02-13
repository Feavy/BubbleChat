package fr.feavy.network.io;

import fr.feavy.network.packet.Packet;

import java.io.DataOutputStream;
import java.io.IOException;

public class PacketSenderRunnable implements Runnable {
    private DataOutputStream outputStream;
    private Packet packet;
    private ConnectionErrorListener errorListener;

    public PacketSenderRunnable(DataOutputStream outputStream, Packet packet, ConnectionErrorListener errorListener) {
        this.outputStream = outputStream;
        this.packet = packet;
        this.errorListener = errorListener;
    }

    @Override
    public void run() {
        try {
            outputStream.writeInt(packet.getID().ordinal());
            packet.writeTo(outputStream);
            outputStream.flush();
        } catch (IOException e) {
            this.errorListener.onError(e);
        }
    }
}
