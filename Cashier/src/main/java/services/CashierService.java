package services;

import domain.Member;

import java.util.List;

public class CashierService {

    private final MemberRepository member;

    public CashierService(MemberRepository memberRepo) {
        member = memberRepo;
    }

    public Member registerMember(String name, String tel, String idCard) {
        if (name == null || name.isBlank()
                || tel == null || tel.isBlank()
                || idCard == null || idCard.isBlank()) {
            return null;
        }
        return member.addMember(new Member(name, tel, idCard));
    }

    public Member cancelMember(String tel) {
        int memId = getId(tel);
        if (memId != -1) {
            return member.removeMember(memId);
        }
        return null;
    }

    public int getId(String tel) {
        return member.findId(tel);
    }

    public Member getMember(String tel) {
        if (tel == null || tel.isBlank()) {
            return null;
        }
        return member.findMember(tel);
    }

    public List<Member> getAllMembers() {
        return member.findAllMembers();
    }

    public Member updateInfo(Member updatedMember) {
        if (updatedMember == null) {
            return null;
        }

        return member.updateMember(updatedMember);
    }

    public int collectPoint(String tel, int price) {
        if (tel == null || tel.isBlank()) {
            return -1;
        }
        return member.increasePoint(tel, price);
    }

    public int discount(String tel, int price) {
        if (tel == null || tel.isBlank()) {
            return -1;
        }
        return member.decreasePoint(tel, price);
    }

}
