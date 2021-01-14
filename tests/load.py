import requests
import numpy as np
import json
import time
from kazoo.client import KazooClient


#list_of_cities = ["Tel-Aviv", 'Haifa', "Eilat", 'Guyland', 'Moon', 'London']
names = ["Idan", "Itay", "Tomer", "Shai", "Shair"]
rides_number = 3
start_time = time.time()
date = "2019-12-31"


cities = [
          {'x':10, 'y':20, 'city_name': 'Haifa', 'city_id':1},     #'Haifa'
          {'x':10, 'y':30, 'city_name': 'Eilat', 'city_id':2},     #'Eilat'
          {'x':40, 'y':10, 'city_name': 'Guyland', 'city_id':3},   #Guyland
          {'x':1000, 'y':50,'city_name': 'Moon', 'city_id':4},    #Moon
          {'x':0, 'y':60, 'city_name': 'London', 'city_id':5},     #London
          ]

rides = [
    {'start_location': cities[0], 'end_location':cities[1], 'date': date,
     'phone_number': 1, 'first_name': names[np.random.choice(len(names))],
     'last_name': names[np.random.choice(len(names))], 'vacancies': 200, 'pd': 1},

    {'start_location': cities[1], 'end_location': cities[2], 'date': date,
     'phone_number': 2, 'first_name': names[np.random.choice(len(names))],
     'last_name': names[np.random.choice(len(names))], 'vacancies': 200, 'pd': 1},

    {'start_location': cities[2], 'end_location': cities[3], 'date': date,
     'phone_number': 3, 'first_name': names[np.random.choice(len(names))],
     'last_name': names[np.random.choice(len(names))], 'vacancies': 200, 'pd': 1}
]

headers = {
    'Content-Type': 'application/json'
}


zk = KazooClient(hosts=['localhost:2181'])
zk.start()

for k in range(10):
    for i in range(len(rides)):
        ride = rides[i]
        ride_str = json.dumps(ride)
        print(ride_str)

        servers = zk.get_children('/rest')
        print(servers)
        server = np.random.choice(servers)
        print(server)
        url = "http://"+str(server)+"/users"
        response = requests.request("POST", url, headers=headers, data = ride_str)

        print(response.text.encode('utf8'))
        print((time.time() - start_time) / (i + 1))



