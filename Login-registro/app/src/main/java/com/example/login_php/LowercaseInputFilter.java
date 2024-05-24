package com.example.login_php;

import android.text.InputFilter;
import android.text.Spanned;

public class LowercaseInputFilter implements InputFilter {
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        // Convertir el texto ingresado a minúsculas
        return source.toString().toLowerCase();
    }
}
