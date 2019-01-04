package com.insta.liker.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class Conversation {
    private final String threadId;
    private final String message;
}
