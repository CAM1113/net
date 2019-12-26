package client.wang.can;

import com.google.gson.Gson;
import com.sun.org.apache.xpath.internal.operations.Or;
import net.wang.can.entitys.OrderEntity;
import net.wang.can.profiles.Profile;
import net.wang.can.protol.head.Head;
import net.wang.can.protol.head.Protocals;
import net.wang.can.protol.utils.ReadUtils;
import net.wang.can.protol.utils.WriteUtils;
import order.wang.can.FileEntities;
import order.wang.can.FileEntity;
import order.wang.can.OrderString;
import util.wang.can.LogUtils;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class OrderSolver {

    public static void solveOrder(String order,String options1,String options2,String currentPath,Socket socket) throws Exception{
        OutputStream outputStream = socket.getOutputStream();
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setOrder(order);
        orderEntity.setOption1(options1);
        orderEntity.setOption2(options2);
        orderEntity.setCurrentPath(currentPath);
        WriteUtils.writeOrderGsonObject(OrderEntity.class.getCanonicalName(),orderEntity,outputStream);
        socket.shutdownOutput();

        InputStream inputStream = socket.getInputStream();
        Head head = ReadUtils.readHead(inputStream);
        switch (order)
        {
            case OrderString.ls:
                FileEntities o  = (FileEntities)ReadUtils.readGsonObject(FileEntities.class.getCanonicalName(),inputStream);
                for (FileEntity fileEntity : o.getFileEntities()) {
                    LogUtils.i(fileEntity.getFileName() + ":" + fileEntity.getFileType());
                }
                break;
            default:
                break;
        }
        socket.shutdownInput();
    }
}
