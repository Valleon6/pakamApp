package com.valleon.pakamapp.modules.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class ResponseMessage<T>   {
    private LocalDateTime timeStamp = LocalDateTime.now();
    private int statusCode;
    private String details;
    private T data = null;

    public int pageNo;
    public int pageSize;
    public Integer prevPage;
    public Integer nextPage;
    public long totalElements;
    public int totalPages;
    boolean hasPrevious;
    boolean hasNext;
    public boolean last;


    public ResponseMessage(LocalDateTime timeStamp, int statusCode, String details, T data) {
        this.timeStamp = timeStamp;
        this.statusCode = statusCode;
        this.details = details;
        this.data = data;
    }

    public ResponseMessage(LocalDateTime timeStamp, int statusCode, String details, T data, int pageNo, int pageSize, Integer prevPage, Integer nextPage, long totalElements, int totalPages, boolean hasPrevious, boolean hasNext, boolean last) {
        this.timeStamp = timeStamp;
        this.statusCode = statusCode;
        this.details = details;
        this.data = data;
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.prevPage = prevPage;
        this.nextPage = nextPage;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.hasPrevious = hasPrevious;
        this.hasNext = hasNext;
        this.last = last;
    }
}
