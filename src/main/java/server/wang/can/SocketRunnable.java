package server.wang.can;

import com.sun.istack.internal.NotNull;
import net.wang.can.entitys.OrderEntity;
import net.wang.can.profiles.Profile;
import net.wang.can.protol.head.Head;
import net.wang.can.protol.head.Protocals;
import net.wang.can.protol.utils.ReadUtils;
import net.wang.can.protol.utils.WriteUtils;
import util.wang.can.IOUtils;
import util.wang.can.LogUtils;

import java.io.*;
import java.net.Socket;

import static net.wang.can.protol.head.Protocals.MESSAGE_NORMAL;
import static net.wang.can.protol.head.Protocals.STATUS_SUCCESS;

public class SocketRunnable implements Runnable {
    Socket socket = null;

    public SocketRunnable(@NotNull Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            if (socket == null) {
                LogUtils.w("socket == null");
                return;
            }
            runNetWork(socket);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.w(e.getMessage());
        } finally {
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                LogUtils.w(e.getMessage());
            }
        }

    }

    private void runNetWork(Socket socket) throws Exception {
        InputStream inputStream = socket.getInputStream();
        Head head = ReadUtils.readHead(inputStream);
        LogUtils.w(socket.getInetAddress().getHostAddress() + ": head.getStatus:" + head.getStatus());
        LogUtils.w(socket.getInetAddress().getHostAddress() + ": head.getContentType:" + head.getContentType());
        LogUtils.w(socket.getInetAddress().getHostAddress() + ": head.getMessage:" + head.getMessage());
        LogUtils.w(socket.getInetAddress().getHostAddress() + ": head.getOther:" + head.getOther());

        switch (head.getContentType()) {
            case Protocals.CONTENT_NONE:
                //空内容
                solveNoneBody(head,socket);
                break;
            case Protocals.CONTENT_ORDER_GSON_OBJ:
                //命令对象
                solveOrder(head,socket);
                break;
            case Protocals.CONTENT_FILE:
                //上传文件
                uploadFile(head,socket);
                break;


        }



    }

    private void uploadFile(Head head, Socket socket) throws Exception{
        File file = new File(Profile.ROOT_PATH + head.getOther());
        if(!file.exists())
        {
            file.createNewFile();
        }
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        InputStream inputStream = socket.getInputStream();
        IOUtils.streamCopy(inputStream,fileOutputStream);
        socket.shutdownInput();
        fileOutputStream.close();
        OutputStream outputStream = socket.getOutputStream();
        WriteUtils.writeNoneBody(STATUS_SUCCESS,MESSAGE_NORMAL,"file upload success",outputStream);
        socket.shutdownOutput();
    }

    private void solveNoneBody(Head head,Socket socket) throws Exception{
        socket.shutdownInput();
        OutputStream outputStream = socket.getOutputStream();
        WriteUtils.writeNoneBody(STATUS_SUCCESS,Protocals.MESSAGE_NORMAL, "head have receive",outputStream);
        socket.shutdownOutput();
    }

    private void solveOrder(Head head,Socket socket) throws Exception{
        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();
        OrderEntity orderEntity = (OrderEntity) ReadUtils.readGsonObject(head.getOther(),inputStream);
        socket.shutdownInput();
        Object result= OrderSolver.solveOrder(orderEntity);
        WriteUtils.writeOrderGsonObject(orderEntity.order,result,outputStream);
        socket.shutdownOutput();
    }
}
