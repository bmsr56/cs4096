from pump import *
from functions import *
from networking import *
import RPi.GPIO as gpio

def parser(msg):
    ingrident = []
    location =[]
    msg = msg.split("+")
    for x in msg:
        wigit = x.split("_")
        ingrident.append(wigit[0])
        location.append(wigit[1])
    return ingrident,location

def main():
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
        stream.close()
    return


if __name__ == '__main__':
    main()
