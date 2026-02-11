package com.example.gestor.security;

import org.springframework.security.crypto.password.PasswordEncoder;

public class PlaintextPasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence rawPassword) {
        return rawPassword == null ? null : rawPassword.toString();
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (rawPassword == null && encodedPassword == null) {
            return true;
        }
        if (rawPassword == null || encodedPassword == null) {
            return false;
        }
        return encodedPassword.equals(rawPassword.toString());
    }
}
