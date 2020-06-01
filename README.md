1. Build:
mvn clean install

2. Eclipse project:
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