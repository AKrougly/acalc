package com.example.acalc;

public final class HistoryDisplay {
    String history;
    char prevOp;
    CalculatorActivity calculatorActivity;
    HistoryDisplay(CalculatorActivity calculatorActivity) {
        this.calculatorActivity = calculatorActivity;
        clearHistory();
    }
    String getHistory() {
        return history;
    }
    void clearHistory() {
        history = "";
        prevOp = InputDisplay.CHAR_EQUAL;
        calculatorActivity.setHistoryDisplayText(history);
    }
    void setHistory(String str1, String str2, char op, char nOp) {
        if (InputDisplay.isNotEmpty(str2)) {
            history = history + " " + op + " " + str2;
            prevOp = op;
            if ((prevOp == InputDisplay.CHAR_PLUS || prevOp == InputDisplay.CHAR_MINUS) &&
                (nOp == InputDisplay.CHAR_STAR || nOp == InputDisplay.CHAR_SLASH)) {
                history = "(" + history + ")";
            }
        } else {
            if (InputDisplay.isNotEmpty(str1)) {
                if (op == InputDisplay.CHAR_EQUAL) {
                    history = str1;
                }
            }
        }
        calculatorActivity.setHistoryDisplayText(history + " " + nOp);
    }
}
