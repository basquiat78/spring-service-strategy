package io.basquiat.strategy.service.impl;

import io.basquiat.strategy.service.StrategyService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * world service
 * @Primary를 적용해서 우선적으로 이 빈을 찾게 만든 서비스
 */
@Primary
@Service("world")
public class WorldService implements StrategyService {
    @Override
    public String getValue(String value) {
        return "World! My Value is " + value;
    }

    @Override
    public String getCode(String code) {
        return "Hello! My Code is " + code;
    }
}
