package com.star.hander;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: 拦截异常处理
 * @Author: ONESTAR
 * @Date: Created in 15:48 2020/3/25
 * @QQ群: 530311074
 * @URL: https://onestar.newstar.net.cn/
 */
//拦截有Controller注解的控制器

/**
 *使用 @ControllerAdvice 实现全局异常处理，只需要定义类，添加该注解即可定义方式如下：
 *
 * @ExceptionHandler 注解用来指明异常的处理类型，
 * 即如果这里指定为 NullpointerException，则数组越界异常就不会进到这个方法中来。
 */
@ControllerAdvice
public class ControllerExceptionHandler  {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    //此注解可以指明异常的处理类型
    @ExceptionHandler(Exception.class)
    public ModelAndView exceptionHander(HttpServletRequest request, Exception e) throws Exception {
        logger.error("Requst URL : {}，Exception : {}", request.getRequestURL(),e);

//        当标识了状态码的时候就不拦截
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) {
            throw e;
        }

        ModelAndView mv = new ModelAndView();
        mv.addObject("url",request.getRequestURL());
        mv.addObject("exception", e);
        mv.setViewName("error/error");
        return mv;
    }
}