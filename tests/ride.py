import requests
import numpy as np
import json 
import time


list_of_cities = ["Tel-Aviv", "Eilat"]
list_of_coordinates = [2,3,5,10,7,11,23]
names = ["Idan", "Itay", "Tomer", "Shai", "Shair"]
start_time = time.time()

for i in range(1):
  src_city = {}
  src_city['x'] = 10
  src_city['y'] = 20
  src_city['city_name'] = 'Haifa'
  src_city['city_id'] = 1
  
  dst_city = {}
  dst_city['x'] = 10
  dst_city['y'] = 30
  dst_city['city_name'] = 'Eilat'
  dst_city['city_id'] = 2

  
  
  date = "2019-12-31"
  
  ride = {}
  ride['start_location'] = src_city
  ride['end_location'] = dst_city
  ride['date'] = date
  ride['phone_number'] = 3
  ride['first_name'] = names[np.random.choice(len(names))]
  ride['last_name'] = 'F'
  ride['vacancies'] = 200
  ride['pd'] =  10000
  
  
  

  

  ride_str = json.dumps(ride)
  print(ride_str)  
  url = "http://localhost:8085/rides"

  headers = {
    'Content-Type': 'application/json'
  }

  response = requests.request("POST", url, headers=headers, data = ride_str)

  print(response.text.encode('utf8'))
  
  
  
  # src_city = {}
  # src_city['x'] = 10
  # src_city['y'] = 30
  # src_city['city_name'] = 'Eilat'
  # src_city['city_id'] = 2
  
  # dst_city = {}
  # dst_city['x'] = 50
  # dst_city['y'] = 50
  # dst_city['city_name'] = 'London'
  # dst_city['city_id'] = 3

  
  
  # date = "2019-12-31"
  
  # ride = {}
  # ride['start_location'] = src_city
  # ride['end_location'] = dst_city
  # ride['date'] = date
  # ride['phone_number'] = 3
  # ride['first_name'] = names[np.random.choice(len(names))]
  # ride['last_name'] = 'F'
  # ride['vacancies'] = 200
  # ride['pd'] =  10000
  
  # ride_str = json.dumps(ride)
  # print(ride_str)  
  # url = "http://localhost:8085/rides"

  # headers = {
    # 'Content-Type': 'application/json'
  # }

  # response = requests.request("POST", url, headers=headers, data = ride_str)

  # print(response.text.encode('utf8'))
  
  # print((time.time() - start_time) / (i + 1))

