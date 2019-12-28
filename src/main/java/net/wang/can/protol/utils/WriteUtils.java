package net.wang.can.protol.utils;

import com.google.gson.Gson;
import net.wang.can.profiles.Profile;
import net.wang.can.protol.head.Head;
import net.wang.can.protol.exceptions.HeadTooLargeException;
import util.wang.can.LogUtils;

import java.io.*;

public class WriteUtils {

    public static boolean writeFile(File file, OutputStream outputStream) throws Exception {
        FileInputStream fileInputStream = new FileInputStream(file);
        streamCopy(fileInputStream, outputStream);
        return true;
    }

    public static boolean writeGsonObject(Object o, OutputStream outputStream) throws Exception {
        if (o == null) {
            LogUtils.w("o == null");
            throw new Exception("o == null");
        }
        String s = new Gson().toJson(o);
        System.out.println(s);
        writeString(s, outputStream);
        return true;
    }

    public static void writeHead(Head head, OutputStream outputStream) throws Exception {
        String string = new Gson().toJson(head);
        if (string.length() > Profile.HEAD_SIZE) {
            throw new HeadTooLargeException("Head's size can't larger than " + Profile.HEAD_SIZE);
        }
        //将字符串编码为指定编码的字节
        byte[] code = string.getBytes(Profile.CODE_TYPE);
        //填充到目标字节数组中。写进输出流
        byte[] targetBytes = new byte[Profile.HEAD_SIZE];
        for (int i = 0; i < code.length; i++) {
            targetBytes[i] = code[i];
        }
        outputStream.write(targetBytes, 0, Profile.HEAD_SIZE);
    }


    //从输入流读取内容并拷贝到到输出流
    private static void streamCopy(InputStream inputStream, OutputStream outputStream) throws Exception {
        byte[] bytes = new byte[1024];
        int len = 0;
        while ((len = inputStream.read(bytes)) > 0) {
            outputStream.write(bytes, 0, len);
        }
    }

    private static void writeString(String s, OutputStream outputStream) throws Exception {
        byte[] bytes = s.getBytes(Profile.CODE_TYPE);
        outputStream.write(bytes);
    }
}
