package com.sebbaindustries.advancedafk.utils;

import java.util.regex.Pattern;

public class StringParser {

    private final String string;
    Pattern pattern = Pattern.compile("^[a-zA-Z0-9]+$");

    public StringParser(String string) {
        this.string = string;
    }

    public boolean isSafe() {
        return pattern.matcher(string).matches();
    }

    public String returnColoredIllegals() {
        StringBuilder sb = new StringBuilder();
        for (char c : string.toCharArray()) {
            if (pattern.matcher(String.valueOf(c)).matches()) {
                sb.append("§a").append(c);
                continue;
            }
            sb.append("§c").append(c);
        }
        return sb.toString();
    }

}
