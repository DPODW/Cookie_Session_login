package hello.login.web.session;

import hello.login.domain.member.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletResponse;

import static org.assertj.core.api.Assertions.*;

public class SessionManagerTest {

    SessionManger sessionManger = new SessionManger();

    @Test
    void sessionTest() {

        //세션 생성
        MockHttpServletResponse response = new MockHttpServletResponse();
        //MockHttpServletResponse <- test 환경에서 response 를 사용하는것은 어렵기 때문에 (Http~ 이게 interface 임)
        //스프링에서 가짜 http 객체를 제공한다. (테스트 용도임)


        Member member = new Member();
        sessionManger.createSession(member, response);

        //요청에 응답 쿠키 저장
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(response.getCookies());

        //세션 조회
        Object result = sessionManger.getSession(request);
        assertThat(result).isEqualTo(member);


        //세션 만료
        sessionManger.expire(request);
        Object expired = sessionManger.getSession(request);
        assertThat(expired).isNull();
    }
}
