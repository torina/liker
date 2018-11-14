package com.insta.liker;

import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.payload.InstagramFeedResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/")
public class Controller {

    @Autowired
    Hashtagee hashtagee;

    @GetMapping("feed")
    public ResponseEntity<InstagramFeedResult> getFeed(@RequestParam("u") String user, @RequestParam("p")String pass, @RequestParam("h")String hashTag) {
        Instagram4j acc = Login.getAccount(user, pass);
        Optional<InstagramFeedResult> feed = hashtagee.getTagFeed(acc, hashTag);
        if(feed.isPresent()) {
            return ResponseEntity.ok(feed.get());
        }
        else
            return ResponseEntity.ok(null);
    }

    @GetMapping("likeAll")
    public ResponseEntity<InstagramFeedResult> likeAllFeed(@RequestParam("u") String user, @RequestParam("p")String pass, @RequestParam("h")String hashTag) {
        Instagram4j acc = Login.getAccount(user, pass);

        try {
            hashtagee.likeAllByHashtag(acc, hashTag);
        } catch (RuntimeException e) {
            e.printStackTrace();
            ResponseEntity.ok("Operation not performed");
        }
            return ResponseEntity.ok(null);
    }
}
