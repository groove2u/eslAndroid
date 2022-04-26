package kr.co.haem.model;

import org.json.JSONObject;

public class OptionModel extends JSONObject {

    private String bituma_125;
    private String bituma_250;

    private String used_125;
    private String used_250;
    private String bunjang_125;
    private String bunjang_250;
    private String passo;

    @Override
    public String toString() {
        return "OptionModel{" +
                "bituma_125='" + bituma_125 + '\'' +
                ", bituma_250='" + bituma_250 + '\'' +
                ", used_125='" + used_125 + '\'' +
                ", used_250='" + used_250 + '\'' +
                ", bunjang_125='" + bunjang_125 + '\'' +
                ", bunjang_250='" + bunjang_250 + '\'' +
                ", passo='" + passo + '\'' +
                '}';
    }

    public String getBituma_125() {
        return bituma_125;
    }

    public void setBituma_125(String bituma_125) {
        this.bituma_125 = bituma_125;
    }

    public String getBituma_250() {
        return bituma_250;
    }

    public void setBituma_250(String bituma_250) {
        this.bituma_250 = bituma_250;
    }

    public String getUsed_125() {
        return used_125;
    }

    public void setUsed_125(String used_125) {
        this.used_125 = used_125;
    }

    public String getUsed_250() {
        return used_250;
    }

    public void setUsed_250(String used_250) {
        this.used_250 = used_250;
    }

    public String getBunjang_125() {
        return bunjang_125;
    }

    public void setBunjang_125(String bunjang_125) {
        this.bunjang_125 = bunjang_125;
    }

    public String getBunjang_250() {
        return bunjang_250;
    }

    public void setBunjang_250(String bunjang_250) {
        this.bunjang_250 = bunjang_250;
    }

    public String getPasso() {
        return passo;
    }

    public void setPasso(String passo) {
        this.passo = passo;
    }

}
