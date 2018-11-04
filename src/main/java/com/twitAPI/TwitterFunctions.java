package com.twitAPI;

import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.*;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TwitterFunctions {
    private Twitterer tweeter;
    private Twitter tweeter2;
    public static int CAPACITY = 60;
    List<String> offerKeywords = new ArrayList<String>();

    public TwitterFunctions() {
        tweeter = new Twitterer();
        tweeter2 = new TwitterFactory().getSingleton();
        //private List<Status> retweets;
    }
//We're troglodytes so getTweetsByHandle still relies on Twitterer class bc im lazy
    // Everything else works off of tweeter2, which is an instance of TwitterFactory.getSingleton(). It's easier this way
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
    /*****************High School Filter and its helpers***********/
    public boolean heightOrGrad(User user)throws TwitterException, IOException {
        String bio = user.getDescription();
        boolean height = (bio.contains("6'")||bio.contains("5'"));
        boolean grad = (bio.contains("2022")||bio.contains("2021")||bio.contains("2020"));//there's gotta be a better way to do this
        boolean hog = (height||grad);
        return hog;
    }
    public List<String> highSchoolOnlyHandles(String bigHandle)throws TwitterException, IOException{
        // a great opportunity to implement more machine learning for identification, but we dont have time
        List<User> followers = new ArrayList<User>();
        followers.addAll(tweeter2.getFollowersList(bigHandle,-1, 100));//list of all followers as User objects
        System.out.print("test1");
        List<String> handles = new ArrayList<String>();
        System.out.print("test2");
        for(int i = 0;i<=49;i++){
            User a = followers.get(i);
            System.out.print("test3");
            if(heightOrGrad(a)){
                System.out.print("test4");
                handles.add(a.getScreenName());
            }
        }
        return handles;
    }

    /*************** URL GETTER ******************/
    public String getProfilePicURL(String handle)throws TwitterException, IOException {
        User user = tweeter2.showUser(handle);
        String url = user.getProfileImageURL();
        return url;
    }
    /*************** Helper ********************/
    public Boolean isOffer(String a){
        return ( a.contains("Blessed")||a.contains("have")||a.contains("receive")|| a.contains("give")|| a.contains("blessed"))&&(a.contains("offer"));
    }

    /*************** Interest Unis ****************/
    public List<String> getProspects(String handle){ //call per athlete in list of highschool athletes. Finds their prospect schools. Returns tweet text.
        List<Status> tweets = getTweetsByHandle(handle);
        List<String> uniTweets= new ArrayList<String>();
        for (int i=0;i<tweets.size();i++) {
            Status a = tweets.get(i);
            String tweet = a.getText();
            System.out.print("test1");
            if (isOffer(tweet)) {
                System.out.print("test2");
                String b = String.valueOf(a.getId());
                uniTweets.add(b);
            }
        }
        return uniTweets;
    }
    /*
    public int gtKeywordRank(String handle){

    }
    public int rtRank(String handle){
        /*in the future, this function could be expanded quite a bit to contain more relevant accounts, like star
        players on the GT team, etc.
          */
    /*
    }

    public int percentFit(String handle){
        int gtRTrank = rtRank(handle);
        int keywordRank = gtKeywordRank(handle);
        int percent = 0;

        return percent;
    }
    */

    /************************* Tester Main Block ***************************/
    public static void main(String[] args) throws TwitterException, IOException{
        List<Status> list;
        List<Status> list2;

        TwitterFunctions tf = new TwitterFunctions();
        //list = tf.getTweetsByHandle("vandyhacks");

        List<String> listS = new ArrayList<String>(Twitterer.CAPACITY);
        List<String> tester = new ArrayList<String>();
        List<String> testlist = tf.highSchoolOnlyHandles("scoutPROcoach");
        System.out.print(testlist);
        for(int i = 0; i<=testlist.size();i++){
            String handle = testlist.get(i);
            System.out.print(tf.getProspects(handle));
        }

        //System.out.print(tf.getProspects("EscoJamaal"));
        //tester = (tf.highSchoolOnlyHandles("scoutPROcoach"));

        /*for (Status s : list) {
            listS.add(s.getText());
            System.out.println(s.getText());
        }

        System.out.println(list.size());
        System.out.println(listS.get(0));*/
    }

}
