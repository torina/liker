package com.insta.liker;

import com.insta.liker.config.Login;
import com.insta.liker.model.TagFeed;
import com.insta.liker.request.Hashtagee;
import com.insta.liker.request.Messagee;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.payload.InstagramFeedResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@org.springframework.stereotype.Controller
public class Controller {

    @Autowired
    Hashtagee hashtagee;

    @Autowired
    Messagee messagee;

    //todo how to make it user-dependant
    @Autowired
    Instagram4j userAccount;


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Model model) {
        TagFeed tagFeed = new TagFeed();
        model.addAttribute("tagFeed", tagFeed);
        return "test";
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String processHashtagFeedForm(@ModelAttribute(value = "tagFeed") TagFeed tagFeed) {
        performLiking(tagFeed.getHashtag(), tagFeed.getAmount());
        return "test";
    }

    private void performLiking(String user, String pass, String hashTag, Integer amount) {

        try {
            hashtagee.likeAllByHashtag(userAccount, hashTag, amount);
        } catch (RuntimeException e) {
            e.printStackTrace();
            ResponseEntity.ok("Operation not performed");
        }
    }


    private void performLiking(String hashTag, Integer amount) {
        try {
            hashtagee.likeAllByHashtag(userAccount, hashTag, amount);
        } catch (RuntimeException e) {
            e.printStackTrace();
            ResponseEntity.ok("Operation not performed");
        }
    }

    @Deprecated
    @GetMapping("/feed")
    public ResponseEntity<InstagramFeedResult> getFeed(@RequestParam("h") String hashTag) {
        Optional<InstagramFeedResult> feed = hashtagee.getTagFeed(userAccount, hashTag);
        if (feed.isPresent()) {
            return ResponseEntity.ok(feed.get());
        } else
            return ResponseEntity.ok(null);
    }

    @Deprecated
    @GetMapping("/likeAll")
    public ResponseEntity<InstagramFeedResult> likeAllFeed(@Value("${acc.user}") String user, @Value("${acc.pass}")
            String pass, @RequestParam("h") String hashTag, @RequestParam("a") Integer amount) {
        performLiking(user, pass, hashTag, amount);
        return ResponseEntity.ok(null);
    }


    @GetMapping("/dm")
    public ResponseEntity<InstagramFeedResult> getDM(@Value("${acc.user}") String user, @Value("${acc.pass}") String pass) {
        messagee.getDMs();
        return ResponseEntity.ok(null);
    }

}
