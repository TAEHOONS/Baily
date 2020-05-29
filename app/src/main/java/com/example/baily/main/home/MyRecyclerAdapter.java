package com.example.baily.main.home;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baily.R;

import java.util.ArrayList;
import java.util.List;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder> {
    private List<CardItem> mDataList;

    public MyRecyclerAdapter(List<CardItem> mDataList) {
        this.mDataList = mDataList;
    }

    //public interface MyRecyclerViewClickListener{
    //   void onItemClicked(int position);
    //}
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View lView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        return new ViewHolder(lView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        CardItem item = mDataList.get(position);
        holder.kgInfor.setText(item.getKg());
        holder.cmInfor.setText(item.getCm());
        holder.girthInfo.setText(item.getHead());
        holder.recodeDate.setText(item.getRecodeDateNow());
        holder.recodeDday.setText(item.getNowDday());

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//카드아이템을 눌렀을 때
                String curDate = holder.recodeDday.getText().toString();
                Toast.makeText(v.getContext(), curDate, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }
    public void addItem(CardItem item) {
        mDataList.add(item);
    }

    public void setItems(ArrayList<CardItem> mDataList) {
        this.mDataList = mDataList;
    }
    /*
    public void addItem(int position, CardItem item) {
        mDataList.add(position, item);
        notifyItemInserted(position);
        notifyItemRangeChanged(position, mDataList.size());
    }
    */


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView kgInfor;
        TextView cmInfor;
        TextView girthInfo;
        TextView recodeDate;
        TextView recodeDday;

        public ViewHolder(View itemView) {
            super(itemView);
            kgInfor = (TextView) itemView.findViewById(R.id.h_kgInfoTxt);
            cmInfor = (TextView) itemView.findViewById(R.id.h_cmInfoTxt);
            girthInfo = (TextView) itemView.findViewById(R.id.h_girthInfoTxt);
            recodeDate = (TextView) itemView.findViewById(R.id.h_recodeDateTxt);
            recodeDday = (TextView) itemView.findViewById(R.id.h_recodeDdayTxt);

        }
    }
}
