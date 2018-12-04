import RPi.GPIO as gpio

gpio.setmode(gpio.BCM)
gpio.setwarnings(False)

PUMP1 = 8
PUMP2 = 9
PUMP3 = 10
PUMP4 = 11

gpio.setup(PUMP1, gpio.OUT)
gpio.output(PUMP1, 1)
gpio.setup(PUMP2, gpio.OUT)
gpio.output(PUMP2, 1)
gpio.setup(PUMP3, gpio.OUT)
gpio.output(PUMP3, 1)
gpio.setup(PUMP4, gpio.OUT)
gpio.output(PUMP4, 1)
