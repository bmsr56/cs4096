from pump import *
from functions import *
from networking import *
import RPi.GPIO as gpio

def main():
    try:
        db, user = connectFB("DrinkMasterPlusPlus@gmail.com", "thisisapassword")
        loadout = db.child("loadout").get()
        for item in loadout.each():
            print(item.key())
            print(item.val())
    
    finally:
        gpio.cleanup()
    return

if __name__ == '__main__':
    main()