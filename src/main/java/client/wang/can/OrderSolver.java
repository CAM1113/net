package client.wang.can;

import net.wang.can.protol.head.Protocals;
import server.wang.can.entitys.OrderEntity;
import net.wang.can.protol.head.Head;
import net.wang.can.protol.utils.ReadUtils;
import net.wang.can.protol.utils.WriteUtils;
import order.wang.can.FileEntities;
import order.wang.can.FileEntity;
import order.wang.can.CommandString;
import util.wang.can.LogUtils;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class OrderSolver {

    public static void solveOrder(String order, String options1, String options2, String currentPath, Socket socket) throws Exception {
        OutputStream outputStream = socket.getOutputStream();
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setOrder(order);
        orderEntity.setOption1(options1);
        orderEntity.setOption2(options2);
        orderEntity.setCurrentPath(currentPath);

        Head head = new Head(Protocals.STATUS_SUCCESS,CommandString.ls,Protocals.CONTENT_GSON_OBJ,"");
        WriteUtils.writeHead(head, outputStream);

        WriteUtils.writeGsonObject(orderEntity, outputStream);
        socket.shutdownOutput();
        InputStream inputStream = socket.getInputStream();
        Head returnHead = ReadUtils.readHead(inputStream);

        FileEntities o = (FileEntities) ReadUtils.readGsonObject(FileEntities.class.getName(), inputStream);
        for (FileEntity fileEntity : o.getFileEntities()) {
            LogUtils.i(fileEntity.getFileName() + ":" + fileEntity.getFileType());
        }

        socket.shutdownInput();
    }
}
