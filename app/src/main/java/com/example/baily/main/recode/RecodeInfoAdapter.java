package com.example.baily.main.recode;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baily.R;

import java.util.ArrayList;

class RecodeInfoAdapter extends RecyclerView.Adapter<RecodeInfoAdapter.CustomViewHolder> {
    private ArrayList<RecodeInfoItem> mList;
    Context context;
    private AdapterView.OnItemClickListener listener;
    private OnItemClickListener listenr;

    public  static interface  OnItemClickListener{
        public void onItemClick(CustomViewHolder holder, View view, int position);
    }
    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView tag;
        String tv;
        AdapterView.OnItemClickListener listener;



        public CustomViewHolder(View view) {
            super(view);
            this.tag =  view.findViewById(R.id.rec_tag);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(listenr != null ){
                        listenr.onItemClick(CustomViewHolder.this, itemView, position);
                    }
            tv = tag.getText().toString();

                }
            });
        }

        public void setOnItemClickListener(OnItemClickListener listener){
            this.listener = (AdapterView.OnItemClickListener) listener;
        }
    }
    public RecodeInfoAdapter(ArrayList<RecodeInfoItem> list) {
        this.mList = list;
    }
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recode_info_item, viewGroup, false);

        CustomViewHolder viewHolder = new CustomViewHolder(view);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewholder, int position) {

        viewholder.tag.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        viewholder.tag.setGravity(Gravity.CENTER);
        viewholder.tag.setText(mList.get(position).getTag());

        //클릭리스너
        viewholder.setOnItemClickListener((OnItemClickListener) listener);
    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }

    //클릭이벤트처리
    public void setOnItemClickListener(OnItemClickListener listenr){
        this.listenr = listenr;
    }


}