import numpy as np
import pandas as pd

from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.linear_model import LogisticRegression
from sklearn.model_selection import cross_val_score
from scipy.sparse import hstack

import pickle

import Pyro4
import os

# Import classifiers from Pickle files
classifiers = []
for i in range(0, 6):
	pkl_filename = "pickle_model" + str(i) + ".pkl"
	with open(pkl_filename, 'rb') as file:
		classifiers.append(pickle.load(file))

# Import vectorizers from Pickle files
with open("word_vectorizer.pkl", 'rb') as file:
	word_vectorizer = pickle.load(file)
with open("char_vectorizer.pkl", 'rb') as file:
	char_vectorizer = pickle.load(file)

@Pyro4.expose
class ToxicityClassifier(object):
	"""Vectorizes a list of strings.

	Args:
		text (list): a list of strings
	Returns:
		csr_matrix: a list of vectors and probabilities?
	"""
	def get_features_helper(self, text):
		word_features = word_vectorizer.transform(text)
		char_features = char_vectorizer.transform(text)
		return hstack([char_features,word_features])

	# Same as above, but for a single string
	def get_features(self, string):
		return self.get_features_helper([string])

	# Returns the probability that a given string is considered 'toxic'.
	def toxic_score(self, string):
		features = self.get_features(string)
		return classifiers[0].predict_proba(features)[:, 1][0]

	def severe_toxic_score(self, string):
		features = self.get_features(string)
		return classifiers[1].predict_proba(features)[:, 1][0]

	def obscene_score(self, string):
		features = self.get_features(string)
		return classifiers[2].predict_proba(features)[:, 1][0]

	def threat_score(self, string):
		features = self.get_features(string)
		return classifiers[3].predict_proba(features)[:, 1][0]

	def insult_score(self, string):
		features = self.get_features(string)
		return classifiers[4].predict_proba(features)[:, 1][0]

	def identity_hate_score(self, string):
		features = self.get_features(string)
		return classifiers[5].predict_proba(features)[:, 1][0]

# Start Pyro daemon
daemon = Pyro4.Daemon(host = "127.0.0.1")
uri = daemon.register(ToxicityClassifier)

print("The object's uri is:", uri)
#os.environ['TOXIC_SERVER_URI'] = str(uri)

with open("TOXIC_SERVER_URI","w") as uri_file:
	uri_file.write(str(uri))
daemon.requestLoop()

"""Vectorizes a list of strings.

Args:
	text (list): a list of strings
Returns:
	csr_matrix: a list of vectors and probabilities?
"""
def get_features_helper(text):
	word_features = word_vectorizer.transform(text)
	char_features = char_vectorizer.transform(text)
	return hstack([char_features,word_features])

# Same as above, but for a single string
def get_features(string):
	return get_features_helper([string])

"""
def test_models():
	class_names = ['toxic', 'severe_toxic', 'obscene', 'threat', 'insult', 'identity_hate']

	train = pd.read_csv('train.csv').fillna(' ')
	test = pd.read_csv('test.csv').fillna(' ')

	train_text = train['comment_text']
	test_text = test['comment_text']
	all_text = pd.concat([train_text, test_text])
	
	train_word_features = word_vectorizer.transform(train_text)
	test_word_features = word_vectorizer.transform(test_text)

	train_char_features = char_vectorizer.transform(train_text)
	test_char_features = char_vectorizer.transform(test_text)

	train_features = hstack([train_char_features, train_word_features])
	test_features = hstack([test_char_features, test_word_features])
	
	for i in range(0, 5):
		score = classifiers[i].score(train_features, train_target)
		print("Test score: {0:.2f} %".format(100 * score))
		prediction = classifiers[i].predict(test_features)
"""