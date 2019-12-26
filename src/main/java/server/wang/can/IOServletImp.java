package server.wang.can;

import net.wang.can.protol.head.Head;
import net.wang.can.protol.interfaces.IOServlet;

import java.io.InputStream;
import java.io.OutputStream;

public class IOServletImp implements IOServlet {
    @Override
    public Head onServerReceive(Head head, InputStream inputStream) throws Exception {
        return null;
    }

    @Override
    public void onServerPost(OutputStream outputStream) throws Exception {

    }
}
