import requests
import numpy as np
import json 

list_of_first_names = ["Guy","Gal"]
list_of_last_names = ["Shapira","Sidi"]
list_of_cities = ["Eilat"]
list_of_coordinates = [2,3,5,10,7,11,23]

for i in range(1):
  location = {}
  location['x'] = 11
  location['y'] = 20
  location['city_name'] = "Tel-Aviv"
  location['city_id'] = 6666

  location1 = {}
  location1['x'] = 10
  location1['y'] = 30
  location1['city_name'] = "Eilat"
  location1['city_id'] = 888
    
  
  date = "2019-12-31"

  user = {}
  
  user['location'] = location
  user['first_name'] = list_of_first_names[i]
  user['last_name'] = list_of_last_names[i]
  user['date'] = date
  user['cities_in_path'] = [location]

  

  user_str = json.dumps(user)
  print(user)  
  url = "http://localhost:8085/users"

  headers = {
    'Content-Type': 'application/json'
  }

  response = requests.request("POST", url, headers=headers, data = user_str)

  print(response.text.encode('utf8'))
