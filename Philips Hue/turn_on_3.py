import json
import requests
import time

ip="http://192.168.0.8"
headers ={'Content-type':'application/json','Accept':'text/plain'}

url = ip+'/api/newdeveloper/lights/3/state'
try:
	pdata = {"on":True, "sat":255, "bri":255,"hue":255}
	r = requests.put(url,data=json.dumps(pdata),headers=headers)
        print r.text

except KeyboardInterrupt:
        print "End"


