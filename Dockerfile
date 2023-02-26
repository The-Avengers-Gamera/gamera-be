FROM ubuntu

RUN sudo apt update &&\
    sudo apt install openjdk-17-jre-headless &&\
    sudo apt install gradle &&\
    sudo apt install docker.io &&\
    sudo apt install make

COPY . .

EXPOSE 8080

ENTRYPOINT ["/bin/bash"]

CMD [ "make", "make app_local_compose_up" ; "make app_local_build " ]
