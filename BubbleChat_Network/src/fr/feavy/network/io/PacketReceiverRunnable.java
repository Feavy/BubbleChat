package fr.feavy.network.io;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class PacketReceiverRunnable implements Runnable {
    private final DataInputStream inputStream;
    private final PacketDataListener dataListener;
    private final ConnectionErrorListener errorListener;

    public PacketReceiverRunnable(InputStream inputStream, PacketDataListener dataListener, ConnectionErrorListener errorListener) {
        this.inputStream = new DataInputStream(inputStream);
        this.dataListener = dataListener;
        this.errorListener = errorListener;
    }

    @Override
    public void run() {
        while (true) {
            int packetId;
            try {
                packetId = inputStream.readInt();
                System.out.println("packetId: "+packetId);
                dataListener.processData(packetId, inputStream);
                System.out.println("Processed, available: "+inputStream.available());
                if(inputStream.available() > 0) {
                    byte[] bytes = new byte[inputStream.available()];
                    inputStream.read(bytes);
                    System.out.println(new String(bytes));
                }
            } catch (IOException e) {
                //e.printStackTrace();
                errorListener.onError(e);
                return;
            }

        }
    }
}
