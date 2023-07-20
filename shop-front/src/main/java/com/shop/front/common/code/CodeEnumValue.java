package com.shop.front.common.code;

import com.shop.core.common.CodeEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CodeEnumValue {
    private String name;
    private String code;

    public CodeEnumValue(CodeEnum codeEnum) {
        this.name = codeEnum.getName();
        this.code = codeEnum.getCode();
    }
}
