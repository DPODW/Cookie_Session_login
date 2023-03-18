package hello.login.domain.member;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;

@Slf4j
@Repository
public class MemberRepository {

    private static Map<Long, Member> store = new HashMap<>();
    private static long sequence = 0L;

    public Member save(Member member){
        member.setId(++sequence);
        log.info("save: member={}" ,member);
        store.put(member.getId(), member);
        return member;
    }
    public Member findById(Long id){
        return store.get(id); //map 형태로 저장되어있으니, 바로 value 를 꺼낼수 있음
    }

    /**
     * Optional 을 사용하여서 null 에 대한 처리를 하였다.
     * stream: List 형식이며, 루프 기능(반복 기능)이 있다.
     * filter(조건) -> member 의 아이디가 매개값 아이디와 같은지 검사
     * findFirst: 맨 처음 찾아지는것(찾아지면 뒤에 있는 값들은 무시됌)
     * ++ null 일시, Optional 에 의한 처리가 이루어짐
     * */
    public Optional<Member> findByLoginId(String loginId) {
        return findAll().stream()
                .filter(m -> m.getLoginId().equals(loginId))
                .findFirst();
    }
    public List<Member> findAll() {
      return  new ArrayList<>(store.values());
    }

    public void clearStore() {
        store.clear();
    }
}
/*
    optional , 람다를 사용하지 않은 findByLoginId
    List<Member> all = findAll();
//findAll 을 하여서 정보가 들어있는 member 를 찾음

        for (Member member : all) {
                if(member.getLoginId().equals(loginId)){
                //member 안에 든 로그인 아이디와, 매개값 아이디가 같다면
                return member;
                }
                }
                return null;*/