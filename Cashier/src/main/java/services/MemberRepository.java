package services;

import domain.Member;
import java.util.List;
import java.util.stream.Stream;

public interface MemberRepository {

    public Member addMember(Member member);

    public Member removeMember(int id);

    public int findId(String tel);

    public Member findMember(String tel);

    public List<Member> findAllMembers();

    public Member updateMember(Member updatedMember);

    public Stream getStream();
}
