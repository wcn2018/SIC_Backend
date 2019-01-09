A Python server/Java class for identifying whether a given string is likely to be toxic, obscene, hateful, etc.

# Getting Started

- Install Python
- Run: `pip install numpy pandas sklearn scipy pickle Pyro4`
- [Download char_vectorizer.pkl](https://drive.google.com/open?id=1mXJczfauF5b3aJhhQy88ESvbheQDfncA). (It's over 120 MB, so I can't upload it to GitHub.)

Run:
`python toxic_server.py`

Once our Python daemon is up and running, you can use the functions in ToxicityClassifer.java:
```java
isToxic(String tweetText)
isSevereToxic(String tweetText)
isObscene(String tweetText)
isThreat(String tweetText)
isInsult(String tweetText)
isIdentityHate(String tweetText)
classify(String tweetText, int version)
```

These take in a string and return a probability. `classify(tweetText, 0)` is equivalent to `isToxic(tweetText)`, `classify(tweetText, 1)` corresponds to `isSevereToxic(tweetText)`, and so on.

Or if you'd prefer to stick with Python, you can run:
```python
python toxic_client.py TEXT VERSION
```
where TEXT is a string, and VERSION is an integer from 0 to 6 corresponding to toxic, severe toxic, etc.

Links I referenced for this project:
- [Logistic regression with words and char n-grams (Kaggle)](https://www.kaggle.com/tunguz/logistic-regression-with-words-and-char-n-grams)
- [scikit-learn: Save and Restore Models (Stack Abuse)](https://stackabuse.com/scikit-learn-save-and-restore-models/)
- [Running system commands in Java applications (Alvin Alexander)](https://alvinalexander.com/java/edu/pj/pj010016)
- [Intro to Pyro 4.60](https://pythonhosted.org/Pyro4/intro.html)