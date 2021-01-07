import requests
import numpy as np
import json 

list_of_first_names = ["Guy","Gal"]
list_of_last_names = ["Shapira","Sidi"]
list_of_cities = ["Tel-Aviv", "Eilat"]
list_of_coordinates = [2,3,5,10,7,11,23]

for i in range(2):
  location = {}
  location['x'] = np.random.choice(100)
  location['y'] = np.random.choice(100)
  location['city_name'] = list_of_cities[i]
  location['city_id'] = i
  
  
  date = "2019-12-31"

  user = {}
  
  user['location'] = location
  user['first_name'] = list_of_first_names[i]
  user['last_name'] = list_of_last_names[i]
  user['date'] = date
  user['cities_in_path'] = [location, location, location]

  

  user_str = json.dumps(user)
  print(user)  
  url = "http://localhost:8085/users"

  headers = {
    'Content-Type': 'application/json'
  }

  response = requests.request("POST", url, headers=headers, data = user_str)

  print(response.text.encode('utf8'))
