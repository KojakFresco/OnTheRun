package com.example.ontherun.page;

import com.example.ontherun.page.TextOverlay;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ParsedResult {
    @SerializedName("TextOverlay")
    @Expose
    private TextOverlay textOverlay;
    @SerializedName("FileParseExitCode")
    @Expose
    private Integer fileParseExitCode;
    @SerializedName("ParsedText")
    @Expose
    private String parsedText;
//    @SerializedName("ErrorMessage")
//    @Expose
//    private String errorMessage;
//    @SerializedName("ErrorDetails")
//    @Expose
//    private String errorDetails;

    public TextOverlay getTextOverlay() {
        return textOverlay;
    }

    public void setTextOverlay(TextOverlay textOverlay) {
        this.textOverlay = textOverlay;
    }

    public Integer getFileParseExitCode() {
        return fileParseExitCode;
    }

    public void setFileParseExitCode(Integer fileParseExitCode) {
        this.fileParseExitCode = fileParseExitCode;
    }

    public String getParsedText() {
        return parsedText;
    }

    public void setParsedText(String parsedText) {
        this.parsedText = parsedText;
    }

//    public String getErrorMessage() {
//        return errorMessage;
//    }
//
//    public void setErrorMessage(String errorMessage) {
//        this.errorMessage = errorMessage;
//    }

//    public String getErrorDetails() {
//        return errorDetails;
//    }
//
//    public void setErrorDetails(String errorDetails) {
//        this.errorDetails = errorDetails;
//    }
}
