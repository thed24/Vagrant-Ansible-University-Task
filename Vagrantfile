# -*- mode: ruby -*-
# vi: set ft=ruby :

Vagrant.configure("2") do |config|

  config.vm.define 'web', primary: true do |dockerWeb|
    dockerWeb.vm.provider 'docker' do |d|
      d.build_dir = "."
      d.has_ssh = true
      d.remains_running = true
      d.env = {
        "DB_HOST":"db",
        "DB_USER":"postgres",
        "DB_PORT":"5432",
        "DB_NAME":"postgres",
        "DB_PASSWORD":"example",
        "PORT":"8080"
      }
      d.cmd = [
        '/usr/sbin/sshd',
        '-D',
        '-e'
      ]
    end

  dockerWeb.vm.provision :ansible_local do |ansible|
      ansible.playbook = "playbook.yml"
      ansible.become = true
    end
  end

  config.vm.define 'db' do |dockerDb|
    dockerDb.vm.provider :docker do |d|
      d.image = 'postgres'
      d.remains_running = true
      d.env = {
        "POSTGRES_USER":"postgres",
        "POSTGRES_PASSWORD":"example"
      }
    end
  end

  config.ssh.username = 'root'
  config.vm.network "forwarded_port", guest: 8080, host: 7000, host_ip: "127.0.0.1", auto_correct: true
end

