from pump import *
from functions import *
from networking import *
import RPi.GPIO as gpio
import threading

def main():
	try:
		db, user = connectFB("DrinkMasterPlusPlus@gmail.com", "thisisapassword")
		db.child("queue").push("1_50.0+3_50.0")
	except KeyboardInterrupt:
		# stream.close()
		pass
	finally:
		gpio.cleanup()
	return


if __name__ == '__main__':
    main()