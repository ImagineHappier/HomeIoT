import json
import requests
import time


ip="http://192.168.0.2"
headers ={'Content-type':'application/json','Accept':'text/plain'}

url = ip+'/api/fred'

data ={'devicetype':'test user','username':'newdeveloper'}

# Setup new user
r = requests.get(url)
print r.text

url = ip+"/api"

r= requests.post(url,json.dumps(data),headers=headers)
print r.text

# Get information on lights
url = ip+'/api/newdeveloper/lights'

r=requests.get(url)

print r.text