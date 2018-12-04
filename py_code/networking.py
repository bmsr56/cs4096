from functions import *
import RPi.GPIO as gpio
import pyrebase

# drinkProcessedFlag = False
currentPath = None

pumpToPin = {
    '1': 10, 
    '2': 9, 
    '3': 11, 
    '4': 8 
}

def queue_handler(message):
    event = message["event"]
    path = message["path"]
    data = message["data"]
    currentPath = path
    if event == "put" and path != "/":
        print("HANDLER PUT TRIGGERED")
        print("PATH", path)
        print("DATA", data)

        drinkString = data[0][path] # data is a list of dicts
        print('This should be the drink string: ', drinkString)

        # call parser on the string
        pumpNumbers, amounts = parser(drinkString) # this is the first item in the drink queue

        # process each ingredient with calls to threaded pumprun fns
        threadList = []
        if len(pumpNumbers) == len(amounts): # this should always be true... just a check
            for pumpNumber, amount in zip(pumpNumbers, amounts):
                # run pumps with threading
                threadList.append(
                    threading.Thread(target=gpioRun, args=[
                        pumpToPin[pumpNumber], mlToSeconds(amount), True
                        ])
                    )
        for t in threadList:
            t.start()
        for t in threadList:
            t.join()        
        # wait for these to end, this is important and might not work

        else:
            print('fail in processQueueItem in queue handler')
            return

        return

def mlToSeconds(ml):
    ml = float(ml)
    rate = 44 / 30
    return (ml / rate)

def processQueueItem(event, path, data):
    """Items stored in the drinkQueue consist of specially formatted strings
    """
    # handle garbage that might be in there at first
    drinkString = data[0][path] # data is a list of dicts
    print('This should be the drink string: ', drinkString)
    # call parser on the string
    pumpNumbers, amounts = parser(drinkString) # this is the first item in the drink queue

    # process each ingredient with calls to threaded pumprun fns
    threadList = []
    if len(pumpNumbers) == len(amounts): # this should always be true... just a check
        for pumpNumber, amount in zip(pumpNumbers, amounts):
            # run pumps with threading
            threadList.append(
                threading.Thread(target=gpioRun, args=[
                    pumpToPin[pumpNumber], mlToSeconds(amount), True
                    ])
            )
    for t in threadList:
        t.start()
    for t in threadList:
        t.join()        
    else:
        print('fail in processQueueItem')
        return

    # remove the item from firebase db
    # set flag to True
    # drinkProcessedFlag = True
    return  

def parser(msg):
    pumpNumbers = []
    amounts = []
    msg = msg.split("+")
    for x in msg:
        wigit = x.split("_")
        pumpNumbers.append(wigit[0])
        amounts.append(wigit[1])
    return pumpNumbers, amounts


def show_handler(message):
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
