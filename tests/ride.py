import requests
import numpy as np
import json 

list_of_cities = ["Tel-Aviv", "Eilat"]
list_of_coordinates = [2,3,5,10,7,11,23]
names = ["Idan", "Itay", "Tomer", "Shai", "Shair"]

for _ in range(3):
  src_city = {}
  src_city['x'] = np.random.choice(100)
  src_city['y'] = np.random.choice(100)
  src_city['city_name'] = 'Tel-Aviv'
  src_city['city_id'] = 1
  
  dst_city = {}
  dst_city['x'] = np.random.choice(100)
  dst_city['y'] = np.random.choice(100)
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
  ride['vacancies'] = 2
  ride['pd'] =  6

  

  ride_str = json.dumps(ride)
  print(ride_str)  
  url = "http://localhost:8085/rides"

  headers = {
    'Content-Type': 'application/json'
  }

  response = requests.request("POST", url, headers=headers, data = ride_str)

  print(response.text.encode('utf8'))
