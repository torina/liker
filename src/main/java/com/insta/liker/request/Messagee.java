package com.insta.liker.request;

import lombok.*;
import lombok.extern.log4j.Log4j;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.InstagramDirectShareRequest;
import org.brunocvcunha.instagram4j.requests.InstagramGetInboxRequest;
import org.brunocvcunha.instagram4j.requests.payload.InstagramInboxResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramInboxThread;
import org.brunocvcunha.instagram4j.requests.payload.InstagramInboxThreadItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.UnaryOperator;

import static com.insta.liker.MessageUtil.BACKEND_FRONTEND_ACTIONS;
import static com.insta.liker.MessageUtil.HELLO_ACTIONS;
import static com.insta.liker.MessageUtil.HOW_ARE_YOU_ACTIONS;

@Component
@Log4j
public class Messagee {

    private Instagram4j userAccount;

    @Autowired
    public Messagee(Instagram4j userAccount) {
        this.userAccount = userAccount;
    }


    public void getDMs() {
        try {
            InstagramInboxResult res = userAccount.sendRequest(new InstagramGetInboxRequest());
            final List<InstagramInboxThread> threads = res.getInbox().getThreads();

            threads.forEach(this::answer);
            System.out.println(res);
            System.out.println("=============== Done ============");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void answer(InstagramInboxThread thread) {
        System.out.println("\nthread: " + thread.getThread_id());
        waitABit();


        Optional<InstagramInboxThreadItem> itemOptional = getLastItem(thread);

        if (!itemOptional.isPresent()) {
            log.debug("No reply from: " + thread);
            return;
        }

        InstagramInboxThreadItem item = itemOptional.get();

        Nested n = new Nested();
        n.setInboxItem(item);
        n.setText("");

        helloProcessing.andThen(howAreYouProcessing).andThen(backendFrontend).apply(n);

        if(!n.getText().isEmpty()) {
            replyTest(n);
        }
    }


    private Optional<InstagramInboxThreadItem> getLastItem(InstagramInboxThread thread) {
        final List<InstagramInboxThreadItem> items = thread.getItems();
        return Optional.ofNullable(items.get(items.size() - 1));
    }

    private BiFunction<AbstractMap.SimpleImmutableEntry<List<String>, String>, InstagramInboxThreadItem, Void> replyStandardInquiry = (wordsToReply, inboxItem) -> {
        if (wordsToReply.getKey().stream().anyMatch(i -> inboxItem.getText().toLowerCase().contains(i))) {
            log.debug("Replying for {}" + inboxItem);
            try {
                userAccount.sendRequest(InstagramDirectShareRequest.builder(InstagramDirectShareRequest.ShareType.MESSAGE,
                        Collections.singletonList(inboxItem.getUser_id())).message(wordsToReply.getValue()).build());
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
        return null;
    };

    private void waitABit() {
        Random rand = new Random();

        int sec = rand.nextInt(6) + 1;

        try {
            Thread.sleep(sec);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    private class Nested {
        InstagramInboxThreadItem inboxItem;
        String text;
    }


    UnaryOperator<Nested> helloProcessing = nested -> {
        nested.setText(nested.text + getPresetText(HELLO_ACTIONS, nested.inboxItem));
        return nested;
    };


    UnaryOperator<Nested> howAreYouProcessing = nested -> {
        nested.setText(nested.text + getPresetText(HOW_ARE_YOU_ACTIONS, nested.inboxItem));
        return nested;
    };

    UnaryOperator<Nested> backendFrontend = nested -> {
        nested.setText(nested.text + getPresetText(BACKEND_FRONTEND_ACTIONS, nested.inboxItem));
        return nested;
    };

    private String getPresetText(AbstractMap.SimpleImmutableEntry<List<String>, String> predefinedPhrases, InstagramInboxThreadItem inboxItem) {
        if (predefinedPhrases.getKey().stream().anyMatch(i -> inboxItem.getText().toLowerCase().contains(i))) {
            log.debug("Replying for {}" + inboxItem);
            return predefinedPhrases.getValue();
        }
        return "";
    }

    private void replyTest(Nested nested) {
        try {
            userAccount.sendRequest(InstagramDirectShareRequest.builder(InstagramDirectShareRequest.ShareType.MESSAGE,
                    Collections.singletonList(nested.inboxItem.getUser_id())).message(nested.text).build());
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    ;
//    private BiFunction<AbstractMap.SimpleImmutableEntry<List<String>, String>, InstagramInboxThreadItem, Nested> containsPresetText = (wordsToReply, inboxItem) -> {
//        if (wordsToReply.getKey().stream().anyMatch(i -> inboxItem.getText().toLowerCase().contains(i))) {
//            return Nested.builder().inboxItem(inboxItem).text(wordsToReply.getValue()).contains(true).build();
//        }
//        return Nested.builder().contains(false).build();
//    };
//    private void reply(InstagramInboxThreadItem threadItem, String text) {
//        try {
//            userAccount.sendRequest(InstagramDirectShareRequest.builder(InstagramDirectShareRequest.ShareType.MESSAGE,
//                    Collections.singletonList(threadItem.getUser_id())).message(text).build());
//        } catch (IOException e) {
//            log.error(e.getMessage());
//        }
//    }

}
