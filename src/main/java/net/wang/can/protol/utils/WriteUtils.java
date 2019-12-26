package net.wang.can.protol.utils;

import com.google.gson.Gson;
import net.wang.can.profiles.Profile;
import net.wang.can.protol.head.Head;
import net.wang.can.protol.head.Protocals;
import net.wang.can.protol.exceptions.HeadTooLargeException;

import java.io.*;

public class WriteUtils {


    public static boolean writeFile(String others, File file, OutputStream outputStream) throws Exception {
        if (!file.exists()) {
            writeHead(Protocals.STATUS_FAIL, Protocals.CONTENT_FILE, Protocals.MESSAGE_FILE_IS_NOT_EXIST,
                    "file is not exist", outputStream);
            return false;
        }
        writeHead(Protocals.STATUS_SUCCESS,Protocals.CONTENT_FILE,Protocals.MESSAGE_NORMAL,others,outputStream);
        FileInputStream fileInputStream = new FileInputStream(file);
        streamCopy(fileInputStream,outputStream);
        fileInputStream.close();
        return true;
    }

    public static boolean writeGsonObject(String others, Object o, OutputStream outputStream) throws Exception
    {
        if(o == null)
        {
            writeHead(Protocals.STATUS_FAIL, Protocals.CONTENT_GSON_OBJ, Protocals.MESSAGE_OBJECT_IS_NULL,
                    "object is null", outputStream);
            return false;
        }
        writeHead(Protocals.STATUS_SUCCESS, Protocals.CONTENT_GSON_OBJ, Protocals.MESSAGE_NORMAL,
                o.getClass().getCanonicalName(), outputStream);
        String s = new Gson().toJson(o);
        writeString(s,outputStream);
        return true;
    }

    //other中存放命令，例如：ls
    public static void writeOrderGsonObject(String order,Object orderBody,OutputStream outputStream) throws Exception
    {
        writeHead(Protocals.STATUS_SUCCESS,Protocals.CONTENT_ORDER_GSON_OBJ,Protocals.MESSAGE_NORMAL,order,outputStream);
        writeString(new Gson().toJson(orderBody),outputStream);
    }

    public static void writeNoneBody(String status, String message, String others, OutputStream outputStream) throws Exception
    {
        writeHead(status,Protocals.CONTENT_NONE,message,others,outputStream);
    }

    private static void writeHead(String status, String contentType, String message, String others, OutputStream outputStream) throws Exception {
        Head head = new Head();
        head.setStatus(status.trim().replace("\n", " "));
        head.setContentType(contentType.trim().replace("\n", " "));
        head.setMessage(message.trim().replace("\n", " "));//去除换行
        head.setOther(others.trim().replace("\n", " "));//去除换行
        String string = new Gson().toJson(head);
        byte[] lengthBytes = string.getBytes();
        if(lengthBytes.length> Profile.HEAD_SIZE)
        {
            throw new HeadTooLargeException("Head's size can't larger than " + Profile.HEAD_SIZE);
        }
        byte[] targetBytes = new byte[Profile.HEAD_SIZE];
        string.getBytes(0,lengthBytes.length,targetBytes,0);
        outputStream.write(targetBytes,0,Profile.HEAD_SIZE);
    }

    //从输入流读取内容并拷贝到到输出流
    private static void streamCopy(InputStream inputStream, OutputStream outputStream) throws Exception {
        byte[] bytes = new byte[1024];
        int len = 0;
        while ((len = inputStream.read(bytes)) > 0) {
            outputStream.write(bytes,0,len);
        }
    }

    private static void writeString(String s, OutputStream outputStream) throws Exception
    {
        byte[] bytes = s.getBytes();
        outputStream.write(bytes);
    }
}
