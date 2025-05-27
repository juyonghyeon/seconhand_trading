package org.koreait.global.libs;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class Utils {

    private final HttpServletRequest request;

    /**
     * CSS, JS 버전
     *
     * @return
     */
    public int version() {
        return 1;
    }

    public String keywords() {
        return "";
    }

    public String description() {
        return "";
    }

    /**
     * 휴대폰 장비 인지, PC 인지
     * @return
     */
    public boolean isMobile() {
        String ua = request.getHeader("User-Agent");
        String pattern = ".*(iPhone|iPod|iPad|BlackBerry|Android|Windows CE|LG|MOT|SAMSUNG|SonyEricsson).*";
        return StringUtils.hasText(ua) && ua.matches(pattern);
    }

    /**
     * mobile, front 템플릿을 분리
     * @param path
     * @return
     */
    public String tpl(String path) {
        String prefix = isMobile() ? "mobile" : "front";
        return String.format("%s/%s", prefix, path);
    }
}
