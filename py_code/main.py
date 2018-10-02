from bluetooth.bluetooth import *
from pump.pump import *
from functions.functions import *
import RPi.GPIO as gpio

def main():
    try:
        outputPins = [4]
        fn.setAsOutputs(outputPins)
        fn.setOutputValues(0, outputPins)
        pumps = pump.assignPumps(4)

        while True:
            input()
            fn.gpioRun(pumps[0], 3)
    
    except KeyboardInterrupt:
        gpio.cleanup()
        
    return

if __name__ == '__main__':
    main()