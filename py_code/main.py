from pump import *
from functions import *
from networking import *
import RPi.GPIO as gpio

def processQueue():
    pass
    # handle garbage
    # LOOP: check length of drinkQueue and process one at a time
        # extract drink string
        # call parser on the string
        # process each ingredient with calls to threaded pumprun fns

    # if len(drinkQueue) > 0:
    #     if drinkFinished is True:


    # return  

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
    pumpPins = [10, 9, 11, 8]
    setAsOutput(pumpPins)
    setOutputValue(1, pumpPins)
    pump1 = 10
    pump2 = 9
    pump3 = 11
    pump4 = 8

    try:
        db, user = connectFB("DrinkMasterPlusPlus@gmail.com", "thisisapassword")
        # loadout = db.child("loadout").get()
        # for item in loadout.each():
        #     print(item.key())
        #     print(item.val())

        stream = db.child("queue").stream(queue_handler)

        print('STREAM TYPE', type(stream))
        print('DRINKQ', drinkQueue)
        # the schema of the database was changed so this had to change
        # db.child("loadout").child("1").set({"amountLeft": 9000})
        # db.child("loadout").child("1").set({"bottleName": "poojuice"})
        # db.child("loadout").child("2").set({"amountLeft": 900000000000})
        # db.child("loadout").child("2").set({"bottleName": "poojuice2"})
	
    finally:
        gpio.cleanup()
        # stream.close()
    return


if __name__ == '__main__':
    main()
