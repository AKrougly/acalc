package com.example.acalc;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public final class InputDisplay {
    static char CR  = (char) 0x0D; /* \r \u000d: carriage return CR */
    /*final static char LF  = (char) 0x0A; /* \n \u000a: linefeed LF */
    static final char DECIMAL_SEPARATOR = '.';
    static final char GROUPING_SEPARATOR = ',';
    static final char CHAR_ZERO = '0';
    static final char CHAR_EQUAL = '=';
    static final char CHAR_SLASH = '/';
    static final char CHAR_STAR = '*';
    static final char CHAR_MINUS = '-';
    static final char CHAR_PLUS = '+';
    static final char CHAR_COMMA = DECIMAL_SEPARATOR;
    static char CHAR_PERCENT = '%';
    char op;
    String str1, str2;
    CalculatorActivity calculatorActivity;
    HistoryDisplay historyDisplay;
    InputDisplay(CalculatorActivity calculatorActivity) {
        this.calculatorActivity = calculatorActivity;
        historyDisplay = new HistoryDisplay(calculatorActivity);
        clearAll();
    }
    boolean isEmpty(String str) {
        return ((str == null) || (str.length() == 0));
    }
    boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }
    void displayText() {
        calculatorActivity.setInputDisplayText(op == CHAR_EQUAL ? str1 : str2);
    }
    void clearAll() {
        op = CHAR_EQUAL;
        str1 = "";
        str2 = "";
        displayText();
    }
    String getText() {
        if (op == CHAR_EQUAL)
            return str1;
        else
            return str2;
    }
    char getStringLastChar(String str) {
        if (isNotEmpty(str))
            return str.charAt(str.length() - 1);
        return CR;
    }
    char getLastChar() {
        if (isNotEmpty(str2))
            return getStringLastChar(str2);
        else if (op != CHAR_EQUAL)
            return op;
        else if (isNotEmpty(str1))
            return getStringLastChar(str1);
        return CHAR_EQUAL;
    }
    void appendCh(char ch) {
        if (op == CHAR_EQUAL)
            str1 += ch;
        else
            str2 += ch;
        displayText();
    }
    void setText(String text) {
        if (op == CHAR_EQUAL)
            str1 = text;
        else
            str2 = text;
        displayText();
    }
    void appendMinusSign() {
        String str = getText();
        if (isNotEmpty(str)) {
            if (str.charAt(0) == CHAR_MINUS)
                str = str.substring(1);
            else
                str = CHAR_MINUS + str;
            setText(str);
        }
    }
    void clearHistory() {
        historyDisplay.clearHistory();
    }
    void setHistory(String text, char ch) {
        historyDisplay.setHistory(text, ch);
    }
    double str2double(String str) throws ParseException {
        NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
        Number number = format.parse(str);
        return number.doubleValue();
    }
    String double2str(double d) {
        //String.format("%" + DECIMAL_SEPARATOR + "6f", d);
        DecimalFormat decimalFormat = new DecimalFormat("#" + GROUPING_SEPARATOR + "##0" + DECIMAL_SEPARATOR + "000000");
        return decimalFormat.format(d);
    }
    String rightTrim(String text, String trimBy) {
        int beginIndex = 0;
        int endIndex = text.length();

        while (text.substring(beginIndex, endIndex).endsWith(trimBy)) {
            endIndex -= trimBy.length();
        }
        return text.substring(beginIndex, endIndex);
    }
    void calculateExpression(double n1, double n2, char op) {
        try {
            double res = 0.0;
            switch (op) {
                case CHAR_PLUS: res = n1 + n2;
                    break;
                case CHAR_MINUS: res = n1 - n2;
                    break;
                case CHAR_STAR: res = n1 * n2;
                    break;
                case CHAR_SLASH: res = n1 / (n2 == 0 ? 1 : n2);
                    break;
            }
            clearAll();
            setText(rightTrim(rightTrim(double2str(res), "0"), DECIMAL_SEPARATOR + ""));
        } catch (Exception e) {
            clearAll();
            clearHistory();
        }
    }
    void executeOp() {
        /* DecimalFormat df = new DecimalFormat();
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(DECIMAL_SEPARATOR);
        symbols.setGroupingSeparator(GROUPING_SEPARATOR);
        df.setDecimalFormatSymbols(symbols); */
        try {
            //Number n1 = df.parse(str1);
            //Number n2 = df.parse(str2);
            double n1 = str2double(str1);
            double n2 = str2double(str2);
            calculateExpression(n1, n2, op);
        } catch (Exception e) {
            clearAll();
            clearHistory();
        }
    }
    void executeOpPercent() {
        try {
            double n1 = str2double(str1);
            double n2 = str2double(str2);
            calculateExpression(n1, n1 * n2 / 100.0, op);
        } catch (Exception e) {
            clearAll();
            clearHistory();
        }
    }
    void setOp(char ch) {
        if (getLastChar() == CHAR_COMMA)
            cutLastCh();
        if (isNotEmpty(str2)) {
            setHistory(
                (op == CHAR_PLUS || op == CHAR_MINUS) &&
                (ch == CHAR_STAR || ch == CHAR_SLASH) ?
                "(" + historyDisplay.getHistory() + " " + op + " " + str2 + ")" :
                historyDisplay.getHistory() + " " + op + " " + str2, ch);
            if (ch == CHAR_PERCENT) {
                executeOpPercent();
                op = CHAR_EQUAL;
            } else {
                executeOp();
                op = ch;
            }
        } else {
            if (isNotEmpty(str1)) {
                op = ch;
                setHistory(str1, ch);
            }
        }
    }
    void setOp(Key key) {
        setOp(key.getChar());
    }
    String trimText(String str) {
        String text = str.substring(0, str.length() - 1);
        if (text.equals("" + CHAR_ZERO) || text.equals("" + CHAR_MINUS + CHAR_ZERO))
            text = "";
        return text;
    }
    void cutLastCh() {
        if (isNotEmpty(str2)) {
            str2 = trimText(str2);
        } else if (op != CHAR_EQUAL) {
            setOp(CHAR_EQUAL);
        } else if (isNotEmpty(str1)) {
            str1 = trimText(str1);
        }
        historyDisplay.clearHistory();
        displayText();
    }
}
