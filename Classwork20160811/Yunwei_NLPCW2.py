#question 1
import nltk
import random
import time
#get the source
posts = nltk.corpus.nps_chat.xml_posts()[:2000]
# feature extraction
def dialog_act_features(post):
    features={}
    for word in nltk.word_tokenize(post):
        features['contains({})'.format(word.lower())] = True
    return features    

featureset = [(dialog_act_features(post.text),post.get('class')) for post in posts]
random.shuffle(featureset)# shuffle
# divide into train set and test set
train_set,test_set = featureset[:1000],featureset[1000:]
#train the 3 classifiers respectively
decisionTree = nltk.DecisionTreeClassifier.train(train_set)
naiveBayes = nltk.NaiveBayesClassifier.train(train_set)
maximumEntropy = nltk.MaxentClassifier.train(train_set)
#get the start time
decisionTreeStartTime = time.time()
# if the accuracy can be output, then the classification has been done
print('accuracy of decision tree is:{}'.format(nltk.classify.accuracy(decisionTree, test_set)))
#get the end time
decisionTreeEndTime = time.time()
print('total classification of decision tree time:{}'.format(decisionTreeEndTime-decisionTreeStartTime))

naiveBayesStartTime = time.time()
print('accuracy of naive Bayes is:{}'.format(nltk.classify.accuracy(naiveBayes, test_set)))
naiveBayesEndTime = time.time()
print('total classification of naive Bayes time:{}'.format(naiveBayesEndTime-naiveBayesStartTime))

maximumEntropyStartTime = time.time()
print('accuracy of maximum Entropy is:{}'.format(nltk.classify.accuracy(maximumEntropy, test_set)))
maximumEntropyEndTime = time.time()
print('total classification of maximum Entropy time:{}'.format(maximumEntropyEndTime-maximumEntropyStartTime))

# ==> Training (100 iterations)
# 
#       Iteration    Log Likelihood    Accuracy
#       ---------------------------------------
#              1          -2.70805        0.054
#              2          -1.15336        0.902
#              3          -0.80451        0.937
#              4          -0.62728        0.950
#              5          -0.51787        0.965
#              6          -0.44330        0.973
#              7          -0.38906        0.976
#              8          -0.34774        0.983
#              9          -0.31514        0.984
#             10          -0.28873        0.984
#             11          -0.26687        0.985
#             12          -0.24844        0.985
#             13          -0.23268        0.985
#             14          -0.21903        0.986
#             15          -0.20708        0.987
#             16          -0.19652        0.988
#             17          -0.18711        0.989
#             18          -0.17866        0.989
#             19          -0.17103        0.989
#             20          -0.16410        0.989
#             21          -0.15777        0.989
#             22          -0.15197        0.989
#             23          -0.14662        0.991
#             24          -0.14168        0.991
#             25          -0.13709        0.991
#             26          -0.13283        0.991
#             27          -0.12884        0.992
#             28          -0.12512        0.992
#             29          -0.12162        0.992
#             30          -0.11833        0.993
#             31          -0.11523        0.993
#             32          -0.11231        0.993
#             33          -0.10955        0.993
#             34          -0.10693        0.994
#             35          -0.10444        0.994
#             36          -0.10208        0.996
#             37          -0.09984        0.996
#             38          -0.09770        0.996
#             39          -0.09565        0.996
#             40          -0.09370        0.996
#             41          -0.09184        0.996
#             42          -0.09006        0.996
#             43          -0.08835        0.996
#             44          -0.08671        0.996
#             45          -0.08513        0.996
#             46          -0.08362        0.996
#             47          -0.08217        0.996
#             48          -0.08077        0.996
#             49          -0.07942        0.996
#             50          -0.07812        0.996
#             51          -0.07687        0.996
#             52          -0.07566        0.996
#             53          -0.07449        0.996
#             54          -0.07337        0.996
#             55          -0.07227        0.996
#             56          -0.07122        0.996
#             57          -0.07020        0.996
#             58          -0.06921        0.996
#             59          -0.06825        0.996
#             60          -0.06732        0.996
#             61          -0.06642        0.996
#             62          -0.06554        0.996
#             63          -0.06469        0.996
#             64          -0.06386        0.996
#             65          -0.06306        0.996
#             66          -0.06228        0.996
#             67          -0.06152        0.996
#             68          -0.06078        0.996
#             69          -0.06006        0.996
#             70          -0.05936        0.996
#             71          -0.05868        0.997
#             72          -0.05802        0.997
#             73          -0.05737        0.997
#             74          -0.05674        0.997
#             75          -0.05612        0.997
#             76          -0.05552        0.997
#             77          -0.05493        0.997
#             78          -0.05436        0.997
#             79          -0.05380        0.997
#             80          -0.05325        0.997
#             81          -0.05272        0.997
#             82          -0.05219        0.997
#             83          -0.05168        0.997
#             84          -0.05118        0.997
#             85          -0.05069        0.997
#             86          -0.05022        0.997
#             87          -0.04975        0.997
#             88          -0.04929        0.997
#             89          -0.04884        0.997
#             90          -0.04840        0.997
#             91          -0.04797        0.997
#             92          -0.04755        0.997
#             93          -0.04713        0.997
#             94          -0.04673        0.997
#             95          -0.04633        0.997
#             96          -0.04594        0.997
#             97          -0.04556        0.997
#             98          -0.04518        0.997
#             99          -0.04482        0.997
#          Final          -0.04445        0.997 
# accuracy of decision tree is:0.718
# total classification of decision tree time:0.0338430404663
# accuracy of naive Bayes is:0.468
# total classification of naive Bayes time:0.181246995926
# accuracy of maximum Entropy is:0.648
# total classification of maximum Entropy time:0.151483058929

#Explanation: the first part of 3 columns is the output during training the decision tree
#Explanation: Once I get the accuracy, the test test has been classified, also, before classifying
#get the time when classifying starts and then get the end time when classifying ends. The difference
#is the testing time
#Compare: according to the result output, the accuracy of decision tree is greatest, then max entropy, 
#and the smallest is naive bayes




#Question 2
def sentence_features(sentence):
    return {'first letter capitalized':sentence[0].isupper(),
            'suffix1':sentence[-1:],
            'suffix2':sentence[-2:],
            'suffix3':sentence[-3:]
            }

def punct_features(post):
    tokens = post.split()
    return {'first-word-capitalized': tokens[0][0].isupper(),
            'more than two word':len(tokens)>2,
            'first word is a char':len(tokens[0]==1)}


postsA = nltk.corpus.nps_chat.xml_posts()[:2000]

featuresetA = [(punct_features(post.text),post.get('class')) for post in postsA]

featuresetB = [(sentence_features(post.text),post.get('class')) for post in postsA]

trainSetA, testSetA = featuresetA[:1000], featuresetA[1000:]
trainSetB, testSetB = featuresetB[:1000], featuresetB[1000:]

classifierA = nltk.NaiveBayesClassifier.train(trainSetA)
classifierB = nltk.NaiveBayesClassifier.train(trainSetB)

print('accuracy of classifierA is {}'.format(nltk.classify.accuracy(classifierA, testSetA)))
print('accuracy of classifierB is {}'.format(nltk.classify.accuracy(classifierB, testSetB)))

# accuracy of classifierA is 0.402
# accuracy of classifierB is 0.488
# compare, when taking sentences as punctuations, the naive Bayes has a lower accuracy then that
# of question 1, but the accuracy is higher when sentences are considered as a whole
