/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author Clouder
 */
public class SelectOneMenuValidator implements javax.faces.validator.Validator{

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if(value instanceof Integer&&(Integer)value==0)throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,component.getId()+" required",""));
    }


}
