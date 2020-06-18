package com.example.baily.main.recode;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
    int INFO_NURSING = 1;
    int INFO_BBFOOD = 2;
    int INFO_BOWEL = 3;
    int INFO_BATH = 4;
    int INFO_DRUG = 5;
    int INFO_HOSP = 6;
    int INFO_PLAY = 7;
    int INFO_PWMILK = 8;
    int INFO_SLEEP = 9;
    int INFO_TEMP = 10;
    public FragRecode fragRecode;
    //클릭이벤트처리 관련 사용자 정의(이 코드없으면 그냥 리사이클러뷰 구조)//////////////////////////////////////////////////////////////////////////
  private OnItemClickListener listener = null; //참고로 OnItemClickListener는 기존에 있는것과 동일한 이름인데 그냥 같은 이름으로 내가 정의를 했다. (리스트뷰에서는 이게 자동구현되있어서 OnItemClickListener를 구현안하고 호출해서 클릭시 이벤트를 처리할 수 있음)




    public   interface  OnItemClickListener{
         void onItemClick(ViewHolder holder, View view, int position);
    }



    public RecodeAdapter(ArrayList<RecodeData> list, FragRecode context) {
        items = list;
    }

    @Override //어댑터에서 관리하는 아이템의 개수를 반환
    public int getItemCount() {
        return items.size();
    }


    @NonNull
    @Override //뷰홀더가 만들어지는 시점에 호출되는 메소드(각각의 아이템을 위한 뷰홀더 객체가 처음만들어지는시점)
    //만약에 각각의 아이템을 위한 뷰홀더가 재사용될 수 있는 상태라면 호출되지않음 (그래서 편리함, 이건내생각인데 리스트뷰같은경우는 convertView로 컨트롤해줘야하는데 이건 자동으로해줌)
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recode_item, viewGroup, false);

        return new ViewHolder(view); //각각의 아이템을 위한 뷰를 담고있는 뷰홀더객체를 반환한다.(각 아이템을 위한 XML 레이아웃을 이용해 뷰 객체를 만든 후 뷰홀더에 담아 반환
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
                    final String tm = textView.getText().toString();

                    if(val.equals("●분유")){
                        Intent intent = new Intent(v.getContext(), InfoPwmilk.class);
                        intent.putExtra("str",tm);
                        ((Activity)v.getContext()).startActivityForResult(intent, FragRecode.INFO_PWMILK);
                    }
                    if(val.equals("●모유")){
                        Intent intent = new Intent(v.getContext(), InfoNursing.class);
                        intent.putExtra("str",tm);
                        ((Activity)v.getContext()).startActivityForResult(intent, FragRecode.INFO_NURSING);
                    }
                    if(val.equals("●이유식")){
                        Intent intent = new Intent(v.getContext(), InfoBbfood.class);
                        intent.putExtra("str",tm);
                        ((Activity)v.getContext()).startActivityForResult(intent, FragRecode.INFO_BBFOOD);
                    }
                    if(val.equals("●잠")){
                        Intent intent = new Intent(v.getContext(), infoSleep.class);
                        intent.putExtra("str",tm);
                        ((Activity)v.getContext()).startActivityForResult(intent, FragRecode.INFO_SLEEP);
                    }
                    if(val.equals("●기저귀")){
                        Intent intent = new Intent(v.getContext(), InfoBowel.class);
                        intent.putExtra("str",tm);
                        ((Activity)v.getContext()).startActivityForResult(intent, FragRecode.INFO_BOWEL);
                    }
                    if(val.equals("●약")){
                        Intent intent = new Intent(v.getContext(), InfoDrug.class);
                        intent.putExtra("str",tm);
                        ((Activity)v.getContext()).startActivityForResult(intent, FragRecode.INFO_DRUG);
                    }
                    if(val.equals("●온도")){
                        Intent intent = new Intent(v.getContext(), InfoTemp.class);
                        intent.putExtra("str",tm);
                        ((Activity)v.getContext()).startActivityForResult(intent, FragRecode.INFO_TEMP);
                    }
                    if(val.equals("●목욕")){
                        Intent intent = new Intent(v.getContext(), InfoBath.class);
                        intent.putExtra("str",tm);
                        ((Activity)v.getContext()).startActivityForResult(intent, FragRecode.INFO_BATH);
                    }
                    if(val.equals("●병원")){
                        Intent intent = new Intent(v.getContext(), InfoHospital.class);
                        intent.putExtra("str",tm);
                        ((Activity)v.getContext()).startActivityForResult(intent, FragRecode.INFO_HOSP);
                    }
                    if(val.equals("●놀이")){
                        Intent intent = new Intent(v.getContext(), InfoPlay.class);
                        intent.putExtra("str",tm);
                        ((Activity)v.getContext()).startActivityForResult(intent, FragRecode.INFO_PLAY);

                    }
                }
            });
            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        }

        //setItem 메소드는 SingerItem 객체를 전달받아 뷰홀더 안에 있는 뷰에 데이터를 설정하는 역할을 합니다.
        public void setItem(RecodeData item) {
            textView.setText(item.getTime());
            textView2.setText("●"+item.getValue());
            switch (item.getValue()){
                case "모유" :
                    textView2.setTextColor(Color.parseColor("#C55A11"));
                    break;
                case "이유식" :
                    textView2.setTextColor(Color.parseColor("#FFA9A9"));
                    break;
                case "잠" :
                    textView2.setTextColor(Color.parseColor("#5781BF"));
                    break;
                case "분유" :
                    textView2.setTextColor(Color.parseColor("#F4B184"));
                    break;
                case "기저귀" :
                    textView2.setTextColor(Color.parseColor("#BF9000"));
                    break;
                case "약" :
                    textView2.setTextColor(Color.parseColor("#A9D18E"));
                    break;
                case "온도" :
                    textView2.setTextColor(Color.parseColor("#2E75B6"));
                    break;
                case "목욕" :
                    textView2.setTextColor(Color.parseColor("#FFE699"));
                    break;
                case "병원" :
                    textView2.setTextColor(Color.parseColor("#00B050"));
                    break;
                case "놀이" :
                    textView2.setTextColor(Color.parseColor("#F4B183"));
                    break;

            }
            imageView.setImageResource(R.drawable.right);
            textView3.setText(item.getDal()+"임");
        }


        //클릭이벤트처리
        public void setOnItemClickListener(OnItemClickListener listenr){
            this.listenr = listenr;
        }


    }



}