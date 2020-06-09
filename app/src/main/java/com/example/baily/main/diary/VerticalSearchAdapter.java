package com.example.baily.main.diary;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baily.R;

import java.util.ArrayList;

public class VerticalSearchAdapter extends RecyclerView.Adapter<VerticalSearchViewHolder> {
    private ArrayList<EventData> eventDataArrayList;
    private Context context;
    public VerticalSearchAdapter(ArrayList<EventData> list, Context context){
        eventDataArrayList = list;
        this.context = context;
    }
    @NonNull
    @Override
    public VerticalSearchViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.item_search_event,viewGroup,false);

        VerticalSearchViewHolder viewHolder = new VerticalSearchViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull VerticalSearchViewHolder holder, int position) {
        final EventData data = eventDataArrayList.get(position);

        holder.name.setText(data.getTitle());

        String[] dateParse = data.getDate().split("-");
        String yearForm = dateParse[0]+"년";
        String month_dayForm = dateParse[1]+"월" + dateParse[2]+ "일";

        holder.year.setText(yearForm);
        holder.month_day.setText(month_dayForm);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ShowEventInfo.class);
                intent.putExtra("eventName",data.getTitle());
                intent.putExtra("eventDate",data.getDate());
                intent.putExtra("eventMemo",data.getMemo());
                intent.putExtra("eventId",data.getId());
                ((SearchEvent)context).startActivityForResult(intent, FragDiaryDate.SHOW_EVENT_INFO);
            }
        });

    }

    @Override
    public int getItemCount() {
        return eventDataArrayList.size();
    }
}

class VerticalSearchViewHolder extends RecyclerView.ViewHolder {
    public TextView name;
    public TextView year;
    public TextView month_day;
    VerticalSearchViewHolder(View view){
        super(view);
        name = (TextView) itemView.findViewById(R.id.text_eventName);
        year = (TextView) itemView.findViewById(R.id.text_eventDate_year);
        month_day = (TextView) itemView.findViewById(R.id.text_eventDate_month_day);

    }
}