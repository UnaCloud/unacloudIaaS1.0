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
import static com.losandes.utils.Constants.*;

/**
 *
 * @author Clouder
 */
public class Windows7 extends AbstractSOConfigurator implements MachineConfigurator {

    @Override
    public void configureIP(String netmask, String ip) {
        AddressUtility au = new AddressUtility(ip, netmask);
        executeCommandInMachine("netsh.exe interface ip set address \"name=Conexión de área local\" static "+au.getIp()+" "+au.getNetmask()+" "+au.getGateway()+" 1");
    }

    @Override
    public void configureDHCP() {
    }

    @Override
    public void changeMac() {
        
    }

    @Override
    public void getMac() {
        
    }

    @Override
    public void configureHostName() {
        //executeCommandInMachine("reg.exe add HKEY_LOCAL_MACHINE\\SYSTEM\\CurrentControlSet\\Control\\ComputerName\\ComputerName /v ComputerName /t REG_SZ /d \""+virtualMachine.getVirtualMachineName()+"\" /f","reg.exe add HKEY_LOCAL_MACHINE\\SYSTEM\\ControlSet002\\services\\Tcpip\\Parameters /v \"NV Hostname\" /t REG_SZ /d \""+virtualMachine.getVirtualMachineName()+"\" /f","reg.exe add HKEY_LOCAL_MACHINE\\SYSTEM\\ControlSet002\\services\\Tcpip\\Parameters /v Hostname /t REG_SZ /d \""+virtualMachine.getVirtualMachineName()+"\" /f");
    }

    @Override
    public void configureHostTable() {
        
    }

    @Override
    public void getIp() {
        
    }

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
        if (shutdown) {
            stop();
        }
    }

    public String getIP(VirtualMachine virtualMachine) {
        return "Operation not implemented";
    }

}
