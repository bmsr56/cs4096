import RPi.GPIO as gpio
import time

def gpioRun(pinNumber, seconds):
    """Turns a gpio pin on for the given time
        Args:
            pinNumber (int): the BCM gpio pin
            seconds (float): the length of time to run it
    """
    gpio.output(pinNumber, 1)
    time.sleep(seconds)
    gpio.output(pinNumber, 0)
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