package client.wang.can;

import com.sun.org.apache.xpath.internal.operations.Or;
import net.wang.can.profiles.Profile;
import order.wang.can.CommandString;

import java.io.File;
import java.net.Socket;

public class ClientApplication {
    public static void main(String[] arge) throws Exception {
        File file = new File("D:\\StudyWork\\test.txt");
        OrderSolver.upload(file,Profile.FILE_SEPARATOR);

//        OrderSolver.downLoad("testNew\\1409.1556.pdf", new File("E:\\1409.1556.pdf"));

//        OrderSolver.deleteFile("wang\\");


//        OrderSolver.ls("\\");

    }



}
