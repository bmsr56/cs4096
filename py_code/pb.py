import pyrebase



def getDbInstance():
    config = {
        "apiKey": "AIzaSyDoofjCqY0kx9IPrjwY0ZD_RaXuqY4kQ5k",
        "authDomain": "drinkmasterplus-ec13a.firebaseapp.com",
        "databaseURL": "https://drinkmasterplus-ec13a.firebaseio.com/",
        "storageBucket": "gs://drinkmasterplus-ec13a.appspot.com"
        }

    firebase = pyrebase.initialize_app(config)

    return firebase.database()

