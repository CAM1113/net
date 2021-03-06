package server.wang.can;

import com.sun.deploy.util.StringUtils;
import entity.wang.can.OrderEntity;
import net.wang.can.profiles.Profile;
import entity.wang.can.FileEntity;
import util.wang.can.Utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static net.wang.can.profiles.Profile.*;

public class OrderSolver {

    public static List<FileEntity> ls(String filePath) {
        List<FileEntity> list = new ArrayList<>();
        File f = new File(Profile.ROOT_PATH + userFolder + filePath);
        String[] names = f.list();
        if (names == null || names.length == 0) {
            return list;
        }
        for (String string : names) {
            if (string.contains("$") || string.contains("/") || string.contains("\\")) {
                //去掉特殊文件，Gson无法解析文件名的文件
                continue;
            }
            File file = new File(Profile.ROOT_PATH + userFolder + filePath + "\\" + string);
            FileEntity fileEntity = new FileEntity();
            fileEntity.setFileName(string);
            if (file.isDirectory()) {
                fileEntity.setFileType(FileEntity.TYPE_FOLDER);
            } else {
                fileEntity.setFileType(FileEntity.TYPE_FILE);
            }
            list.add(fileEntity);
        }
        return list;
    }

    //currentPath中存放当前路径和文件名
    public static void upload(String currentPath, InputStream inputStream) throws Exception {

        File file = new File(Profile.ROOT_PATH + userFolder + currentPath);
        if (file.exists()) {
            //文件已经存在，把输入流中的数据消耗掉，避免客户端异常
            file.delete();
        }
        createFile(file);
        FileOutputStream outputStream = new FileOutputStream(file);
        Utils.streamCopy(inputStream, outputStream);
        outputStream.close();
    }


    //currentPath中存放上传的当前路径和文件名
    //把文件写进outputStream
    public static void download(File file, OutputStream outputStream) throws Exception {
        if (!file.exists()) {
            //文件不存在，不做处理
            return;
        }
        FileInputStream fileInputStream = new FileInputStream(file);
        Utils.streamCopy(fileInputStream, outputStream);
        fileInputStream.close();
    }


    public static void createFile(File file) throws Exception {
        if (file==null) {
            return;
        }
        if(file.exists()){
            file.delete();
        }
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        file.createNewFile();
    }

}
