package com.shop.front.controller;

import com.shop.front.common.code.CodeEnumValue;
import com.shop.front.common.code.CodeProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class CodeController {
    private final CodeProvider codeProvider;

    @GetMapping("/api/v1/codes")
    public Map<String, List<CodeEnumValue>> codes() {
        return codeProvider.getCodes();
    }
}
