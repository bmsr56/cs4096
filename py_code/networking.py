
from functions import *
import RPi.GPIO as gpio
import pyrebase
# from firebase_admin import credentials
# from firebase_admin import db


# cred = credentials.Certificate('drinkmasterplus-ec13a-firebase-adminsdk-0ek53-cd1c117f9d.json')
# default_app = firebase_admin.initialize_app(cred, {
#     'databaseURL': 'https://drinkmasterplus-ec13a.firebaseio.com'
#     })

# https://www.raspberrypi.org/documentation/configuration/security.md

# Make Pi an access point
# Manage security rules
# Other network related functions

# Authenticate with Firebase DB


config = {
    "apiKey": "AIzaSyDoofjCqY0kx9IPrjwY0ZD_RaXuqY4kQ5k",
    "authDomain": "drinkmasterplus-ec13a.firebaseapp.com",
    "databaseURL": "https://drinkmasterplus-ec13a.firebaseio.com",
    "projectId": "drinkmasterplus-ec13a",
    "storageBucket": "drinkmasterplus-ec13a.appspot.com"
    # messagingSenderId: "1027801399754"
}

# User credentials
email = "DrinkMasterPlusPlus@gmail.com"
password = "thisisapassword"

# Get an instance
firebase = pyrebase.initialize_app(config)
print('got instance')
# Get a reference to the auth service
auth = firebase.auth()
print('auth ref')

# Log the user in
user = auth.sign_in_with_email_and_password(email, password)
print('authd')

# Get a reference to the database service
db = firebase.database()
print('db ref')

# data to save
data = {
    "name": "Drinky drinkerson"
}

# Pass the user's idToken to the push method
results = db.child("accounts").get()
print(results)
print('got accounts')

