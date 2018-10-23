from pump import *
from functions import *
import RPi.GPIO as gpio

def main():
    try:
        outputPins = [4, 17, 27, 22]
        setAsOutput(outputPins)
        setOutputValue(0, outputPins)
        
        # pumps is a dictionary <int>:<int>:
        # e.g. {0 : pin1, 1 : pin2 ... 5 : pin5}
        pumps = assignPumps(outputPins)

        while True:
            gpioRun(pumps[0], 2)
            gpioRun(pumps[1], 2)
            gpioRun(pumps[2], 2)
            gpioRun(pumps[3], 2)

    except KeyboardInterrupt:
        gpio.cleanup()
        
    return

if __name__ == '__main__':
    main()