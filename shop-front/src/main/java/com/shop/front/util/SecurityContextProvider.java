package com.shop.front.util;

import com.shop.front.common.security.MemberDetails;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityContextProvider {
    private SecurityContextProvider() {
    }

    public static MemberDetails getMember() {
        return (MemberDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
