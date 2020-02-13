package fr.feavy.network.io;

import java.io.DataInputStream;
import java.io.IOException;

public interface PacketDataListener {
    void processData(int id, DataInputStream inputStream) throws IOException;
}