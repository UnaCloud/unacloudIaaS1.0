/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.losandes.machineconfigurators;

import com.losandes.deploy.IPGenerationPolicy;
import com.losandes.persistence.entity.VirtualMachine;
import com.losandes.utils.AddressUtility;
import static com.losandes.utils.Constants.*;
import com.losandes.virtualmachine.PairMachineExecution;
import com.losandes.vo.HostTable;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * Class responsible to implement methods to configure Scientific Linux virtual machines
 * @author Clouder
 */
public class ScientificLinux extends AbstractSOConfigurator implements MachineConfigurator {

    /**
     * Configures the ip address of the Scientific Linux managed virtual machine
     * @param netmask
     * @param ip
     */
    @Override
    public void configureIP(String netmask, String ip) {
        AddressUtility au = new AddressUtility(ip, netmask);
        try {
            PrintWriter pw = new LinuxPrintWriter(archivoTemporal);
            pw.println("# Advanced Micro Devices [AMD] 79c970 [PCnet32 LANCE]");
            pw.println("DEVICE=eth0");
            pw.println("BOOTPROTO=none");
            pw.println("ONBOOT=yes");
            pw.println("NETMASK=" + au.getNetmask());
            pw.println("IPADDR=" + au.getIp());
            pw.println("GATEWAY=" + au.getGateway());
            pw.println("TYPE=Ethernet");
            pw.println("USERCTL=no");
            pw.println("IPV6INIT=no");
            pw.println("PEERDNS=yes");
            pw.close();
            writeMachineFile("/etc/sysconfig/networking/profiles/default/ifcfg-eth0");
            executeCommandInMachine("/sbin/ifdown eth0", "/sbin/ifup eth0");
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Configures a DHCP client of the Scientific Linux managed virtual machine
     */
    @Override
    public void configureDHCP() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Change the mac address of the Scientific Linux managed virtual machine
     */
    @Override
    public void changeMac() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Get the mac address of the Scientific Linux managed virtual machine
     */
    @Override
    public void getMac() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Configures the host name of the Scientific Linux managed virtual machine
     */
    @Override
    public void configureHostName() {
        try {
            PrintWriter pw = new LinuxPrintWriter(archivoTemporal);
            pw.println("NETWORKING=yes");
            pw.println("NETWORKING_IPV6=no");
            pw.println("HOSTNAME=" + virtualMachine.getVirtualMachineName());
            pw.close();
            writeMachineFile("/etc/sysconfig/network");
            executeCommandInMachine("/bin/hostname " + virtualMachine.getVirtualMachineName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Configure the host table of the Scientific Linux managed virtual machine
     */
    @Override
    public void configureHostTable() {
        try {
            PrintWriter pw = new LinuxPrintWriter(archivoTemporal);
            pw.println("127.0.0.1 localhost");
            for (int e = 0; e < hosts.size(); e++) {
                pw.println(hosts.get(e).ip + " " + hosts.get(e).hostName);
            }
            writeMachineFile("/etc/hosts");
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Return the IP address of the Scientific Linux managed virtual machine
     */
    @Override
    public void getIp() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Configures this Scientific Linux managed virtual machine
     * @param virtualMachineExecution
     * @param hosts
     * @param shutdown
     */
    public void configureMachine(PairMachineExecution virtualMachineExecution, HostTable hosts, boolean shutdown) {
        virtualMachineExecution.getExecution().setIsPercentage(false);
        virtualMachineExecution.getExecution().setVirtualMachineExecutionStatus(COPYING_STATE);
        virtualMachineExecution.getExecution().setShowProgressBar(true);
        virtualMachineExecution.getExecution().setVirtualMachineExecutionStatusMessage("Starting configuration");
        virtualMachineExecution.getExecution().setMax(3);
        virtualMachineExecution.getExecution().setCurrent(1);
        if(virtualMachineExecutionServices!=null)virtualMachineExecutionServices.updateVirtualMachineExecution(virtualMachineExecution.getExecution());

        VirtualMachine virtualMachine = virtualMachineExecution.getVirtualMachine();
        init(virtualMachine, hosts);
        /*if(virtualMachine.getMacPolicy()==MACGenerationPolicy.RANDOM)changeMac();
        else if(virtualMachine.getMacPolicy()==MACGenerationPolicy.STATIC_MACHINE_BASED);*///Completar
        start();
        virtualMachineExecution.getExecution().setVirtualMachineExecutionStatusMessage("Setting hostname");
        virtualMachineExecution.getExecution().setCurrent(2);
        if(virtualMachineExecutionServices!=null)virtualMachineExecutionServices.updateVirtualMachineExecution(virtualMachineExecution.getExecution());
        configureHostName();
        virtualMachineExecution.getExecution().setVirtualMachineExecutionStatusMessage("Setting IP");
        virtualMachineExecution.getExecution().setCurrent(3);
        if(virtualMachineExecutionServices!=null)virtualMachineExecutionServices.updateVirtualMachineExecution(virtualMachineExecution.getExecution());
        if (virtualMachine.getIpPolicy() == IPGenerationPolicy.PUBLIC_MACHINE_BASED) {
            configureIP(virtualMachine.getPhysicalMachine().getPhysicalMachineVirtualNetmask(), virtualMachine.getVirtualMachineIP());
        } else if (virtualMachine.getIpPolicy() == IPGenerationPolicy.DHCP) {
            configureDHCP();//Completar
        } else if (virtualMachine.getIpPolicy() == IPGenerationPolicy.PRIVATE) {
            configureIP(virtualMachine.getPhysicalMachine().getPhysicalMachineVirtualNetmask(), virtualMachine.getVirtualMachineIP());//Completar
        }
        /*System.out.println(virtualMachine.getTemplate().getTemplateType().getTemplateTypeName());
        if(virtualMachine.getTemplate().getTemplateType().getTemplateTypeName().toLowerCase().equals("grid")){
        configureHostTable();
        //Configurar enlaces lógicos
        }*/
        if (shutdown) {
            stop();
        }
    }

    public String getIP(VirtualMachine virtualMachine) {
        throw new UnsupportedOperationException("Not supported yet.");
    }


}
