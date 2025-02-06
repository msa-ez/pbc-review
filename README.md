# Review App

## Model
www.msaez.io/#/198020783/storming/review

## Before Running Services
### Make sure there is a Kafka server running
```
cd infra
docker-compose up -d
```
- Check the Kafka messages:
```
cd infra
docker-compose exec -it kafka /bin/bash
cd /bin
./kafka-console-consumer --bootstrap-server localhost:9092 --topic
```
### Make sure that it does not conflict with the running port number
Port number used by review app
- Frontend: 8050
- Backend: 8052
- API Gateway: 8058

In the event of a port number conflict, you must modify the port number in the following files:
- Frontend: frontend/package.json
- Backend: review/src/main/resources/application.yml
- API Gateway: gateway/src/main/resources/application.yml


## Run the backend micro-services
See the README.md files inside the micro-services directory:
- review
  

## Run API Gateway (Spring Gateway)
```
cd gateway
mvn spring-boot:run
```


## Test by API
- review
```
http :8088/reviews id="id" itemId="itemId" rating="rating" text="text" userId="userId" userImg="userImg"
```


## Run the frontend
```
cd frontend
npm i
npm run serve
```
To use as a web component in another project, see the [**README.md**](https://github.com/msa-ez/pbc-review/blob/main/frontend/README.md) file in the frontend directory.


## Test by UI
Open a browser to localhost:8058


## Required Utilities

- httpie (alternative for curl / POSTMAN) and network utils
```
sudo apt-get update
sudo apt-get install net-tools
sudo apt install iputils-ping
pip install httpie
```

- kubernetes utilities (kubectl)
```
curl -LO "https://dl.k8s.io/release/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl"
sudo install -o root -g root -m 0755 kubectl /usr/local/bin/kubectl
```

- aws cli (aws)
```
curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip"
unzip awscliv2.zip
sudo ./aws/install
```

- eksctl 
```
curl --silent --location "https://github.com/weaveworks/eksctl/releases/latest/download/eksctl_$(uname -s)_amd64.tar.gz" | tar xz -C /tmp
sudo mv /tmp/eksctl /usr/local/bin
```
