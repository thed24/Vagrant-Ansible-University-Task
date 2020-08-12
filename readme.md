# Task 1: Vagrant and Ansible

**Student Name:** Dominic-Bruno Codespoti

**Student ID:** 102115980

**Unit Details:** SWE30004 Software Deployment and Evolution, Semester 2

**Due Date:** 2 Sep by 23:59

**Declaration of the task level attempted (P/C/D/HD):** HD

# Issues and Fixes

As Vagrant allows for utilizing multiple providers for the VMs they spin up, I chose to use Docker, as it was my prefered choice of the options listed, however I quickly ran into a bug in Vagrant. My development environment utilizes WSL 2, which Vagrant only experimentally supports. As such, I encountered several errors that many other users have also encountered, as shown below:

* [GitHub Issue Example](https://github.com/hashicorp/vagrant/issues/10576)
* [GitHub Issue Example 2](https://github.com/hashicorp/vagrant/issues/11604)
  
I thought maybe this would be a Docker specific issue, however it turns out that all forms of folder syncing are broken in WSL 2, and this is an issue as a good Development environment requires folder syncing so that work can be done on the host device and the changes can dynamically appear in the Development environment hosted on Vagrant. As there is no fix for this bug, I chose to develop one. The first error is shown below:

**Add screenshot!**

The issue here is Vagrant incorrectly checks for the filesystem used in WSL, as it uses a conditional check like this: 

``` if info && info[:type] == "drvfs" ```

The issue there is that it does not check for the filesystem of the WSL file server, which is [9P](https://en.wikipedia.org/wiki/9P_%28protocol%29). By including this check, we can get passed this issue of Vagrant throwing the exception even though we are in the correct filesystem. The change to the conditional is as below:

``` if info && (info[:type] == "drvfs" || info[:type] == "9p") ```

The next issue is that Vagrant attempts to parse the source file destination as a Windows path. Refer to the code below, where the host string is source on the volume mapping:

              host, guest = v.split(":", 2)	
              host = Vagrant::Util::Platform.windows_path(host)

As WSL 2 is Unix based, it does not parse file locations like Windows, and as such, we can forego this and utilize the unedited host string for the volume mapping, as Docker can read it. As such, I put these two lines in a conditional that only fires off if the conditional in the *if statement* is met:

              if (!ENV["VAGRANT_WSL_ENABLE_WINDOWS_ACCESS"])
                host = Vagrant::Util::Platform.windows_path(host)
                host.gsub!(/^[^A-Za-z]+/, "")
              end

With these changes, Vagrant is able to mount a directory within the host filesystem to Docker. As this is an error many other users of Vagrant have encountered, I opened a pull request with my fixes for others to use here: https://github.com/hashicorp/vagrant/pull/11803

This does not fix the sync issue for other providers, however as I am not using them I did not endeavour to fix them, as this fix works fine for the issue I encountered.