import RPi.GPIO as gpio
import time

# gpio general settings
gpio.setmode(gpio.BCM)
gpio.setwarnings(False)

def assignPumps(*args):
    """Assign the pumps to gpio pins
        Args:
            args (ints): 6 BCM pin numbers in order of how you want them assigned to the pumps.
        Returns:
            pump (dict): Maps a zero index of pumps to the given pins args.
    """
    pump = {}
    if len(args) <= 6:
        pumpNum = 0
        for pin in args:
            pump[pumpNum] = pin
            pumpNum += 1
    else:
        print('error in setPumpPins')
    return pump

