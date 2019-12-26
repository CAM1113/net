package client.wang.can;

import net.wang.can.profiles.Profile;
import order.wang.can.OrderString;

import java.net.Socket;

public class ClientApplication {
    public static void main(String[] arge) throws Exception {
        Socket socket = new Socket(Profile.SERVER_IP,Profile.PORT);
        OrderSolver.solveOrder(OrderString.ls,"","","wang",socket);
    }



}
