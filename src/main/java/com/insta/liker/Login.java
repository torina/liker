package com.insta.liker;

import org.brunocvcunha.instagram4j.Instagram4j;

import java.io.IOException;

/**
 * login to Instagram
 */
public class Login {

    static Instagram4j getAccount(String userName, String pass) {
        Instagram4j instagram;
        instagram = Instagram4j.builder()
                .username(userName)
                .password(pass)
                .build();
        instagram.setup();
        try {
            instagram.login();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return instagram;
    }
}
