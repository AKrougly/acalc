package com.example.acalc;

import android.util.Log;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Expression {
    static BigDecimal performOp(BigDecimal n1, BigDecimal n2, char op) {
        try {
            switch (op) {
                case InputDisplay.CHAR_PLUS: return n1.add(n2);
                case InputDisplay.CHAR_MINUS: return n1.subtract(n2);
                case InputDisplay.CHAR_STAR: return n1.multiply(n2);
                case InputDisplay.CHAR_SLASH:
                    return n1.divide(n2.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ONE : n2, 18, RoundingMode.HALF_UP);
            }
            return BigDecimal.ZERO;
        }
        catch (ArithmeticException e) {
            return BigDecimal.ZERO;
        }
    }
    static String rightTrim(String text, String trimBy) {
        int beginIndex = 0;
        int endIndex = text.length();

        while (text.substring(beginIndex, endIndex).endsWith(trimBy)) {
            endIndex -= trimBy.length();
        }
        return text.substring(beginIndex, endIndex);
    }
    static String formatRes(String res) {
        return
            res.contains(Character.toString(InputDisplay.DECIMAL_SEPARATOR)) ?
                rightTrim(rightTrim(res, "0"), InputDisplay.DECIMAL_SEPARATOR + "") :
                res;
    }
    static String calcOp(String s1, String s2, char op) {
        /* DecimalFormat df = new DecimalFormat();
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(DECIMAL_SEPARATOR);
        symbols.setGroupingSeparator(GROUPING_SEPARATOR);
        df.setDecimalFormatSymbols(symbols); */
        /*DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(14);
        df.setMinimumFractionDigits(0);
        df.setGroupingUsed(false);
        return df.format(performOp(new BigDecimal(s1), new BigDecimal(s2), op));
        */
        return formatRes(performOp(new BigDecimal(s1), new BigDecimal(s2), op).toPlainString());
    }
}
