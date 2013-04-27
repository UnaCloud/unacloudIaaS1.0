UnaCloud
========

Opportunistic Cloud Computing Infrastructure as a Service

##Introduction

UnaCloud is a project developed by the investigation group [COMIT](http://comit.uniandes.edu.co) (Comunicaciones y Tecnología de Información) from Universidad de los Andes, Colombia. UnaCloud is an opportunistic cloud computing Infrastructure as a Service (IaaS) model implementation, which provides at lower cost basic computing resources (processing, storage and networking) to run arbitrary software, including operating systems and applications. The IaaS model is provided through the opportunistic use of idle computing resources available in the university campus. 

UnaCloud deals with the problems associated to use commodity, non-dedicated, distributed, and heterogeneous computing resources that are part of different administrative domains. We propose an IaaS architecture based on two strategies: an opportunistic strategy that allows the use of idle computing resources in a nonintrusive manner, and a virtualization strategy to allow the on demand deployment of customized execution environments.

## Installation Guide

This guide allows you to install UnaCloud 1.0 on your infrastructure.

### Requirements

For UnaCloud Server:

* A 4GB and 2 cores machine
* Windows Server 2003 or Windows 7 SO.
* 1 GB for UnaCloud Server and at least 80 GB of hard disk for Virtual Machines
* Java JDK SE 6 or better

For UnaCloud Agents:

* At least 200 MB of free RAM.
* 50 MB for UnaCloud client and at least 20 GB of hard disk for Virtual Machines.
* Java JRE SE 6 or better
* One of the following hypervisors:
* VMware Player 7 or better (you must install VMware Player and VMware VIX together)
* VMware Workstation 6 or better (Workstation installs VMware VIX by default)

### UnaCloud Server installation

Download UnaCloud project or clone the repository. (Warning, you must use a folder with an absolute path with no white spaces on its route).
Run the start.bat script that is on the dist folder.

### UnaCloud server configuration guide

Changing root password

As first step you should change the root user password of the server. To do this enter to your UnaCloud server instance through a web browser.

The browser leads you to the UnaCloud server main page. Click on login and then a login dialog will be displayed. The first time you should use this credentials:

* User: root
* Pass: unacloudIaaS
Then you will be conducted to UnaCloud users main page. Here click on the user icon . On this page you must change your root password.

Configuring server - agent parameters

You must set the protocol ports and network parameters to connect UnaCloud server and Client. To do this, as root user go to the Configurarion\Server configuration option.

At this page you can set the server ip and the TCP ports used between Server and Agents. We not recommend to change the ports as possible. On Server IP you must set the ip of the server, so the Agents can establish connection. For example our UnaCloud server instance has the IP 157.253.236.160

Next you have to go to Configuration\QoS configuration option. Here you can enable/disable the infrastructure monitoring. By activating the QoS, each physical machine is going to monitor its resources and stores a historic log to model and profile resources. This data is stored on the selected dabase at QoS configuration. As default we store this data in a QoS scheme on UnaCloud database, but you can change it.

At last you must go to Configuration\Security configuration to change the RSA keys used to encrypt server - agent communications. You must change these keys so no one but UnaCloud server can send instructions to Agents.

### Creating Physical Laboratories

UnaCloud sorts Physical machines by Physical Laboratories for GUI convenience. As first step you need to create a Physical Laboratory to add physical machines to it. Go to Administration\Manage Laboratories Information option and click on new icon .

On the popup panel, insert your Laboratory parameters. As example we have the next Laboratory on our UnaCloud instance:

* Laboratory name: Waira I
* Laboratory rows: 6
* Laboratory Columns: 6
* Laboratory Type: Physical
* Creating Physical Machines

A physical machine is a computer that has an UnaCloud agent installed and is used to deploy opportunistic virtual machines. Each physical machine is associated to a Physical Laboratory. To create a physical machine go to Administration\Manage Physical Machines Information option and click on new icon .

On the popup panel, insert your Physical Machine parameters. Do this step for each machine in your infrastructure. As an example we have the next Machine on our UnaCloud instance:

* Name: ISC401 (should be the hostname of the machine, on windows you can get it executing hostname on command line console)
* Laboratory: Waira I (The laboratory that contains the machine)
* IP Address: 157.253.201.141
* MAC Address: AA:BB:CC:DD:EE:FF (used to automatically start physical machines using Wake-On-Lan)
* CPU Cores: 8
* Hard Disk Space: 50 GB (for UnaCloud)
* RAM Memory: 16384 MB
* Architecture: x86
* Hypervisor Path: C:\Program Files\VMware\VIX (The path to hypervisor binaries. For VMware products you must put the path that contains the vmrun.exe.)
* Operating System: Windows 7
* Virtual IP: 157.253.201.191
* Virtual netmask: 255.255.255.128

### Creating Virtual Machine Templates

A template is a Virtual Machine Image that is deployable by UnaCloud. A template have a type, a name and a path to its virtual machine files. To create a new template go to Administration\Manage Template Information option and click on new icon .

As example we have the following template:

Template Name: Gromacs
Operating System: Ubuntu 9 (You need to be as accurate as possible with this field)
Template Type: GRID
Image location: Z:\gromacs\gromacs1.vmx (The location, on server, where the virtual machine is)
Customizable: true (if the template can be changed by users or not)
High availability: true (for future QoS)
Applications: Linux Default Applications (Information only)
To use a VMware virtual machine, it must have installed the latest version of vmware tools. You can (and must) check that your virtual machine is compatible. To do this use the next commands:

vmrun -T ws start //Virtual Machine Path//
vmrun -T ws -gu //user// -gp //password// listProcessesInGuest //Virtual Machine Path//
Those commands must list all running processes on your virtual machines.

### Creating a Security Access Method

A security access method is a tuple that contains information of administrator credentials of a virtual machine. This information is used by UnaCloud to configure the virtual machines after deployment. To create an security access method go to Administration\Manage Security Access Information option and click on new icon . Here is an example that is used to deploy gromacs machines:

* Security access name: GromacsSA
* Authorized User: root (virtual machine administrator)
* Password: gr0macs15753
* Access Mechanism: SSH (for all linux based machines)
* Access Port: 22
* Creating a Deployable Virtual Machine

A virtual machine is an instance of a template that is deployed on a physical machine. A virtual machine has a pointer to its template and to the physical machine that is going to execute it. To create a virtual machine go to Administration\Manage Virtual Machines Information option and click on new icon . The next is an example of a gromacs virtual machine that is deployed on machine ISC401:

* Name: node1 (virtual machine hostname)
* Physical machine: ISC401
* IP Address: 157.253.201.191 (The IP that is going to be used by this virtual machine)
* IP Generation Policy: public machine based(Actually we support public machine based)
* MAC Generation Policy: static machine based
* CPU cores: 4 (can be chagend by user on each deploy)
* Hard Disk Space: 10 GB (for information only)
* RAM Memory: 2048 (can be chagend by user on each deploy)
* Hypervisor: VMware Player
* Path: E:\GRID\gromacs1.vmx (The system is going to copy the template image on this path for this physical machine)
* Template: gromacs
* Security Schema: gromacsSA
* Locally stored: false (If the template image is already copied on physical machine or not. If false UnaCloud will copy automatically the template image)
* Configured: false (If the virtual machine is already copied and configured)
* Snapshot id: (for future extentions)
* AutoProtect: false (for future extentions)
Creating a System User

Now, we must create users and gave them permissions to deploy the created virtual machine. To do this go to Administration\Manage System Users Information and click on . This will let you to create a new unacloud user. Once you create the user, you must assign him templates. To do that, click on  on user table row.

About user roles: GRID users can deploy GRID templates, IaaS users can deploy Cloud templates, and IaaS-GRID users can deploy both.

### UnaCloud Agents Installation

Enter at your UnaCloud Server instance and follow the guide on Configuration\Client Installation Guide label. You must install the agent on each physical machine.

## Resources

For more information about UnaCloud project, you can follow this [link] (http://unacloud.uniandes.edu.co) that will guide you to the university wiki

## License

GPU GNL version 2.0