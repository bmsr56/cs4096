import threading
import time

def loop1_10():
    for i in range(1, 11):
        time.sleep(1)
        print(i)

# this will print 2 numbers at the same time for 10 seconds
# https://en.wikibooks.org/wiki/Python_Programming/Threading

# instead of having threads open all the time, open a thread when you need one for as long as you need it then kill it -> repeat
threading.Thread(target=loop1_10).start()
threading.Thread(target=loop1_10).start()
