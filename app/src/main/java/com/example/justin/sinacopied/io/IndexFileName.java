package com.example.justin.sinacopied.io;

import java.util.Random;

public class IndexFileName {

    private static String randomEnglish() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 6; i++) {
            String charOrNum = random.nextInt(10) >= 4 ? "uppercase" : "lowercase";
            switch (charOrNum) {
                case "lowercase":
                    int num1 = random.nextInt(10);
                    sb.append((char) (random.nextInt(26) + 65));
                    sb.append(num1);
                    break;
                case "uppercase":
                    int num2 = random.nextInt(10);
                    sb.append((char) (random.nextInt(26) + 97));
                    sb.append(num2);
                    break;
            }
        }
        return sb.toString();
    }

    public static String getFileName(String url) {
        String BlackSlant = "%2F";
        int index = url.lastIndexOf(BlackSlant);
        if (index != -1) {
            return url.substring(index + BlackSlant.length());
        } else {
            return randomEnglish();
        }
    }
}
