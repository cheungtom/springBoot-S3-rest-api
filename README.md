1.) Build:

mvn clean install

2.) Eclipse project:

mvn eclipse:eclipse

3. Local test:

MacOS or Linux

export AWS_ACCESS_KEY_ID=XXX

export AWS_SECRET_ACCESS_KEY=XXX

export AWS_REGION=ap-southeast-1

mvn spring-boot:run

4.) Test at PostMan (list your file content by S3 folder Name)

http://localhost:8081/v1/s3?folderName=tom-s3-poc

5.) Health Check:

http://localhost:8081/actuator/health
Return this is correct:
{"status":"UP"}

http://localhost:8081/actuator/info
http://localhost:8081/actuator

6.) Docker build:

docker build -t cheungtom/s3-svc .

7.) Run docker container

MacOS or Linux

export AWS_ACCESS_KEY_ID=XXX

export AWS_SECRET_ACCESS_KEY=XXX

export AWS_REGION=ap-southeast-1

docker run -d --name s3-svc --env AWS_ACCESS_KEY_ID --env AWS_SECRET_ACCESS_KEY --env AWS_REGION -p 8081:8080 cheungtom/s3-svc  

docker ps -a

8.) Logs

docker logs -f s3-svc

9.) Docker Image push

docker login

docker push cheungtom/s3-svc

10.) Kubernetes deploy

kubectl apply -f secret.yaml 

kubectl get secret mysecret -o yaml

kubectl apply -f s3-svc-replicaset.yaml

kubectl get pods

kubectl logs -f s3-svc-p6xrb

kubectl get rs

kubectl apply -f s3-svc-service.yaml

kubectl get svc

http://localhost:80/v1/s3?folderName=tom-s3-poc

11.a) Install OpenShift on AWS by CloudFormtaion

Follow this link:

https://aws.amazon.com/quickstart/architecture/openshift/

Web OCP Admin console link:

https://red-hat-o-openshif-19mmubql4jye5-283815188.ap-southeast-2.elb.amazonaws.com/console/catalog

Connect to ansible-configserver

ssh -A -i "sydney_key.pem" ec2-user@ec2-3-25-75-82.ap-southeast-2.compute.amazonaws.com

Admin login:

sudo -s

oc whoami

You should be cluster admin system:admin

exit

Normal user login:

oc login

Server: https://Red-Hat-O-OpenShif-19MMUBQL4JYE5-283815188.ap-southeast-2.elb.amazonaws.com:443 (openshift)

User: admin

Pwd: When you set admin password for CloudFormation template
 
oc logout

The following section assume you use normal user login

11.b) Deploy to OpenShift - Manually build

Deploy S3 API Microservice:

oc new-project s3

oc project s3

oc apply -f secret.yaml

oc get secret mysecret -o yaml

oc apply -f s3-svc-replicaset.yaml

oc get pods

oc get rs

oc apply -f s3-svc-service.yaml

oc get svc

oc expose service/s3-svc

oc get routes

oc create -f nginx-pod.yml

oc exec -it nginx bash

apt update

apt install iputils-ping

apt install curl

apt install jq

Get by Service ELB

curl http://a2f86208aa3e211eaaab50a75c7027c0-982612140.ap-southeast-2.elb.amazonaws.com:80/actuator/health

curl http://a2f86208aa3e211eaaab50a75c7027c0-982612140.ap-southeast-2.elb.amazonaws.com:80/v1/s3?folderName=tom-s3-poc | jq '.'

Get by K8s service name

curl s3-svc:80/actuator/health

curl s3-svc:80/v1/s3?folderName=tom-s3-poc | jq '.'

Get by OpenShift Inbound Traffic Router

curl s3-svc-s3.router.default.svc.cluster.local:80/actuator/health

curl http://s3-svc-s3.router.default.svc.cluster.local:80/v1/s3?folderName=tom-s3-poc | jq '.'

oc delete all --all

12.) Deploy by S2I image builder (Only apply for Maven jar project, war not support)

oc new-project s3-s2i

oc project s3-s2i

Deploy S3 API microservice

oc new-app java~https://github.com/cheungtom/springBoot-S3-rest-api.git  \

-e AWS_ACCESS_KEY_ID=XXX \

-e AWS_SECRET_ACCESS_KEY=XXX \

-e AWS_REGION=ap-southeast-1

oc logs -f bc/springboot-s3-rest-api

oc status

oc get pods

oc get svc

oc get replicationcontrollers

oc get dc

oc get bc

oc get build

oc get is

oc expose svc/springboot-s3-rest-api

oc get routes

oc edit svc springboot-s3-rest-api

Change below:

Change spec -> type: LoadBalancer

Add

  - name: 80-tcp
  
    port: 80
    
    protocol: TCP
    
    targetPort: 8080

oc edit dc/springboot-s3-rest-api

(change spec -> replicas to 2)

oc new-app --name=nginx --docker-image=bitnami/nginx

oc get pods

oc logs nginx-1-lz2lp

oc exec -it nginx-1-lz2lp bash

Get by Service ELB

curl http://a688a60dca46511eaaab50a75c7027c0-1545828223.ap-southeast-2.elb.amazonaws.com:80/actuator/health

curl http://a688a60dca46511eaaab50a75c7027c0-1545828223.ap-southeast-2.elb.amazonaws.com:80//v1/s3?folderName=tom-s3-poc | jq '.'

Get by K8s service name

curl springboot-s3-rest-api:80/actuator/health

curl springboot-s3-rest-api:80/v1/s3?folderName=tom-s3-poc | jq '.'

Get by OpenShift Inbound Traffic Router

curl http://springboot-s3-rest-api-s3-s2i.router.default.svc.cluster.local:80/actuator/health

curl http://springboot-s3-rest-api-s3-s2i.router.default.svc.cluster.local:80//v1/s3?folderName=tom-s3-poc | jq '.'

oc delete all --all

13.) Deploy by S2I image builder template (Only apply for Maven jar project, war not support)

oc new-project s3-ui

oc project s3-ui

We use openjdk18-web-basic-s2i template

Same as your select image builder "OpenJDK 8" at OC Admin UI

Admin UI build limit: cannot pass env var

oc process --parameters openjdk18-web-basic-s2i -n openshift

Deploy S3 API microservice

oc new-app --template=openjdk18-web-basic-s2i -p APPLICATION_NAME=springboot-s3-rest-api \

-p SOURCE_REPOSITORY_URL=https://github.com/cheungtom/springBoot-S3-rest-api.git \

-p SOURCE_REPOSITORY_REF=master \

-p CONTEXT_DIR= \

-e AWS_ACCESS_KEY_ID=XXX \

-e AWS_SECRET_ACCESS_KEY=XXX \

-e AWS_REGION=ap-southeast-1

oc export template openjdk18-web-basic-s2i -n openshift

oc edit svc springboot-s3-rest-api

Change below:

Change spec -> type: LoadBalancer

Add

  - name: 80-tcp
  
    port: 80

    protocol: TCP

    targetPort: 8080

oc edit dc/springboot-s3-rest-api

(change spec -> replicas to 2)


oc new-app --name=nginx --docker-image=bitnami/nginx

oc logs nginx-1-wd9z7

oc exec -it nginx-1-wd9z7 bash

Get by Service ELB

curl http://a78b5218ba48411eaaab50a75c7027c0-743038885.ap-southeast-2.elb.amazonaws.com:80/actuator/health

curl http://a78b5218ba48411eaaab50a75c7027c0-743038885.ap-southeast-2.elb.amazonaws.com:80//v1/s3?folderName=tom-s3-poc | jq '.'

Get by K8s service name

curl springboot-s3-rest-api:80/actuator/health

curl springboot-s3-rest-api:80/v1/s3?folderName=tom-s3-poc | jq '.'

Get by OpenShift Inbound Traffic Router

curl http://springboot-s3-rest-api-s3-ui.router.default.svc.cluster.local:80/actuator/health

curl http://springboot-s3-rest-api-s3-ui.router.default.svc.cluster.local:80//v1/s3?folderName=tom-s3-poc | jq '.'

oc delete all --all


