package com.insta.liker.config;

import org.brunocvcunha.instagram4j.Instagram4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * login to Instagram
 */

//todo think how to impl it for multiple users.

@Configuration
public class Login {


    @Bean
    Instagram4j userAccount(@Value("${acc.user}") String userName, @Value("${acc.pass}") String pass) {
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
