package kr.co.haem.retrofit.model;

public class Paging {
    private int page;
    private int pageSize;
    private String productName;
    private int productCode;
    private int cateL;
    private int cateM;
    private int cateS;


    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getCateL() {
        return cateL;
    }

    public void setCateL(int cateL) {
        this.cateL = cateL;
    }

    public int getCateM() {
        return cateM;
    }

    public void setCateM(int cateM) {
        this.cateM = cateM;
    }

    public int getCateS() {
        return cateS;
    }

    public void setCateS(int cateS) {
        this.cateS = cateS;
    }

    public int getProductCode() {
        return productCode;
    }

    public void setProductCode(int productCode) {
        this.productCode = productCode;
    }
}
