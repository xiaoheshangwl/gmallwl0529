package interceptor;

import annotation.LoginRequire;


import constant.CookieConstant;



import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import utils.CookieUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component
public class LoginRequireInterceptor implements HandlerInterceptor{
    //目标方法执行以前
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object o) throws Exception {
        //拿到将要执行的目标方法，判断是否需要登陆
        HandlerMethod handlerMethod =  (HandlerMethod)o;
        LoginRequire methodAnnotation = handlerMethod.getMethodAnnotation(LoginRequire.class);
        String token = request.getParameter("token");
        String cookieValue = CookieUtils.getCookieValue(request, CookieConstant.SSO_COOKIE_NAME);
        if(methodAnnotation != null){
            //有注解，验证是否存在登陆的cookie 和请求参数的token
            //1、验证是否第一次过来只带了一个参数位置的token字符串

            if(!StringUtils.isEmpty(token)){
                Cookie cookie = new Cookie(CookieConstant.SSO_COOKIE_NAME, token);
                cookie.setPath("/");
                response.addCookie(cookie);
                return true;
            }
            //2、验证是否只存在登陆的cookie
            if(!StringUtils.isEmpty(cookieValue)){
                //说明之前登陆过
                //验证令牌，远程验证

                RestTemplate restTemplate = new RestTemplate();
                String confirmTokenUrl = "http://www.gmallsso.com/confirmToken?token="+cookieValue;
                try{
                    String confirmToken = restTemplate.getForObject(confirmTokenUrl, String.class);

                    if("ok".equals(confirmToken)){
                        Map<String,Object> map = CookieUtils.resolveTokenData(cookieValue);
                        request.setAttribute("CookieConstant.USERINFO_IN_COOKIEVALUE",map);


                        return true;
                    }else{
                        String redirectUrl = "http://www.gmallsso.com/login?originUrl="+request.getRequestURL();
                        response.sendRedirect(redirectUrl);
                        return false;
                    }
                }catch (Exception e){
                    System.out.println("出错了！！！");
                   e.printStackTrace();
                }


            }
            //两个都没有
            if(StringUtils.isEmpty(token) && StringUtils.isEmpty(cookieValue)){
               String redirectUrl = "http://www.gmallsso.com/login?originUrl="+request.getRequestURL();
                response.sendRedirect(redirectUrl);
                return false;
            }
        }else{
            return true;
        }
        return false;
 }
//目标方法执行以后
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }
//页面渲染出来以后
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
