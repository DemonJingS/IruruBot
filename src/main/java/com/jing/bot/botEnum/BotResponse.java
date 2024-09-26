package com.jing.bot.botEnum;

import lombok.Getter;
import okhttp3.internal.http2.ErrorCode;

/**
 * @Author：DemonJing
 * @Package：com.jing.bot.botEnum
 * @Project：IruruBot
 * @name：BotResponse
 * @Date：2024/9/18 下午6:24
 * @Filename：BotResponse
 */
@Getter
public enum BotResponse {

    SUCCESS("10000", "操作成功"),

    USER_IS_BAN("10001", "用户被封禁"),

    USER_NOT_EXIST("10002", "用户不存在"),

    ERROR("10003", "操作失败"),

    UNKNOWN_ERROR("10010", "未知错误");



    private final String code;
    private final String msg;

    BotResponse(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    // 静态方法，根据代码获取消息
    public static String getMsgByCode(String code) {
        for (BotResponse br : BotResponse.values()) {
            if (br.getCode().equals(code)) {
                return br.getMsg();
            }
        }
        return "未知错误";
    }

}
