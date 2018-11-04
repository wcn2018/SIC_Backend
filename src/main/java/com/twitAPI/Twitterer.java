package com.twitAPI;
import twitter4j.GeoLocation;       // jar found at http://twitter4j.org/en/index.html
import twitter4j.Paging;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.TwitterException;

import java.io.IOException;
import java.io.PrintStream;

import java.util.ArrayList;
import java.util.List;

public class Twitterer
{
    private Twitter twitter;
    private PrintStream consolePrint;
    private List<Status> statuses;
    //private List<Status> retweets;


    public Twitterer(PrintStream console)
    {
        // Makes an instance of Twitter - this is re-useable and thread safe.
        // Connects to Twitter and performs authorizations.
        twitter = TwitterFactory.getSingleton();
        consolePrint = console;
        statuses = new ArrayList<Status>();
        //retweets = new ArrayList<Status>();
    }

    /******************  Adapted Tweet Grabber *******************/
    /**
     * This method queries the tweets of a particular user's handle.
     * @param *String  the Twitter handle (username) without the @sign
     * Passes to arrays containing plain statuses and retweets (optional)
     */
    @SuppressWarnings("unchecked")
    public void queryHandle(String handle) throws TwitterException, IOException
    {
        statuses.clear();// queryHandle will be looped, clear statuses each time or else it's a mess
        getTweets(handle);
        int numTweets = statuses.size();
        while (numTweets > 0){
            numTweets--;
            System.out.println("Tweet"+numTweets+": "+statuses.get(numTweets).getText());
        }
    }

    /**
     * This helper method fetches the most recent 2,000 tweets of a particular user's handle and
     * stores them in an arrayList of Status objects.  Populates statuses.
     * @param *String  the Twitter handle (username) without the @sign
     */
    private void getTweets(String handle) throws TwitterException, IOException
    {
        //total tweets grabbed = p*count in the Paging instance
        Paging page = new Paging(1,100); // grabs 100 tweets per page
        int p = 1;
        while (p<=10){
            page.setPage(p);
            statuses.addAll(twitter.getUserTimeline(handle));
            //retweets.addAll(twitter.getRetweetsOfMe());
            p++;
        }
    }

}
