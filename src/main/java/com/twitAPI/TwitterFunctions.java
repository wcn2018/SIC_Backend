package com.twitAPI;

import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TwitterFunctions {
    private Twitterer tweeter;

    public TwitterFunctions() {
        tweeter = new Twitterer();
    }

    public List<Status> getTweetsByHandle(String handle) {
        tweeter.reset();

        try {
            tweeter.getTweets(handle);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TwitterException e) {
            e.printStackTrace();
        }

        return tweeter.getStatuses();
    }

    public static void main(String[] args) {
        List<Status> list;
        List<Status> list2;

        TwitterFunctions tf = new TwitterFunctions();
        list = tf.getTweetsByHandle("wcn2018");

        List<String> listS = new ArrayList<String>(Twitterer.CAPACITY);

        for (Status s : list) {
            listS.add(s.getText());
            System.out.println(s.getText());
        }

        System.out.println(list.size());
        System.out.println(listS.get(0));
    }
}
