package com.twitAPI;

import com.toxicity.ToxicityClassifier;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/*
@author WIlliam Chen for GT hackathon
@author Jeffery Luo for GT hackathon
*/

public class TwitterFunctions {
    private Twitterer tweeter;
    private Twitter tweeter2;
    public static int CAPACITY = 60;
    List<String> offerKeywords = new ArrayList<String>();
    List<Status> tweets;

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

    /*****************  High School Filter and its helpers  ***********/
    public static boolean heightOrGrad(User user) throws TwitterException, IOException {
        String bio = user.getDescription();
        boolean height = (bio.contains("6'") || bio.contains("5'"));
        boolean grad = (bio.contains("2019") || bio.contains("2020") || bio.contains("2021") || bio.contains("2022"));//there's gotta be a better way to do this
        return (height || grad);
    }

    public List<String> highSchoolOnlyHandles(String bigHandle, int numScanned) throws TwitterException, IOException {
        // a great opportunity to implement more machine learning for identification, but we dont have time
        int numHS = 50;
        List<User> followers = new ArrayList<User>(numScanned);
        followers.addAll(tweeter2.getFollowersList(bigHandle, -1, numScanned));//list of all followers as User objects

        List<String> handles = new ArrayList<String>(numHS);
        int checker = 0;
        for (int i = 0; i < numScanned && i < followers.size(); i++) {
            User a = followers.get(i);
            if (heightOrGrad(a)) {
                handles.add(a.getScreenName());
                checker++;
            }
            if (checker > numHS) {
                break;
            }
        }
        return handles;
    }

    /*************** URL GETTER ******************/
    public String getProfilePicURL(String handle) throws TwitterException, IOException {
        User user = tweeter2.showUser(handle);
        String url = user.getProfileImageURL();
        return url;
    }

    /*************** Helper ********************/
    public static Boolean isOffer(String a) {
        return (a.contains("Blessed") || a.contains("have") || a.contains("receive") || a.contains("give") || a.contains("blessed")) && (a.contains("offer"));
    }

    /*************** Interest Unis ****************/
    public List<String> getProspects(String handle) { //call per athlete in list of highschool athletes. Finds their prospect schools. Returns tweet text.
        List<Status> tweets = getTweetsByHandle(handle);
        List<String> uniTweets = new ArrayList<String>(10);
        for (int i = 0; i < tweets.size(); i++) {
            Status a = tweets.get(i);
            String tweet = a.getText();
            if (isOffer(tweet)) {
                uniTweets.add(tweet);
                String b = String.valueOf(a.getId());
                uniTweets.add(b);
            }
        }
        return uniTweets;
    }

    public List<String> getFlaggedTweets(String handle) {
        if (tweets == null)
            tweets = getTweetsByHandle(handle);

        List<String> uniTweets = new ArrayList<String>();
        for (int i = 0; i < tweets.size(); i++) {

            String tweet = tweets.get(i).getText();

            if (ToxicityClassifier.isInsult(tweet) > 0.5 || ToxicityClassifier.isIdentityHate(tweet) > 0.5
                    || ToxicityClassifier.isObscene(tweet) > 0.5 || ToxicityClassifier.isSevereToxic(tweet) > 0.5 ||
                    ToxicityClassifier.isThreat(tweet) > 0.5 || ToxicityClassifier.isToxic(tweet) > 0.5) {

                uniTweets.add(tweet);
            }

            // uniTweets.add(tweet);
        }

        return uniTweets;
    }

    public String getName(String handle) throws TwitterException, IOException {
        User user = tweeter2.showUser(handle);
        return user.getName();
    }

    public int rtRank(String handle) throws TwitterException {
        /*in the future, this function could be expanded quite a bit to contain more relevant accounts, like star
        players on the GT team, etc.
        It could also contain the retweets themselves later (front end has no time for this atm)
          */
        List<Status> allStatuses = new ArrayList<Status>(CAPACITY * 3);
        List<Status> rts = new ArrayList<Status>(CAPACITY * 3);
        List<Status> gtrts = new ArrayList<Status>(CAPACITY);
        int numrts = 0;
        //tweetgrabber
        Paging page = new Paging(1, 20); // grabs max 20 per page
        int p = 1;
        while (p <= 3) {
            page.setPage(p);
            allStatuses.addAll(tweeter2.getUserTimeline(handle, page));
            p++;
        }
        for (int i = 0; i <= CAPACITY * 3; i++) {
            if (allStatuses.get(i).isRetweet()) {
                rts.add(allStatuses.get(i).getQuotedStatus());
            }
        }
        //check if is GT retweet
        for (int i = 0; i <= CAPACITY; i++) {
            String originalTweeter = rts.get(i).getUser().getScreenName();
            if (originalTweeter == "TWGrecruiting" || originalTweeter == "GeorgiaTechFB" || originalTweeter == "GTAthletics") {
                numrts++;
            }
        }
        //block for returning ranks
        if (numrts >= 6) {
            return 2;
        }
        if (numrts >= 3) {
            return 1;
        }
        return 0;
    }

    public boolean followsGT(String handle) throws TwitterException {
        List<User> following = new ArrayList<User>();
        following.addAll(tweeter2.getFriendsList(handle, -1));
        for (int i = 0; i <= following.size(); i++) {
            String followHandle = following.get(i).getScreenName();
            if (followHandle == "TWGrecruiting" || followHandle == "GeorgiaTechFB" || followHandle == "GTAthletics") {
                return true;
            }
        }
        return false;
    }

    public int gtKeywordRank(String handle) throws TwitterException, IOException {
        List<String> tester = new ArrayList<String>(CAPACITY);
        int numKeys = 0;
        for (int i = 0; i <= CAPACITY; i++) {
            String string = (String.valueOf(tester.get(i)));
            if (string.contains("engineering") || string.contains("Tech") || string.contains("Georgia Tech")) {//these are some crappy keywords but thye're here as example
                numKeys++;
            }
        }
        if (numKeys >= 6) {
            return 2;
        }
        if (numKeys >= 3) {
            return 1;
        }
        return 0;
    }

    //interest starts at 50%
//For each keyword rank or retweet rank, add 5%
//Adds 30% if following a GT account
//removes some percent if no retweet or keyword. This is kinda unfair irl but for now it's a way of getting lower numbers
    public int percentFit(String handle) throws TwitterException, IOException {
        int gtRTrank = rtRank(handle);
        int keywordRank = gtKeywordRank(handle);
        int percent = 50;
        percent += gtRTrank * 5 + keywordRank * 5;
        if (followsGT(handle)) {
            percent += 30;
        } else {
            if (keywordRank == 0) {
                percent -= 10;
            }
            if (gtRTrank == 0) {
                percent -= 10;
            }
        }
        return percent;
    }

/****************************************End NEW*********************************************/
//s
//s

    /************************* Tester Main Block ***************************/

    public static void main(String[] args) throws TwitterException, IOException {
        List<Status> list;
        List<Status> list2;

        TwitterFunctions tf = new TwitterFunctions();
        //list = tf.getTweetsByHandle("vandyhacks");

        List<String> listS = new ArrayList<String>(Twitterer.CAPACITY);
        List<String> tester = new ArrayList<String>(50);
        /*List<String> testlist = tf.highSchoolOnlyHandles("scoutPROcoach");
        System.out.print(testlist);
        for (int i = 0; i <= testlist.size(); i++) {
            String handle = testlist.get(i);
            System.out.print(tf.getProspects(handle));
        }*/

        //System.out.print(tf.getProspects("EscoJamaal"));
        tester = (tf.highSchoolOnlyHandles("scoutPROcoach", 150));
        System.out.print(tester);

        /*for (Status s : list) {
            listS.add(s.getText());
            System.out.println(s.getText());
        }

        System.out.println(list.size());
        System.out.println(listS.get(0));*/
    }

}
