package kr.co.haem.retrofit;

import android.media.session.MediaSession;
import android.util.Log;


import kr.co.haem.retrofit.model.ArticleList;
import kr.co.haem.retrofit.model.Paging;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CallRetrofit {


    public ArticleList getArticleList(Paging paging){
        boolean isRight = false;
        ArticleList list = new ArticleList();
        //Retrofit 호출
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
            }
            @Override
            public void onFailure(Call<ArticleList> call, Throwable t) {
                Log.e("연결실패", t.getMessage());
            }
        });
        return list;

    }
    public MediaSession.Token insertToken(MediaSession.Token t){
        return t;

    }
}
