/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.losandes.virtualmachine;

import com.losandes.persistence.entity.*;
import com.losandes.virtualmachineexecution.IVirtualMachineExecutionServices;
import java.io.ByteArrayInputStream;
import java.util.*;
import java.util.ArrayList;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.richfaces.component.SortOrder;

/**
 *
 * @author Clouder
 */
public class ExecutionBean {

    @EJB
    private IVirtualMachineServices virtualMachineServices;
    @EJB
    private VirtualMachineOperationsLocal virtualMachineOperations;
    @EJB
    private IVirtualMachineExecutionServices virtualMachineExecutionServices;
    private List<VirtualMachineExecution> virtualMachineExecution;
    private Map<String, VirtualMachineExecution> virtualMachineExecutionMap;
    private int id;
    private String exId;
    private int extendExecutionTime;
    private boolean selectAll=false;
    private boolean viewAll=false;
    private SortOrder nameSortOrder = SortOrder.ascending;
    private String user;
    /** Creates a new instance of ExecutionBean */
    public ExecutionBean() {
        virtualMachineExecutionMap = new TreeMap<String, VirtualMachineExecution>();
        //tblObject
    }

    public List<VirtualMachineExecution> getAllUserVirtualMachines() {
        if(user==null)user=FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
        List<VirtualMachineExecution> temp = viewAll?virtualMachineServices.getAllVirtualMachineExecutions():virtualMachineServices.getVirtualMachineExecutions(user);
        if (virtualMachineExecution == null) {
            setAllUserVirtualMachines(temp);
        } else if (temp.size() != virtualMachineExecutionMap.size()) {
            setAllUserVirtualMachines(temp);
        } else {
            for (VirtualMachineExecution vme : temp) {
                VirtualMachineExecution vm = virtualMachineExecutionMap.get(vme.getVirtualMachineExecutionCode());
                if (vm == null) {
                    setAllUserVirtualMachines(temp);
                    break;
                } else {
                    vm.setVirtualMachineExecutionStatus(vme.getVirtualMachineExecutionStatus());
                    vm.setVirtualMachineExecutionStatusMessage(vme.getVirtualMachineExecutionStatusMessage());
                    vm.setShowProgressBar(vme.isShowProgressBar());
                    vm.setIsPercentage(vme.isIsPercentage());
                    vm.setCurrent(vme.getCurrent());
                    vm.setMax(vme.getMax());
                }
            }
        }
        return virtualMachineExecution;
    }

    private void setAllUserVirtualMachines(List<VirtualMachineExecution> temp) {
        virtualMachineExecution = temp;
        virtualMachineExecutionMap.clear();
        for (VirtualMachineExecution vme : virtualMachineExecution) {
            virtualMachineExecutionMap.put(vme.getVirtualMachineExecutionCode(), vme);
        }
    }

    /**
     * @return the virtualMachineExecution
     */
    public List<VirtualMachineExecution> getVirtualMachineExecution() {
        return virtualMachineExecution;
    }

    /**
     * @param virtualMachineExecution the virtualMachineExecution to set
     */
    public void setVirtualMachineExecution(List<VirtualMachineExecution> virtualMachineExecution) {
        this.virtualMachineExecution = virtualMachineExecution;
    }

    public String findByID() {
        //virtualMachine = virtualMachineServices.getVirtualMachineByID(id);
        return null;
    }

    public String turnOffUserVirtualMachine() {
        List<VirtualMachineExecution> vmes = getSelectedVirtualMachines();
        List<String> vmsIds = new ArrayList<String>();
        for(VirtualMachineExecution vme:vmes)vmsIds.add(vme.getVirtualMachineExecutionCode());
        virtualMachineServices.turnOffVirtualMachineCluster(vmsIds);
        return "CloudManagement";
    }

    public String restartUserVirtualMachine() {
        virtualMachineServices.restartVirtualMachineCluster(getSelectedVirtualMachines());
        return "CloudManagement";
    }

    public String monitorUserVirtualMachine() {
        return "ViewVirtualMachineCloud";
    }

    public String timeUserVirtualMachine() {
        virtualMachineExecutionServices.getVirtualMachineExecutionByID(exId);
        return "TimeVirtualMachineCloud";
    }

    public String getExId() {
        return exId;
    }

    public void setExId(String exId) {
        this.exId = exId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getExtendExecutionTime() {
        return extendExecutionTime;
    }

    public void setExtendExecutionTime(int extendExecutionTime) {
        this.extendExecutionTime = extendExecutionTime;
    }

    public String extendClusterExecutionTime() {
        virtualMachineServices.extendVirtualMachineExecutionTime(extendExecutionTime, getSelectedVirtualMachines().toArray(new VirtualMachineExecution[0]));
        return "CloudManagement";
    }

    public String storeVirtualMachine() {
        virtualMachineOperations.retrieveVirtualMachine(virtualMachineServices.getVirtualMachineByID(id), FacesContext.getCurrentInstance().getExternalContext().getRemoteUser());
        return "CloudManagement";
    }

    public List<VirtualMachineExecution> getSelectedVirtualMachines() {
        List<VirtualMachineExecution> vmes = new ArrayList<VirtualMachineExecution>();
        for (VirtualMachineExecution vme : virtualMachineExecutionMap.values()) {
            if (vme.isSelected()) {
                vmes.add(vme);
            }
        }
        return vmes;
    }

    public SortOrder getNameSortOrder() {
        return nameSortOrder;
    }

    public void setNameSortOrder(SortOrder nameSortOrder) {
        this.nameSortOrder = nameSortOrder;
    }

    public void sortByName() {
        if (nameSortOrder.equals(SortOrder.ascending)) {
            setNameSortOrder(SortOrder.descending);
        } else {
            setNameSortOrder(SortOrder.ascending);
        }
    }

    public StreamedContent getFile() {
        String h = "";
        for (VirtualMachineExecution vme : virtualMachineExecution) {
            if (vme.getVirtualMachineExecutionStatus() == 1) {
                h += vme.getVirtualMachine().getVirtualMachineName() + "\t" + vme.getVirtualMachine().getVirtualMachineIP() + "\r\n";
            }
        }
        StreamedContent f = new DefaultStreamedContent(new ByteArrayInputStream(h.getBytes()), "text/plain", "list.txt");
        return f;
    }

    public void selectAllComponents(ValueChangeEvent event) {
        for(VirtualMachineExecution vme :virtualMachineExecution){
            vme.setSelected(!selectAll);
        }
    }

    public boolean isSelectAll() {
        return selectAll;
    }

    public void setSelectAll(boolean selectAll) {
        this.selectAll = selectAll;
    }

    public boolean isViewAll() {
        return viewAll;
    }

    public void setViewAll(boolean viewAll) {
        this.viewAll = viewAll;
    }

}
