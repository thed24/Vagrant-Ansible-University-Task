# -*- mode: ruby -*-
# vi: set ft=ruby :

Vagrant.configure("2") do |config|

  config.vm.define 'db' do |dockerDb|
    dockerDb.vm.network :private_network, type: "dhcp"
    dockerDb.vm.provider :docker do |d|
      d.image = 'postgres'
      d.name = "db"
      d.remains_running = true
      d.ports = ["5432:5432"]
      d.env = {
        "POSTGRES_USER":"postgres",
        "POSTGRES_PASSWORD":"example"
      }
    end
  end

  config.vm.define 'web', primary: true do |dockerWeb|
    dockerWeb.vm.network :private_network, type: "dhcp"
    dockerWeb.vm.provider 'docker' do |d|
      d.ports = ["8080:8080"]
      d.build_dir = "."
      d.name = "web"
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
      ansible.playbook = "ansible/playbook.yml"
      ansible.become = true
    end
  end

  config.ssh.username = 'root'
end

