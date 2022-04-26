package kr.co.haem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import kr.co.haem.retrofit.RetrofitClient;
import kr.co.haem.retrofit.model.Article;
import kr.co.haem.retrofit.model.ArticleList;
import kr.co.haem.retrofit.model.Paging;
import kr.co.haem.util.DividerItemDecoration;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OnItemClick, SwipeRefreshLayout.OnRefreshListener{

    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;


    private RecyclerView mRecyclerView;
    private List<Article> mList = new ArrayList<>();
    private int pageNo = 1;
    private int pageSize = 20;
    private boolean isLoading = false;
    public static boolean isBack = false;
    ArticleItemRecyclerViewAdapter mAdapter;
    private Context mContext;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private EditText etKeyword;
    private ImageButton btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setTheme(R.style.Theme_AppCompat_NoActionBar);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);


        mRecyclerView = findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext));



        mSwipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        etKeyword = findViewById(R.id.etKeyword);
        btnSearch = findViewById(R.id.btnSearch);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String keyword = etKeyword.getText().toString();
                if(StringUtils.isEmpty(keyword)){
                    Toast.makeText(mContext,"검색 키워드를 입력해주세요",Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    InputMethodManager imm = (InputMethodManager) mContext.getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(etKeyword.getWindowToken(), 0);
                    //etKeyword.setText("");
                    mList = new ArrayList<>();
                    setData();


                }
            }
        });


        initScrollListener();
        setData();
    }

    @Override
    public void onClick (View v,String productCode){
        // value this data you receive when increment() / decrement() called
        /*
        pageNo = 1;
        mList.clear();
*/
        //Toast.makeText(this.getContext(),value, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, ProductDetail.class) ;
        intent.putExtra("productCode",productCode);
        startActivity(intent) ;
    }

    private void setData(){

        ArticleList list = new ArticleList();
        Paging paging = new Paging();
        paging.setPage(pageNo);
        paging.setPageSize(pageSize);

        if(!StringUtils.isEmpty(etKeyword.getText())){
            paging.setProductName(etKeyword.getText().toString());
        }

        /*
        pUtil = new PreferenceUtil(mContext);
        String searchOption = pUtil.getStringPreferences(Constant.SEARCH_OPTION_KEY);
        String searchKeyword = pUtil.getStringPreferences(Constant.SEARCH_OPTION_KEYWORD);

        if(StringUtils.isNotEmpty(searchOption)){
            paging.setSearchOption(searchOption);
        }
        if(StringUtils.isNotEmpty(searchKeyword)){
            paging.setSearchKeyword(searchKeyword);
        }
*/
        Call<ArticleList> call = RetrofitClient.getApiService().getArticleList(paging);
        call.enqueue(new Callback<ArticleList>() {
            @Override
            public void onResponse(Call<ArticleList> call, Response<ArticleList> response) {
                if(!response.isSuccessful()){
                    Log.e("연결이 비정상적 : ", "error code : " + response.code());
                    return;
                }
                ArticleList list = response.body();
                Log.d("연결이 성공적 : ", response.body().toString());
                if(mList.size() == 0){
                    mList = list.getList();
                    mAdapter = new ArticleItemRecyclerViewAdapter(mList, MainActivity.this::onClick,mContext);
                    mRecyclerView.setAdapter(mAdapter);

                }else{
                    if(isBack){
                        mAdapter = new ArticleItemRecyclerViewAdapter(mList, MainActivity.this::onClick,mContext);
                        mRecyclerView.setAdapter(mAdapter);
                        isBack = false;
                    }else{
                        mList.remove(mList.size() - 1);
                        int scrollPosition = mList.size();
                        mAdapter.notifyItemRemoved(scrollPosition);
                        mList.addAll(list.getList());

                    }
                }
                Log.d("List size=",Integer.toString(mList.size()));
                isLoading = false;
                if(mList.size() == pageSize){

                }else{
                    mAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onFailure(Call<ArticleList> call, Throwable t) {
                Log.e("연결실패", t.getMessage());
            }
        });



    }

    private void initScrollListener() {
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();


                if (!isLoading) {
                    Log.d("skyblue","load more list size="+Integer.toString(mList.size() -1)+"findLastCompletelyVisibleItemPosition"+layoutManager.findLastCompletelyVisibleItemPosition());
                    if (layoutManager != null && layoutManager.findLastCompletelyVisibleItemPosition() == mList.size() - 1) {
                        //리스트 마지막
                        Log.d("skyblue","load more list size="+Integer.toString(mList.size() -1)+"findLastCompletelyVisibleItemPosition"+layoutManager.findLastCompletelyVisibleItemPosition());
                        loadMore();
                        isLoading = true;
                        Log.d("skyblue","로딩중이 아님");
                    }
                }else{
                    Log.d("skyblue","로딩중");
                }
            }
        });
    }
    public void loadMore(){
        pageNo++;
        if(pageNo >= 10){
            Toast.makeText(mContext,"마지막 데이터입니다.", Toast.LENGTH_SHORT).show();
        }else{
            if(mList.size() != 0){
                //mList.add(mList.get(mList.size() -1));
                mList.add(null);
                // mAdapter.notifyItemInserted(mList.size() - 1);
                setData();
                Handler handler = new Handler();
            /*
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mList.remove(mList.size() - 1);
                    int scrollPosition = mList.size();
                    mAdapter.notifyItemRemoved(scrollPosition);
                    setData();

                }
            }, 2000);

             */
            }

        }
    }

    @Override
    public void onRefresh() {
        pageNo = 1;
        mList.clear();
        initScrollListener();
        setData();
        mSwipeRefreshLayout.setRefreshing(false);
    }

}