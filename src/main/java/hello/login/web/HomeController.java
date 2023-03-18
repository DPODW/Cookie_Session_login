package hello.login.web;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import hello.login.web.argumentResolver.Login;
import hello.login.web.session.SessionManger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {
/**
 * memberId -> 순서
 * memberLoginId -> 실질적 아이디
 *
 * */
    private final MemberRepository memberRepository;
    private final SessionManger sessionManger;

//    @GetMapping("/")
    public String home() {
        return "home";
    }

//    @GetMapping("/")
    public String homeLogin(@CookieValue(name = "memberId", required = false) Long memberId, Model model){
        /* Spring 제공 쿠키를 가져오는 어노테이션 -> @CookieValue
        *  쿠키 name 지정 하여서 쿠키 특정, 로그인 하지 않은 사용자도 접근해야 하기 때문에 require -> false
        * */

        if(memberId == null) {
            return "home";
        }
        
        //로그인
        Member loginMember = memberRepository.findById(memberId);
        if(loginMember == null){
            return "home";
        }
            model.addAttribute("member",loginMember);
            return "loginHome";
    }

//    @GetMapping("/")
    public String homeLoginV2(HttpServletRequest request, Model model){

        //세션 관리자에 저장된 회원 정보 조회
        Member member = (Member) sessionManger.getSession(request);

        if(member == null){
            return "home";
        }
        model.addAttribute("member",member);
        return "loginHome";
    }

//        @GetMapping("/")
         public String homeLoginV3(HttpServletRequest request, Model model){
            HttpSession session = request.getSession(false);
            if(session == null) {
                return "home";
            }

            Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
            //getAttribute 의 name 은 판별을 위한 이름일 뿐.

        //세션에 회원 데이터가 없으면 home
        if(loginMember == null){
            return "home";
        }

        //세션이 유지되면 로그인으로 이동
        model.addAttribute("member",loginMember);
        return "loginHome";
    }

//    @GetMapping("/")
    public String homeLoginV3Spring(@SessionAttribute(name = SessionConst.LOGIN_MEMBER,required = false)Member loginMember, Model model){
        //로그인이 되었을때만 세션이 생성되어야 하는데, true 속성으로 무조건 생성되게 하면 400 발생

        if(loginMember == null) {
            return "home";
        }
        //세션이 유지되면 로그인으로 이동
        model.addAttribute("member",loginMember);
        return "loginHome";
    }

    @GetMapping("/")
    public String homeLoginV3ArgumentResolver(@Login Member loginMember, Model model){


        if(loginMember == null) {
            return "home";
        }
        //세션이 유지되면 로그인으로 이동
        model.addAttribute("member",loginMember);
        return "loginHome";
    }

}