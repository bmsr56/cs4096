import bluetooth
import RPi.gpio as gpio        #calling for header file which helps in using gpios of PI
from functions.functions import * as fn
LED=21

gpio.setmode(gpio.BCM)     #programming the gpio by BCM pin numbers. (like PIN40 as gpio21)
gpio.setwarnings(False)
gpio.setup(LED,gpio.OUT)  #initialize gpio21 (LED) as an output Pin
gpio.output(LED,0)

server_socket = bluetooth.BluetoothSocket( bluetooth.RFCOMM )

port = 1
server_socket.bind(("",port))
server_socket.listen(1)
print 'Waiting for connection...'
client_socket,address = server_socket.accept()
print "Accepted connection from ",address
while 1:

 data = client_socket.recv(1024)
 print "Received: %s" % data
 if (data == "0"):    #if '0' is sent from the Android App, turn OFF the LED
  print ("gpio 21 LOW, LED OFF")
  gpio.output(LED,0)
 if (data == "1"):    #if '1' is sent from the Android App, turn OFF the LED
  print ("gpio 21 HIGH, LED ON")
  gpio.output(LED,1)
 if (data == "q"):
  print ("Quit")
  break

client_socket.close()
server_socket.close()
