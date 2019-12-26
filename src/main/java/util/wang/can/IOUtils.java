package util.wang.can;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

public class IOUtils {

    public static void streamCopy(InputStream inputStream, OutputStream outputStream) throws Exception {
        byte[] bytes = new byte[1024];
        int len = 0;
        while ((len = inputStream.read(bytes)) > 0) {
            outputStream.write(bytes,0,len);
        }
    }

    static void mkdir(String fullPath)
    {
        File file = new File(fullPath);
        file.mkdir();
    }


}
