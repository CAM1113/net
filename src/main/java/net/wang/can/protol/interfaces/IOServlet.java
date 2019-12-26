package net.wang.can.protol.interfaces;

import net.wang.can.protol.head.Head;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public interface IOServlet {
    void onServerReceive(Head head, InputStream inputStream, OutputStream outputStream) throws Exception;
}
