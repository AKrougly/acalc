package com.example.acalc;

public final class HistoryDisplay {
    String history;
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
        calculatorActivity.setHistoryDisplayText(history);
    }
    void setHistory(String text, char ch) {
        history = text;
        calculatorActivity.setHistoryDisplayText(history + " " + ch);
    }
}
