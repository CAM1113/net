package net.wang.can.protol.interfaces;

import net.wang.can.protol.head.Head;

import java.io.InputStream;
import java.io.OutputStream;

public interface IOServlet {
    Head onServerReceive(Head head, InputStream inputStream) throws Exception;
    void onServerPost(OutputStream outputStream) throws Exception;
}
