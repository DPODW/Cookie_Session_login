package hello.login.web.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

@Slf4j
public class LogFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
       log.info("log filter init");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("log filter doFilter");
        /**
         * ServletRequest , ServletResponse 는 HttpServletRequest , HttpServletResponse 의 부모 객체이다.
         * 그러나 기능이 별로 없으므로 Http~ 객체로 다운 캐스팅 하여서 사용한다.
         * */

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();

        String uuid = UUID.randomUUID().toString();
        //URI 를 구분해서 보기 위한 uuid

        try {
            log.info("REQUEST [{}][{}]", uuid, requestURI);
            chain.doFilter(request, response);
            //  chain.doFilter -> 다음 필터를 사용할거면 사용을 명시하고, 없으면 서블릿(정상 로직) 이 수행됌
            // request, response 이렇게 넘긴다는 것은 정상 로직으로 넘긴다는 것
        }catch(Exception e){
            throw  e;
        }finally {
            log.info("RESPONSE [{}][{}]", uuid, requestURI);
        }

    }

    @Override
    public void destroy() {
        log.info("log filter destroy");
    }
}
