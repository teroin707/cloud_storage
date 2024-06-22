package com.nyist.config;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginHandlerInterceptor implements HandlerInterceptor {
    /**
     * 拦截�?
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取session中的用户�?
        Object loginUser = request.getSession().getAttribute("USER_ID");
        if (loginUser == null){
            //为空就返回登�?
            request.setAttribute("status","登录已过�?");
            request.getRequestDispatcher("/loginandreg.html").forward(request,response);
            return false;
        }else{
            return true;
        }
    }
}
