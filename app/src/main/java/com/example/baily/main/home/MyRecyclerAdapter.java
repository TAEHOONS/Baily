package com.example.baily.main.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baily.R;

import java.util.List;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder>{
    private final List<CardItem> mDataList;
    public MyRecyclerAdapter(List<CardItem> dataList){
        mDataList=dataList;
    }
    public interface MyRecyclerViewClickListener{
        void onItemClicked(int position);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View lView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card,parent,false);
        return new ViewHolder(lView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CardItem item = mDataList.get(position);
        holder.kgInfor.setText(item.getKg());
        holder.cmInfor.setText(item.getCm());
        holder.girthInfo.setText(item.getHead());
        holder.feverInfo.setText(item.getfever());
        holder.recodeDate.setText(item.getRecodeDateNow());
        holder.recodeDday.setText(item.getNowDday());

    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }
    public void addItem(int position, CardItem item){
        mDataList.add(position, item);
        notifyItemInserted(position);
        notifyItemChanged(position,mDataList.size());
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView kgInfor;
        TextView cmInfor;
        TextView girthInfo;
        TextView feverInfo;
        TextView recodeDate;
        TextView recodeDday;

        public ViewHolder(View itemView){
            super(itemView);
            kgInfor = (TextView)itemView.findViewById(R.id.h_kgInfoTxt);
            cmInfor = (TextView)itemView.findViewById(R.id.h_cmInfoTxt);
            girthInfo = (TextView)itemView.findViewById(R.id.h_girthInfoTxt);
            feverInfo = (TextView)itemView.findViewById(R.id.h_feverInfoTxt);
            recodeDate = (TextView)itemView.findViewById(R.id.h_recodeDateTxt);
            recodeDday = (TextView)itemView.findViewById(R.id.h_recodeDdayTxt);

        }
    }
}
