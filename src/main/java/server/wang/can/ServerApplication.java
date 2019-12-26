package server.wang.can;

import net.wang.can.entitys.HelloWorld;
import net.wang.can.profiles.Profile;
import net.wang.can.protol.head.Head;
import net.wang.can.protol.utils.ReadUtils;
import net.wang.can.protol.utils.WriteUtils;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class ServerApplication {
    private static Executor executor = Executors.newWorkStealingPool();
    public static void main(String[] args) throws Exception {
        ServerSocket server = new ServerSocket(Profile.PORT);
        while(true)
        {
            Socket socket = server.accept();
            SocketRunnable socketRunnable = new SocketRunnable(socket);
            executor.execute(socketRunnable);
        }
    }

}
