/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package sit.cashier;

import domain.Member;
import repository.MemMemberRepo;
import services.CashierService;

/**
 *
 * @author Student
 */
public class Cashier {

    public static void main(String[] args) {
        MemMemberRepo memberService = new MemMemberRepo();
        CashierService cashier = new CashierService(memberService);
        cashier.registerMember("Most", "0952968102", "1102400123456");
        cashier.registerMember("Both", "0933362989", "1102400789012");
        cashier.getAllMembers().stream().forEach(ele->System.out.println(ele));
        cashier.cancelMember("0952968102");
        System.out.println("");
        Member m2 = new Member("Most", "0985101234", "1102400789012");
        cashier.updateInfo(m2);
        cashier.getAllMembers().stream().forEach(ele->System.out.println(ele));
    }
}
