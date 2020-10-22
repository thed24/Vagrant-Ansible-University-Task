FROM debian:buster-slim

RUN apt-get update \
    && apt-get install -y python openssh-server sudo wget curl puppet \
    && apt-get clean

RUN mkdir -p /root/.ssh
RUN wget --no-check-certificate https://raw.github.com/mitchellh/vagrant/master/keys/vagrant.pub -O /root/.ssh/authorized_keys  
RUN chmod 0700 /root/.ssh
RUN chmod 0600 /root/.ssh/authorized_keys  

RUN mkdir /var/run/sshd
RUN sed -i 's/PermitRootLogin prohibit-password/PermitRootLogin yes/' /etc/ssh/sshd_config

EXPOSE 22 8080