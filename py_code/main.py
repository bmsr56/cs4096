from bt import *
from pump import *
from functions import *
import RPi.GPIO as gpio, pyrebase
import pyrebase

def main():
    try:
        config = {
            "apiKey": "AIzaSyDoofjCqY0kx9IPrjwY0ZD_RaXuqY4kQ5k",
            "authDomain": "drinkmasterplus-ec13a.firebaseapp.com",
            "databaseURL": "https://drinkmasterplus-ec13a.firebaseio.com/",
            "storageBucket": "gs://drinkmasterplus-ec13a.appspot.com"
            }

        firebase = pyrebase.initialize_app(config)

        # Get a reference to the auth service
        auth = firebase.auth()

        email = 'drinkmasterplusplus@gmail.com'
        password = 'thisisapassword'
        
        # Log the user in
        user = auth.sign_in_with_email_and_password(email, password)

        # Get a reference to the database service
        db = firebase.database()

        # data to save
        data = {
            "name": "Mortimer 'Morty' Smith"
        }

        # Pass the user's idToken to the push method
        results = db.child("users").push(data, user['idToken'])

        outputPins = [4, 17, 27, 22, 5, 6]
        setAsOutput(outputPins)
        setOutputValue(0, outputPins)
        
        # pumps is a dictionary <int>:<int>:
        # e.g. {0 : pin1, 1 : pin2 ... 5 : pin5}
        pumps = assignPumps(outputPins)

        while True:
            input()
            gpioRun(pumps[0], 3)
    
    except KeyboardInterrupt:
        gpio.cleanup()
        
    return

if __name__ == '__main__':
    main()