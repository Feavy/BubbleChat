package fr.feavy.client.network.packet;

import fr.feavy.client.network.ClientConnection;
import fr.feavy.network.io.ConnectionErrorListener;
import fr.feavy.network.packet.Packet;

import java.io.DataOutputStream;
import java.io.IOException;

public class ClientPacketSenderRunnable implements Runnable {
    private DataOutputStream outputStream;
    private Packet packet;
    private ConnectionErrorListener errorListener;

    public ClientPacketSenderRunnable(DataOutputStream outputStream, Packet packet, ConnectionErrorListener errorListener) {
        this.outputStream = outputStream;
        this.packet = packet;
        this.errorListener = errorListener;
    }

    @Override
    public void run() {
        try {
            outputStream.writeInt(packet.getID().ordinal());
            if(packet.getID().isSecurePacket())
                outputStream.writeUTF(ClientConnection.get().getUUID());
            packet.writeTo(outputStream);
            outputStream.flush();
        } catch (IOException e) {
            this.errorListener.onError(e);
        }
    }
}
