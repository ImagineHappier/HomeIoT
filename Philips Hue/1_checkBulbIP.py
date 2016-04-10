import json
import requests
import time

ip="http://192.168.0.2"
headers ={'Content-type':'application/json','Accept':'text/plain'}

url = ip+'/api/fred'

data ={'devicetype':'test user','username':'newdeveloper'}