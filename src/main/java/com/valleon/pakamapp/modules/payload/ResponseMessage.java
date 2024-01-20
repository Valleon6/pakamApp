package com.valleon.pakamapp.modules.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseMessage<T> {
    private LocalDateTime timeStamp = LocalDateTime.now();
    private int statusCode;
    private String details;
    private T data = null;
}
