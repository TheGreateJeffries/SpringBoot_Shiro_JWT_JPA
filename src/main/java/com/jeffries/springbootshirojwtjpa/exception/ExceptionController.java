package com.jeffries.springbootshirojwtjpa.exception;

import com.jeffries.springbootshirojwtjpa.dto.ResultMap;
import javax.servlet.http.HttpServletRequest;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.AccountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {
  private final ResultMap resultMap;

  @Autowired
  public ExceptionController(ResultMap resultMap) {
    this.resultMap = resultMap;
  }

  // 捕捉shiro的异常
  @ExceptionHandler(ShiroException.class)
  public ResultMap handle401() {
    return resultMap.fail().code(401).message("您没有权限访问！");
  }

  // 捕捉其他所有异常
  @ExceptionHandler(Exception.class)
  public ResultMap globalException(HttpServletRequest request, Throwable ex) {
    return resultMap.fail()
        .code(getStatus(request).value())
        .message("访问出错，无法访问: " + ex.getMessage());
  }

  private HttpStatus getStatus(HttpServletRequest request) {
    Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
    if (statusCode == null) {
      return HttpStatus.INTERNAL_SERVER_ERROR;
    }
    return HttpStatus.valueOf(statusCode);
  }
}
