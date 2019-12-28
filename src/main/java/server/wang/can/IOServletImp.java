package server.wang.can;

import net.wang.can.profiles.Profile;
import net.wang.can.protol.head.Head;
import net.wang.can.protol.head.Protocals;
import net.wang.can.protol.interfaces.IOServlet;
import net.wang.can.protol.utils.WriteUtils;
import order.wang.can.CommandString;
import entity.wang.can.FileEntities;
import entity.wang.can.FileEntity;
import util.wang.can.LogUtils;
import util.wang.can.Utils;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
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
                delete(head, inputStream, outputStream);
                break;
        }
    }


    //获取列表，
    // 参数head.getMessage() == CommandString.ls
    //head.getOther()中存放当前路径
    //成功，则在输出流中返回文件列表
    //失败，则在headOther
    private void ls(Head head, InputStream inputStream, OutputStream outputStream) throws Exception {
        FileEntities fileEntities = new FileEntities();
        try {
            List<FileEntity> list = OrderSolver.ls(Utils.exchangeFileSaperator(head.getOther()));
            fileEntities.setFileEntities(list);
        } catch (Exception e) {
            LogUtils.w("Exception in ls e = " + e.getMessage());
            Head returnHead = new Head(Protocals.STATUS_FAIL, head.getMessage(), Protocals.CONTENT_NONE, e.getMessage());
            WriteUtils.writeHead(returnHead, outputStream);
            return;
        }
        Head returnHead = new Head(Protocals.STATUS_SUCCESS, head.getMessage(), Protocals.CONTENT_GSON_OBJ, "");
        WriteUtils.writeHead(returnHead, outputStream);
        WriteUtils.writeGsonObject(fileEntities, outputStream);
    }

    //上传文件
    //参数head.getMessage() == CommandString.upload
    //参数head.other()存放上传的路径,包含文件名
    //inputStream中存放上传的文件
    //上传失败，head返回失败、异常、空返回体、异常信息
    //上传成功。返回正常Head
    private void upload(Head head, InputStream inputStream, OutputStream outputStream) throws Exception {
        try {
            OrderSolver.upload(Utils.exchangeFileSaperator(head.getOther()), inputStream);
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
        File file = solveFileExist(head, outputStream);
        if (file == null) {
            return;
        }
        Head returnHead = new Head(Protocals.STATUS_SUCCESS, head.getMessage(), Protocals.CONTENT_FILE, "file download begin");
        WriteUtils.writeHead(returnHead, outputStream);
        OrderSolver.download(file, outputStream);
    }


    //删除文件或文件夹
    //参数head.getMessage() == CommandString.delete
    //参数head.other()存放下载的路径,包含文件名,如果是文件夹，则递归全部删除
    //删除失败，返回异常head
    private void delete(Head head, InputStream inputStream, OutputStream outputStream) throws Exception {
        //处理路径不正常情况
        File file = solveFileExist(head, outputStream);
        if (file == null) {
            return;
        }
        if (file.isDirectory()) {
            //递归删除文件夹
            delFiles(file.getAbsolutePath());
        }
        file.delete();
        Head returnHead = new Head(Protocals.STATUS_SUCCESS, head.getMessage(), Protocals.CONTENT_FILE, "file delete success");
        WriteUtils.writeHead(returnHead, outputStream);
    }


    //判断给定路径的文件是否为空。为空则返回异常输出，方法返回null
    private File solveFileExist(Head head, OutputStream outputStream) throws Exception {
        String currentPath = Utils.exchangeFileSaperator(head.getOther());
        File file = new File(Profile.ROOT_PATH + userFolder + currentPath);
        if (!file.exists()) {
            Head returnHead = new Head(Protocals.STATUS_FAIL, Protocals.MESSAGE_EXCEPTION_HAPPEN, Protocals.CONTENT_NONE, "file doesn't exist");
            WriteUtils.writeHead(returnHead, outputStream);
            return null;
        }
        return file;
    }


    public static void delFiles(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return;
        }
        String[] list = file.list();
        File temp = null;
        String path = null;
        for (String item : list) {
            path = filePath + File.separator + item;
            temp = new File(path);
            if (temp.isFile()) {
                temp.delete();
                continue;
            }
            if (temp.isDirectory()) {
                delFiles(path);
                new File(path).delete();
                continue;
            }
        }
    }


}
