from functions import *
import RPi.GPIO as gpio
import pyrebase

def sh_show(message):
    print('Event: ', message["event"]) # put
    print('Path: ', message["path"]) # /-K7yGTTEp7O549EzTYtI
    print('Data: ', message["data"]) # {'title': 'Pyrebase', "body": "etc..."}

def sh_loadout(message):
    event = message["event"]
    path = message["path"]
    data = message["data"]
    if event == "put":
        print("STREAM DATA:", data)
        print("STREAM DATA TYPE:", type(data))
    return

def queue_handler(message):
    event = message["event"]
    path = message["path"]
    data = message["data"]
    if event == "put":
        print("STREAM DATA:", data)
        print("STREAM DATA TYPE:", type(data))
    return

def connectFB(email, password):
    print('FB Configuration initiated...\n---')
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
    print('Firebase instance obtained\n---')

    # Get a reference to the auth service
    print('Obtaining Auth reference...')    
    auth = firebase.auth()
    print('Auth reference obtained\n---')

    # Log the user in
    print('Logging {} in...'.format(email))
    user = auth.sign_in_with_email_and_password(email, password)
    print('{} logged in\n---'.format(email))

    # Get a reference to the database service
    print('Obtaining Firebase reference...')
    db = firebase.database()
    print('Firebase reference obtained\n---')
    
    return db, user
