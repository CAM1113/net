package server.wang.can;

import net.wang.can.protol.utils.ReadUtils;
import net.wang.can.protol.utils.WriteUtils;
import server.wang.can.entitys.OrderEntity;
import net.wang.can.profiles.Profile;
import order.wang.can.FileEntities;
import order.wang.can.FileEntity;
import order.wang.can.CommandString;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class OrderSolver {

    public static List<FileEntity> ls(OrderEntity orderEntity) {
        String filePath = orderEntity.getCurrentPath();
        List<FileEntity> list = new ArrayList<>();
        File f = new File(Profile.ROOT_PATH + "\\" + filePath);
        String[] names = f.list();
        for (String string : names) {
            File file = new File(Profile.ROOT_PATH + "\\" + filePath + "\\" + string);
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


}
