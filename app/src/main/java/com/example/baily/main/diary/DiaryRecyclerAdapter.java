package com.example.baily.main.diary;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baily.R;
import com.example.baily.main.home.CardItem;
import com.example.baily.main.home.MyRecyclerAdapter;

import java.util.List;

public class DiaryRecyclerAdapter extends RecyclerView.Adapter<DiaryRecyclerAdapter.ViewHolder>{
    private final List<DiaryItem> mDataList;
    public DiaryRecyclerAdapter(List<DiaryItem> dataList){
        mDataList=dataList;
    }
    public interface MyRecyclerViewClickListener{
        void onItemClicked(int position);
    }
    @NonNull
    @Override
    public DiaryRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View lView = LayoutInflater.from(parent.getContext()).inflate(R.layout.diary_item,parent,false);
        return new DiaryRecyclerAdapter.ViewHolder(lView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DiaryItem item = mDataList.get(position);
        holder.diaryTitle.setText(item.getDiaryTitle());
        holder.diaryDate.setText(item.getRecodeDay());
        holder.diaryContents.setText(item.getDiaryContents());
        //holder.diaryPicture.setImageResource(item.getDiaryImg());

    }


    @Override
    public int getItemCount() {
        return mDataList.size();
    }
    public void addItem(int position, DiaryItem item){
        mDataList.add(position, item);
        notifyItemInserted(position);
        notifyItemChanged(position,mDataList.size());
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView diaryTitle;
        TextView diaryDate;
        TextView diaryContents;
       // ImageView diaryPicture;

        public ViewHolder(View itemView){
            super(itemView);
            diaryTitle = (TextView)itemView.findViewById(R.id.diaryTitleTxt);
            diaryDate = (TextView)itemView.findViewById(R.id.diaryDate);
            diaryContents = (TextView)itemView.findViewById(R.id.diaryContents);
          //  diaryPicture = (ImageView) itemView.findViewById(R.id.diaryPicture);

        }
    }
}
