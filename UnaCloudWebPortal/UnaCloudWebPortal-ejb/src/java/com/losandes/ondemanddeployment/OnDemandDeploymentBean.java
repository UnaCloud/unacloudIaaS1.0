/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.losandes.ondemanddeployment;

import com.losandes.persistence.IPersistenceServices;
import com.losandes.persistence.entity.PhysicalMachine;
import com.losandes.persistence.entity.VirtualMachine;
import com.losandes.user.IUserServices;
import com.losandes.utils.Queries;
import com.losandes.virtualmachine.VirtualMachineOperationsLocal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author Clouder
 */
@Deprecated
@Stateless
public class OnDemandDeploymentBean implements OnDemandDeploymentLocal {

    @EJB
    private IPersistenceServices persistence;
    @EJB
    private IUserServices userServices;
    @EJB
    private VirtualMachineOperationsLocal vmServices;

    public OnDemandDeploymentBean() {
    }

    public void deployMachine(String route, int numberMachines, String userName, String passwd,int executionHours, int cores, int ram) {
        /*System.out.println("WS");
        List vms =  persistence.executeNativeSQLList(Queries.getVirtualMachine(route,userName),VirtualMachine.class);
        Collections.sort(vms,new Comparator<VirtualMachine>(){
            public int compare(VirtualMachine o1, VirtualMachine o2) {
                PhysicalMachine p1 = o1.getPhysicalMachine();
                PhysicalMachine p2 = o2.getPhysicalMachine();
                int a = Double.compare(p1.getExpectedFailures(),p2.getExpectedFailures());
                return a==0?Double.compare(p2.getAverageAvailability(),p1.getAverageAvailability()):a;
            }
        });
        ArrayList<VirtualMachine> vmPrender=new ArrayList<VirtualMachine>(numberMachines);
        for(int e=0,i=Math.min(vms.size(),numberMachines);e<i;e++)
            vmPrender.add((VirtualMachine)vms.get(e));
        System.out.println("Levantar WS: "+vmPrender.size());
        for(VirtualMachine v:vmPrender)System.out.println(v.getVirtualMachineName()+" "+v.getPhysicalMachine().getPhysicalMachineName());
        System.out.println(executionHours+" "+userName);
        vmServices.turnOnCluster(cores, ram, executionHours, userServices.getUserByID(userName), vmPrender.toArray(new VirtualMachine[0]));*/
    }

}

