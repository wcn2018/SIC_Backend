import Pyro4
import sys

with open("TOXIC_SERVER_URI","r") as uri_file:
	uri=uri_file.read()

tweet_text = sys.argv[1]
version = sys.argv[2]

classifier = Pyro4.Proxy(uri)

if version == "0" or version == "toxic":
	print(classifier.toxic_score(tweet_text))
elif version == "1" or version == "severe_toxic":
	print(classifier.severe_toxic_score(tweet_text))
elif version == "2" or version == "obscene":
	print(classifier.obscene_score(tweet_text))
elif version == "3" or version == "threat":
	print(classifier.threat_score(tweet_text))
elif version == "4" or version == "insult":
	print(classifier.insult_score(tweet_text))
elif version == "5" or version == "identity_hate":
	print(classifier.identity_hate_score(tweet_text))
else:
	print("Version parameter must be an integer from 0 to 5",
		"or one of ['toxic', 'severe_toxic', 'obscene', 'threat', 'insult', 'identity_hate']")