package top.archiesean.common.response;

import lombok.Data;
import top.archiesean.common.exception.RespStatus;

/**
 * 统一返回结构
 *
 * @author ArchieSean
 * @date 2022-07-07 18:00
 */
@Data
public class Result<T> {
    /**
     * 编码
     */
    private int code;
    /**
     * 消息
     */
    private String msg;
    /**
     * 数据
     */
    private T data;

    private Result(int code, String message, T data) {
        this.code = code;
        this.msg = message;
        this.data = data;
    }

    /**
     * 成功响应
     *
     * @param data 数据
     * @param <T>  泛型
     * @return result
     */
    public static <T> Result<T> ok(T data) {
        return new Result<>(RespStatus.SUCCESS.getCode(),
                RespStatus.SUCCESS.getMessage(), data);
    }

    /**
     * 异常响应
     *
     * @param respStatus 异常枚举
     * @param <T>        泛型
     * @return result
     */
    public static <T> Result<T> error(RespStatus respStatus) {
        return new Result<>(respStatus.getCode(), respStatus.getMessage(), null);
    }
}
