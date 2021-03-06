package util.wang.can;

import net.wang.can.profiles.Profile;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

public class Utils {

    public static void streamCopy(InputStream inputStream, OutputStream outputStream) throws Exception {
        byte[] bytes = new byte[1024];
        int len = 0;
        while ((len = inputStream.read(bytes)) > 0) {
            outputStream.write(bytes, 0, len);
            outputStream.flush();
        }
    }

    //将输入流消耗掉
    public static void killInputStream(InputStream inputStream) throws Exception {
        byte[] bytes = new byte[1024];
        int len = 0;
        while (inputStream.read(bytes) > 0) {
        }
    }


    public static String exchangeFileSaperator(String path) {
        return path.replace(Profile.FILE_SEPARATOR,java.io.File.separator);
    }


}
