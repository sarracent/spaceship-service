# spaceship-service

Building a Docker image
docker build -t spaceship-service .

Starting up the service
docker run --rm -p8080:8080 -e "SPRING_PROFILES_ACTIVE=docker" spaceship-service 


