package org.vxwo.springboot.experience.util;

public class ExceptionUtils {
    public static String getStackTrace(Throwable throwable) {
        return getStackTrace(throwable, -1);
    }

    public static String getStackTrace(Throwable throwable, int maxLines) {
        int lines = 0;
        StringBuilder sb = new StringBuilder();
        sb.append(throwable.toString());
        for (StackTraceElement element : throwable.getStackTrace()) {
            if (maxLines > 0 && ++lines > maxLines) {
                break;
            }

            sb.append("\n  at ");
            sb.append(element.toString());
        }

        return sb.toString();
    }
}
