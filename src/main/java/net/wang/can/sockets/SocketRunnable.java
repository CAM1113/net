package net.wang.can.sockets;

import com.sun.istack.internal.NotNull;
import net.wang.can.entitys.OrderEntity;
import net.wang.can.profiles.Profile;
import net.wang.can.protol.head.Head;
import net.wang.can.protol.head.Protocals;
import net.wang.can.protol.interfaces.IOServlet;
import net.wang.can.protol.utils.ReadUtils;
import net.wang.can.protol.utils.WriteUtils;
import server.wang.can.OrderSolver;
import util.wang.can.IOUtils;
import util.wang.can.LogUtils;

import java.io.*;
import java.net.Socket;


import static net.wang.can.protol.head.Protocals.STATUS_SUCCESS;

public class SocketRunnable implements Runnable {
    Socket socket = null;
    IOServlet ioServlet = null;

    public SocketRunnable(@NotNull Socket socket, @NotNull IOServlet ioServlet) {
        this.socket = socket;
        this.ioServlet = ioServlet;
    }

    @Override
    public void run() {
        try {
            if (socket == null) {
                LogUtils.w("socket == null");
                return;
            }
            if (ioServlet == null) {
                LogUtils.w("ioServlet == null");
                return;
            }
            runNetWork(socket, ioServlet);
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

    private void runNetWork(Socket socket, IOServlet ioServlet) throws Exception {
        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();

        Head head = ReadUtils.readHead(inputStream);
        LogUtils.w(socket.getInetAddress().getHostAddress() + ": head.getStatus:" + head.getStatus());
        LogUtils.w(socket.getInetAddress().getHostAddress() + ": head.getContentType:" + head.getContentType());
        LogUtils.w(socket.getInetAddress().getHostAddress() + ": head.getMessage:" + head.getMessage());
        LogUtils.w(socket.getInetAddress().getHostAddress() + ": head.getOther:" + head.getOther());

        //外部接受客户端的输入

        Head sendHead;
        try {
            sendHead = ioServlet.onServerReceive(head, inputStream);
        } catch (Exception e) {
            //用户处理输入流时产生异常
            Head exceptionHead = new Head(Protocals.STATUS_FAIL, Protocals.MESSAGE_EXCEPTION_HAPPEN,
                    Protocals.CONTENT_NONE, e.getMessage());
            WriteUtils.writeHead(exceptionHead, outputStream);
            socket.shutdownOutput();
            LogUtils.w("exception :" + e.getMessage());
            return;
        } finally {
            socket.shutdownInput();
        }
        if (sendHead == null) {
            sendHead = new Head(STATUS_SUCCESS, head.getMessage(), head.getContentType(), head.getOther());
        }
        //外部响应客户端的输出
        WriteUtils.writeHead(sendHead, outputStream);
        ioServlet.onServerPost(outputStream);
        socket.shutdownOutput();
    }

   /* private void uploadFile(Head head, Socket socket) throws Exception {
        File file = new File(Profile.ROOT_PATH + head.getOther());
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        InputStream inputStream = socket.getInputStream();
        IOUtils.streamCopy(inputStream, fileOutputStream);
        socket.shutdownInput();
        fileOutputStream.close();
        OutputStream outputStream = socket.getOutputStream();
        WriteUtils.writeNoneBody(STATUS_SUCCESS, MESSAGE_NORMAL, "file upload success", outputStream);
        socket.shutdownOutput();
    }

    private void solveNoneBody(Head head, Socket socket) throws Exception {
        socket.shutdownInput();
        OutputStream outputStream = socket.getOutputStream();
        WriteUtils.writeNoneBody(STATUS_SUCCESS, Protocals.MESSAGE_NORMAL, "head have receive", outputStream);
        socket.shutdownOutput();
    }

    private void solveOrder(Head head, Socket socket) throws Exception {
        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();
        OrderEntity orderEntity = (OrderEntity) ReadUtils.readGsonObject(head.getOther(), inputStream);
        socket.shutdownInput();
        Object result = OrderSolver.solveOrder(orderEntity);
        WriteUtils.writeOrderGsonObject(orderEntity.order, result, outputStream);
        socket.shutdownOutput();
    }*/
}
