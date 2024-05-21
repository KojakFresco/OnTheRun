package com.example.ontherun;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import com.example.ontherun.page.ParsedResult;

import java.util.List;

public class Page {

    @SerializedName("ParsedResults")
    @Expose
    private List<ParsedResult> parsedResults;
    @SerializedName("OCRExitCode")
    @Expose
    private Integer oCRExitCode;
    @SerializedName("IsErroredOnProcessing")
    @Expose
    private Boolean isErroredOnProcessing;
    @SerializedName("ErrorMessage")
    @Expose
    private List<String> errorMessage;
//    @SerializedName("ErrorDetails")
//    @Expose
//    private String errorDetails;
//    @SerializedName("SearchablePDFURL")
//    @Expose
//    private String searchablePDFURL;
//    @SerializedName("ProcessingTimeInMilliseconds")
//    @Expose
//    private String processingTimeInMilliseconds;

    public List<ParsedResult> getParsedResults() {
        return parsedResults;
    }

    public void setParsedResults(List<ParsedResult> parsedResults) {
        this.parsedResults = parsedResults;
    }

    public Integer getOCRExitCode() {
        return oCRExitCode;
    }

    public void setOCRExitCode(Integer oCRExitCode) {
        this.oCRExitCode = oCRExitCode;
    }

    public Boolean getIsErroredOnProcessing() {
        return isErroredOnProcessing;
    }

    public void setIsErroredOnProcessing(Boolean isErroredOnProcessing) {
        this.isErroredOnProcessing = isErroredOnProcessing;
    }

    public List<String> getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(List<String> errorMessage) {
        this.errorMessage = errorMessage;
    }

//    public Object getErrorDetails() {
//        return errorDetails;
//    }
//
//    public void setErrorDetails(String errorDetails) {
//        this.errorDetails = errorDetails;
//    }

//    public String getSearchablePDFURL() {
//        return searchablePDFURL;
//    }
//
//    public void setSearchablePDFURL(String searchablePDFURL) {
//        this.searchablePDFURL = searchablePDFURL;
//    }

//    public String getProcessingTimeInMilliseconds() {
//        return processingTimeInMilliseconds;
//    }
//
//    public void setProcessingTimeInMilliseconds(String processingTimeInMilliseconds) {
//        this.processingTimeInMilliseconds = processingTimeInMilliseconds;
//    }

}

