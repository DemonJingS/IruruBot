package com.jing.constant;

import java.time.format.DateTimeFormatter;

/**
 * @Author：DemonJing
 * @Package：com.jing.constant
 * @Project：IruruBot
 * @name：GameConstant
 * @Date：2024/9/17 0:33
 * @Filename：GameConstant
 */
public class GameConstant {

    public static final String SPECIAL_CHAR_REGEX = "^[\\u4e00-\\u9fa5a-zA-Z0-9,.，。！!]+$";

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 默认暂停次数
     */
    public static final String LOCK_GAME_PAUSECOUNT_DEFAULT = "0";

    /**
     * 内测标识
     */
    public static final String BETA_FLAG = "1";

    /**
     * 加时
     */
    public static final String LOCK_GAME_ADD_TIME = "addTime";

    /**
     * 同意
     */
    public static final String LOCK_GAME_AGREE = "agree";

    /**
     * 拒绝
     */
    public static final String LOCK_GAME_REFUSE = "refuse";
}
