package com.example.baily.main.diary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baily.R;

import java.util.ArrayList;

public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalViewHolder>{
    private ArrayList<EventData> eventDataArrayList;
    private Context context;
    public HorizontalAdapter(ArrayList<EventData> list, FragDiaryDate context){
        eventDataArrayList = list;
    }

    @NonNull
    @Override
    public HorizontalViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.item_horizontal_event,viewGroup,false);

        HorizontalViewHolder viewHolder = new HorizontalViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HorizontalViewHolder holder, int position) {
        final EventData data = eventDataArrayList.get(position);


        holder.date.setText(data.getDate());
        holder.name.setText(data.getName());



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                Intent intent = new Intent(context, ShowEventInfo.class);
                intent.putExtra("eventName",data.getName());
                intent.putExtra("eventDate",data.getDate());
                intent.putExtra("eventMemo",data.getMemo());
                intent.putExtra("eventId",data.getId());

                bundle.putString("eventName",data.getName());
                bundle.putString("eventDate",data.getDate());
                bundle.putString("eventMemo",data.getMemo());
                bundle.putString("eventId",data.getId());
                ((Activity)context).startActivityForResult(intent, FragDiaryDate.SHOW_EVENT_INFO);
            }
        });
    }


    @Override
    public int getItemCount() {
        return eventDataArrayList.size();
    }

}

class HorizontalViewHolder extends RecyclerView.ViewHolder {
    public TextView name;
    public TextView date;
    HorizontalViewHolder(View view){
        super(view);
        name = (TextView) itemView.findViewById(R.id.item_name);
        date = (TextView) itemView.findViewById(R.id.item_date);

    }
}
