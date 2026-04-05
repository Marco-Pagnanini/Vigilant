package io.vigilant;

import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.StackTraceElementProxy;

public class VigilantParser {

    public static String parseStackTrace(IThrowableProxy throwable) {
        if (throwable == null) return null;

        StringBuilder sb = new StringBuilder(throwable.getClassName())
                .append(": ")
                .append(throwable.getMessage());

        for (StackTraceElementProxy el : throwable.getStackTraceElementProxyArray()) {
            sb.append("\n\t").append(el.toString());
        }

        return sb.toString();
    }
}
