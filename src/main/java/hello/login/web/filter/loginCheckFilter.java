package hello.login.web.filter;

import hello.login.web.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.regex.Pattern;

@Slf4j
public class loginCheckFilter implements Filter {
    private static final String[] whitelist = {"/", "/members/add", "/login", "/logout", "/css/*"};

    /**
     * init , destroy 는 디폴트라서 생략 가능 (굳이 구현 할 필요 없음)
     * whitelist: 필터를 제외할 경로 (모든 고객이 일단은 들어와야할 페이지... 일단 회원가입 하려고 해도 들어는 와야 하니까 , css(화면은 정상 출력되어야지!))
     * */

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();

        HttpServletResponse httpResponse = (HttpServletResponse) response;

        try{
            log.info("인증 체크 필터 시작{}", requestURI);

            if(isLoginCheckPath(requestURI)){
                log.info("인증 체크 로직 실행{}", requestURI);
                HttpSession session = httpRequest.getSession(false);
                if(session == null || session.getAttribute(SessionConst.LOGIN_MEMBER)==null){
                    log.info("미인증 사용자 요청{}",requestURI);
                    //로그인 화면으로 리다이렉트
                    httpResponse.sendRedirect("/login?redirectURL=" + requestURI);
                    /**
                     * 로그인 하지 않는 회원이 로그인이 필요한 화면 접근시, 로그인 화면으로 이동된다.
                     * 그러면 고객은 로그인을 하고, 바로 자신이 좀 전에 보고 있던 화면으로 이동되는 것이 생산적이다.
                     * 해당 리다이렉트 기능은 그러한 처리를 해준다.
                     *  미 인증 사용자 로그인 필요 화면 접근 -> 로그인 화면으로 이동 -> 로그인 성공 -> 첫번째 화면으로 자동 리다이렉트
                     *      ㄴ 일단 해당 기능이 해주는 구간은 requestURI 을 URL 에 띄워주는 구간 까지다.
                     *      ㄴ 즉 해당 기능 만으로 첫번째 화면 리다이렉트가 가능하진 않음 (LoginController 에서 해당 기능 처리)
                     * */
                    return;

                }
            }
            chain.doFilter(request,response);
        } catch (Exception e){
            throw  e;
        }finally {
            log.info("인증 체크 필터 종료{}"+request);
        }
    }

    /**
     * 화이트 리스트의 경우 인증 체크 x
     * */
    private boolean isLoginCheckPath(String requestURI) {
        return !PatternMatchUtils.simpleMatch(whitelist, requestURI);
        //PatternMatchUtils.simpleMatch: uri 의 경로를 비교해주는 스프링 제공 메소드. ! 부정 연산자를 통해
        // whitelist uri 와 매치가 안되는것들 만 추려낸다. (필터링 되어야 할 경로 말이다)

    }

}
