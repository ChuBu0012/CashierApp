/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository.member;

import domain.Member;
import services.MemberRepository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class FileMemberRepo implements MemberRepository {

    private static final String PATH_FILE = "members.txt";

    @Override
    public Member addMember(Member member) {
        List<Member> existingMembers = findAllMembers();

        // Check for duplicate idCard
        if (existingMembers.stream().anyMatch(m -> m.getIdCard().equals(member.getIdCard()))) {
            System.out.println("Already have this ID card number...");
            return null;
        }

        member.setId(generateNewId(existingMembers));
        existingMembers.add(member);
        writeToFile(existingMembers);
        return member;
    }

    @Override
    public Member removeMember(int id) {
        List<Member> members = findAllMembers();
        Optional<Member> memberToRemove = members.stream()
                .filter(m -> m.getId() == id)
                .findFirst();

        if (memberToRemove.isPresent()) {
            members.remove(memberToRemove.get());
            writeToFile(members);
            return memberToRemove.get();
        } else {
            System.out.println("Member not found.");
            return null;
        }
    }

    @Override
    public int findIdByIdCard(String idCard) {
        return findAllMembers().stream()
                .filter(m -> m.getIdCard().equals(idCard))
                .mapToInt(Member::getId)
                .findFirst()
                .orElse(-1);
    }

    @Override
    public int findIdByTel(String tel) {
        return findAllMembers().stream()
                .filter(m -> m.getTel().equals(tel))
                .mapToInt(Member::getId)
                .findFirst()
                .orElse(-1);
    }

    @Override
    public Member findMember(String tel) {
        return findAllMembers().stream()
                .filter(m -> m.getTel().equals(tel))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Member> findAllMembers() {
        List<Member> members = new ArrayList<>();
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(PATH_FILE));
             ObjectInputStream ois = new ObjectInputStream(bis)) {
            while (true) {
                try {
                    members.add((Member) ois.readObject());
                } catch (EOFException e) {
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("File Not Found : " + e.getMessage());
        }
        return members;
    }

    @Override
    public Member updateMember(Member updatedMember) {
        List<Member> members = findAllMembers();
        for (int i = 0; i < members.size(); i++) {
            if (members.get(i).getId() == updatedMember.getId()) {
                members.set(i, updatedMember);
                writeToFile(members);
                return updatedMember;
            }
        }
        System.out.println("Member not found.");
        return null;
    }

    @Override
    public int increasePoint(String tel, int point) {
        return updatePointForMember(tel, point, true);
    }

    @Override
    public int decreasePoint(String tel, int point) {
        return updatePointForMember(tel, point, false);
    }

    @Override
    public Stream<Member> getStream() {
        return findAllMembers().stream();
    }

    private int generateNewId(List<Member> members) {
        return members.isEmpty() ? 1 : members.stream()
                .mapToInt(Member::getId)
                .max()
                .orElse(0) + 1;
    }

    private void writeToFile(List<Member> members) {
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(PATH_FILE));
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            for (Member member : members) {
                oos.writeObject(member);
            }
        } catch (IOException e) {
            System.out.println("Error saving members: " + e.getMessage());
        }
    }

    private int updatePointForMember(String tel, int point, boolean increase) {
        List<Member> members = findAllMembers();
        for (Member member : members) {
            if (member.getTel().equals(tel)) {
                if (increase) {
                    member.addPoint(point);
                } else {
                    member.minusPoint(point);
                }
                writeToFile(members);
                return member.getPoint();
            }
        }
        System.out.println("Member not found.");
        return -1;
    }
}