package org.koreait.trend.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TrendCollectServiceTest {

    @Autowired
    private TrendCollectService service;

    @Test
    void test() {
        //NewsTrend data = service.process();
        //System.out.println(data);
        service.scheduledJob();
    }
}
