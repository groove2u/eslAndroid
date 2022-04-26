package kr.co.haem;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Random;

import kr.co.haem.retrofit.RetrofitClient;
import kr.co.haem.retrofit.model.Article;
import kr.co.haem.retrofit.model.ArticleWrap;
import kr.co.haem.retrofit.model.Paging;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductLocation extends AppCompatActivity implements View.OnClickListener{

    private TextView tvCateL;
    private TextView tvCateM;
    private TextView tvCateS;
    private TextView tvProductName;
    private TextView tvPrice;
    private TextView tvDiscount;
    private TextView tvDesc;

    private TextView tvGate1;
    private TextView tvGate2;
    private TextView tvGate3;
    private ImageView ivPin;
    private ImageButton btnClose;
    private ImageButton btnBack;

    private Context mContext;
    private int productCode;
    private int position;


    float dpHeight;
    float dpWidth;
    Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_AppCompat_NoActionBar);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mContext = this;

        setContentView(R.layout.activity_product_location);

        productCode = Integer.parseInt(getIntent().getStringExtra("productCode"));
        position = Integer.parseInt(getIntent().getStringExtra("position"));
        btnClose = findViewById(R.id.btnClose);


        tvGate1 = findViewById(R.id.tvGate1);
        tvGate2 = findViewById(R.id.tvGate2);
        tvGate3 = findViewById(R.id.tvGate3);

        btnClose.setOnClickListener(this);

        ivPin = findViewById(R.id.ivPin);

        animation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
        animation.setDuration(500); // duration - half a second
        animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
        animation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
        animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the end so the button will fade back in

        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        dpWidth = displayMetrics.widthPixels / displayMetrics.density;


        setData();
        //gateway 1 or gateway 2 choice

/*
        int gatewayLocation = getGatewayLocation();

        Log.d("skyblue","======getGatewayLocation="+Integer.toString(gatewayLocation));



        if (position <4){
            setGateway123(position);
        }else{
            setGateway456(position);
        }
*/
    }

    private void setGateway123(int num){

        Random random = new Random();

        int margin = 400 * (num-1);
        int height = 30;

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivPin.getLayoutParams();
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        params.setMargins(margin, 0, 0, 0);

        ivPin.setLayoutParams(params); //causes layout update
        ivPin.startAnimation(animation);



    }
    private void setGateway456(int num){


        Random random = new Random();


        int margin = (400 * (num-4));
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivPin.getLayoutParams();
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params.setMargins(0, 0, 0, margin);

        ivPin.setLayoutParams(params); //causes layout update
        ivPin.startAnimation(animation);

    }
    private int getGatewayLocation(){
        int start = 1;
        int end = 2;

        Random random = new Random();

        int val = (int) (Math.random() * (end-start+1)) + start;

        return val;

    }
    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btnBack || view.getId() == R.id.btnClose){
            finish();
        }
    }

    private void setData(){

        Paging paging = new Paging();
        paging.setProductCode(productCode);

        Call<ArticleWrap> call = RetrofitClient.getApiService().getArticleLocation(paging);

        call.enqueue(new Callback<ArticleWrap>() {
            @Override
            public void onResponse(Call<ArticleWrap> call, Response<ArticleWrap> response) {
                if(!response.isSuccessful()){
                    Log.e("연결이 비정상적 : ", "error code : " + response.code());
                    return;
                }
                ArticleWrap wrap = response.body();

                Article data = wrap.getData();

                try{
                    tvGate1.setText(data.getGate1());
                    tvGate2.setText(data.getGate2());
                    tvGate3.setText(data.getGate3());

                    int sig1 = Integer.parseInt(data.getSignal1());
                    int sig2 = Integer.parseInt(data.getSignal2());
                    int sig3 = Integer.parseInt(data.getSignal3());

                    int idx = getMostSignal(sig1,sig2,sig3);

                    if(idx == 1){
                        position = 2;
                    }else if (idx == 2){
                        position = 5;

                    }else if (idx == 3){
                        position = 6;

                    }
                    if (position <4){
                        setGateway123(position);
                    }else{
                        setGateway456(position);
                    }

                }catch (Exception e){
                    Toast.makeText(mContext,"위치정보를 찾을수없습니다",Toast.LENGTH_SHORT).show();
                    finish();
                }


                /*
                tvCateL.setText(data.getCateLName());
                tvCateM.setText(data.getCateMName());
                tvCateS.setText(data.getCateSName());

                tvProductName.setText(data.getProductName());
                tvPrice.setText(data.getPrice()+"원");

                int discount = 0;
                discount = data.getDiscount();
                tvDiscount.setText(Integer.toString(discount) + "%");
                tvDesc.setText(data.getDesc());
*/
                Log.d("연결이 성공적 : ", response.body().toString());
            }
            @Override
            public void onFailure(Call<ArticleWrap> call, Throwable t) {
                Log.e("연결실패", t.getMessage());
            }
        });



    }
    private int getMostSignal(int sig1,int sig2,int sig3){
        ArrayList<Integer> data = new ArrayList<>();


        data.add(sig1);
        data.add(sig2);
        data.add(sig3);
        if( data == null || data.size() == 0){
            return -999999;
        }

        int maxValue = data.get(0);
        int maxIndex = 0;
        for (int i = 0; i < data.size(); i++){

            if(maxValue < data.get(i)){
                maxValue = data.get(i);
                maxIndex = i;
            }
        }
        return maxIndex+1;
    }
}