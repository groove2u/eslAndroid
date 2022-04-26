package kr.co.haem;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import kr.co.haem.retrofit.model.Article;


    public class ArticleItemRecyclerViewAdapter extends RecyclerView.Adapter<ArticleItemRecyclerViewAdapter.MyViewHolder> {
        // 이 데이터들을 가지고 각 뷰 홀더에 들어갈 텍스트 뷰에 연결할 것

        private final int VIEW_TYPE_ITEM = 0;
        private final int VIEW_TYPE_LOADING = 1;

        private final List<Article> mValues;
        private Context mContext;
        private OnItemClick mCallback;

        // 생성자
        public ArticleItemRecyclerViewAdapter(List<Article> items, OnItemClick listener,Context c){
            mContext = c;
            mValues = items;
            this.mCallback = listener;
        }

        // 리사이클러뷰에 들어갈 뷰 홀더, 그리고 그 뷰 홀더에 들어갈 아이템들을 지정
        public static class MyViewHolder extends  RecyclerView.ViewHolder{
            public Article mItem;
            public RelativeLayout mItemWrap;

            public TextView tvTitle;
            public TextView tvSaleType;
            public TextView tvDiscount;
            public TextView tvPrice;

            public MyViewHolder(View view){
                super(view);

                this.mItemWrap = view.findViewById(R.id.item_wrap);
                this.tvTitle = view.findViewById(R.id.tvTitle);
                this.tvSaleType = view.findViewById(R.id.tvSaleType);
                this.tvDiscount = view.findViewById(R.id.tvDiscount);
                this.tvPrice = view.findViewById(R.id.tvPrice);
            }
        }

        public void setItem(MyViewHolder holder,int position) {
            holder.mItem = mValues.get(position);


            holder.tvTitle.setText(mValues.get(position).getProductName());

            String saleType = mValues.get(position).getSaleType();
            String saleTypeTxt="";
            try{
                switch (Integer.parseInt(saleType)){
                    case 0 : saleTypeTxt="";break;
                    case 1 : saleTypeTxt="1+1";break;
                    case 2 : saleTypeTxt="2+1";break;
                }

            }catch (Exception e){

            }
            holder.tvSaleType.setText(saleTypeTxt);

            int discount = mValues.get(position).getDiscount();
            holder.tvDiscount.setText(Integer.toString(discount)+"%");
            holder.tvPrice.setText(mValues.get(position).getPrice());


            //Glide.with(holder.mItemImage.getContext()).load(mValues.get(position).getImg()).into(holder.mItemImage);

            holder.mItemWrap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int productCode = mValues.get(position).getProductCode();

                    mCallback.onClick(v,Integer.toString(productCode));

                }
            });

        }
        // 어댑터 클래스 상속시 구현해야할 함수 3가지 : onCreateViewHolder, onBindViewHolder, getItemCount
        // 리사이클러뷰에 들어갈 뷰 홀더를 할당하는 함수, 뷰 홀더는 실제 레이아웃 파일과 매핑되어야하며, extends의 Adater<>에서 <>안에들어가는 타입을 따른다.
        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View holderView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_item, viewGroup, false);
            MyViewHolder myViewHolder = new MyViewHolder(holderView);
            return myViewHolder;
        }

        // 실제 각 뷰 홀더에 데이터를 연결해주는 함수
        @Override
        public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
            setItem(myViewHolder,i);
        }

        // iOS의 numberOfRows, 리사이클러뷰안에 들어갈 뷰 홀더의 개수
        @Override
        public int getItemCount() {

            return mValues == null ? 0 : mValues.size();
        }

        @Override
        public int getItemViewType(int position) {
            return mValues.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
        }
    }

