/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.losandes.machineconfigurators;

import com.losandes.deploy.IPGenerationPolicy;
import com.losandes.persistence.entity.VirtualMachine;
import com.losandes.utils.AddressUtility;
import com.losandes.virtualmachine.PairMachineExecution;
import com.losandes.vo.HostTable;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import static com.losandes.utils.Constants.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Clouder
 */
public class Ubuntu extends AbstractSOConfigurator implements MachineConfigurator {

    /**
     * Configures the ip address of the Ubuntu managed virtual machine
     * @param netmask
     * @param ip
     */
    @Override
    public void configureIP(String netmask, String ip) {
        AddressUtility au = new AddressUtility(ip, netmask);
        try {
            PrintWriter pw = new LinuxPrintWriter(archivoTemporal);
            pw.println("auto lo");
            pw.println("iface lo inet loopback");
            pw.println("auto eth0");
            pw.println("iface eth0 inet static");
            pw.println("address " + au.getIp());
            pw.println("netmask " + au.getNetmask());
            pw.println("network " + au.getNetwork());
            pw.println("broadcast " + au.getBroadcast());
            pw.println("gateway " + au.getGateway());
            pw.close();
            writeMachineFile("/etc/network/interfaces");
            pw = new LinuxPrintWriter(archivoTemporal);
            pw.println("/bin/rm /etc/udev/rules.d/*net.rules");
            pw.close();
            writeMachineFile("/unacloud.sh");
            executeCommandInMachine("/bin/sh /unacloud.sh","/sbin/ifdown eth0","/sbin/ifup eth0");
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Configures a DHCP client of the Ubuntu managed virtual machine
     */
    @Override
    public void configureDHCP() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Change the mac address of the Ubuntu managed virtual machine
     */
    @Override
    public void changeMac() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Get the mac address of the Ubuntu managed virtual machine
     */
    @Override
    public void getMac() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Configures the host name of the Ubuntu managed virtual machine
     */
    @Override
    public void configureHostName() {
        try {
            PrintWriter pw = new LinuxPrintWriter(archivoTemporal);
            pw.println("" + virtualMachine.getVirtualMachineName());
            pw.close();
            writeMachineFile("/etc/hostname");

            executeCommandInMachine("/bin/hostname " + virtualMachine.getVirtualMachineName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Configure the host table of the Ubuntu managed virtual machine
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
     * Return the IP address of the Ubuntu managed virtual machine
     */
    @Override
    public void getIp() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Configures this Ubuntu managed virtual machine
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
        System.out.println("Start");
        start();
        System.out.println("Host name");
        virtualMachineExecution.getExecution().setVirtualMachineExecutionStatusMessage("Setting hostname");
        virtualMachineExecution.getExecution().setCurrent(2);
        if(virtualMachineExecutionServices!=null)virtualMachineExecutionServices.updateVirtualMachineExecution(virtualMachineExecution.getExecution());
        configureHostName();
        virtualMachineExecution.getExecution().setVirtualMachineExecutionStatusMessage("Setting IP");
        virtualMachineExecution.getExecution().setCurrent(3);
        if(virtualMachineExecutionServices!=null)virtualMachineExecutionServices.updateVirtualMachineExecution(virtualMachineExecution.getExecution());
        System.out.println(virtualMachine.getIpPolicy());
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
        try {
            Thread.sleep(60000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Ubuntu.class.getName()).log(Level.SEVERE, null, ex);
        }
        stop();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Ubuntu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getIP(VirtualMachine virtualMachine) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
