package com.losandes.virtualmachine;

import com.losandes.persistence.entity.*;
import java.util.*;
import javax.ejb.Local;

/**
 * @author Edgar Eduardo Rosales Rosero
 * Interface responsible for exposing the Virtual Machine services
 */
@Local
public interface IVirtualMachineServices {

    public boolean createVirtualMachine(com.losandes.persistence.entity.VirtualMachine machine);

    public boolean updateVirtualMachine(com.losandes.persistence.entity.VirtualMachine machine);

    public boolean deleteVirtualMachine(int code);

    public com.losandes.persistence.entity.VirtualMachine getVirtualMachineByID(int VirtualMachineCode);

    public java.util.List getVirtualMachines();

    public java.lang.String turnOnVirtualCluster(List<PhysicalMachine> physicalMachines, int executionTime, int templateSelected, int vmCores, int HDsize, int vmRAM, String userName);

    public VirtualMachineExecution[] turnOnVirtualClusterBySize(int template, int executionTime, int numberInstances, int vmCores, int HDsize, int vmRAM, String userName,boolean retry);
    
    //public java.lang.String restartVirtualMachine(java.util.ArrayList<PhysicalMachine> physicalMachines, java.lang.String virtualMachineName);

    public java.lang.String turnOffVirtualMachineCluster(List<String> virtualMachineExecution);

    public java.lang.String restartVirtualMachineCluster(List<VirtualMachineExecution> virtualMachineExecution);

    //public java.lang.String restartVirtualMachineCloud(com.losandes.persistence.entity.VirtualMachine virtualMachine);

    public java.util.List getAvailableVirtualHardDisk(int templateSelected);

    public java.util.List getAvailableVirtualMachines(int templateSelected, int virtualMachineDisk, int virtualMachineCores, int virtualMachineRAM);

    public java.util.List getVirtualMachineExecutions(String systemUserName);

    public java.util.List getAllVirtualMachineExecutions();

    public java.lang.String extendVirtualMachineExecutionTime(int executionTime,VirtualMachineExecution ... virtualMachineExecution);

    public com.losandes.persistence.entity.VirtualMachineExecution getVirtualMachineExecution(String systemUserName, int virtualMachineCode);

    public boolean updateVirtualMachineExecution(com.losandes.persistence.entity.VirtualMachineExecution virtualMachineExecution);

    public java.util.List getGridAvailableVirtualHardDisk(int templateSelected, String systemUserName);

    public java.util.List getGridAvailableVirtualMachines(int templateSelected, int virtualMachineDisk, int virtualMachineCores, int virtualMachineRAM, String systemUserName);

    public void setVirtualMachineExecutionState(int executionId, int state, String message);

    public List<VirtualMachine> getAllOnVirtualMachines();

    public void writeFileOnVirtualMachine(String virtualMachineExecutionId,String path,String content);
}// end of IVirtualMachineServices

