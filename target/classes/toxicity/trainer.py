#!/usr/bin/env python
# coding: utf-8

# In[81]:


import numpy as np
import pandas as pd

from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.linear_model import LogisticRegression
from sklearn.model_selection import cross_val_score
from scipy.sparse import hstack

import pickle


# In[82]:


class_names = ['toxic', 'severe_toxic', 'obscene', 'threat', 'insult', 'identity_hate']

train = pd.read_csv('train.csv').fillna(' ')
test = pd.read_csv('test.csv').fillna(' ')

train_text = train['comment_text']
test_text = test['comment_text']
all_text = pd.concat([train_text, test_text])


# In[83]:


word_vectorizer = TfidfVectorizer(
    sublinear_tf=True,
    strip_accents='unicode',
    analyzer='word',
    token_pattern=r'\w{1,}',
    stop_words='english',
    ngram_range=(1, 1),
    max_features=10000)
word_vectorizer.fit(all_text)


char_vectorizer = TfidfVectorizer(
    sublinear_tf=True,
    strip_accents='unicode',
    analyzer='char',
    stop_words='english',
    ngram_range=(2, 6),
    max_features=50000)
char_vectorizer.fit(all_text)


# In[9]:


train_word_features = word_vectorizer.transform(train_text)
test_word_features = word_vectorizer.transform(test_text)

train_char_features = char_vectorizer.transform(train_text)
test_char_features = char_vectorizer.transform(test_text)

train_features = hstack([train_char_features, train_word_features])
test_features = hstack([test_char_features, test_word_features])


# In[10]:


scores = []
submission = pd.DataFrame.from_dict({'id': test['id']})
classifiers = []
for class_name in class_names:
    train_target = train[class_name]
    classifier = LogisticRegression(C=0.1, solver='sag')
    classifiers.append(classifier)
    cv_score = np.mean(cross_val_score(classifier, train_features, train_target, cv=3, scoring='roc_auc'))
    scores.append(cv_score)
    print('CV score for class {} is {}'.format(class_name, cv_score))

    classifier.fit(train_features, train_target)
    submission[class_name] = classifier.predict_proba(test_features)[:, 1]

print('Total CV score is {}'.format(np.mean(scores)))

submission.to_csv('submission.csv', index=False)


# In[103]:


def get_features_helper(text):
    word_features = word_vectorizer.transform(text)
    char_features = char_vectorizer.transform(text)
    return hstack([char_features,word_features])


# In[104]:


def get_features(string):
    return get_features_helper([string])


# In[107]:


def toxic_score(string):
    features = get_features(string)
    return classifiers[0].predict_proba(features)[:, 1][0]


# In[108]:


def severe_toxic_score(string):
    features = get_features(string)
    return classifiers[1].predict_proba(features)[:, 1][0]


# In[109]:


def obscene_score(string):
    features = get_features(string)
    return classifiers[2].predict_proba(features)[:, 1][0]


# In[110]:


def threat_score(string):
    features = get_features(string)
    return classifiers[3].predict_proba(features)[:, 1][0]


# In[111]:


def insult_score(string):
    features = get_features(string)
    return classifiers[4].predict_proba(features)[:, 1][0]


# In[112]:


def identity_hate_score(string):
    features = get_features(string)
    return classifiers[5].predict_proba(features)[:, 1][0]


# In[116]:


#obscene_score("wow")
features = get_features_helper(["wow"])
classifiers[0].predict_proba(features)[:, 1][0]


# In[16]:


# Dump classifiers
for i in range(0, 6):
    pkl_filename = "pickle_model" + str(i) + ".pkl"
    with open(pkl_filename, 'wb') as file:
        pickle.dump(classifiers[i], file)


# In[85]:


# Dump vectorizers
with open("word_vectorizer.pkl", 'wb') as file:
    pickle.dump(word_vectorizer, file)
with open("char_vectorizer.pkl", 'wb') as file:
    pickle.dump(char_vectorizer, file)


# In[17]:


pickle_models = []
for i in range(0, 6):
    pkl_filename = "pickle_model" + str(i) + ".pkl"
    with open(pkl_filename, 'rb') as file:
        pickle_models.append(pickle.load(file))


# In[18]:


for i in range(0, 6):
    score = pickle_models[i].score(train_features, train_target)
    print("Test score: {0:.2f} %".format(100 * score))
    prediction = pickle_models[i].predict(test_features)

