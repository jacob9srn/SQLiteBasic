package com.example.sqlitebasic;

public class MemoData {

   private String item_title;
   private String item_contens;
    private int item_id;

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public MemoData(String item_title, String item_contens, int item_id) {
        this.item_title = item_title;
        this.item_contens = item_contens;
        this.item_id = item_id;
    }

    public MemoData(String item_title, String item_contens) {
        this.item_title = item_title;
        this.item_contens = item_contens;
    }

    public String getItem_title() {
        return item_title;
    }

    public void setItem_title(String item_title) {
        this.item_title = item_title;
    }

    public String getItem_contens() {
        return item_contens;
    }

    public void setItem_contens(String item_contens) {
        this.item_contens = item_contens;
    }
}
