package server.wang.can;

import com.sun.org.apache.xpath.internal.operations.Or;
import net.wang.can.protol.head.Head;
import net.wang.can.protol.head.Protocals;
import net.wang.can.protol.interfaces.IOServlet;
import net.wang.can.protol.utils.ReadUtils;
import net.wang.can.protol.utils.WriteUtils;
import order.wang.can.CommandString;
import order.wang.can.FileEntities;
import order.wang.can.FileEntity;
import server.wang.can.entitys.OrderEntity;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

public class IOServletImp implements IOServlet {
    @Override
    public void onServerReceive(Head head, InputStream inputStream, OutputStream outputStream) throws Exception {
        switch (head.getMessage())
        {
            case CommandString.ls:
                OrderEntity orderEntity = (OrderEntity) ReadUtils.readGsonObject(OrderEntity.class.getCanonicalName(),inputStream);

                List<FileEntity> list = OrderSolver.ls(orderEntity);
                FileEntities fileEntities = new FileEntities(list);
                Head returnHead = new Head(Protocals.STATUS_SUCCESS,head.getMessage(),Protocals.CONTENT_GSON_OBJ,"");
                WriteUtils.writeHead(returnHead,outputStream);
                WriteUtils.writeGsonObject(fileEntities,outputStream);
                break;

        }
    }
}
