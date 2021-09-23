package com.example.album.media;

/**
 * @version : v1.0.0
 * @author: Tomdog
 * @since: 2021/9/23
 */
public class LogUtil {
    private static final boolean isDebug = false;
    private static final String TAG = "MediaLogUtil";

    public LogUtil() {
    }

    public static void d(Object... args) {
    }

    public static void e(Object... args) {
    }

    private static String appendStr(Object... args) {
        if (args != null && args.length >= 1) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(args[0]);

            for(int i = 1; i < args.length; ++i) {
                stringBuffer.append(" = ");
                stringBuffer.append(args[i]);
            }

            return stringBuffer.toString();
        } else {
            return "LogUtil empty log";
        }
    }
}
