from pump import *
from functions import *
from networking import *
import RPi.GPIO as gpio
import threading

drinkProcessedFlag = False
pumpToPin = {
    '1': 10, 
    '2': 9, 
    '3': 11, 
    '4': 8 
}

def mlToSeconds(ml):
    ml = float(ml)
    rate = 44 / 30
    return (ml / rate)

def processQueueItem():
    """Items stored in the drinkQueue consist of specially formatted strings
    """
    # handle garbage that might be in there at first

    # call parser on the string
    pumpNumbers, amounts = parser(drinkQueue[0]) # this is the first item in the drink queue

    # process each ingredient with calls to threaded pumprun fns
    if len(pumpNumbers) == len(amounts): # this should always be true... just a check
        for pumpNumber, amount in zip(pumpNumbers, amounts):
            # run pumps with threading
            threading.Thread(target=gpioRun, args=[
                pumpToPin[pumpNumber], mlToSeconds(amount), True
                ]).start()
    else:
        print('fail in processQueueItem')
    
    # remove the just processed item from the queue
    # db.child
    # set flag to True
    
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

def main():
    
    
    setAsOutput([10, 9, 11, 8])
    setOutputValue(1, [10, 9, 11, 8])

    try:
        db, user = connectFB("DrinkMasterPlusPlus@gmail.com", "thisisapassword")
        # loadout = db.child("loadout").get()
        # for item in loadout.each():
        #     print(item.key())
        #     print(item.val())
    
        stream = db.child("queue").stream(queue_handler)

        # print('STREAM TYPE', type(stream))

        # while len(drinkQueue) > 0 and drinkProcessedFlag is False:
        #     processQueueItem()

        # the schema of the database was changed so this had to change
        # db.child("loadout").child("1").set({"amountLeft": 9000})
        # db.child("loadout").child("1").set({"bottleName": "poojuice"})
        # db.child("loadout").child("2").set({"amountLeft": 900000000000})
        # db.child("loadout").child("2").set({"bottleName": "poojuice2"})
    except KeyboardInterrupt:
        stream.close()
    finally:
        gpio.cleanup()
    return


if __name__ == '__main__':
    main()
