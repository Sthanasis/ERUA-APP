# ABOUT THE APP

The app is configured to run containerized with docker so we need to have docker engine installed in the host machine.
To install docker engine on ubuntu follow this documentation https://docs.docker.com/engine/install/ubuntu/

## Start the app

- Once docker installation is completed, copy the entire docker-compose.yaml file in the host machine.
- On the same directory you copied the file, execute one of the following commands in the terminal.
``docker-compose up``
``docker-compose up -d``

The first one shows logs but hijacks the terminal, and the second one does not do either of those (the seconds command is recommended to be used on production).

The client is configured to run on port 80
The backend is configured to run on port 8080

## Swagger

A swagger exists in order to document the services that are implemented on the backend.
You can access swagger json version by visiting <host-ip>:8080/api, or you can access the swagger ui by visiting <host-ip>:8080/api-ui