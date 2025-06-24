package org.koreait.file.controllers;

import lombok.Data;

@Data
public class RequestThumb {
    private Long seq;
    private int width;
    private int height;
    private String url;

}
