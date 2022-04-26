package kr.co.haem.retrofit;


import kr.co.haem.model.Keyword;
import kr.co.haem.model.KeywordList;
import kr.co.haem.model.Token;
import kr.co.haem.retrofit.model.Article;
import kr.co.haem.retrofit.model.ArticleList;
import kr.co.haem.retrofit.model.ArticleWrap;
import kr.co.haem.retrofit.model.Paging;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitAPI {

    @POST("/getProductList")
    Call<ArticleList> getArticleList(@Body Paging paging);

    @POST("/getProductView")
    Call<ArticleWrap> getArticleDetail(@Body Paging paging);
    @POST("/getProductLocation")
    Call<ArticleWrap> getArticleLocation(@Body Paging paging);
}
