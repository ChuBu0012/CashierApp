package domain;

import java.io.Serializable;

public class Cashier implements Serializable {
    private int cashier_id;
    private String name;
    private String password;
    private String idCard;
    private Role role;
    private String tel;

    public Cashier(int cashier_id, String name, String password, String idCard, Role role, String tel) {
        this.cashier_id = cashier_id;
        this.name = name;
        this.password = password;
        this.idCard = idCard;
        this.role = role;
        this.tel = tel;
    }

    public int getCashier_id() {
        return cashier_id;
    }

    public void setCashier_id(int cashier_id) {
        this.cashier_id = cashier_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    @Override
    public String toString() {
        return
                "cashier_id=" + cashier_id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", idCard='" + idCard + '\'' +
                ", role='" + role + '\'' +
                ", tel='" + tel + '\'';
    }
}
