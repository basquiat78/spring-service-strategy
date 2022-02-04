package io.basquiat.strategy.service.impl;

import io.basquiat.strategy.service.StrategyService;
import org.springframework.stereotype.Service;

/**
 * hello service
 */
@Service("hello")
public class HelloService implements StrategyService {
    @Override
    public String getValue(String value) {
        return "Hello! My Value is " + value;
    }

    @Override
    public String getCode(String code) {
        return "Hello! My Code is " + code;
    }
}
