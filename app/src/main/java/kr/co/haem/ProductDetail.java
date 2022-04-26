package kr.co.haem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import java.nio.file.Files;

import kr.co.haem.retrofit.RetrofitClient;
import kr.co.haem.retrofit.model.Article;
import kr.co.haem.retrofit.model.ArticleList;
import kr.co.haem.retrofit.model.ArticleWrap;
import kr.co.haem.retrofit.model.Paging;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetail extends AppCompatActivity implements View.OnClickListener{

    private TextView tvCateL;
    private TextView tvCateM;
    private TextView tvCateS;
    private TextView tvProductName;
    private TextView tvPrice;
    private TextView tvDiscount;
    private TextView tvDesc;

    private ImageButton btnClose;
    private ImageButton btnBack;
    private ImageButton btnLocation;


    private int productCode;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_AppCompat_NoActionBar);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_product_detail);


        productCode = Integer.parseInt(getIntent().getStringExtra("productCode"));
        btnClose = findViewById(R.id.btnClose);
        btnBack = findViewById(R.id.btnBack);
        btnLocation = findViewById(R.id.btnLocation);

        btnClose.setOnClickListener(this);
        btnBack.setOnClickListener(this);

        btnLocation.setOnClickListener(this);

        tvCateL = findViewById(R.id.tvCateL);
        tvCateM = findViewById(R.id.tvCateM);
        tvCateS = findViewById(R.id.tvCateS);

        tvProductName = findViewById(R.id.tvProductName);
        tvPrice = findViewById(R.id.tvPrice);
        tvDiscount = findViewById(R.id.tvDiscount);
        tvDesc = findViewById(R.id.tvDesc);
        setData();
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btnBack || view.getId() == R.id.btnClose){
            finish();
        }else{
            Intent intent = new Intent(ProductDetail.this, ProductLocation.class) ;
            intent.putExtra("productCode",Integer.toString(productCode));
            intent.putExtra("position",Integer.toString(position));
            startActivity(intent) ;

        }
    }

    private void setData(){

        Paging paging = new Paging();
        paging.setProductCode(productCode);

        Call<ArticleWrap> call = RetrofitClient.getApiService().getArticleDetail(paging);

        call.enqueue(new Callback<ArticleWrap>() {
            @Override
            public void onResponse(Call<ArticleWrap> call, Response<ArticleWrap> response) {
                if(!response.isSuccessful()){
                    Log.e("연결이 비정상적 : ", "error code : " + response.code());
                    return;
                }
                ArticleWrap wrap = response.body();

                Article data = wrap.getData();

                tvCateL.setText(data.getCateLName());
                tvCateM.setText(data.getCateMName());
                tvCateS.setText(data.getCateSName());

                tvProductName.setText(data.getProductName());
                tvPrice.setText(data.getPrice()+"원");

                int discount = 0;
                discount = data.getDiscount();
                position = data.getPosition();
                tvDiscount.setText(Integer.toString(discount) + "%");
                tvDesc.setText(data.getDesc());

                Log.d("연결이 성공적 : ", response.body().toString());
            }
            @Override
            public void onFailure(Call<ArticleWrap> call, Throwable t) {
                Log.e("연결실패", t.getMessage());
            }
        });



    }
}