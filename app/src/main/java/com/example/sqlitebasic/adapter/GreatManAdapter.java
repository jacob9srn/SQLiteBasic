package com.example.sqlitebasic.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sqlitebasic.R;

import java.util.ArrayList;

// CTRL + I  눌러보면 ONCREATE 자동 으로 생성해줌
public class GreatManAdapter extends RecyclerView.Adapter<GreatManAdapter.CustomViewHolder> {

    private Context mContext;
    private ArrayList<GreatManData> mArraylist;


    public GreatManAdapter(Context context, ArrayList<GreatManData> arrayList) {
        mContext = context;
        mArraylist = arrayList;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_list_greatman, parent, false);
        return new CustomViewHolder(view); // 이름을 다음부터는 달리줘야겠다. 다른 어뎁터와 이름이 겹친다.
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {

        GreatManData item = mArraylist.get(position); // arrayList
        holder.nameText.setText(item.getItem_name());
        if( null !=item.getItem_image()) {
            holder.imageView.setImageBitmap(BitmapFactory.decodeByteArray(item.getItem_image(), 0, item.getItem_image().length));
        }else{
            holder.imageView.setImageResource(R.drawable.ic_more_horiz_black_24dp);
        }
        holder.itemView.setTag(position); // ?


    }


    // 커스텀 리스너 만들기
    public interface OnItemClickListener{ // 어뎁터 내에서 커스텀 리스너 인터페이스를 정의. 질문해야겠다.
        void onItemClick(View v, int position);
    }

    private MemoAdapter.OnItemClickListener mListener = null; // 리스트 객체 참조를 저장하는 변수

    public void setOnItemClickListener(MemoAdapter.OnItemClickListener listener){
        this.mListener = listener; //   외부에서 리스너 객체 참조를 어댑터에 전달하는 메서드
    }


    @Override
    public int getItemCount() {
        return mArraylist.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        public TextView nameText;
        public TextView birthDay;
        public TextView nationality;
        public ImageView imageView;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.modify_itemlist_great_name);
            //birthDay = itemView.findViewById(R.id.itemlist_great_birthYear);
            imageView = itemView.findViewById(R.id.itemlist_great_iv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition(); // 몇번째 리스트인지 아는 것.
                    if(position!=RecyclerView.NO_POSITION){
                        // 리사이클러뷰가 갱신됐을 때, 삭제로 인해 해당 아이템이 없을 수 있다.
                        if(mListener !=null){
                            mListener.onItemClick(v,position);
                        }
                    }
                }
            });
        }
    }
}
