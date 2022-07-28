package top.archiesean.common.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 全局异常处理
 *
 * @author ArchieSean
 * @since 2022-07-28 23:50
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GlobalException extends RuntimeException {
    private RespStatus respStatus;
}
