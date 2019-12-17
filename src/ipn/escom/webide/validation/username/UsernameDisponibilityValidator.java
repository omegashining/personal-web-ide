package ipn.escom.webide.validation.username;

import ipn.escom.webide.dao.user.UserDAO;
import ipn.escom.webide.entity.user.User;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

public class UsernameDisponibilityValidator implements Validator {
	
	@Override
	public void validate(FacesContext facesContext, UIComponent uiComponent, Object object) throws ValidatorException {
		UserDAO userDAO = (UserDAO) uiComponent.getAttributes().get("userDAO");
        String username = (String) object;
		
		if(username != null && !username.equals("")) {
			List<User> usersQuery = userDAO.getUsersByName(username);
			
			if(usersQuery.size() != 0) {
                FacesMessage message = new FacesMessage();
                message.setSummary("Nombre de usuario no disponible.");
                
                throw new ValidatorException(message);
			}
		}
	}

}
