package com.insta.liker;

import com.google.common.collect.ImmutableList;

import java.util.AbstractMap;
import java.util.List;

public class MessageUtil {
    public static List<String> HELLO_OPTIONS = ImmutableList.of("hi", "hello", "hallo", "hey", "good morning");

    //todo make list to list
    public static AbstractMap.SimpleImmutableEntry<List<String>, String> HELLO_ACTIONS = new AbstractMap.SimpleImmutableEntry(HELLO_OPTIONS, "Hi! \uD83D\uDE0A");

    public static List<String> HOW_ARE_YOU_OPTIONS = ImmutableList.of("How are you", "how r u", "how you doing");

    public static AbstractMap.SimpleImmutableEntry<List<String>, String> HOW_ARE_YOU_ACTIONS = new AbstractMap.SimpleImmutableEntry(HOW_ARE_YOU_OPTIONS, "I'm good, thanks! \uD83D\uDE03");

    public static List<String> BACKEND_FRONTEND_OPTIONS = ImmutableList.of("backend", "frontend");

    public static AbstractMap.SimpleImmutableEntry<List<String>, String> BACKEND_FRONTEND_ACTIONS =
            new AbstractMap.SimpleImmutableEntry(BACKEND_FRONTEND_OPTIONS, "I'm a backend developer! You can read more on my profile page \uD83D\uDE0A");
}
