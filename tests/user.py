import requests
import numpy as np
import json 
import time

list_of_first_names = ["Guy","Gal"]
list_of_last_names = ["Shapira","Sidi"]
list_of_cities = ["Eilat"]
list_of_coordinates = [2,3,5,10,7,11,23]

start_time = time.time()

for i in range(1):
  location = {}
  location['x'] = 10
  location['y'] = 20
  location['city_name'] = "Haifa"
  location['city_id'] = 1

  location1 = {}
  location1['x'] = 10
  location1['y'] = 30
  location1['city_name'] = "Eilat"
  location1['city_id'] = 2
  
  # location2 = {}
  # location2['x'] = 50
  # location2['y'] = 50
  # location2['city_name'] = "London"
  # location2['city_id'] = 4
    
  # location3 = {}
  # location3['x'] = 500000
  # location3['y'] = 500000
  # location3['city_name'] = "AAA"
  # location3['city_id'] = 555
    
  date = "2019-12-31"

  user = {}
  
  user['location'] = location
  user['first_name'] = list_of_first_names[0]
  user['last_name'] = list_of_last_names[0]
  user['date'] = date
  user['cities_in_path'] = [location1]

  

  user_str = json.dumps(user)
  print(user)  
  url = "http://localhost:8085/users"

  headers = {
    'Content-Type': 'application/json'
  }

  response = requests.request("POST", url, headers=headers, data = user_str)

  print(response.text.encode('utf8'))
  print((time.time() - start_time) / (i + 1))