package org.koreait.global.exceptions;

import org.springframework.http.HttpStatus;

public class NotFoundException extends CommonException {
    public NotFoundException() {
        super("NotFound", HttpStatus.NOT_FOUND);
        setErrorCode(true); // 메세지 파일의 코드이다.
    }
}
