package com.example.sqlitebasic.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import com.example.sqlitebasic.R;
import com.example.sqlitebasic.database.MemoDbHelper;

public class MemoAdapter extends RecyclerView.Adapter<MemoAdapter.CustomViewHolder> {


    static final String TAG = "메인어뎁터";

    private ArrayList<MemoData> arrayList;
    public Context context;
    public MemoDbHelper memoDbHelper;

    public MemoAdapter(Context context, ArrayList<MemoData> arrayList) {
        this.arrayList = arrayList;
        this.context = context;

        Log.i(TAG,"MainAdapter(ArrayList<MemoData>) and ??arrayList.get(0).getitem_title?? : ");
    }

    @NonNull
    @Override
    public MemoAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    // LayoutInflater는 뷰를 만든다.
        Log.i(TAG,"==START onCreateViewHolder==");
         View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list,parent,false); // from: parent의 컨텍스트를 받아옴.
        CustomViewHolder holder = new CustomViewHolder(view); //아이템 1개의 레이아웃을 뷰홀더(?)에 전달한다.
        Log.i(TAG,"==end onCreateViewHolder== holder.toString() : "+holder.toString());
        return holder;
    }

    @Override // 뷰홀더에 1개의 데이터를 설정하는 부분.
    public void onBindViewHolder(@NonNull final MemoAdapter.CustomViewHolder holder, final int position) {
        Log.i(TAG,"==START onBindViewHolder==");
        MemoData item = arrayList.get(position); // arrayList
        holder.item_title.setText(item.getItem_title());
        holder.item_contents.setText((item.getItem_contens()));// setText가 안먹혔다 어떻게 해결했냐면 MemoData 안에 들어있는 변수명을 똑같이 바꿔주니까 됐다. 그전에는 et_title 이렇게했다.
        if( null !=item.getItem_image()) {
            holder.item_iv.setImageBitmap(BitmapFactory.decodeByteArray(item.getItem_image(), 0, item.getItem_image().length));
        }else{
            holder.item_iv.setImageResource(R.drawable.ic_more_horiz_black_24dp);
        }
        holder.itemView.setTag(position); // ?
        Log.i(TAG,"==holder.itemView.setTag(position) of next ==  position : " + position+"what is holder.itemView.setTag(position)?");
        Log.i(TAG,"==end onBindViewHolder==");
    }

        public void remove(int position){
          try{
              memoDbHelper = MemoDbHelper.getInstance(context);
              memoDbHelper.delete(position);
            arrayList.remove(position);
            notifyItemRemoved(position); // 노티파이는 새로고침

        }catch (IndexOutOfBoundsException ex){
            ex.printStackTrace();
        }
    }

  // 커스텀 리스너 만들기
    public interface OnItemClickListener{ // 어뎁터 내에서 커스텀 리스너 인터페이스를 정의. 질문해야겠다.
        void onItemClick(View v, int position);
    }


    private OnItemClickListener mListener = null; // 리스트 객체 참조를 저장하는 변수


    public void setOnItemClickListener(OnItemClickListener listener){
        Log.i(TAG,"insetOnItemClickListener");
        this.mListener = listener; //   외부에서 리스너 객체 참조를 어댑터에 전달하는 메서드
    }



    public class CustomViewHolder extends RecyclerView.ViewHolder{ // 내부클래스 이게 첫번째 같은데
        protected TextView item_title;
        protected TextView item_contents;
        protected ImageView item_iv;
        public CustomViewHolder(@NonNull View itemView) {

            super(itemView);
            Log.i(TAG,"==START CustomViewHolder== super.toString() : "+super.toString());

            this.item_title = (TextView)itemView.findViewById(R.id.item_title);
            this.item_contents = (TextView) itemView.findViewById(R.id.item_contents);
            this.item_iv = (ImageView) itemView.findViewById(R.id.item_list_iv);


           itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG,"in itemView.setOnclickListener");
                    int position = getAdapterPosition(); // 몇번째 리스트인지 아는 것.
                    if(position!=RecyclerView.NO_POSITION){
                        // 리사이클러뷰가 갱신됐을 때, 삭제로 인해 해당 아이템이 없을 수 있다.
                        if(mListener !=null){
                            mListener.onItemClick(v,position);
                        }
                    }
                }
            });

            Log.i(TAG,"==end CustomViewHolder==");
        }
    }

    @Override
    public int getItemCount() {

        Log.i(TAG,"getItemCount : "+ arrayList.size());
        return arrayList.size();
    }

}

