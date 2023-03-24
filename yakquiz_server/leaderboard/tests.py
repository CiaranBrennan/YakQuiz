from django.test import TestCase
from leaderboard.models import User, Score
from django.contrib.auth import authenticate

def login(user, password):
    try:
        user = User.objects.get(username = user, password = password)
        return 1
    except:
        return 0

def register(user, password):
    try:
        User.objects.create_user(user, "", password)
        return 1
    except:
        return 0

def addScore(user, category, difficulty, score):
    username = User.objects.get(username = user)
    try:
        previousBest = Score.objects.get(difficulty = difficulty, category = category, user = username)
        previousBest.score += int(score)
        return previousBest.score
    except:
        score = Score(score = score, difficulty = difficulty, category = category, user = username)
        return score.score

def getScores():
    scores = []
    scoreQuery = Score.objects.order_by("-score", "category")
    for score in scoreQuery:
        scores.append({"category":score.category, "difficulty":score.difficulty, "user":score.user.username, "score":score.score})
    return scores

class LoginTestCase(TestCase):
    def setUp(self):
        User.objects.create(username = "testname", password = "password")

    def test_successful_login(self):
        loginValue = login("testname", "password")
        self.assertEqual(loginValue, 1)

    def test_incorrect_username(self):
        loginValue = login("test", "password")
        self.assertEqual(loginValue, 0)

    def test_incorrect_password(self):
        loginValue = login("testname", "pass")
        self.assertEqual(loginValue, 0)

class RegisterTestCase(TestCase):
    def setUp(self):
        User.objects.create(username = "testname", password = "password")

    def test_successful_register(self):
        registerValue = register("test", "password")
        self.assertEqual(registerValue, 1)

    def test_account_exists(self):
        registerValue = register("testname", "password")
        self.assertEqual(registerValue, 0)

class ScoreTestCase(TestCase):
    def setUp(self):
        user = User(username = "testname", password = "password")
        user.save()
        Score.objects.create(category = "Animals", difficulty = "Easy", user = user, score = "6")

    def test_new_score(self):
        scoreValue = addScore("testname", "Books", "Normal", "7")
        self.assertEqual(scoreValue, "7")

    def test_improve_score(self):
        scoreValue = addScore("testname", "Animals", "Easy", "10")
        self.assertEqual(scoreValue, 16)

class GetScoreTestCase(TestCase):
    def setUp(self):
        user = User(username = "testname", password = "password")
        user.save()
        Score.objects.create(category = "Animals", difficulty = "Easy", user = user, score = "6")
        Score.objects.create(category = "Books", difficulty = "Normal", user = user, score = "10")

    def test_return_scores(self):
        exampleScores = [
        {
            "category":"Books",
            "difficulty":"Normal",
            "user":"testname",
            "score":10
        },
        {
            "category":"Animals",
            "difficulty":"Easy",
            "user":"testname",
            "score":6
        }]
        scores = getScores()
        self.assertEqual(scores, exampleScores)
