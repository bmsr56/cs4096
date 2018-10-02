from bluetooth.bluetooth import * as bt
from pump.pump import * as pump
from functions.functions import * as fn
import RPi.gpio as gpio

def main():
    try:
    outputPins = [4]
    fn.setAsOutputs(outputPins)
    fn.setOutputValues(0, outputPins)
    pump = pump.assignPumps(4)

    while True:
        input()
        fn.gpioRun(pump[0], 3)
    
    except KeyboardInterrupt:
        gpio.cleanup()
        
    return

if __name__ == '__main__':
    main()