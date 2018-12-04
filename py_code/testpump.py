from pump import *
from functions import *
from networking import *
import RPi.GPIO as gpio
import threading

def main():
    try:
        db, user = connectFB("DrinkMasterPlusPlus@gmail.com", "thisisapassword")
        # loadout = db.child("loadout").get()
        # for item in loadout.each():
        #     print(item.key())
        #     print(item.val())
    
        # stream = db.child("queue").stream(queue_handler)

        # print('STREAM TYPE', type(stream))

        # while 1:
        #     if currentPath is not None:
        #         db.child("queue").child(currentPath).remove()
        #         currentPath = None
        #     time.sleep(1)

        # the schema of the database was changed so this had to change
        db.child("queue").push("1_50.0+3_50.0")
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