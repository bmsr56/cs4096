import bluetooth
import RPi.GPIO as GPIO        #calling for header file which helps in using GPIOs of PI


# location of database on pi
database_location = ''

LED=21

GPIO.setmode(GPIO.BCM)     #programming the GPIO by BCM pin numbers. (like PIN40 as GPIO21)
GPIO.setwarnings(False)
GPIO.setup(LED,GPIO.OUT)  #initialize GPIO21 (LED) as an output Pin
GPIO.output(LED,0)

server_socket=bluetooth.BluetoothSocket( bluetooth.RFCOMM )

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
        print ("GPIO 21 LOW, LED OFF")
        GPIO.output(LED,0)
    if (data == "1"):    #if '1' is sent from the Android App, turn OFF the LED
        print ("GPIO 21 HIGH, LED ON")
        GPIO.output(LED,1)
    if (data == "q"):
        print ("Quit")
        break

client_socket.close()
server_socket.close()


# example of connecting to database
with sqlite3.connect(database_location) as conn:
    cur = conn.cursor()

    cur.execute(''' SELECT user_login, user_pass
                    FROM Users
                    WHERE user_login == ?
                ''', [login])
                # where 'login' is passed from Android
    entries = cur.fetchall()
conn.commit()
