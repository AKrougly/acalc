package com.example.acalc;

public final class Key {
    String buttonId;
    String keyboardKey;
    int keyCode;
    KeyType keyType;
    KeyMeaning keyMeaning;
    Key (String buttonId, String keyboardKey, int keyCode, KeyType keyType, KeyMeaning keyMeaning) {
        this.buttonId = buttonId;
        this.keyboardKey = keyboardKey;
        this.keyCode = keyCode;
        this.keyType = keyType;
        this.keyMeaning = keyMeaning;
    }
    char getChar() {
        return keyboardKey.charAt(0);
    }
    @Override
    public boolean equals(Object obj)
    {
        // checking if both the object references are
        // referring to the same object.
        if(this == obj)
            return true;

        // it checks if the argument is of the
        // type Geek by comparing the classes
        // of the passed argument and this object.
        // if(!(obj instanceof Geek)) return false; ---> avoid.
        if(obj == null || obj.getClass() != this.getClass())
            return false;

        // type casting of the argument.
        com.example.acalc.Key key = (com.example.acalc.Key)obj;

        // comparing the state of argument with
        // the state of 'this' Object.
        return (
            key.keyboardKey.equals(this.keyboardKey) &&
                key.keyCode == this.keyCode &&
                key.keyType == this.keyType &&
                key.keyMeaning == this.keyMeaning
        );
    }
}
