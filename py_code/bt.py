import bluetooth
import RPi.GPIO as gpio        #calling for header file which helps in using gpios of PI
from functions import *

# Set GPIO settings here
gpio.setmode(gpio.BCM)     #programming the gpio by BCM pin numbers. (like PIN40 as gpio21)
gpio.setwarnings(False)

# Set outputs here

# Initialize outputs here

def connectBluetooth(pin):
  server_socket = bluetooth.BluetoothSocket( bluetooth.RFCOMM )
  port = 1
  server_socket.bind(("",port))
  server_socket.listen(1)
  print('Waiting for connection...')
  client_socket,address = server_socket.accept()
  print('Accepted connection from ', address)
  while 1:
    data = client_socket.recv(1024)
    print ("Received: %s" % data)
    if (data == "0"):    #if '0' is sent from the Android App, turn OFF the LED
      print ("gpio 21 LOW, LED OFF")
      gpio.output(pin, 0)
    if (data == "1"):    #if '1' is sent from the Android App, turn OFF the output
      print ("gpio 21 HIGH, output ON")
      gpio.output(pin, 1)
    if (data == "q"):
      print ("Quit")
      break

  client_socket.close()
  server_socket.close()
  return
