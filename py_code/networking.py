from functions import *
import RPi.GPIO as gpio
import pyrebase

def connectFB(email, password):
    config = {
        "apiKey": "AIzaSyDoofjCqY0kx9IPrjwY0ZD_RaXuqY4kQ5k",
        "authDomain": "drinkmasterplus-ec13a.firebaseapp.com",
        "databaseURL": "https://drinkmasterplus-ec13a.firebaseio.com",
        "projectId": "drinkmasterplus-ec13a",
        "storageBucket": "drinkmasterplus-ec13a.appspot.com"
        # messagingSenderId: "1027801399754"
    }

    # Get an instance
    print('Obtaining Firebase instance...')
    firebase = pyrebase.initialize_app(config)
    print('Firebase instance obtained')

    # Get a reference to the auth service
    print('Obtaining Auth reference...')    
    auth = firebase.auth()
    print('Auth reference obtained')

    # Log the user in
    print('Logging {} in...'.format(email))
    user = auth.sign_in_with_email_and_password(email, password)
    print('{} logged in'.format(email))

    # Get a reference to the database service
    print('Obtaining Firebase reference...')
    db = firebase.database()
    print('Firebase reference obtained')
    
    return db, user

