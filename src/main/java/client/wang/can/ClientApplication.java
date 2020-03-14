package client.wang.can;

import com.google.gson.Gson;
import com.sun.org.apache.xpath.internal.operations.Or;
import net.wang.can.profiles.Profile;
import net.wang.can.protol.head.Head;
import order.wang.can.CommandString;

import java.io.File;
import java.net.Socket;
import java.util.Scanner;

public class ClientApplication {
    public static void main(String[] arge) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=========远程文件管理=========");
        System.out.print("请输入命令:");
        String commands = scanner.nextLine().trim();
        String[] arr = commands.split("\\s+");
        if (arr.length != 3) {
            System.out.println("命令参数错误，注意使用空格分割");
            return;
        }
        if (arr[0].equals("download")) {
            System.out.print("确定进行下载操作(0取消，1确定)");
            int i = scanner.nextInt();
            if (i==0){
                return;
            }else {
                OrderSolver.downLoad(arr[1], arr[2]);
            }
        } else if (arr[0].equals("upload")) {
            System.out.print("确定进行上传操作(0取消，1确定)");
            int i = scanner.nextInt();
            if (i==0){
                return;
            }else {
                OrderSolver.uploadFolders(arr[1], arr[2]);
            }
        } else {
            System.out.println("命令错误，只接受download和upload命令");
        }


//            switch (command) {
//                case 0:
//                    System.out.println("bye");
//                    System.exit(0);
//                case 1:
//                    System.out.println("输入下载的文件路径(包含文件名):");
//                    targetPath = scanner.nextLine();
//
//                    System.out.println("请输入本地的文件路径(包含文件名)：");
//                    localPath = scanner.nextLine();
//                    System.out.println("下载中。。。。。。");
//                    OrderSolver.downLoad(targetPath, localPath);
//                    System.out.println("下载结束");
//                    break;
//                case 2:
//                    System.out.println("输入本地的文件路径(包含文件名，文件夹名):");
//                    localPath = scanner.nextLine();
//                    System.out.println("请输入服务器的文件路径(包含文件名，跟文件夹名)：");
//                    targetPath = scanner.nextLine();
//                    System.out.println("上传中。。。。。。");
//                    OrderSolver.uploadFolders(localPath, targetPath);
//                    System.out.println("上传结束");
//                    break;
//                default:
//                    continue;
//            }

//        OrderSolver.uploadFolders("F:\\client", Profile.FILE_SEPARATOR + "testFolder");

    }


}
