package com.example.sqlitebasic.adapter;

public class GreatManData {


    private String item_name;
    private String item_content;
    private String item_birthDay;
    private String item_nationality;
    private String item_date;
    private String item_mot;
    private int item_id;
    private byte[] item_image;

    public GreatManData(String item_name, String item_content, String item_birthDay, String item_nationality, String item_date, String item_mot, int item_id, byte[] item_image) {
        this.item_name = item_name;
        this.item_content = item_content;
        this.item_birthDay = item_birthDay;
        this.item_nationality = item_nationality;
        this.item_date = item_date;
        this.item_mot = item_mot;
        this.item_id = item_id;
        this.item_image = item_image;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_content() {
        return item_content;
    }

    public void setItem_content(String item_content) {
        this.item_content = item_content;
    }

    public String getItem_birthDay() {
        return item_birthDay;
    }

    public void setItem_birthDay(String item_birthDay) {
        this.item_birthDay = item_birthDay;
    }

    public String getItem_nationality() {
        return item_nationality;
    }

    public void setItem_nationality(String item_nationality) {
        this.item_nationality = item_nationality;
    }

    public String getItem_date() {
        return item_date;
    }

    public void setItem_date(String item_date) {
        this.item_date = item_date;
    }

    public String getItem_mot() {
        return item_mot;
    }

    public void setItem_mot(String item_mot) {
        this.item_mot = item_mot;
    }

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public byte[] getItem_image() {
        return item_image;
    }

    public void setItem_image(byte[] item_image) {
        this.item_image = item_image;
    }
}