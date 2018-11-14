package com.insta.liker;

import lombok.extern.log4j.Log4j;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.InstagramLikeRequest;
import org.brunocvcunha.instagram4j.requests.InstagramTagFeedRequest;
import org.brunocvcunha.instagram4j.requests.payload.InstagramFeedItem;
import org.brunocvcunha.instagram4j.requests.payload.InstagramFeedResult;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
@Log4j
public class Hashtagee {

    private static String LIMIT_LIKES = "100";
    private static int LIMIT_LIKES_INT = 1000;

    Optional<InstagramFeedResult> getTagFeed(Instagram4j instagram, String hashtag) {
        try {
             return Optional.of(instagram.sendRequest(new InstagramTagFeedRequest(hashtag)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    void likeAllByHashtag(Instagram4j account, String hashTag, int numLikes) {
        int finalLikeAmount = numLikes < LIMIT_LIKES_INT? numLikes : LIMIT_LIKES_INT;

        Optional<InstagramFeedResult> res = getTagFeed(account, hashTag);
        int i = 0;
        if(res.isPresent()) {
            for(InstagramFeedItem item: res.get().getItems()){

                if(!item.has_liked && i++ < finalLikeAmount) {
                    try {
                        account.sendRequest(new InstagramLikeRequest(item.pk));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    log.info("Finished liking.");
                    break;
                }
            }
        }
    }

}
