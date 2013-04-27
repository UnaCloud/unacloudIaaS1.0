/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package administration;

import com.losandes.persistence.entity.PhysicalMachine;
import com.losandes.persistence.entity.VirtualMachine;
import com.losandes.persistence.entity.VirtualMachineExecution;
import com.losandes.physicalmachine.IPhysicalMachineServices;
import com.losandes.virtualmachine.IVirtualMachineServices;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import org.apache.commons.collections.map.LinkedMap;
import org.primefaces.model.chart.*;
import static com.losandes.utils.Constants.*;

/**
 *
 * @author Clouder
 */
public class StaticticsBean {

    @EJB
    private IVirtualMachineServices virtualMachineServices;
    @EJB
    private IPhysicalMachineServices physicalMachineServices;
    private PieChartModel pieModelUsedCores;
    private PieChartModel pieModelUsedCoresByUsers;
    private PieChartModel pieModelUsedCoresByTemplate;
    private PieChartModel pieModelUsedRAM;
    private PieChartModel pieModelUsedRAMByUsers;
    private PieChartModel pieModelUsedRAMByTemplate;

    public StaticticsBean() {
    }

    @PostConstruct
    public void init() {
        List<VirtualMachine> vms = virtualMachineServices.getAllOnVirtualMachines();
        List<PhysicalMachine> pms = physicalMachineServices.getPhysicalMachines();
        Map<String, Number> users = new TreeMap<String, Number>();
        Map<String, Number> templates = new TreeMap<String, Number>();
        Map<String, Number> usersRam = new TreeMap<String, Number>();
        Map<String, Number> templatesRam = new TreeMap<String, Number>();
        int aviableCores = 0, usedCores = 0, unaviableCores = 0;
        int aviableRam = 0, usedRam = 0, unaviableRam = 0;
        for (PhysicalMachine pm : pms) {
            if (pm.getPhysicalMachineState() == ON_STATE) {
                aviableCores += pm.getPhysicalMachineCores();
                aviableRam += pm.getPhysicalMachineRAMMemory();
            }
            if (pm.getPhysicalMachineState() == OFF_STATE) {
                unaviableCores += pm.getPhysicalMachineCores();
                unaviableRam += pm.getPhysicalMachineRAMMemory();
            }
        }
        for (VirtualMachine vm : vms) {
            aviableCores -= vm.getVirtualMachineCores();
            aviableRam -= vm.getVirtualMachineRAMMemory();
            usedCores += vm.getVirtualMachineCores();
            usedRam += vm.getVirtualMachineRAMMemory();
            String user = "";
            for (VirtualMachineExecution vme : vm.getVirtualMachineExecutions()) {
                if (vme.getVirtualMachineExecutionStatus() != OFF_STATE) {
                    user = vme.getSystemUser().getSystemUserName();
                }

            }
            if (!user.isEmpty()) {
                Number i = users.get(user);
                users.put(user, (i == null ? 0 : i.intValue()) + vm.getVirtualMachineCores());
                i = usersRam.get(user);
                usersRam.put(user, (i == null ? 0 : i.intValue()) + vm.getVirtualMachineRAMMemory());
            }
            Number i = templates.get(vm.getTemplate().getTemplateName());
            templates.put(vm.getTemplate().getTemplateName(), (i == null ? 0 : i.intValue()) + vm.getVirtualMachineCores());
            i = templatesRam.get(vm.getTemplate().getTemplateName());
            templatesRam.put(vm.getTemplate().getTemplateName(), (i == null ? 0 : i.intValue()) + vm.getVirtualMachineRAMMemory());
        }
        users = fixModel(aviableCores, unaviableCores,users,1);
        usersRam = fixModel(aviableRam, unaviableRam,usersRam,1024);
        templates = fixModel(aviableCores, unaviableCores,templates,1);
        templatesRam = fixModel(aviableRam, unaviableRam,templatesRam,1024);
        pieModelUsedCoresByUsers = new PieChartModel(users);
        pieModelUsedCoresByTemplate = new PieChartModel(templates);
        pieModelUsedRAMByUsers = new PieChartModel(usersRam);
        pieModelUsedRAMByTemplate = new PieChartModel(templatesRam);
        pieModelUsedCores = new PieChartModel();
        pieModelUsedCores.set("Unavailable: " + unaviableCores, unaviableCores);
        pieModelUsedCores.set("Available: " + aviableCores, aviableCores);
        pieModelUsedCores.set("Used: " + usedCores, usedCores);
        pieModelUsedRAM = new PieChartModel();
        pieModelUsedRAM.set("Unavailable: " + unaviableRam / 1024, unaviableRam / 1024);
        pieModelUsedRAM.set("Available: " + aviableRam / 1024, aviableRam / 1024);
        pieModelUsedRAM.set("Used: " + usedRam / 1024, usedRam / 1024);
    }

    private Map fixModel(int available,int unavailable,Map<String,Number> mapa,int div) {
        LinkedMap map = new LinkedMap();
        map.put("Unavailable: " + unavailable/div, unavailable/div);
        map.put("Available: " + available/div, available/div);
        for (Map.Entry<String, Number> e : mapa.entrySet()) {
            map.put(e.getKey() + ": " + e.getValue().intValue()/div, e.getValue().intValue()/div);
        }
        return map;
    }

    public PieChartModel getPieModelUsedCores() {
        return pieModelUsedCores;
    }

    public PieChartModel getPieModelUsedCoresByTemplate() {
        return pieModelUsedCoresByTemplate;
    }

    public PieChartModel getPieModelUsedCoresByUsers() {
        return pieModelUsedCoresByUsers;
    }

    public PieChartModel getPieModelUsedRAM() {
        return pieModelUsedRAM;
    }

    public PieChartModel getPieModelUsedRAMByTemplate() {
        return pieModelUsedRAMByTemplate;
    }

    public PieChartModel getPieModelUsedRAMByUsers() {
        return pieModelUsedRAMByUsers;
    }



}
