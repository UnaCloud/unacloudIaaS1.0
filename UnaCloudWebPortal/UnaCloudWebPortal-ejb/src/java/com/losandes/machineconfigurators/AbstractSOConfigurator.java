/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.losandes.machineconfigurators;

import com.losandes.communication.security.utils.*;
import com.losandes.communication.security.SecureSocket;
import com.losandes.multicast.UnicastSender;
import com.losandes.persistence.entity.VirtualMachine;
import com.losandes.virtualmachineexecution.IVirtualMachineExecutionServices;
import com.losandes.vo.HostTable;
import java.io.File;
import static com.losandes.utils.Constants.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Abstract operating system configurator that implements a set ot utility methods to be used by virtual machine configurations
 * @author Clouder
 */
public abstract class AbstractSOConfigurator {

    /**
     * The port used to communicate with unacloud client
     */
    private int clouderClientPort;

    /**
     * The port used to transfer files to unacloud client
     */
    private int fileTransferSocket;

    /**
     * Virtual machine representing the one that must be configured by this abstract configurator
     */
    protected VirtualMachine virtualMachine;

    /**
     * Host table to be configured on this abstract configurator managed virtual machine
     */
    protected HostTable hosts;

    /**
     * MAC address to be configured on the managed virtual machine
     */
    protected String mac;

    /**
     * Temporal file used to write volatile data
     */
    protected File archivoTemporal;

    /**
     * virtual machine executions services used to report configuration process status
     */
    protected IVirtualMachineExecutionServices virtualMachineExecutionServices;

    /**
     * Inits this abstract configurator with the given ports
     * @param clouderClientPort
     * @param fileTransferSocket
     */
    public void init(int clouderClientPort,int fileTransferSocket){
        this.clouderClientPort=clouderClientPort;
        this.fileTransferSocket=fileTransferSocket;
    }

    /**
     * Inits this abstract configurator with the given virtual machine and virtual machine host table
     * @param virtualMachine
     * @param hosts
     */
    protected void init(VirtualMachine virtualMachine, HostTable hosts) {
        this.virtualMachine = virtualMachine;
        this.hosts = hosts;
        archivoTemporal = new File("./data/temp" + virtualMachine.getPhysicalMachine().getPhysicalMachineIP() + ".txt");
        archivoTemporal.getParentFile().mkdirs();
    }

    /**
     * Changes the MAC address of the virtual machine configured by this abstract configurator
     * @param prefijo
     */
    protected void changeMac(String prefijo) {
        try {
            SecureSocket socket = new SecureSocket(virtualMachine.getPhysicalMachine().getPhysicalMachineIP(),clouderClientPort);
            AbstractCommunicator communication = socket.connect();
            communication.writeUTF(new String[]{"" + VIRTUAL_MACHINE_CONFIGURATION, "CambiarMac", virtualMachine.getVirtualMachinePath(), prefijo, virtualMachine.getPhysicalMachine().getPhysicalMachineHypervisorPath()});
            mac = communication.readUTF().substring(prefijo.length());
            mac = mac.replace(" ", "").replace("=", "").toUpperCase();
            System.out.println(mac);
            communication.close();
        } catch (ConnectionException ex) {
            Logger.getLogger(AbstractSOConfigurator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns the MAC address that is configured on the managed virtual machine
     * @param prefijo
     */
    protected void getMac(String prefijo) {
        try {
            SecureSocket socket = new SecureSocket(virtualMachine.getPhysicalMachine().getPhysicalMachineIP(),clouderClientPort);
            AbstractCommunicator communication = socket.connect();
            communication.writeUTF(new String[]{"" + VIRTUAL_MACHINE_CONFIGURATION, "SacarMac", virtualMachine.getVirtualMachinePath(), prefijo});
            mac = communication.readUTF().substring(prefijo.length());
            mac = mac.replace(" ", "").replace("=", "").toUpperCase();
            System.out.println(mac);
            communication.close();
        } catch (ConnectionException ex) {
            Logger.getLogger(AbstractSOConfigurator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Writes the content of the temporal file on the given path in the managed virtual machine
     * @param path
     */
    public void writeMachineFile(String path) {
        try {
            SecureSocket socket = new SecureSocket(virtualMachine.getPhysicalMachine().getPhysicalMachineIP(),clouderClientPort);
            AbstractCommunicator communication = socket.connect();
            communication.writeUTF("" + VIRTUAL_MACHINE_CONFIGURATION, "EscribirArchivoMaquina", virtualMachine.getHypervisor().getHypervisorCode().intValue()+"", virtualMachine.getVirtualMachinePath(),
                    virtualMachine.getVirtualMachineSecurity().getVirtualMachineSecurityUser(),
                    virtualMachine.getVirtualMachineSecurity().getVirtualMachineSecurityPassword(),
                    path, virtualMachine.getPhysicalMachine().getPhysicalMachineHypervisorPath());
            communication.readUTF();
            try {
                UnicastSender.sendFile(virtualMachine.getPhysicalMachine().getPhysicalMachineIP(), archivoTemporal,fileTransferSocket);
            } catch (Exception e) {
                e.printStackTrace();
            }
            communication.readUTF();
            communication.close();
        } catch (ConnectionException ex) {
            Logger.getLogger(AbstractSOConfigurator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Starts the managed virtual machine
     */
    public void start() {
        try {
            SecureSocket socket = new SecureSocket(virtualMachine.getPhysicalMachine().getPhysicalMachineIP(),clouderClientPort);
            AbstractCommunicator communication = socket.connect();
            communication.writeUTF("" + VIRTUAL_MACHINE_CONFIGURATION, "StartMachine", virtualMachine.getHypervisor().getHypervisorCode().intValue()+"", virtualMachine.getVirtualMachinePath(), virtualMachine.getPhysicalMachine().getPhysicalMachineHypervisorPath());
            communication.readUTF();
            communication.close();
            Thread.sleep(30000);
        } catch (Exception ex) {
            Logger.getLogger(AbstractSOConfigurator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Stops the managed virtual machine
     */
    public void stop() {
        try {
            SecureSocket socket = new SecureSocket(virtualMachine.getPhysicalMachine().getPhysicalMachineIP(),clouderClientPort);
            AbstractCommunicator communication = socket.connect();
            communication.writeUTF(new String[]{"" + VIRTUAL_MACHINE_CONFIGURATION, "StopMachine", virtualMachine.getHypervisor().getHypervisorCode().intValue()+"",virtualMachine.getVirtualMachinePath(), virtualMachine.getPhysicalMachine().getPhysicalMachineHypervisorPath()});
            communication.readUTF();
            communication.close();
        } catch (ConnectionException ex) {
            Logger.getLogger(AbstractSOConfigurator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Execute a list of commands on the managed virtual machine
     * @param comandos
     */
    public void executeCommandInMachine(String... comandos) {
        try {
            SecureSocket socket = new SecureSocket(virtualMachine.getPhysicalMachine().getPhysicalMachineIP(),clouderClientPort);
            AbstractCommunicator communication = socket.connect();
            String[] peticion = new String[8 + comandos.length];
            peticion[0] = "" + VIRTUAL_MACHINE_CONFIGURATION;
            peticion[1] = "comandMachine";
            peticion[2] = virtualMachine.getHypervisor().getHypervisorCode().intValue()+"";
            peticion[3] = virtualMachine.getVirtualMachinePath();
            peticion[4] = virtualMachine.getVirtualMachineSecurity().getVirtualMachineSecurityUser();
            peticion[5] = virtualMachine.getVirtualMachineSecurity().getVirtualMachineSecurityPassword();
            peticion[6] = virtualMachine.getPhysicalMachine().getPhysicalMachineHypervisorPath();
            peticion[7] = "" + comandos.length;
            for (int e = 0; e < comandos.length; e++) {
                peticion[8 + e] = comandos[e];
            }
            communication.writeUTF(peticion);
            communication.readUTF();
            communication.close();
        } catch (ConnectionException ex) {
            Logger.getLogger(AbstractSOConfigurator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This method is responsible to configure the ip of the managed virtual machine
     * @param netmask
     * @param ip
     */
    public abstract void configureIP(String netmask, String ip);

    /**
     * This method is responsible to configure a DHCP client on the managed virtual machine
     */
    public abstract void configureDHCP();

    /**
     * This method is responsible to change the MAC address of the managed virtual machine
     */
    public abstract void changeMac();

    /**
     * This method is responsible to get the MAC address of the managed virtual machine
     */
    public abstract void getMac();

    /**
     * This method is responsible to configure the host name of the managed virtual machine
     */
    public abstract void configureHostName();

    /**
     * This method is responsible to configure the host table of the managed virtual machine
     */
    public abstract void configureHostTable();

    /**
     * This method is responsible to get the IP address of the managed virtual machine
     */
    public abstract void getIp();

    /**
     * Sets the instance of virtual machine executions services to be used by this abstract configurator
     * @param vmes
     */
    public void setVirtualMachineExecutionServices(IVirtualMachineExecutionServices vmes){
        virtualMachineExecutionServices=vmes;
    }
}
