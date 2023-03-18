package hello.login;

import hello.login.web.argumentResolver.LoginMemberArgumentResolver;
import hello.login.web.filter.LogFilter;
import hello.login.web.filter.loginCheckFilter;
import hello.login.web.interceptor.LogInterceptor;
import hello.login.web.interceptor.LoginCheckInterceptor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;
import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) { //인터셉터 등록
        registry.addInterceptor(new LogInterceptor())
                .order(1) //인터셉터 순위
                .addPathPatterns("/**") //인터셉터를 사용할 경로 (/** -> 전체 다 사용)
                .excludePathPatterns("/css/**", "/*.ico", "/error");  //addPathPatterns 에서 제외할 패턴

        registry.addInterceptor(new LoginCheckInterceptor())
                .order(2)
                .addPathPatterns("/**")
                .excludePathPatterns("/", "members/add","/login","/logout","/css/**","/*.ico","/error");
    }

    /**
     * 스프링 부트에서 필터를 사용하기 위한 설정
     *  FilterRegistrationBean 을 스프링 컨테이너에 빈으로 등록해서 사용한다.
     *  FilterRegistrationBean 에 우리가 사용할 필터의 설정을 한다.
     *
     * */
//    @Bean
    public FilterRegistrationBean logFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LogFilter()); //우리가 사용할 필터 등록
        filterRegistrationBean.setOrder(1); //필터 순서 설정
        filterRegistrationBean.addUrlPatterns("/*"); //url 형식 등록 (/* -> 모든 url 적용)

        return filterRegistrationBean;
    }

    //@Bean
    public FilterRegistrationBean loginCheckFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new loginCheckFilter()); //우리가 사용할 필터 등록
        filterRegistrationBean.setOrder(2); //필터 순서 설정
        filterRegistrationBean.addUrlPatterns("/*"); //url 형식 등록 (/* -> 모든 url 적용)

        return filterRegistrationBean;
    }
}
