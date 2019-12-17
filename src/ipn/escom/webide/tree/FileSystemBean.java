package ipn.escom.webide.tree;

import ipn.escom.webide.utilities.WebUtilities;
import ipn.escom.webide.web.user.UserBean;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Scope("session")
@Component
public class FileSystemBean {
	
	private HttpServletRequest request;
	private FileSystemNode[] nodes;

	@Autowired(required = true)
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setNodes(FileSystemNode[] nodes) {
		this.nodes = nodes;
	}

	public synchronized FileSystemNode[] getNodes() {
		if(nodes == null) {
			UserBean userBean = (UserBean) WebUtilities.getBean("userBean", request);
			String shortPath = "/usuarios/" + userBean.getUsername();

			Information information = new Information(WebUtilities.getRealPath(shortPath, request));
			information.setExtension("root");
			information.setType("root");
			
			nodes = new FileSystemNode[1];
			nodes[0] = new FileSystemNode();
			nodes[0].setPath(shortPath);
			nodes[0].setName(userBean.getUsername());
			nodes[0].setChildren(new FileSystemNode(shortPath, request).getNodes());
			nodes[0].setInformation(information);
		}
		
		return nodes;
	}
	
}
