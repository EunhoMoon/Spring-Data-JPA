package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberJpaRepositoryTest {

    @Autowired
    MemberJpaRepository memberRepository;

    @Test
    public void testMember() {
        // when
        Member member = new Member("memberA");
        Member savedMember = memberRepository.save(member);

        // given
        Member findMember = memberRepository.find(savedMember.getId());

        // then
        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    public void basicCRUD() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        // 단건 조회 검증
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        // 리스트 조회 검증
        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        // count 검증
        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        // 삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);
        long deletedCount = memberRepository.count();
        assertThat(deletedCount).isEqualTo(0);
    }

    @Test
    public void paging() {
        // given
        memberRepository.save(new Member("member1", 10, null));
        memberRepository.save(new Member("member2", 10, null));
        memberRepository.save(new Member("member3", 10, null));
        memberRepository.save(new Member("member4", 10, null));
        memberRepository.save(new Member("member5", 10, null));

        int age = 10;
        int offset = 0;
        int limit = 3;

        // when
        List<Member> members = memberRepository.findByPage(age, offset, limit);
        long totalCount = memberRepository.totalCount(age);
        for (Member member : members) {
            System.out.println("member = " + member);
        }

        // then
        assertThat(members.size()).isEqualTo(3);
        assertThat(totalCount).isEqualTo(5);
    }

    @Test
    public void bulkUpdate() {
        // given
        memberRepository.save(new Member("member1", 10, null));
        memberRepository.save(new Member("member2", 19, null));
        memberRepository.save(new Member("member3", 20, null));
        memberRepository.save(new Member("member4", 21, null));
        memberRepository.save(new Member("member5", 40, null));

        // when
        int resultCount = memberRepository.bulkAgePlus(20);

        // then
        assertThat(resultCount).isEqualTo(3);
    }

}