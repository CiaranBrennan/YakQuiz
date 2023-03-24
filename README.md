# Quiz App

This is our implementation of a Quiz App, where the user can answer up to 50 questions each quiz from 24 different question categories, in both True/False and Multiple choice formats, at a difficulty level of their choice!

## User Profile and Leaderboard

If users wish to save their scores, they can register a profile to log in to the game with which will automatically track their scores!
These scores are stored on a web server and links to the leaderboard in the app. The leaderboard screen allows users to view the top scores out of all of the category and difficulty combinations! It is ordered by score primarily, and then by category as a tiebreaker.
If the user isn't logged in, their score isn't stored anywhere and they are informed that if they want to track their scores, they have to log in first.
The user will also have the option to share their score on any appropriate app they have installed, such as email, Messenger, WhatsApp etc.

## Quiz Selection and Gameplay

Before beginning the quiz, users can set the number of questions they wish to answer (between 1-50) and modify the type of quiz they wish to take, the difficulty of the questions and the answer format. Each of these settings can also be left as "Any" for a truly varied quiz containing questions from all possible selections from each setting!

When the user confirms their quiz settings, the quiz will start and present them with a screen containing both the question, and possible answers. The user will click their choice which, if correct, will turn green, and if incorrect will turn red, showing the correct answer in green. After a short delay, the next question will load. This repeats until the quiz is over, after which their score is saved to their profile and submitted to the leaderboard database.
