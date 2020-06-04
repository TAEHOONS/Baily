package com.example.baily.main.recode;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baily.R;

import java.util.ArrayList;

public class RecodeAdapter extends RecyclerView.Adapter<RecodeAdapter.ViewHolder>  {
    Context context;
    ArrayList<RecodeData> items = new ArrayList<RecodeData>();
    ArrayList<RecodeData> filteredList;

    //클릭이벤트처리 관련 사용자 정의(이 코드없으면 그냥 리사이클러뷰 구조)//////////////////////////////////////////////////////////////////////////
    OnItemClickListener listener; //참고로 OnItemClickListener는 기존에 있는것과 동일한 이름인데 그냥 같은 이름으로 내가 정의를 했다. (리스트뷰에서는 이게 자동구현되있어서 OnItemClickListener를 구현안하고 호출해서 클릭시 이벤트를 처리할 수 있음)




    public  static interface  OnItemClickListener{
        public void onItemClick(ViewHolder holder, View view, int position);
    }



    public  RecodeAdapter(Context context){
        this.context =  context;
    }

    @Override //어댑터에서 관리하는 아이템의 개수를 반환
    public int getItemCount() {
        return items.size();
    }


    @NonNull
    @Override //뷰홀더가 만들어지는 시점에 호출되는 메소드(각각의 아이템을 위한 뷰홀더 객체가 처음만들어지는시점)
    //만약에 각각의 아이템을 위한 뷰홀더가 재사용될 수 있는 상태라면 호출되지않음 (그래서 편리함, 이건내생각인데 리스트뷰같은경우는 convertView로 컨트롤해줘야하는데 이건 자동으로해줌)
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.recode_item,  viewGroup, false);//viewGroup는 각각의 아이템을 위해서 정의한 xml레이아웃의 최상위 레이아우싱다.

        return new ViewHolder(itemView); //각각의 아이템을 위한 뷰를 담고있는 뷰홀더객체를 반환한다.(각 아이템을 위한 XML 레이아웃을 이용해 뷰 객체를 만든 후 뷰홀더에 담아 반환
    }



    //각각의 아이템을 위한 뷰의 xml레이아웃과 서로 뭉쳐지는(결합되는) 경우 자동으로 호출( 즉 뷰홀더가 각각의 아이템을 위한 뷰를 담아주기위한 용도인데 뷰와 아이템이 합질때 호출)
    // Replace the contents of a view //적절한 데이터를 가져와 뷰 소유자의 레이아웃을 채우기 위해 사용(뷰홀더에 각 아이템의 데이터를 설정함)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {


        RecodeData item = items.get(position); //리사이클러뷰에서 몇번쨰게 지금 보여야되는시점이다 알려주기위해
        viewHolder.setItem(item); //그거를 홀더에넣어서 뷰홀더가 데이터를 알 수 있게되서 뷰홀더에 들어가있는 뷰에다가 데이터 설정할 수 있음

        //클릭리스너
        viewHolder.setOnItemClickListener(listener);

    }

    //아이템을 한개 추가해주고싶을때
    public  void addItem(RecodeData item){
        items.add(item);
    }

    //한꺼번에 추가해주고싶을때
    public void addItems(ArrayList<RecodeData> items){
        this.items = items;
    }


    public RecodeData getItem(int position){
        return  items.get(position);
    }

    //클릭리스너관련
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    //뷰홀더
    //뷰홀더 객체는 뷰를 담아두는 역할을 하면서 동시에 뷰에 표시될 데이터를 설정하는 역할을 맡을 수 있습니다.
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        TextView textView2;
        ImageView imageView;
        TextView textView3;
        OnItemClickListener listenr; //클릭이벤트처리관련 변수

        public ViewHolder(@NonNull final View itemView) { //뷰홀더는 각각의 아이템을 위한 뷰를 담고있다.
            super(itemView);

            textView = itemView.findViewById(R.id.rec_date);
            textView2 = itemView.findViewById(R.id.rec_value);
            imageView = itemView.findViewById(R.id.rec_info);
            textView3 = itemView.findViewById(R.id.rec_dal);

            //아이템 클릭이벤트처리
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(listenr != null ){
                        listenr.onItemClick(ViewHolder.this, itemView, position);
                    }
                    final String val = textView2.getText().toString();
             if(val.equals("분유")){
                 Intent intent = new Intent(context, RecodePwmilk.class);
                 intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                 context.startActivity(intent);
            }
                    if(val.equals("수유")){
                        Intent intent = new Intent(context,RecodeNursing.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                    if(val.equals("이유식")){
                        Intent intent = new Intent(context,RecodeBbfood.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                    if(val.equals("수면")){
                        Intent intent = new Intent(context,RecodeSleep.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                    if(val.equals("배변")){
                        Intent intent = new Intent(context, RecodeBowel.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                    if(val.equals("투약")){
                        Intent intent = new Intent(context,RecodeDosage.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                    if(val.equals("온도체크")){
                        Intent intent = new Intent(context,RecodeTemp.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                    if(val.equals("목욕")){
                        Intent intent = new Intent(context, RecodeBath.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                    if(val.equals("입원")){
                        Intent intent = new Intent(context, RecodeHealth.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                    if(val.equals("놀이")){
                        Intent intent = new Intent(context, RecodePlay.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                }
            });
            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        }

        //setItem 메소드는 SingerItem 객체를 전달받아 뷰홀더 안에 있는 뷰에 데이터를 설정하는 역할을 합니다.
        public void setItem(RecodeData item) {
            textView.setText(item.getTime());
            textView2.setText(item.getValue());
            imageView.setImageResource(R.drawable.right);
        }


        //클릭이벤트처리
        public void setOnItemClickListener(OnItemClickListener listenr){
            this.listenr = listenr;
        }


    }
}