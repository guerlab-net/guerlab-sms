package net.guerlab.sms.server.utils;

import java.util.concurrent.ThreadLocalRandom;

import static java.util.concurrent.ThreadLocalRandom.current;

/**
 * 随机类
 *
 * @author Guer
 */
public final class RandomUtil {

    /**
     * 默认随机字符集合
     */
    public static final char[] CHAR_LIST = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o',
            'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
            'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4',
            '5', '6', '7', '8', '9' };

    private RandomUtil() {
    }

    /**
     * 随机字符串，取值范围(a-zA-Z0-9)
     *
     * @param length
     *         字符串长度
     * @return 随机字符串
     */
    public static String nextString(final int length) {
        return nextString(length, CHAR_LIST);
    }

    /**
     * 自定义取值范围的随机字符串
     *
     * @param length
     *         字符串长度
     * @param chars
     *         取值范围
     * @return 随机字符串
     */
    public static String nextString(final int length, final char[] chars) {
        if (length <= 0) {
            return "";
        }

        char[] nowChars = chars;

        if (nowChars == null || nowChars.length == 0) {
            nowChars = CHAR_LIST;
        }

        char[] list = new char[length];

        ThreadLocalRandom random = current();

        for (int i = 0; i < list.length; i++) {
            list[i] = nowChars[random.nextInt(nowChars.length)];
        }

        return new String(list);
    }
}
