import RPi.GPIO as gpio
import time
import threading

def runForTime(pump, time):
    gpio.output(pump, 0)
    time.sleep(time)
    gpio.output(pump, 1)

PUMP1 = 10
PUMP2 = 9
PUMP3 = 11
PUMP4 = 8
gpio.setmode(gpio.BCM)
gpio.setwarnings(False)

gpio.setup(PUMP1, gpio.OUT)
gpio.output(PUMP1, 1)
gpio.setup(PUMP2, gpio.OUT)
gpio.output(PUMP2, 1)
gpio.setup(PUMP3, gpio.OUT)
gpio.output(PUMP3, 1)
gpio.setup(PUMP4, gpio.OUT)
gpio.output(PUMP4, 1)


# this will print 2 numbers at the same time for 10 seconds
threading.Thread(target=runForTime, args=[PUMP1, 10]  ).start()
threading.Thread(target=runForTime, args=[PUMP2, 10]  ).start()
threading.Thread(target=runForTime, args=[PUMP3, 10]  ).start()
threading.Thread(target=runForTime, args=[PUMP4, 10]  ).start()


def gpioRun(pinNumber, seconds, verbose = False):
    """Turns a gpio pin on for the given time
        Args:
            pinNumber (int): the BCM gpio pin
            seconds (float): the length of time to run it
    """
    gpio.output(pinNumber, 1)
    if verbose is True:
        print('{} on'.format(pinNumber))

    time.sleep(seconds)
    
    gpio.output(pinNumber, 0)
    if verbose is True:
        print('{} off'.format(pinNumber))
    return

def setAsOutput(*args):
    """Sets given pins as outputs
        Args:
            args (ints): BCM Pins
    """
    for pin in args:
        gpio.setup(pin, gpio.OUT)
    return

def setOutputValue(value, *args):
    """Sets output value for arbitrary number of pins
        Args:
            value (int): 0 or 1
            args (ints): BCM Pins to set 
    """
    for pin in args:
        if value == 1:
            gpio.output(pin, value)
        elif value == 0:
            gpio.output(pin, value)
        else:
            print('error in setOutputs')            
    return