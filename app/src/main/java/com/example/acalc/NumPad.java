package com.example.acalc;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

enum KeyType {
    NUMBER,
    OPERATOR,
    COMMAND,
    MEMORY,
}

enum KeyMeaning {
    CLEAR,
    BACKSPACE,
    SWITCH_SIGN,
    DIGIT,
    COMMA,
    OPERATOR_PERCENT,
    OPERATOR_DIVIDE,
    OPERATOR_MULTIPLY,
    OPERATOR_SUBTRACT,
    OPERATOR_ADD,
    EQUAL,
    MEMORY_READ,
    MEMORY_CLEAR,
    MEMORY_ADD,
    MEMORY_SUBTRACT;
    public static final int size;
    static {
        size = values().length;
    }
}
public final class NumPad {
    static char CR  = (char) 0x0D; /* \r \u000d: carriage return CR */
    CalculatorActivity calculatorActivity;
    InputDisplay inputDisplay;
    Subtotal subtotal;
    Key KEY_EQUAL = new Key("button_key_equal", Character.toString(inputDisplay.CHAR_EQUAL), KeyEvent.KEYCODE_EQUALS, KeyType.OPERATOR, KeyMeaning.EQUAL);
    Key KEY_BACKSPACE = new Key("button_key_backspace", "BackSpace", KeyEvent.KEYCODE_DEL, KeyType.COMMAND, KeyMeaning.BACKSPACE);
    KeyEvent lastKeyEvent;
    Key lastKey;
    //Key[] key = new Key[KeyMeaning.values().length];
    Key[] keyArray = new Key[] {
        new Key("button_key_zero", "0", KeyEvent.KEYCODE_0, KeyType.NUMBER, KeyMeaning.DIGIT),
        new Key("button_key_one", "1", KeyEvent.KEYCODE_1, KeyType.NUMBER, KeyMeaning.DIGIT),
        new Key("button_key_two", "2", KeyEvent.KEYCODE_2, KeyType.NUMBER, KeyMeaning.DIGIT),
        new Key("button_key_three", "3", KeyEvent.KEYCODE_3, KeyType.NUMBER, KeyMeaning.DIGIT),
        new Key("button_key_four", "4", KeyEvent.KEYCODE_4, KeyType.NUMBER, KeyMeaning.DIGIT),
        new Key("button_key_five", "5", KeyEvent.KEYCODE_5, KeyType.NUMBER, KeyMeaning.DIGIT),
        new Key("button_key_six", "6", KeyEvent.KEYCODE_6, KeyType.NUMBER, KeyMeaning.DIGIT),
        new Key("button_key_seven", "7", KeyEvent.KEYCODE_7, KeyType.NUMBER, KeyMeaning.DIGIT),
        new Key("button_key_eight", "8", KeyEvent.KEYCODE_8, KeyType.NUMBER, KeyMeaning.DIGIT),
        new Key("button_key_nine", "9", KeyEvent.KEYCODE_9, KeyType.NUMBER, KeyMeaning.DIGIT),

        new Key("button_key_divide", Character.toString(inputDisplay.CHAR_SLASH), KeyEvent.KEYCODE_SLASH, KeyType.OPERATOR, KeyMeaning.OPERATOR_DIVIDE),
        new Key("button_key_multiply", Character.toString(inputDisplay.CHAR_STAR), KeyEvent.KEYCODE_STAR, KeyType.OPERATOR, KeyMeaning.OPERATOR_MULTIPLY),
        new Key("button_key_subtract", Character.toString(inputDisplay.CHAR_MINUS), KeyEvent.KEYCODE_MINUS, KeyType.OPERATOR, KeyMeaning.OPERATOR_SUBTRACT),
        new Key("button_key_add", Character.toString(inputDisplay.CHAR_PLUS), KeyEvent.KEYCODE_PLUS, KeyType.OPERATOR, KeyMeaning.OPERATOR_ADD),
        new Key("button_key_percent", Character.toString(inputDisplay.CHAR_PERCENT), 0, KeyType.OPERATOR, KeyMeaning.OPERATOR_PERCENT),

        new Key("button_key_clear", "Del", KeyEvent.KEYCODE_C, KeyType.COMMAND, KeyMeaning.CLEAR),
        new Key("button_key_switch_sign", "Ins", KeyEvent.KEYCODE_INSERT, KeyType.COMMAND, KeyMeaning.SWITCH_SIGN),
        new Key("button_key_comma", Character.toString(inputDisplay.DECIMAL_SEPARATOR), KeyEvent.KEYCODE_COMMA, KeyType.NUMBER, KeyMeaning.COMMA),
        KEY_BACKSPACE,
        KEY_EQUAL,

        new Key("button_key_memory_read", "Home", KeyEvent.KEYCODE_HOME, KeyType.MEMORY, KeyMeaning.MEMORY_READ),
        new Key("button_key_memory_clear", "End", KeyEvent.KEYCODE_MOVE_END, KeyType.MEMORY, KeyMeaning.MEMORY_CLEAR),
        new Key("button_key_memory_add", "PgUp", KeyEvent.KEYCODE_PAGE_UP, KeyType.MEMORY, KeyMeaning.MEMORY_ADD),
        new Key("button_key_memory_subtract", "PgDown", KeyEvent.KEYCODE_PAGE_DOWN, KeyType.MEMORY, KeyMeaning.MEMORY_SUBTRACT),
    };
    List<Key> keyList = Arrays.asList(keyArray);
    NumPad(CalculatorActivity calculatorActivity) {
        this.calculatorActivity = calculatorActivity;
        this.inputDisplay = new InputDisplay(calculatorActivity);
        this.subtotal = new Subtotal();
        inputDisplay.clearHistory();
        lastKey = KEY_EQUAL;

        for(Key key : keyList) {
            try {
                Button button = (Button) calculatorActivity.findViewById(calculatorActivity.getResources().getIdentifier(key.buttonId, "id", calculatorActivity.getPackageName()));
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        keyHandler(key);
                    }
                });
            } catch(Exception e) {
                Log.e("ACalc", "Bad key" + key.buttonId);
            }
        }
    }
    Key findKeyByCode(int keyCode) {
        return
            keyList
                .stream()
                .filter(item -> item.keyCode == keyCode)
                .findFirst()
                .orElse(null);
    }
    Key findKeyByKeyboardKey(String keyboardKey) {
        return
            keyList
                .stream()
                .filter(item -> item.keyboardKey.equals(keyboardKey))
                .findFirst()
                .orElse(null);
    }
    void keyClearHandler() {
        inputDisplay.clearAll();
        inputDisplay.clearHistory();
    }
    void keyBackspaceHandler() {
        inputDisplay.cutLastCh();
        lastKey = findKeyByKeyboardKey(Character.toString(inputDisplay.getLastChar()));
    }
    void keySwitchSignHandler() {
        inputDisplay.appendMinusSign();
    }
    void keyTypeCommandHandler(Key key) {
        switch (key.keyMeaning) {
            case CLEAR:
                keyClearHandler();
                break;
            case BACKSPACE:
                keyBackspaceHandler();
                break;
            case SWITCH_SIGN:
                keySwitchSignHandler();
                break;
            default:
                //throw new IllegalStateException("Invalid keyType: "+ key.keyType);
                break;
        }
    }
    void keyDigitHandler(Key key) {
        if (key.getChar() != inputDisplay.CHAR_ZERO ||
            !inputDisplay.getText().equals("" + inputDisplay.CHAR_ZERO) &&
            !inputDisplay.getText().equals("" + inputDisplay.CHAR_MINUS + inputDisplay.CHAR_ZERO)
        ) {
            inputDisplay.appendCh(key.getChar());
        }
    }
    void keyCommaHandler(Key key) {
        if (!inputDisplay.getText().contains("" + inputDisplay.CHAR_COMMA)) {
            if (inputDisplay.isEmpty(inputDisplay.getText()))
                inputDisplay.appendCh(inputDisplay.CHAR_ZERO);
            inputDisplay.appendCh(inputDisplay.CHAR_COMMA);
        }
    }
    void keyTypeNumberHandler(Key key) {
        if (lastKey.keyMeaning == KeyMeaning.EQUAL || lastKey.keyMeaning == KeyMeaning.OPERATOR_PERCENT) {
            inputDisplay.clearAll();
            inputDisplay.clearHistory();
        }
        switch (key.keyMeaning) {
            case DIGIT:
                keyDigitHandler(key);
                break;
            case COMMA:
                keyCommaHandler(key);
                break;
            default:
                //throw new IllegalStateException("Invalid keyType: "+ key.keyType);
                break;
        }
    }
    void keyTypeOperatorHandler(Key key) {
        if (inputDisplay.getLastChar() == inputDisplay.CHAR_COMMA)
            keyHandler(KEY_BACKSPACE);
        inputDisplay.setOp(key);
    }
    void keyMemoryClearHandler() {
        subtotal.clearSubtotal();
    }
    void keyMemoryReadHandler() {
        inputDisplay.setText(subtotal.readSubtotal());
    }
    void keyMemoryAddHandler() {
        subtotal.setSubtotal(
            inputDisplay.calcExpr(subtotal.readSubtotal(), inputDisplay.getText(), inputDisplay.CHAR_PLUS)
        );
    }
    void keyMemorySubtractHandler() {
        subtotal.setSubtotal(
            inputDisplay.calcExpr(subtotal.readSubtotal(), inputDisplay.getText(), inputDisplay.CHAR_MINUS)
        );
    }
    void keyTypeMemoryHandler(Key key) {
        Key lk = lastKey;
        switch (key.keyMeaning) {
            case MEMORY_CLEAR:
                keyMemoryClearHandler();
                break;
            case MEMORY_READ:
                keyMemoryReadHandler();
                break;
            case MEMORY_ADD:
                keyMemoryAddHandler();
                break;
            case MEMORY_SUBTRACT:
                keyMemorySubtractHandler();
                break;
            default:
                //throw new IllegalStateException("Invalid keyType: "+ key.keyType);
                break;
        }
        lastKey = lk;
    }
    void keyHandler(Key key) {
        if (key != null) {
            switch (key.keyType) {
                case NUMBER:
                    keyTypeNumberHandler(key);
                    break;
                case OPERATOR:
                    keyTypeOperatorHandler(key);
                    break;
                case COMMAND:
                    keyTypeCommandHandler(key);
                    break;
                case MEMORY:
                    keyTypeMemoryHandler(key);
                    break;
                default:
                    //throw new IllegalStateException("Invalid keyType: "+ key.keyType);
                    break;
            }
            lastKey = key;
        }
    }
    public void keyEventHandler(KeyEvent event) {
        int keyCode = event.getKeyCode();
        if (lastKeyEvent != null) {
            if (lastKeyEvent.getKeyCode() == KeyEvent.KEYCODE_SHIFT_LEFT) {
                keyCode =
                        (keyCode == KeyEvent.KEYCODE_8 ? KeyEvent.KEYCODE_STAR :
                        (keyCode == KeyEvent.KEYCODE_EQUALS ? KeyEvent.KEYCODE_PLUS : keyCode)
                        );
            } else {
                keyCode = keyCode == KeyEvent.KEYCODE_PERIOD ?
                        KeyEvent.KEYCODE_COMMA :
                        keyCode == KeyEvent.KEYCODE_ENTER ?
                                KeyEvent.KEYCODE_EQUALS :
                                keyCode;
            }
        }
        //Log.i("UnicodeChar", Integer.toString(event.getUnicodeChar()) + " " + Integer.toString(keyCode));
        keyHandler(findKeyByCode(keyCode));
        lastKeyEvent = event;
    }
}
