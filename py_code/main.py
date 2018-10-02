from bluetooth.bluetooth import *
from pump.pump import *
from functions.functions import *
import RPi.GPIO as gpio

def main():
    try:
        outputPins = [4]
        setAsOutputs(outputPins)
        setOutputValues(0, outputPins)
        pumps = assignPumps(4)

        while True:
            input()
            gpioRun(pumps[0], 3)
    
    except KeyboardInterrupt:
        gpio.cleanup()
        
    return

if __name__ == '__main__':
    main()