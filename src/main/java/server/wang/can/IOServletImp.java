package server.wang.can;

import com.sun.org.apache.xpath.internal.operations.Or;
import net.wang.can.profiles.Profile;
import net.wang.can.protol.head.Head;
import net.wang.can.protol.head.Protocals;
import net.wang.can.protol.interfaces.IOServlet;
import net.wang.can.protol.utils.ReadUtils;
import net.wang.can.protol.utils.WriteUtils;
import order.wang.can.CommandString;
import order.wang.can.FileEntities;
import order.wang.can.FileEntity;
import server.wang.can.entitys.OrderEntity;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

import static net.wang.can.profiles.Profile.userFolder;

public class IOServletImp implements IOServlet {
    //从输入流中获取输入，将处理结果写进输出流，不要对流进行关闭操作
    @Override
    public void onServerReceive(Head head, InputStream inputStream, OutputStream outputStream) throws Exception {
        switch (head.getMessage()) {
            case CommandString.ls:
                ls(head, inputStream, outputStream);
                break;
            case CommandString.upload:
                upload(head, inputStream, outputStream);
                break;
            case CommandString.downLoad:
                download(head, inputStream, outputStream);
                break;
            case CommandString.delete:
                delete(head,inputStream,outputStream);
        }
    }

    private void delete(Head head, InputStream inputStream, OutputStream outputStream) throws Exception {

    }

    //获取列表，
    // 参数head.getMessage() == CommandString.ls
    //inputStream中存放OrderEntity对象
    //OrderEntity对象中 orderEntity.getCurrentPath() 存放当前工作路径
    private void ls(Head head, InputStream inputStream, OutputStream outputStream) throws Exception {

        OrderEntity orderEntity = (OrderEntity) ReadUtils.readGsonObject(OrderEntity.class.getCanonicalName(), inputStream);
        List<FileEntity> list = OrderSolver.ls(orderEntity);
        FileEntities fileEntities = new FileEntities(list);
        Head returnHead = new Head(Protocals.STATUS_SUCCESS, head.getMessage(), Protocals.CONTENT_GSON_OBJ, "");
        WriteUtils.writeHead(returnHead, outputStream);
        WriteUtils.writeGsonObject(fileEntities, outputStream);
    }

    //上传文件
    //参数head.getMessage() == CommandString.upload
    //参数head.other()存放上传的路径,包含文件名
    //inputStream中存放上传的文件
    //上传失败，head返回失败、异常、空返回体、异常信息
    private void upload(Head head, InputStream inputStream, OutputStream outputStream) throws Exception {
        try {
            OrderSolver.upload(head.getOther(), inputStream);
        } catch (Exception e) {
            Head returnHead = new Head(Protocals.STATUS_FAIL, Protocals.MESSAGE_EXCEPTION_HAPPEN,
                    Protocals.CONTENT_NONE, "file upload Fail:" + e.getMessage());
            WriteUtils.writeHead(returnHead, outputStream);
            return;
        }
        Head returnHead = new Head(Protocals.STATUS_SUCCESS, head.getMessage(),
                Protocals.CONTENT_NONE, "file upload success");
        WriteUtils.writeHead(returnHead, outputStream);
    }


    //下载文件
    //参数head.getMessage() == CommandString.downLoad
    //参数head.other()存放下载的路径,包含文件名
    //outputStream中会写入下载的文件
    //下载失败，head返回失败、异常、空返回体、异常信息===文件不存在
    private void download(Head head, InputStream inputStream, OutputStream outputStream) throws Exception {
        String currentPath = head.getOther();
        File file = new File(Profile.ROOT_PATH + userFolder + currentPath);
        if (!file.exists()) {
            Head returnHead = new Head(Protocals.STATUS_FAIL, Protocals.MESSAGE_EXCEPTION_HAPPEN, Protocals.CONTENT_NONE, "file doesn't exist");
            WriteUtils.writeHead(returnHead,outputStream);
        }
        Head returnHead = new Head(Protocals.STATUS_SUCCESS, head.getMessage(), Protocals.CONTENT_FILE, "file download begin");
        WriteUtils.writeHead(returnHead,outputStream);
        OrderSolver.download(file,outputStream);
    }



}
