package com.toxicity;

import java.io.*;

public class ToxicityClassifier {

    /**
     * Returns the probability that the given text is (one of six types of) problematic.
     * version = 0: toxic
     * version = 1: severe_toxic
     * version = 2: obscene
     * version = 3: threat
     * version = 4: insult
     * version = 5: identity_hate
     *
     * @param tweetText
     * @param version
     * @return the probability that the given string is toxic based on the given version
     */
    public static double classify(String tweetText, int version) {
        tweetText = tweetText.replaceAll("\"", "\\\\\"").replace("\\", "");
        String clientCommand = "python toxic_client.py " + tweetText + " " + version;
        String s = null;

        try {
            Process p = Runtime.getRuntime().exec(clientCommand);
            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(p.getInputStream()));
            BufferedReader stdError = new BufferedReader(new
                    InputStreamReader(p.getErrorStream()));

            // read the output from the command
            while ((s = stdInput.readLine()) != null) {
                System.out.println("yo");
                return Double.parseDouble(s);
            }

            // read any errors from the attempted command
            // System.out.println("Here is the standard error of the command (if any):\n");
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }
            System.exit(0);
        } catch (IOException e) {
            System.out.println("exception happened - here's what I know: ");
            e.printStackTrace();
            System.exit(-1);
        }
        return -1;
    }

    public static double isToxic(String tweetText) {
        return classify(tweetText, 0);
    }

    public static double isSevereToxic(String tweetText) {
        return classify(tweetText, 1);
    }

    public static double isObscene(String tweetText) {
        return classify(tweetText, 2);
    }

    public static double isThreat(String tweetText) {
        return classify(tweetText, 3);
    }

    public static double isInsult(String tweetText) {
        return classify(tweetText, 4);
    }

    public static double isIdentityHate(String tweetText) {
        return classify(tweetText, 5);
    }
}
