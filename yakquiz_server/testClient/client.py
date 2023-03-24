import requests
import json
import ast

def main():
    site = "http://localhost:8000/"
    while 1:
        print("What do you want to test:\n1 - add score\n2 - view all scores\n3 - view ciaran's scores\n4 - view scores for the easy Animal quiz")
        choice = input()
        if choice == "1":
            body = {
            "username":"ciaran",
            "category":"Animals",
            "difficulty":"easy",
            "score":"66"
            }
            requests.get(site + "addscore/", json = body)
        elif choice == "2":
            scoreString = requests.get(site + "getscores/")
            scores = json.loads(scoreString.text)
            for score in scores:
                print("Category:", score["category"], "\nDifficulty:", score["difficulty"], "\nScore:", score["score"], "\nScored by:", score["user"])
        elif choice == "3":
            scoreString = requests.get(site + "userscores/", json = {"username":"ciaran"})
            scores = json.loads(scoreString.text)
            for score in scores:
                print("Category:", score["category"], "\nDifficulty:", score["difficulty"], "\nHigh score:", score["score"])
        elif choice == "4":
            scoreString = requests.get(site + "quizscores/", json = {"category":"Animals", "difficulty":"easy"})
            scores = json.loads(scoreString.text)
            print("Scores for the easy Animal quiz:")
            for score in scores:
                print("User:", score["user"], "\nHigh score:", score["score"])
if __name__ == '__main__':
    main()
