import serial
import os
import matplotlib.pyplot as plt
from drawnow import *

ACCX = []
ACCY = []
ACCZ = []
raw_data = []

arduinodata=serial.Serial('COM3',9600)

def makeplotting():
        plt.ylabel('ACCX (°/sec)')
        plt.xlabel('Time')

        plt.plot(ACCX)
        plt.plot(ACCY)
        plt.plot(ACCZ)

while True:
        while(arduinodata.inWaiting()==0):
                pass
        arduinostring = arduinodata.readline()
        arduinostring = str(arduinostring,encoding="utf-8")
        raw_data = arduinostring.split(',')

        with open('D:\Projects\Minor Project\IOT-embedded-social-safety-enhancing-system\Acc Data\ fall_data.txt', 'a+') as filehandle:
                filehandle.write('[')
                for listitem in raw_data:
                        filehandle.write('%s, '% float(listitem))
        with open('D:\Projects\Minor Project\IOT-embedded-social-safety-enhancing-system\Acc Data\ fall_data.txt', 'rb+') as filehandle:
                filehandle.seek(-2,os.SEEK_END)
                filehandle.truncate()
        with open('D:\Projects\Minor Project\IOT-embedded-social-safety-enhancing-system\Acc Data\ fall_data.txt', 'a+') as filehandle:
                filehandle.write('],\n')

        ACCX.append(float(raw_data[0]))
        ACCY.append(float(raw_data[1]))
        ACCZ.append(float(raw_data[2]))



        if len(ACCX) <= 30 and len(ACCY) <= 30 and len(ACCZ) <= 30:
                drawnow(makeplotting)
        else:
                with open('D:\Projects\Minor Project\IOT-embedded-social-safety-enhancing-system\Acc Data\ fall_data.txt', 'rb+') as filehandle:
                        filehandle.seek(-3, os.SEEK_END)
                        filehandle.truncate()
                with open('D:\Projects\Minor Project\IOT-embedded-social-safety-enhancing-system\Acc Data\ fall_data.txt', 'a+') as filehandle:
                        filehandle.write(']\n')
                        filehandle.write(',\n')
                        filehandle.write('[')
                exit()

