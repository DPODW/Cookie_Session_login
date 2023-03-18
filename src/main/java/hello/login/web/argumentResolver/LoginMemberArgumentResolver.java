package hello.login.web.argumentResolver;

import hello.login.domain.member.Member;
import hello.login.web.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) { //해당 어노테이션을 지원하는지 여부
            log.info("supportsParameter 실행");

        boolean hasLoginAnnotation = parameter.hasParameterAnnotation(Login.class);
        //해당 어노테이션이 붙어있는지 확인하는 로직. (HomeController 의 @Login 어노테이션이 붙어있는지 )
        boolean hasMemberType = Member.class.isAssignableFrom(parameter.getParameterType());
        //파라미터의 타입과, Member 타입이 일치하는지 확인하는 로직

        return hasLoginAnnotation && hasMemberType;
        //두가지 로직을 모두 통과했을때 true 리턴
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        log.info("resolverArgument 실행");

        HttpServletRequest request = (HttpServletRequest)webRequest.getNativeRequest();
        /**
         * webRequest: 웹에 관련된 요청 기능 처리
         * getNativeRequest: webRequest 에서 제공하는 기능으로, Http~ 를 직접 사용할수 있게 해준다.
         * */
        HttpSession session = request.getSession();
        if(session == null){
            return null;
        }
        return session.getAttribute(SessionConst.LOGIN_MEMBER);
    }
}
