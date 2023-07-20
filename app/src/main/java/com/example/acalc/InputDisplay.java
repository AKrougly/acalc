package com.example.acalc;

public final class InputDisplay {
    static char CR  = (char) 0x0D; /* \r \u000d: carriage return CR */
    /*final static char LF  = (char) 0x0A; /* \n \u000a: linefeed LF */
    static final char DECIMAL_SEPARATOR = '.';
    //static final char GROUPING_SEPARATOR = ',';
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
    static boolean isEmpty(String str) {
        return ((str == null) || (str.length() == 0));
    }
    static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }
    String getText() {
        if (op == CHAR_EQUAL)
            return str1;
        else
            return str2;
    }
    void displayText() {
        calculatorActivity.setInputDisplayText(getText());
    }
    void clearAll() {
        op = CHAR_EQUAL;
        str1 = "";
        str2 = "";
        displayText();
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
    void setText(String text) {
        String str = text.substring(0, Math.min(text.length(), 24));
        if (op == CHAR_EQUAL)
            str1 = str;
        else
            str2 = str;
        displayText();
    }
    void appendCh(char ch) {
        setText(getText() + ch);
    }
    void appendDigit(char ch) {
        if (ch != CHAR_ZERO ||
            !getText().equals("" + CHAR_ZERO) &&
            !getText().equals("" + CHAR_MINUS + CHAR_ZERO)
        ) {
            appendCh(ch);
        }
    }
    void appendComma() {
        if (!getText().contains("" + CHAR_COMMA)) {
            if (isEmpty(getText()))
                appendCh(CHAR_ZERO);
            appendCh(CHAR_COMMA);
        }
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
    void executeOp(char op) {
        try {
            String res = Expression.calcOp(str1, str2, op);
            clearAll();
            setText(res);
        } catch (Exception e) {
            clearAll();
            clearHistory();
        }
    }
    void executeOpPercent() {
        try {
            String res = Expression.calcOp(str1, Expression.calcOp(str1, Expression.calcOp(str2, "100", CHAR_SLASH), CHAR_STAR), op);
            clearAll();
            setText(res);
        } catch (Exception e) {
            clearAll();
            clearHistory();
        }
    }
    void setOp(char nOp) {
        if (getLastChar() == CHAR_COMMA)
            cutLastCh();
        historyDisplay.setHistory(str1, str2, op, nOp);
        if (isNotEmpty(str2)) {
            if (nOp == CHAR_PERCENT) {
                executeOpPercent();
                op = CHAR_EQUAL;
            } else {
                if (op != CHAR_EQUAL)
                    executeOp(op);
                op = nOp;
            }
        } else {
            op = nOp;
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
