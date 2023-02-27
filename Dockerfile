FROM ubuntu

RUN sudo apt update &&\
    sudo apt install openjdk-17-jre-headless &&\
    sudo apt install gradle -y &&\
    sudo apt install docker.io -y &&\
    sudo apt install docker-compose -y &&\
    sudo apt install make

COPY . .

EXPOSE 8080

ENTRYPOINT ["/bin/bash"]

CMD [ "make", "app_local_compose_up" ; "app_local_build " ]
