package domain;

import java.io.Serializable;

public class Member implements Serializable{
    private String name;
    private String tel;
    private String idCard;

    public Member(String name, String tel, String idCard) {
        this.name = name;
        this.tel = tel;
        this.idCard = idCard;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    @Override
    public String toString() {
        return "Member{" + "name=" + name + ", tel=" + tel + ", idCard=" + idCard + '}';
    }
    
    
    
    
}
