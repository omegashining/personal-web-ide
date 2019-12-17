package ipn.escom.webide.validation.email;

import java.util.List;

import ipn.escom.webide.dao.user.UserDAO;
import ipn.escom.webide.entity.user.User;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

public class EmailDisponibilityValidator implements Validator {
	
	@Override
	public void validate(FacesContext facesContext, UIComponent uiComponent, Object object) throws ValidatorException {
		UserDAO userDAO = (UserDAO) uiComponent.getAttributes().get("userDAO");
        String email = (String) object;
		
		if(email != null && !email.equals("")) {
			List<User> usersQuery = userDAO.getUsersByEmail(email);
			
			if(usersQuery.size() != 0) {
                FacesMessage message = new FacesMessage();
                message.setSummary("El e-mail ya esta en uso.");
                
                throw new ValidatorException(message);
			}
		}
	}

}
