package com.example.sqlitebasic;

public class MemoData {

   private String item_title;
   private String item_contens;

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
