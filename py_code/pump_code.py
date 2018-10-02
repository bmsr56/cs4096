import RPi.GPIO as gpio
import time

PUMP = 21
gpio.setmode(gpio.BCM)
gpio.setwarnings(False)
gpio.setup(PUMP, gpio.OUT)
gpio.output(PUMP, 0)

try:
    while 1:
        inp = raw_input()
        if inp == 'on':
            print 'it\'s on'
            gpio.output(PUMP, 1)
	    time.sleep(2)
#    if inp == 'off':
            print 'it\'s off'
            gpio.output(PUMP, 0)
except KeyboardInterrupt:
    gpio.cleanup()

