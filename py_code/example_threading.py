import threading
import time
import RPi.GPIO as gpio 

def loop1_10():
    for i in range(1, 11):
        time.sleep(1)
        print(i)

def runForTime(pump, time1):
    gpio.output(pump, 0)
    time.sleep(time1)
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
