package com.losandes.operatingsystem;

import com.losandes.beans.IOperatingSystemServices;
import com.losandes.beans.IOperatingSystemServicesLocal;
import com.losandes.persistence.entity.OperatingSystem;
import com.losandes.persistence.entity.OperatingSystemType;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.model.SelectItem;
import static com.losandes.utils.Constants.*;

/**
 * @author Edgar Eduardo Rosales Rosero
 * Responsible for managing the OperatingSystem JSF
 */
public class OperatingSystemBean{
    @EJB
    private IOperatingSystemServicesLocal operatingSystemServices;
    private OperatingSystem operatingSystem;
    private OperatingSystemType operatingSystemType;
    private String operatingSystemError;
    private int id;

    /** Creates a new instance of OperatingSystemServices */
    public OperatingSystemBean() {
        operatingSystem = new OperatingSystem();
        operatingSystemType = new OperatingSystemType();
    }

    public SelectItem[] getOperatingSystemTypes() {
        List<OperatingSystemType> opts = operatingSystemServices.getOperatingSystemTypes();
        SelectItem[] items = new SelectItem[opts.size()];
        for (int i = 0; i < items.length; i++) {
            items[i] = new SelectItem(opts.get(i).getOperatingSystemTypeCode(), opts.get(i).getOperatingSystemTypeName());
        }
        return items;
    }

    public String createOrUpdate() {
        operatingSystem.setOperatingSystemType(operatingSystemType);
        if(operatingSystemType.getOperatingSystemTypeCode()==0)
        {
            operatingSystemError = ERROR_MESSAGE +"the operating system type must be selected";
            return null;
        }
        if (operatingSystem.getOperatingSystemCode() != 0) {
            if (!operatingSystemServices.updateOperatingSystem(operatingSystem)) {
                operatingSystemError = ERROR_MESSAGE +"the operating system were not updated";
            } else {
                operatingSystemError = "the operating system was modified";
            }
        } else {
            if (!operatingSystemServices.createOperatingSystem(operatingSystem)) {
                operatingSystemError = ERROR_MESSAGE +"the operating system was not created";
            } else {
                operatingSystemError = "The operating system was created";
            }
        }
        return null;
    }

    public String findByID() {
        operatingSystem = operatingSystemServices.getOperatingSystemByID(getId());
        setOperatingSystemType(operatingSystem.getOperatingSystemType());
        return null;
    }

    public String delete() {
        if (!operatingSystemServices.deleteOperatingSystem(id)) {
            operatingSystemError = ERROR_MESSAGE +"the operating system was not deleted";
        } else {
            operatingSystemError = "The operating system was deleted";
        }
        return null;
    }

    public List getAllOperatingSystems() {
        return operatingSystemServices.getOperatingSystems();
    }

    /**
     * @return the operatingSystem
     */
    public OperatingSystem getOperatingSystem() {
        return operatingSystem;
    }

    /**
     * @param operatingSystem the operatingSystem to set
     */
    public void setOperatingSystem(OperatingSystem operatingSystem) {
        this.operatingSystem = operatingSystem;
    }

    /**
     * @return the operatingSystemType
     */
    public OperatingSystemType getOperatingSystemType() {
        return operatingSystemType;
    }

    /**
     * @param operatingSystemType the operatingSystemType to set
     */
    public void setOperatingSystemType(OperatingSystemType operatingSystemType) {
        this.operatingSystemType = operatingSystemType;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the operatingSystemError
     */
    public String getOperatingSystemError() {
        return operatingSystemError;
    }

    /**
     * @param operatingSystemError the operatingSystemError to set
     */
    public void setOperatingSystemError(String operatingSystemError) {
        this.operatingSystemError = operatingSystemError;
    }

    public String clearComponents(){
        operatingSystem=new OperatingSystem();
        return null;
    }

}
