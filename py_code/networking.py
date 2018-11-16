import RPi.GPIO as gpio
import pyrebase

config = {
    "apiKey": "AIzaSyDoofjCqY0kx9IPrjwY0ZD_RaXuqY4kQ5K",
    "authDomain": "drinkmaserplus-c13afirebaseaoo.com",
    "databaseURL": "https://drinkmasterplus-ec13a.firebaseio.com",
    "projectId": "drinkmasterplus-ec13a",
    "storageBucket": "drinkmasterplus-ec13a.appspot.com"
}

email = "DrinkMasterPlusPlus@gmail.com"
password = "thisisapassword"

firebase = pyrebase.initialize_app(config)
auth = firebase.auth()
user = auth.sigh_in_with_email_and_password(email, password)

db = firebase.database()
data = {
    "name": "Drunky Drinkerson"
}

queue = db.child("queue").get()
print(queue)

