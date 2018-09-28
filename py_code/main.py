from bluetooth.bluetooth_gpio import * as bt
from motor.motor_gpio import * as pump
from functions.functions import * as fn

def main():
    try:
    outputPins = [4]
    fn.setAsOutputs(outputPins)
    fn.setOutputValues(0, outputPins)
    pump = pump.assignPumps(4)
       while True:
           input()
           fn.gpioRun(pump[0], 3)
    return

if __name__ == '__main__':
    main()