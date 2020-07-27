package com.minio.ls.util;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Code Generator Utils
 *
 * @author lishen
 * @date 2020/07/23
 * @version 1.0
 */
public class CodeGeneratorUtils {

    /**
     * uuid replaced - and with three random int
     *
     * @return {@link String}
     * @author lishen
     */
    public static String code() {
        int times = 3;
        ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
        StringBuilder sb = new StringBuilder();
        sb.append(uuidReplaced());
        for (int i = 0; i < times; i++) {
            sb.append(threadLocalRandom.nextInt(10));
        }
        return sb.toString();
    }

    /**
     * uuid
     *
     * @return {@link String}
     * @author lishen
     */
    public static String uuid() {
        return UUID.randomUUID().toString();
    }

    /**
     * uuid replaced -
     *
     * @return {@link String}
     * @author lishen
     */
    public static String uuidReplaced() {
        return uuid().replaceAll("-","");
    }

}
