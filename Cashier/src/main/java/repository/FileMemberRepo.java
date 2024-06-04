/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

import domain.Member;

import java.io.EOFException;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import services.MemberRepository;

public class FileMemberRepo implements MemberRepository {

    private final String pathFile = "members.txt";

    @Override
    public Member addMember(Member member) {
        try {
            List<Member> existingMembers = findAllMembers();
            // Check idCard Already
            for (Member existingMember : existingMembers) {
                if (existingMember.getIdCard().equals(member.getIdCard())) {
                    System.out.println("Already have this ID card number...");
                    return null;
                }
            }
            member.setId(existingMembers.isEmpty()
                    ? 1
                    : existingMembers.get(existingMembers.size() - 1).getId() + 1);
            
            existingMembers.add(member);

            try ( ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(pathFile))) {
                for (Member existingMember : existingMembers) {
                    outputStream.writeObject(existingMember);
                }
            }
        } catch (IOException e) {
            System.out.println("register Unsuccesful!" + e.getMessage());
        }
        return member;
    }

    @Override
    public Member removeMember(int id) {
        List<Member> members = findAllMembers();
        Optional<Member> memberToRemove = members.stream()
                .filter(member -> member.getId() == id)
                .findFirst();

        if (memberToRemove.isPresent()) {
            members.remove(memberToRemove.get());
            try ( ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(pathFile))) {
                for (Member member : members) {
                    outputStream.writeObject(member);
                }
            } catch (IOException e) {
                System.out.println("Error saving members: " + e.getMessage());
                return null;
            }
            return memberToRemove.get();
        } else {
            System.out.println("Member not found.");
            return null;
        }
    }

    @Override
    public int findIdByIdCard(String idCard) {
        return findAllMembers().stream()
                .filter(member -> member.getIdCard().equals(idCard))
                .map(Member::getId)
                .findFirst()
                .orElse(-1); // Return -1 if not found
    }

    @Override
    public int findIdByTel(String tel) {
        return findAllMembers().stream()
                .filter(member -> member.getTel().equals(tel))
                .map(Member::getId)
                .findFirst()
                .orElse(-1); // Return -1 if not found
    }

    @Override
    public Member findMember(String tel) {
        return findAllMembers().stream()
                .filter(member -> member.getTel().equals(tel))
                .findFirst()
                .orElse(null); // Return null if not found
    }

    @Override
    public List<Member> findAllMembers() {
        List<Member> members = new ArrayList<>();
        try ( ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(pathFile))) {
            while (true) {
                try {
                    Member member = (Member) inputStream.readObject();
                    members.add(member);
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
                try ( ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(pathFile))) {
                    for (Member member : members) {
                        outputStream.writeObject(member);
                    }
                } catch (IOException e) {
                    System.out.println("Error saving members: " + e.getMessage());
                    return null;
                }
                return updatedMember;
            }
        }
        System.out.println("Member not found.");
        return null;
    }

    @Override
    public int increasePoint(String tel, int point) {
        List<Member> members = findAllMembers();
        for (Member member : members) {
            if (member.getTel().equals(tel)) {
                member.addPoint(point);
                try ( ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(pathFile))) {
                    for (Member m : members) {
                        outputStream.writeObject(m);
                    }
                } catch (IOException e) {
                    System.out.println("Error saving members: " + e.getMessage());
                    return -1;
                }
                return member.getPoint();
            }
        }
        System.out.println("Member not found.");
        return -1;
    }

    @Override
    public int decreasePoint(String tel, int point) {
        List<Member> members = findAllMembers();
        for (Member member : members) {
            if (member.getTel().equals(tel)) {
                member.minusPoint(point);
                try ( ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(pathFile))) {
                    for (Member m : members) {
                        outputStream.writeObject(m);
                    }
                } catch (IOException e) {
                    System.out.println("Error saving members: " + e.getMessage());
                    return -1;
                }
                return member.getPoint();
            }
        }
        System.out.println("Member not found.");
        return -1;
    }

    @Override
    public Stream<Member> getStream() {
        return findAllMembers().stream();
    }

}
