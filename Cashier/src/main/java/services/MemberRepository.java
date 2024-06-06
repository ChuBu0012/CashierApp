package services;

import domain.Member;
import java.util.List;
import java.util.stream.Stream;

public interface MemberRepository {

    public Member addMember(Member member);

    public Member removeMember(int id);

    public int findIdByTel(String tel);
    public int findIdByIdCard(String idCard);

    public Member findMember(String tel);

    public List<Member> findAllMembers();

    public Member updateMember(Member updatedMember);

    public int increasePoint(String tel,int point);
    public int decreasePoint(String tel,int point);
    public Stream getStream();
}
