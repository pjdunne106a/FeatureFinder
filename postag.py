import requests
word="test";
r = requests.get('http://localhost:8080/postagtext?text='+word);
print (r.json());