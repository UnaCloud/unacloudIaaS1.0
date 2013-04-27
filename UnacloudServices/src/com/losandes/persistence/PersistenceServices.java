/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * $Id$ SecurityServiceTest.java
 * Universidad de los Andes (Bogotá - Colombia)
 * Departamento de Ingeniería de Sistemas y Computación
 * Licenciado bajo el esquema Academic Free License version 2.1
 *
 * Ejercicio: Muebles los Alpes
 * Autor: German Sotelo
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
package com.losandes.persistence;
import com.losandes.beans.*;
import com.losandes.utils.VirtualMachineCPUStates;
import java.util.Properties;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.util.*;
import javax.persistence.FlushModeType;
import unacloudws.UnaCloudOperations;
import static com.losandes.utils.Constants.*;
/**
 * Prueba la implementacion de la interfaz SecurityServiceTest proveida por la aplicación
 * @author German Sotelo
 */
public class PersistenceServices {

    EntityManager em;
    /**
     * Constructor de la prueba
     * Pide la instancia de la unidad de persistencia a la aplicación
     */
    public PersistenceServices(){
        em =Persistence.createEntityManagerFactory("UnacloudServicesPU").createEntityManager();
    }

    public void updatePhysicalMachineState(int state,String ... machineIds){
        for(String machineId:machineIds){
            updatePhysicalMachine(state, machineId);
        }
    }

    private void updatePhysicalMachine(int state,String machineId) {
        Physicalmachine p = (Physicalmachine)em.find(Physicalmachine.class, machineId);
        if(p==null){
            System.out.println("Not found");
            return;
        }
        em.getTransaction().begin();
        p.setPhysicalmachinestate(state);
        em.merge(p);
        em.flush();
        em.getTransaction().commit();
    }

    public void logginPhysicalMachineUser(Object machineId, String user) {
        Physicalmachine p = (Physicalmachine)em.find(Physicalmachine.class, machineId);
        em.getTransaction().begin();
        if(p==null){
            p=new Physicalmachine();
            p.setPhysicalmachinename((String)machineId);
            em.persist(p);
            em.flush();
            return;
        }
        p.setPhysicalmachineuser(user);
        p.setPhysicalmachinestate(ON_STATE);
        em.merge(p);
        em.flush();
        em.getTransaction().commit();
    }

    public void updateVirtualMachineState(Object virtualMachineExecutionCode, int state, String message) {
        Virtualmachineexecution vme = (Virtualmachineexecution)em.find(Virtualmachineexecution.class,virtualMachineExecutionCode);
        if(vme!=null){
            em.getTransaction().begin();
            if(state==OFF_STATE)vme.getVirtualmachine().getPhysicalmachine().setPhysicalmachinestate(VM_TURN_ON);
            if(state==ERROR_STATE)vme.getVirtualmachine().getPhysicalmachine().setPhysicalmachinestate(VM_TURN_ON);
            if(state==ON_STATE){
                vme.getVirtualmachine().setVirtualmachinestate(ON_STATE);
                em.merge(vme.getVirtualmachine());
            }
            vme.setVirtualmachineexecutionstatus(state);
            vme.setVirtualmachineexecutionstatusmessage(message);
            em.merge(vme);
            em.flush();
            em.getTransaction().commit();
        }
    }

    public void updateVirtualMachineCPUState(Object virtualMachineExecutionCode, VirtualMachineCPUStates cpuState) {
        Virtualmachineexecution vme = (Virtualmachineexecution)em.find(Virtualmachineexecution.class,virtualMachineExecutionCode);
        if(vme!=null){
            em.getTransaction().begin();
            vme.getVirtualmachine().setCpustate(cpuState.ordinal());
            em.merge(vme.getVirtualmachine());
            em.flush();
            em.getTransaction().commit();
            Elasticrule er = vme.getTemplate().getElasticrule();
            if(er!=null&&er.getActive()){
                Collection<Virtualmachine> vms = vme.getTemplate().getVirtualmachineCollection();
                int busy=0,on=0;
                for(Virtualmachine vm:vms){
                    if(vm.getVirtualmachinestate()!=OFF_STATE){
                        on++;
                        if(vm.getCpustate().intValue()==VirtualMachineCPUStates.BUSY.ordinal())busy++;
                    }
                }
                int perc=((busy*100)/on);
                System.out.println("busy %= "+busy+"/"+on);
                if(er.getLowerlimit()>perc){
                    int t = 0;
                    if(er.getAddition())t=er.getFactor().intValue();
                    else if(er.getMultiply())t=(int)(on/er.getFactor());
                    t=Math.min(on,t);

                }else if(er.getUpperlimit()<perc){
                    int t = 0;
                    if(er.getAddition())t=er.getFactor().intValue();
                    else if(er.getMultiply()){
                        t=(int)(on*er.getFactor())-on;
                    }
                    t=Math.min(vms.size()-on,t);
                    System.out.println("Prender "+t);
                    UnaCloudOperations.turnOnVirtualCluster(vme.getSystemuser().getSystemusername(),"",vme.getTemplate().getTemplatecode(),t,vme.getVirtualmachineexecutionrammemory(),vme.getVirtualmachineexecutioncores(),0,20);
                }
            }
            
        }
        System.out.println(virtualMachineExecutionCode+" is "+cpuState);
    }

    public String[] getAllPhysicalMachines() {
        List l = em.createNativeQuery("SELECT pm.* FROM PhysicalMachine pm ;",Physicalmachine.class).setHint("toplink.refresh", "true").getResultList();
        String[] ret = new String[l.size()];
        for(int e=0;e<l.size();e++)ret[e]=((Physicalmachine)l.get(e)).getPhysicalmachinename();
        return ret;
    }



}