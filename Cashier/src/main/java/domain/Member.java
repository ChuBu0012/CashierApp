package domain;

import java.io.Serializable;

public class Member implements Serializable {
    private int id;
    private String name;
    private String tel;
    private String idCard;
    private int point = 0;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPoint() {
        return point;
    }

    public int addPoint(int point) {
        return this.point += point;
    }

    public int minusPoint(int point) {
        return this.point -= point;
    }

    @Override
    public String toString() {
        return "Member{" +
                "name='" + name + '\'' +
                ", tel='" + tel + '\'' +
                ", idCard='" + idCard + '\'' +
                ", point=" + point +
                '}';
    }
}
