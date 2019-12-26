package client.wang.can;

import com.google.gson.Gson;
import com.sun.org.apache.xpath.internal.operations.Or;
import net.wang.can.entitys.HelloWorld;
import net.wang.can.profiles.Profile;
import net.wang.can.protol.head.Head;
import net.wang.can.protol.utils.ReadUtils;
import net.wang.can.protol.utils.WriteUtils;
import order.wang.can.OrderString;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ClientApplication {
    public static void main(String[] arge) throws Exception {
        Socket socket = new Socket(Profile.SERVER_IP,Profile.PORT);
        OrderSolver.solveOrder(OrderString.ls,"","","wang",socket);
    }



}
