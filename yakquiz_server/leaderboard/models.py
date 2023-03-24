from django.db import models
from django.contrib.auth.models import User

# Create your models here.
class Score(models.Model):
    score = models.IntegerField("Score")
    difficulty = models.CharField("Quiz Difficulty", max_length=10)
    category = models.CharField("Quiz Category", max_length=120)
    user = models.ForeignKey(User, on_delete = models.CASCADE)
