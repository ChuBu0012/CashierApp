package services;

import domain.Member;
import java.util.List;
import java.util.stream.Stream;

public interface MemberRepository {

     Member addMember(Member member);
     Member removeMember(int id);

     int findIdByTel(String tel);
     int findIdByIdCard(String idCard);

     Member findMember(String tel);

     List<Member> findAllMembers();

     Member updateMember(Member updatedMember);

     int increasePoint(String tel,int point);
     int decreasePoint(String tel,int point);
     Stream getStream();
}
