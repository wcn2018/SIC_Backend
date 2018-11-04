Getting set up:

- Install Python
- Run: `pip install numpy pandas sklearn scipy pickle Pyro4`
- [Download char_vectorizer.pkl](https://drive.google.com/open?id=1mXJczfauF5b3aJhhQy88ESvbheQDfncA) into /toxicity. (It's over 120 MB, so I can't upload it to GitHub.)

In /toxicity, run:
`python toxic_server.py`

Then, you can use `ToxicityClassifer.isToxic(String tweetText)` and similar functions, to get a probability that the given string is considered, for instance, toxic.

ToxicityClassifer.java has seven methods:
```java
isToxic(String tweetText)
isSevereToxic(String tweetText)
isObscene(String tweetText)
isThreat(String tweetText)
isInsult(String tweetText)
isIdentityHate(String tweetText)
classify(String tweetText, int version)
```

`classify(tweetText, 0)` is equivalent to `isToxic(tweetText)`, `classify(tweetText, 1)` = `isSevereToxic(tweetText)`, and so on.

Links I used for this project:
- [Logistic regression with words and char n-grams (Kaggle)](https://www.kaggle.com/tunguz/logistic-regression-with-words-and-char-n-grams)
- [scikit-learn: Save and Restore Models (Stack Abuse)](https://stackabuse.com/scikit-learn-save-and-restore-models/)
- [Running system commands in Java applications (Alvin Alexander)](https://alvinalexander.com/java/edu/pj/pj010016)
