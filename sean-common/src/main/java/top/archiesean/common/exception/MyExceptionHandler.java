package top.archiesean.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import top.archiesean.common.response.Result;

/**
 * 统一异常处理
 *
 * @author ArchieSean
 * @since 2022-07-28 23:53
 */
@Slf4j
@ControllerAdvice
public class MyExceptionHandler {
    /**
     * 统一异常处理
     *
     * @param e 统一异常
     * @return result
     */
    @ExceptionHandler(GlobalException.class)
    public Result<?> handleException(GlobalException e) {
        return Result.error(e.getRespStatus());
    }

    /**
     * 统一异常处理
     *
     * @param e 异常
     * @return result
     */
    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e) {
        log.error("系统异常--->", e);
        return Result.error(RespStatus.FAIL);
    }
}
