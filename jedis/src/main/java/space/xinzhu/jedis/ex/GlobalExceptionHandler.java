package space.xinzhu.jedis.ex;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import space.xinzhu.jedis.vo.R;

import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * @description: ???
 * Created by 馨竹 on 2022/12/23
 * --------------------------------------------
 * Update for ??? on ???? / ?? / ?? by ???
 **/
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 异常处理方法: SQL主键重复异常处理
     *
     * @return
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex) {
        log.error(ex.getMessage());   // Duplicate entry 'lisi' for key 'idx_username'
        if (ex.getMessage().contains("Duplicate entry")) {
            String[] split = ex.getMessage().split(" ");
            String msg = split[2] + "已存在";
            return R.error(msg);
        }
        return R.error("未知错误");
    }


    /**
     * 异常处理方法: SQL主键重复异常处理
     *
     * @return
     */
    @ExceptionHandler(Exception.class)
    public R<String> exceptionHandler(Exception ex) {
        log.error(ex.getMessage());   // Duplicate entry 'lisi' for key 'idx_username'
        if (ex.getMessage().contains("Duplicate entry")) {
            String[] split = ex.getMessage().split(" ");
            String msg = split[2] + "已存在";
            return R.error(msg);
        }
        return R.error("未知错误");
    }


}

