package com.losandes.application;

import com.losandes.persistence.entity.Application;
import java.util.List;
import javax.ejb.EJB;
import static com.losandes.utils.Constants.*;

/**
 * @author Edgar Eduardo Rosales Rosero
 * Responsible for managing the Application JSF
 */
public class ApplicationBean {

    @EJB
    private IApplicationServices applicationServices;
    private Application application;
    private String applicationError;
    private int id;

    /** Creates a new instance of ApplicationBean */
    public ApplicationBean() {
        application = new Application();
    }

    public String createOrUpdate() {
        if (getApplication().getApplicationCode() != 0) {
            if (!applicationServices.updateApplication(application)) {
                applicationError = ERROR_MESSAGE + "the application was not updated";
            } else {
                applicationError = "the application was modified";
            }
        } else {
            if (!applicationServices.createApplication(application)) {
                applicationError = ERROR_MESSAGE + "the application was not created";
            } else {
                applicationError = "The application was created";
            }
        }
        return null;
    }

  public String findByID() {
        application= applicationServices.getApplicationByID(getId());
        return null;
    }

  public String delete() {
        if (!applicationServices.deleteApplication(id)) {
            applicationError = ERROR_MESSAGE +"the application was not deleted";
        } else {
            applicationError = "The application was deleted";
        }
        return null;
    }

      public List getAllApplications() {
        return applicationServices.getApplications();
    }

    /**
     * @return the applicationServices
     */
    public IApplicationServices getApplicationServices() {
        return applicationServices;
    }

    /**
     * @param applicationServices the applicationServices to set
     */
    public void setApplicationServices(IApplicationServices applicationServices) {
        this.applicationServices = applicationServices;
    }

    /**
     * @return the application
     */
    public Application getApplication() {
        return application;
    }

    /**
     * @param application the application to set
     */
    public void setApplication(Application application) {
        this.application = application;
    }

    /**
     * @return the applicationError
     */
    public String getApplicationError() {
        return applicationError;
    }

    /**
     * @param applicationError the applicationError to set
     */
    public void setApplicationError(String applicationError) {
        this.applicationError = applicationError;
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

    public String clearComponents(){
        application=new Application();
        return null;
    }


}
