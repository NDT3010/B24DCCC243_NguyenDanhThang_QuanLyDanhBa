package com.example.quanlydanhba;

import java.io.Serializable;

/**
 * Lớp biểu diễn một liên hệ trong danh bạ.
 * Implements Serializable để có thể truyền qua Intent (putExtra / getSerializableExtra).
 */
public class Contact implements Serializable {

    private static final long serialVersionUID = 1L;

    private String fullName;
    private String phone;
    private String group;
    private String gender;
    private boolean favorite;
    private String note;

    public Contact(String fullName, String phone, String group,
                   String gender, boolean favorite, String note) {
        this.fullName = fullName;
        this.phone = phone;
        this.group = group;
        this.gender = gender;
        this.favorite = favorite;
        this.note = note;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
