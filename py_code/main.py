from bt import *
from pump import *
from functions import *
from networking import *
import RPi.GPIO as gpio

def main():
    try:
        db, user = connectFB("DrinkMasterPlusPlus@gmail.com", "thisisapassword")
        db.child
    
    finally:
        gpio.cleanup()
    return

if __name__ == '__main__':
    main()