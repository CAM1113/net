package util.wang.can;

import sun.rmi.runtime.Log;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LogUtils {
    public static void w(String message) {
        Logger.getGlobal().warning(message);
    }

    public static void i(String message) {
        Logger.getGlobal().info(message);
    }

    public static void setLogLevel(Level logLevel) {
        Logger.getGlobal().setLevel(logLevel);
    }

    public static void infoLevel() {
        setLogLevel(Level.INFO);
    }

    public static void warmLevel() {
        setLogLevel(Level.WARNING);
    }

}
