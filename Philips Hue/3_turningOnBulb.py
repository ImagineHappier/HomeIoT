import json
import requests
import time

ip="http://192.168.0.2"
headers ={'Content-type':'application/json','Accept':'text/plain'}

url = ip+'/api/newdeveloper/lights/2/state'
try:
        while True:
                pdata = {"on":False, "sat":255, "bri":255,"hue":1000}

                r = requests.put(url,data=json.dumps(pdata),headers=headers)

                print r.text

                time.sleep(2)

                pdata = {"on":True, "sat":255, "bri":255,"hue":1000}

                r = requests.put(url,data=json.dumps(pdata),headers=headers)

                time.sleep(2)
                print r.text

except KeyboardInterrupt:
        print "End"
