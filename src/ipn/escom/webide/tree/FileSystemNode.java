package ipn.escom.webide.tree;

import ipn.escom.webide.utilities.WebUtilities;

import java.util.Set;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

public class FileSystemNode {
	
	private static FileSystemNode[] CHILDREN_ABSENT = new FileSystemNode[0];
	
	private HttpServletRequest request;
	private String path;
	private String name;
	private Information information;
	private FileSystemNode[] children;

	public FileSystemNode() {
	}
	
	public FileSystemNode(String path, HttpServletRequest request) {
		this.path = path;
		int index = path.lastIndexOf('/');
		
		if(index != -1)
			name = path.substring(index + 1);
		else
			name = path;
		
		information = new Information(WebUtilities.getRealPath(path, request));
		
		this.request = request;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Information getInformation() {
		return information;
	}

	public void setInformation(Information information) {
		this.information = information;
	}

	public FileSystemNode[] getChildren() {
		return children;
	}

	public void setChildren(FileSystemNode[] children) {
		this.children = children;
	}

	@SuppressWarnings("rawtypes")
	public synchronized FileSystemNode[] getNodes() {
		if(children == null) {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			ExternalContext externalContext = facesContext.getExternalContext();
			Set resourcePaths = externalContext.getResourcePaths(path);
			
			if(resourcePaths != null) {
				Object[] nodes = (Object[]) resourcePaths.toArray();
				children = new FileSystemNode[nodes.length];
				
				for(int i = 0; i < nodes.length; i++) {
					String nodePath = nodes[i].toString();
					
					if(nodePath.endsWith("/"))
						nodePath = nodePath.substring(0, nodePath.length() - 1);
					
					children[i] = new FileSystemNode(nodePath, request);
				}
			} else {
				children = CHILDREN_ABSENT;
			}
		}

		return children;
	}
	
	public String toString() {
		return name;
	}

}