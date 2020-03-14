package client.wang.can;

import com.google.gson.Gson;
import com.sun.org.apache.xpath.internal.operations.Or;
import net.wang.can.profiles.Profile;
import net.wang.can.protol.head.Head;
import order.wang.can.CommandString;

import java.io.File;
import java.net.Socket;

public class ClientApplication {
    public static void main(String[] arge) throws Exception {
        File file = new File("D:\\CAM.jks");
        Head head = OrderSolver.upload(file, Profile.FILE_SEPARATOR + "testfilefolder" + Profile.FILE_SEPARATOR);
        Gson gson = new Gson();
        String s = gson.toJson(head);
        System.out.println(s);
    }



}
