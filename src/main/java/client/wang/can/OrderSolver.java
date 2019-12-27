package client.wang.can;

import com.sun.xml.internal.ws.api.pipe.ContentType;
import net.wang.can.profiles.Profile;
import net.wang.can.protol.head.Protocals;
import server.wang.can.entitys.OrderEntity;
import net.wang.can.protol.head.Head;
import net.wang.can.protol.utils.ReadUtils;
import net.wang.can.protol.utils.WriteUtils;
import order.wang.can.FileEntities;
import order.wang.can.FileEntity;
import order.wang.can.CommandString;
import util.wang.can.IOUtils;
import util.wang.can.LogUtils;

import java.io.*;
import java.net.Socket;

public class OrderSolver {

    public static void solveOrder(String order, String options1, String options2, String currentPath, Socket socket) throws Exception {
        OutputStream outputStream = socket.getOutputStream();
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setOrder(order);
        orderEntity.setOption1(options1);
        orderEntity.setOption2(options2);
        orderEntity.setCurrentPath(currentPath);

        Head head = new Head(Protocals.STATUS_SUCCESS, CommandString.ls, Protocals.CONTENT_GSON_OBJ, "");
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

    public static Head upload(File file, String currentPath) throws Exception {
        if (!file.exists()) {
            throw new Exception("file doesn't exist");
        }
        Head head = new Head(Protocals.STATUS_SUCCESS, CommandString.upload, Protocals.CONTENT_FILE, currentPath + file.getName());
        Socket socket = getSocket(head);
        OutputStream outputStream = socket.getOutputStream();
        FileInputStream fileInputStream = new FileInputStream(file);
        IOUtils.streamCopy(fileInputStream, outputStream);
        fileInputStream.close();
        socket.shutdownOutput();
        InputStream inputStream = socket.getInputStream();
        Head returnHead = ReadUtils.readHead(inputStream);
        socket.shutdownInput();
        socket.close();
        return returnHead;
    }

    //currentPath:下载文件的路径，包含文件名
    public static Head downLoad(String currentPath, File localFile) throws Exception {
        if (localFile.exists()) {
            throw new Exception("localFile has already exist");
        }
        Head head = new Head(Protocals.STATUS_SUCCESS, CommandString.downLoad, Protocals.CONTENT_NONE, currentPath);
        Socket socket = getSocket(head);
        socket.shutdownOutput();
        InputStream inputStream = socket.getInputStream();
        Head returnHead = ReadUtils.readHead(inputStream);
        if (!returnHead.getStatus().equals(Protocals.STATUS_SUCCESS)) {
            //服务器下载不成功
            IOUtils.killInputStream(inputStream);
            socket.shutdownInput();
            socket.close();
            throw new Exception(head.getOther());
        }
        FileOutputStream fileOutputStream = new FileOutputStream(localFile);
        IOUtils.streamCopy(inputStream, fileOutputStream);
        fileOutputStream.close();
        socket.shutdownInput();
        socket.close();
        return returnHead;
    }

    //currentPath:删除文件的路径，包含文件名
    public static Head deleteFile(String currentPath) throws Exception {
        Head head = new Head(Protocals.STATUS_SUCCESS, CommandString.delete, Protocals.CONTENT_NONE, currentPath);
        Socket socket = getSocket(head);
        socket.shutdownOutput();
        InputStream inputStream = socket.getInputStream();
        Head returnHead = ReadUtils.readHead(inputStream);
        socket.shutdownInput();
        socket.close();
        return returnHead;
    }

    //创建Socket对象，同时写进Head
    public static Socket getSocket(Head head) throws Exception {
        Socket socket = new Socket(Profile.SERVER_IP, Profile.PORT);
        OutputStream outputStream = socket.getOutputStream();
        WriteUtils.writeHead(head, outputStream);
        return socket;
    }

}
