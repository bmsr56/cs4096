from pump import *
from functions import *
from networking import *
import RPi.GPIO as gpio

def main():
    try:
        db, user = connectFB("DrinkMasterPlusPlus@gmail.com", "thisisapassword")
        # loadout = db.child("loadout").get()
        # for item in loadout.each():
        #     print(item.key())
        #     print(item.val())

        loadout_stream = db.child("loadout").stream(streamTester)

        db.child("loadout").child("1").update({"poojuice": "9000"})


    finally:
        gpio.cleanup()
        loadout_stream.close()
    return

if __name__ == '__main__':
    main()
