package top.archiesean.common.exception;

import lombok.Getter;

/**
 * 异常枚举
 *
 * @author ArchieSean
 * @since 2022-07-28 23:40
 */
@Getter
public enum RespStatus {
    SUCCESS(200, "success"),
    FAIL(10000, "system error");

    private final int code;
    private final String message;

    RespStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }
}

