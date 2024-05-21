package com.example.ontherun.page;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Word {
    @SerializedName("WordText")
    @Expose
    private String wordText;
    @SerializedName("Left")
    @Expose
    private Integer left;
    @SerializedName("Top")
    @Expose
    private Integer top;
    @SerializedName("Height")
    @Expose
    private Integer height;
    @SerializedName("Width")
    @Expose
    private Integer width;

    public String getWordText() {
        return wordText;
    }

    public void setWordText(String wordText) {
        this.wordText = wordText;
    }

    public Integer getLeft() {
        return left;
    }

    public void setLeft(Integer left) {
        this.left = left;
    }

    public Integer getTop() {
        return top;
    }

    public void setTop(Integer top) {
        this.top = top;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }
}
