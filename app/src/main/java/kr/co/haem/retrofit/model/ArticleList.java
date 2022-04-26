package kr.co.haem.retrofit.model;

import java.util.List;

public class ArticleList {

    private String total;
    private List<Article> list;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<Article> getList() {
        return list;
    }

    public void setList(List<Article> list) {
        this.list = list;
    }


}
