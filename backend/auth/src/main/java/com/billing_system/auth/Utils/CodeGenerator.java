package com.billing_system.auth.Utils;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Set;

@Component
public class CodeGenerator {
    private static final SecureRandom random = new SecureRandom();
    private static final Set<Integer> usedCodes = new HashSet<>();

    public int generateUniqueCode() {
        int code;
        do {
            code = 100_000 + random.nextInt(900_000);
        } while (usedCodes.contains(code));

        usedCodes.add(code);
        return code;
    }
}
