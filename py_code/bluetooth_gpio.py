import bluetooth
import RPi.GPIO as GPIO        #calling for header file which helps in using GPIOs of PI
import sqlite3

# location of database on pi
database_location = 'database.db'

LED=21

GPIO.setmode(GPIO.BCM)     #programming the GPIO by BCM pin numbers. (like PIN40 as GPIO21)
GPIO.setwarnings(False)
GPIO.setup(LED,GPIO.OUT)  #initialize GPIO21 (LED) as an output Pin
GPIO.output(LED,0)

# test database info
#with sqlite3.connect(database_location) as conn:
#    cur = conn.cursor()
#    cur.execute(''' INSERT INTO Users VALUES ("nathan", "pass"); ''')
#    cur.execute(''' CREATE TABLE Users ( user_login varhar(20), user_pass  carchar(20) ); ''')

server_socket=bluetooth.BluetoothSocket( bluetooth.RFCOMM )

port = 1
server_socket.bind(("",port))
server_socket.listen(1)
print 'Waiting for connection...'
client_socket,address = server_socket.accept()
print 'Accepted connection from ', address

while 1:
    try:
    # how is data being passed back and fourth?
        data = client_socket.recv(1024)
	print 'Received: %s' % data
#        print type(data)
        data = data.split('+')
#        print 'split: %s' % data

	if (data[0] == 'login'):
 #           print 'login: %s' % data
            # split info into username and password
            u_p = data[1].split('_')
            username, password = u_p[0], u_p[1]
            print username, password
            with sqlite3.connect(database_location) as conn:
                cur = conn.cursor()

                cur.execute(''' SELECT user_login, user_pass
                                FROM Users
                                WHERE user_login == ?
                            ''', [username])
                            # where 'login' is passed from Android
                entries = cur.fetchall()
 #               print type(entries)
#                print len(entries)
  #              print entries
            if len(entries) > 0 and entries[0][1] == password:
                client_socket.send('accept')
                print 'accept'
            else:
                client_socket.send('deny')
                print 'deny'


	    if (data == "q"):
		# might have to put whole thing in a try_catch
		print  'quitting'
		break


    except Exception as e:
	print 'exception:', e
	print 'Waiting for connection...'
	client_socket,address = server_socket.accept()
	print 'Accepted connection from ', address


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

# this only needs to happen when writing to database not reading from it
conn.commit()
