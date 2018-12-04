from pump import *
from functions import *
import time
import RPi.GPIO as gpio

pins = [4, 17, 27, 22]

try:
	setAsOutput(pins)
	setOutputValue(1, pins)
	pumps = assignPumps(pins)
	
	print(pumps)

	
		
#	gpio.output(4, 0)
#	time.sleep(1)
#	gpio.output(4, 1)
	time.sleep(1)
	gpioRun(pumps[0], 1)
	gpioRun(pumps[1], 1)
	gpioRun(pumps[2], 1)
	gpioRun(pumps[3], 1) 
except KeyboardInterrupt:
	gpio.cleanup()
finally:
	gpio.cleanup()
