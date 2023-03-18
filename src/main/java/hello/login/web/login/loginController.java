package hello.login.web.login;

import hello.login.domain.login.loginService;
import hello.login.domain.member.Member;
import hello.login.web.SessionConst;
import hello.login.web.session.SessionManger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequiredArgsConstructor
public class loginController implements SessionConst {

    private final loginService loginService1;
    private final SessionManger sessionManger;


    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm")LoginForm form){
        return "login/loginForm";
    }

//    @PostMapping("/login")
    public String login(@Validated @ModelAttribute LoginForm form, BindingResult bindingResult, HttpServletResponse response){
        if(bindingResult.hasErrors()){
            return "login/loginForm";
        }
        Member loginMember = loginService1.login(form.getLoginId(), form.getPassword());

        if(loginMember == null){
            bindingResult.reject("loginfail", "아이디 또는 비밀번호가 맞지 않습니다");
            return "login/loginForm";
        }
        
        // 로그인 성공 처리

        Cookie idCookie = new Cookie("memberId", String.valueOf(loginMember.getId()));
        /*쿠키 필요 parameter -> id , 쿠키 value (string 타입)
        *                                   ㄴ loginMember.getId == int
        *                                    ㄴ 그래서 string 으로 변환
        * */ 
        response.addCookie(idCookie); //세션 쿠키임
        
        return "redirect:/";
    }

//    @PostMapping("/login")
    public String loginV2(@Validated @ModelAttribute LoginForm form, BindingResult bindingResult, HttpServletResponse response){
        if(bindingResult.hasErrors()){
            return "login/loginForm";
        }
        Member loginMember = loginService1.login(form.getLoginId(), form.getPassword());

        if(loginMember == null){
            bindingResult.reject("loginfail", "아이디 또는 비밀번호가 맞지 않습니다");
            return "login/loginForm";
        }

        //세션 관리자를 통해 세션을 생성하고, 회원 데이터를 보관
        sessionManger.createSession(loginMember, response);

        return "redirect:/";
    }

//    @PostMapping("/login")
    public String loginV3(@Validated @ModelAttribute LoginForm form, BindingResult bindingResult,HttpServletRequest request){
        if(bindingResult.hasErrors()){
            return "login/loginForm";
        }
        Member loginMember = loginService1.login(form.getLoginId(), form.getPassword());

        if(loginMember == null){
            bindingResult.reject("loginfail", "아이디 또는 비밀번호가 맞지 않습니다");
            return "login/loginForm";
        }

        //로그인 성공 처리
        //세션이 있으면 있는 세션 반환, 없으면 신규 세션을 생성
        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER,loginMember);

        return "redirect:/";
    }

    @PostMapping("/login")
    public String loginV4(@Validated @ModelAttribute LoginForm form, BindingResult bindingResult,
                          @RequestParam(defaultValue = "/") String redirectURL
                          ,HttpServletRequest request){
        if(bindingResult.hasErrors()){
            return "login/loginForm";
        }
        Member loginMember = loginService1.login(form.getLoginId(), form.getPassword());

        if(loginMember == null){
            bindingResult.reject("loginfail", "아이디 또는 비밀번호가 맞지 않습니다");
            return "login/loginForm";
        }

        //로그인 성공 처리
        //세션이 있으면 있는 세션 반환, 없으면 신규 세션을 생성
        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER,loginMember);

        return "redirect:" + redirectURL;
        //loginCheckFilter 에서 생성해준 url 을 @RequestParam 으로 받아오고, 실질적으로 리다이렉트 해준다
    }

    //@PostMapping("/logout")
    public String logout(HttpServletResponse response){
        expireCookie(response,"memberId");
        return "redirect:/";
    }

//    @PostMapping("/logout")
    public String logoutV2(HttpServletRequest request){
       sessionManger.expire(request);
        return "redirect:/";
    }


    @PostMapping("/logout")
    public String logoutV3(HttpServletRequest request){
    HttpSession session = request.getSession(false);
    /*getSession 에서 true(default) 값을 주게 되면 getSession 시, 세션이 없으면 새로 생성한다.
    그러나 false 를 주면 session 이 없으면 새로 만드는 것이 아닌 null 를 리턴한다.*/
        
    if(session != null){
        session.invalidate(); //invalidate -> 세션을 날린다(없엔다)
    }
        return "redirect:/";
    }
    
    private void expireCookie(HttpServletResponse response, String cookieName){
        Cookie Xcookie = new Cookie(cookieName, null);
        Xcookie.setMaxAge(0);
        response.addCookie(Xcookie);
    }
}
