package ipn.escom.webide.web.user;

import javax.servlet.http.HttpServletRequest;

import ipn.escom.webide.utilities.JDOMUtilities;
import ipn.escom.webide.utilities.WebUtilities;

import org.jdom.Document;
import org.jdom.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;;

@Scope("session")
@Component
public class UserBean {

	private HttpServletRequest request;
	private String username;

	@Autowired(required = true)
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public String getUsername() {
		if(username == null){
			username = WebUtilities.getUser().getUsername();
			createXMLFile();
		}
		
		return username;
	}
	
	private void createXMLFile() {
	    Element root = new Element(username);
        Document document = new Document(root);
        Element ipAddress = new Element("RemoteAddress").setText(request.getRemoteAddr());
        root.addContent(ipAddress);
        Element localName = new Element("LocalName").setText(request.getLocalName());
        root.addContent(localName);

        JDOMUtilities.documentToFile(document, WebUtilities.getRealPath("/ips/"+username+".xml", request));
	}

}
