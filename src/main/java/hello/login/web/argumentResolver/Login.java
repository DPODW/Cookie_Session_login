package hello.login.web.argumentResolver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER) //해당 어노테이션으로 받을 값의 형식(파라미터)
@Retention(RetentionPolicy.RUNTIME) //해당 어노테이션이 실행될 구간 (런타임)
public @interface Login {
}
