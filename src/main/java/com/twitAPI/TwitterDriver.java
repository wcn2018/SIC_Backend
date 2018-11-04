package com.twitAPI;
/**
 * Twitter Driver and Client
 *
 * @author Ria Galanos
 * @author Tony Potter
 * @author WIlliam Chen, adapted for GT hackathon.
 * Original idea by Ria Galanos, whose documentation and source can be found at
 * https://github.com/riagalanos/cs1-twitter
 **/

//This whole driver simply creates an instance of Twitterer and calls its methods.
//Originally, this received the handle from a console input on IDEA,
//Tweak this to take a handle from the front end instead.

import twitter4j.TwitterException;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

public class TwitterDriver {
    private static PrintStream consolePrint;

    /*
    public static void main(String[] args) throws TwitterException, IOException {
        // set up classpath and properties file

        Twitterer scraper = new Twitterer(consolePrint);

        // Tweet scraper
        // Choose a public Twitter user's handle and analyze their tweets

        Scanner scan = new Scanner(System.in);//edit later to pass in handle directly
        System.out.print("Please enter a Twitter handle, do not include the '@' symbol (or 'done' to quit.)");
        String twitter_handle = scan.next(); //put the input handle here, pass to scan.
        while (!"done".equals(twitter_handle)) {
            scraper.queryHandle(twitter_handle);
            System.out.print("Please enter a Twitter handle, do not include the '@' symbol (or 'done' to quit.)");
            twitter_handle = scan.next();
        }
    }

    */

}
         
   