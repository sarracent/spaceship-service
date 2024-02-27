# spaceship-service


RUN SERVICE: 
Building a Docker image:
docker build -t spaceship-service .

Running the container in detached mode:
docker run -d -p8080:8080 -e "SPRING_PROFILES_ACTIVE=docker" --name my-prd-srv spaceship-service

Get the log output from our container:
docker logs my-prd-srv -f

Stopping and removing the container:
docker rm -f my-prd-srv



