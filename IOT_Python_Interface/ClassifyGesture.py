import serial
import json
import requests
import joblib
import numpy as np
gesture = {0: 'Normal gesture',
		   1: 'Falling Gesture',
		   2: 'Women Insecurity',
		   3: 'Women Insecurity'}

acc_data = list()
data_set = list()
raw_data = list()
index = 0
rang = 31

arduinodata = serial.Serial('COM4', 9600)

def prediction(data):
    pred = joblib.load(r'H:\Projects\IOT-embedded-social-safety-enhancing-system\model.pkl')
    x_valid = np.array([data]).reshape(1, 90)
    predictions = pred.predict(x_valid)
    return predictions[0]

def check_ges(data_refined):
	for j in range(0, len(data_refined)-1):
		data_refined[j][0] = int(abs(data_refined[j+1][0]-data_refined[j][0])*100)
		data_refined[j][1] = int(abs(data_refined[j+1][1]-data_refined[j][1])*100)
		data_refined[j][2] = int(abs(data_refined[j+1][2]-data_refined[j][2])*100)
	data_refined.remove(data_refined[30])
	return prediction(data_refined)

def send_data():
    x = {
    "username": "Anand",
    "latitude": "22.67",
    "longitude":"77.67"
    }
    y = json.dumps(x)
    print(y)
    req2 = requests.post("https://sos-rest-api.herokuapp.com/api/xnotifyothers", y)
    print(req2)
    print(req2.text)

while True:
    while arduinodata.inWaiting() == 0:
        pass
    arduinostring = arduinodata.readline()
    arduinostring = str(arduinostring, encoding="utf-8")
    raw_data = arduinostring.split(',')

    acc_data.append(list(map(float, raw_data)))
    if len(acc_data) == 31:
        data_set = acc_data
        print(data_set)
        value = gesture[check_ges(data_set)]
        print(value)
        if value == 'Women Insecurity'or 'Falling Gesture':
            send_data()
        else:
            print('Not Emergency')
        acc_data.clear()
