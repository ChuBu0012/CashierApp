package repository;

import domain.Member;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import services.MemberRepository;

public class MemMemberRepo implements MemberRepository {

    private static int nextId = 0;
    private final Map<Integer, Member> repo = new HashMap<>();

    @Override
    public Member addMember(Member member) {
        return repo.put(nextId++, member);
    }

    @Override
    public Member removeMember(int id) {
        return repo.remove(id);
    }

    @Override
    public int findId(String tel) {
        Optional<Map.Entry<Integer, Member>> result = getStream()
                .filter(entry -> entry.getValue().getTel().equals(tel))
                .findFirst();

        return result.map(Map.Entry::getKey).orElse(-1);
    }

    @Override
    public Member findMember(String tel) {
        Optional<Map.Entry<Integer, Member>> result = getStream()
                .filter(entry -> entry.getValue().getTel().equals(tel))
                .findFirst();

        return result.map(Map.Entry::getValue).orElse(null);
    }
    
    @Override
    public List<Member> findAllMembers() {
        return new ArrayList<>(repo.values());
    }
    
    @Override
     public Member updateMember(Member updatedMember) {
        for (Map.Entry<Integer, Member> entry : repo.entrySet()) {
            Member member = entry.getValue();
            if (member.getIdCard().equals(updatedMember.getIdCard())) {
                
                member.setName(updatedMember.getName());
                member.setTel(updatedMember.getTel());
     
                return member;
            }
        }
        return null;
    }

    @Override
    public Stream<Map.Entry<Integer, Member>> getStream() {
        return repo.entrySet().stream();
    }
}
