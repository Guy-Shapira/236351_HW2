import requests
import numpy as np
import json 

list_of_cities = ["Tel-Aviv", "Eilat"]
list_of_coordinates = [2,3,5,10,7,11,23]

for _ in range(2):
  src_city = {}
  src_city['x'] = np.random.choice(100)
  src_city['y'] = np.random.choice(100)
  src_city['city_name'] = np.random.choice(list_of_cities)
  src_city['city_id'] = 1
  
  dst_city = {}
  dst_city['x'] = np.random.choice(100)
  dst_city['y'] = np.random.choice(100)
  dst_city['city_name'] = np.random.choice(list_of_cities)
  dst_city['city_id'] = 2

  
  
  date = "2019-12-31"
  
  ride = {}
  ride['start_location'] = src_city
  ride['end_location'] = dst_city
  ride['date'] = date
  ride['phone_number'] = 3
  ride['first_name'] = 'Shai'
  ride['last_name'] = 'F'
  ride['vacancies'] = np.random.choice(50)
  ride['pd'] =  np.random.choice(100)

  

  ride_str = json.dumps(ride)
  print(ride_str)  
  url = "http://localhost:8080/rides"

  headers = {
    'Content-Type': 'application/json'
  }

  response = requests.request("POST", url, headers=headers, data = ride_str)

  print(response.text.encode('utf8'))
