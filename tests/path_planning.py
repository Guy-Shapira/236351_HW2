import requests
import numpy as np
import json
import time
from kazoo.client import KazooClient
import asyncio
from multiprocessing.dummy import Pool

def calc(k):


    list_of_first_names = ["Guy", "Gal", "Mickey", "Minie"]
    list_of_last_names = ["Shapira", "Sidi", "Mouse", "Mouse"]
    list_of_cities = ["Eilat"]
    #list_of_coordinates = [2,3,5,10,7,11,23]



    date = "2019-12-31"

    cities = [
        {'x':10, 'y':20, 'city_name': 'Haifa', 'city_id':1},     #'Haifa'
        {'x':10, 'y':30, 'city_name': 'Eilat', 'city_id':2},     #'Eilat'
        {'x':40, 'y':10, 'city_name': 'Guyland', 'city_id':3},   #Guyland
        {'x':1000, 'y':50,'city_name': 'Moon', 'city_id':4},    #Moon
        {'x':0, 'y':60, 'city_name': 'London', 'city_id':5},     #London
    ]

    users = [
        {'location': cities[0], 'first_name': list_of_first_names[0], 'last_name': list_of_last_names[0], 'date': date,
         'cities_in_path':[cities[1]]},
        {'location': cities[1], 'first_name': list_of_first_names[1], 'last_name': list_of_last_names[1], 'date': date,
         'cities_in_path':[cities[2]]},
        {'location': cities[2], 'first_name': list_of_first_names[2], 'last_name': list_of_last_names[2], 'date': date,
         'cities_in_path':[cities[4]]}

    ]


    url = "http://localhost:8085/users"

    headers = {
        'Content-Type': 'application/json'
    }
    zk = KazooClient(hosts=['localhost:2181'])
    zk.start()
    for i,user in enumerate(users):
        user_str = json.dumps(user)
        #print(user)

        servers = zk.get_children('/rest')
        #print(servers)
        server = np.random.choice(servers)
        #print(server)
        url = "http://"+str(server)+"/users"
        start_time = time.time()
        response = requests.request("POST", url, headers=headers, data = user_str)
        print(k*3+i)

        #print(response.text.encode('utf8'))
        print((time.time() - start_time))

        if i == 2:
            assert(response.text.encode('utf8').split()[0]==b'Sorry,')
        else:
            assert(response.text.encode('utf8').split()[0]!=b'Sorry,')

if __name__ == '__main__':
    pool = Pool(10)
    futures = []
    for i in range(19):
        futures.append(pool.apply_async(calc, (i,)))

    time.sleep(4)
    for future in futures:
        print(future.get())