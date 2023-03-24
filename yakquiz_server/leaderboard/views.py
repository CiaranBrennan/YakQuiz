from django.shortcuts import render
from django.http import HttpResponse, JsonResponse
from django.core import serializers
from django.views.decorators.csrf import csrf_exempt
from django.contrib.auth import authenticate
from django.contrib.auth.models import User
from leaderboard.models import Score
import json, ast

# Create your views here.
def ConnectionTest(request):
    return HttpResponse (1)

@csrf_exempt
def HandleRegisterRequest(request):
    #Retreive the username and password sent by the app
    loginObj = json.loads(request.body)
    username = loginObj["username"]
    password = loginObj["password"]
    #An error means that a user with that name already exists
    try:
        User.objects.create_user(username, "", password)
        return HttpResponse (1)
    except:
        return HttpResponse (0)

@csrf_exempt
def HandleLoginRequest(request):
    loginObj = json.loads(request.body)
    username = loginObj["username"]
    password = loginObj["password"]
    #Check whether the user's details are in the database
    user = authenticate(request, username = username, password = password)
    #If they are, a success code is sent. Otherwise and error code is
    if user is not None:
        return HttpResponse(1)
    else:
        return HttpResponse(0)

@csrf_exempt
def GetScoresRequest(request):
    scores = []
    #Retreive all scores from the database sorted by score descending,
        #followed by category ascending
    scoreQuery = Score.objects.order_by("-score", "category")
    #Format the scores as a JSON array and send them to the app
    for score in scoreQuery:
        scores.append({"category":score.category, "difficulty":score.difficulty, "user":score.user.username, "score":score.score})
    return HttpResponse(json.dumps(scores), content_type="application/json")

@csrf_exempt
def GetUsersScoresRequest(request):
    params = json.loads(request.body)
    username = params["username"]
    scores = []
    #Gets all the scores of a specific user
    scoreQuery = Score.objects.filter(user__username = username).order_by("category", "difficulty")
    for score in scoreQuery:
        scores.append({"category":score.category, "difficulty":score.difficulty, "score":score.score})
    return HttpResponse(json.dumps(scores), content_type="application/json")

@csrf_exempt
def GetQuizScoresRequest(request):
    params = json.loads(request.body)
    category = params["category"]
    difficulty = params["difficulty"]
    scores = []
    #Gets all the scores for a specific category and difficulty
    scoreQuery = Score.objects.filter(category__name = category, difficulty = difficulty).order_by("score")
    for score in scoreQuery:
        scores.append({"user":score.user.username, "score":score.score})
    return HttpResponse(json.dumps(scores), content_type="application/json")

@csrf_exempt
def AddScoreRequest(request):
    #Retreive data from request and convert it to json
    params = json.loads(request.body)
    score = params["score"]
    difficulty = params["difficulty"]
    category = params["category"]
    username = User.objects.get(username = params["username"])
    #Check whether user has previously completed this quiz
    try:
        #If they have, add the user's new score to the currently stored one
        previousBest = Score.objects.get(difficulty = difficulty, category = category, user = username)
        previousBest.score += score
        previousBest.save()
        return HttpResponse(1)
    except:
        #If not, the score is added as a new record
        score = Score(score = score, difficulty = difficulty, category = category, user = username)
        score.save()
        return HttpResponse(2)
