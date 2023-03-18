package hello.login.web.session;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

@Slf4j
@RestController
public class SessionInfoController {

    @GetMapping("/session-info")
    public String sessionInfo(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if(session == null){
            return "세션이 없습니다!";
        }
        session.getAttributeNames().asIterator()
                .forEachRemaining(name -> log.info("session name={}, value={}",name,session.getAttribute(name)));
        /*  getAttributeNames 를 통해 세션의 값을 꺼내고, asIterator 로 반복, forEachRemaining 로 모든 요소가 처리될때 까지 () 안의 기능을 수행한다
        *   세션의 이름(name[loginMember]) 과 값(name 을 getAttribute 해서 얻은 값) 을 로그로 찍는다.
        * 세션 데이터 출력 과정임
        *  */

        log.info("sessionId={}", session.getId()); //세션 아이디
        log.info("getMaxInactiveInterval={}", session.getMaxInactiveInterval()); //세션 최대 허용 시간
        log.info("creationTime={}", new Date(session.getCreationTime())); //세션 생성 시간
        log.info("lastAccessedTime={}",new Date(session.getLastAccessedTime())); //세션이 마지막으로 있었던 시간
        log.info("isNew={}", session.isNew()); //새로만들어진 세션인지 여부

        return "세션 출력";
    }
}
