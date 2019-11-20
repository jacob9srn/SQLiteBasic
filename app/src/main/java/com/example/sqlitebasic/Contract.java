package com.example.sqlitebasic;

import android.provider.BaseColumns;

public final class Contract { // 개념이므로 상속을 금지하기 위해 파이날
    //인스턴스화 금지
    private Contract(){

    }
    public static class MemoEntry implements BaseColumns { //베이스컬럼? : _ID 와 _COUNT 를 상수로 가지고 있다.
        // 만약 여러개의 테이블 정보를 계약클래스에 정의하려면 테이블 개수만큼 내부 클래스로 엔트리를 생성하면 된다.

        public static final String TABLE_NAME = "memo";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_CONTENTS = "contents";
        public static final String COLUM_NAME_IMAGE = "image";
    }


}
