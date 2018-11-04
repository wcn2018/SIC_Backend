package com.twitAPI;
import twitter4j.*;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class Twitterer {
    public static int CAPACITY = 60;
    private Twitter twitter;
    private PrintStream consolePrint;
    private List<Status> statuses;
    private List<IDs> follows;
    private List<IDs> followers;
    //private List<Status> retweets;
    private List<String> offerKeywords = new ArrayList<String>();


    public Twitterer() {
        twitter = TwitterFactory.getSingleton();
        statuses = new ArrayList<Status>(CAPACITY);
    }

    public void reset() {
        statuses = new ArrayList<Status>(CAPACITY);
    }


    public Twitterer(PrintStream console) {
        // Makes an instance of Twitter - this is re-useable and thread safe.
        // Connects to Twitter and performs authorizations.
        twitter = TwitterFactory.getSingleton();
        consolePrint = console;
        statuses = new ArrayList<Status>();
        follows = new ArrayList<IDs>();
        followers = new ArrayList<IDs>();

        //retweets = new ArrayList<Status>();
    }

    /******************  Adapted Tweet Grabber *******************/
    /**
     * This method queries the tweets of a particular user's handle.
     *
     * @param *String the Twitter handle (username) without the @sign
     *                Passes to arrays containing plain statuses and retweets (optional)
     */
    @SuppressWarnings("unchecked")
    public void queryHandle(String handle) throws TwitterException, IOException {
        statuses.clear();// queryHandle will be looped, clear statuses each time or else it's a mess
        follows.clear();//same
        followers.clear();

        getTweets(handle);
        int numTweets = statuses.size();
        while (numTweets > 0) {
            numTweets--;
            System.out.println("Tweet" + numTweets + ": " + statuses.get(numTweets).getText());
        }

        getNetwork(handle);//creates arraylist of follows and followers
    }

    /**
     * This helper method fetches the most recent 2,000 (or however many) tweets of a particular user's handle and
     * stores them in an arrayList of Status objects.  Populates statuses.
     *
     * @param *String the Twitter handle (username) without the @sign
     */
    public void getTweets(String handle) throws TwitterException, IOException {
        //total tweets grabbed = p*count in the Paging instance
        Paging page = new Paging(1, 20); // grabs max 20 per page
        int p = 1;
        while (p <= 3) {
            page.setPage(p);
            statuses.addAll(twitter.getUserTimeline(handle, page));
            //retweets.addAll(twitter.getRetweetsOfMe());
            p++;
        }
        for (int i=0;i<statuses.size();i++){
            if (statuses.get(i).isRetweet()){
                statuses.remove(i);
            }
        }
    }

    public List<Status> getStatuses() {
        return statuses;
    }

    /**
     * Another helper method. This one gets the user's followers
     */
    private void getNetwork(String handle) throws TwitterException, IOException {
        follows.add(twitter.getFriendsIDs(handle, -1));
        followers.add(twitter.getFollowersIDs(handle, -1));
    }
    public void getUniversities(){

    }
}