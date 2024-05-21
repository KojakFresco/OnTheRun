package com.example.ontherun.page;

import com.example.ontherun.page.Word;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Line {
    @SerializedName("Words")
    @Expose
    private List<Word> words;
    @SerializedName("MaxHeight")
    @Expose
    private Integer maxHeight;
    @SerializedName("MinTop")
    @Expose
    private Integer minTop;

    public List<Word> getWords() {
        return words;
    }

    public void setWords(List<Word> words) {
        this.words = words;
    }

    public Integer getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(Integer maxHeight) {
        this.maxHeight = maxHeight;
    }

    public Integer getMinTop() {
        return minTop;
    }

    public void setMinTop(Integer minTop) {
        this.minTop = minTop;
    }
}
