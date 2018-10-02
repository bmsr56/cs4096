import threading
import time

def loop1_10():
    for i in range(1, 11):
        time.sleep(1)
        print(i)

# this will print 2 numbers at the same time for 10 seconds
threading.Thread(target=loop1_10).start()
threading.Thread(target=loop1_10).start()
