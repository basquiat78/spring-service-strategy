package io.basquiat.strategy.web;

import io.basquiat.strategy.service.StrategyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class StrategyController {

    private final StrategyService strategyService;

    /**
     * 심플하게 스트링 값 하나 던져준다.
     * @return String
     */
    @GetMapping("/strategy/value/{value}")
    public String fetchValue(@PathVariable("value")String value) {
        return strategyService.getValue(value);
    }

    /**
     * 심플하게 스트링 값 하나 던져준다.
     * @return String
     */
    @GetMapping("/strategy/code/{code}")
    public String fetchCode(@PathVariable("code")String code) {
        return strategyService.getCode(code);
    }

}