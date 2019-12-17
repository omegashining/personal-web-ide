package ipn.escom.webide.userfiles;

import static ipn.escom.webide.constants.SystemConstants.FILESEPARATOR;

import ipn.escom.webide.tree.Information;
import ipn.escom.webide.tree.FileSystemBean;
import ipn.escom.webide.utilities.FolderUtilities;
import ipn.escom.webide.utilities.WebUtilities;
import ipn.escom.webide.web.development.CodeBean;

import java.io.File;

import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;

import org.ajax4jsf.component.html.HtmlAjaxSupport;
import org.richfaces.component.html.ContextMenu;
import org.richfaces.component.html.HtmlMenuItem;
import org.richfaces.component.html.HtmlTreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Scope("session")
@Component
public class FolderBean {
	
	private HttpServletRequest request;
	private String name;
	private String newName;
	private Information information;

	@Autowired
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void clearName() {
		name = "";
	}

	public String getNewName() {
		return newName;
	}

	public void setNewName(String newName) {
		this.newName = newName;
	}
	
	public void clearNewName() {
		newName = "";
	}

	public void create() {
		if(!name.equals("") && !name.contains(".")) {
			FolderUtilities.create(information.getPath() + FILESEPARATOR + name);
			updateTreeNodes();
			
		    name = "";
		}
	}

	public String rename() {
		if(!newName.equals("") && !newName.contains(".")) {
			String path = information.getPath();
			String newPath = path.substring(0, path.lastIndexOf(FILESEPARATOR) + 1) + newName;
			FolderUtilities.rename(path, newPath);
			updateTreeNodes();
			updateCodePath(path, newPath);
			
		    newName = "";
		}
	    
	    return "rename";
	}
	
	public String delete() {
		File folder = new File(information.getPath());
		delete(folder);
		updateTreeNodes();
		
		return "delete";
	}

	public boolean delete(File file) {
		if(file.isDirectory()) {
			String[] children = file.list();
			File[] files = file.listFiles();
			
			for (int i = 0; i < children.length; i++) {
				clearCodeInfo(files[i].getAbsolutePath());
				
				if(!delete(new File(file, children[i])))
					return false;
			}
		}
		
		return file.delete();
	}
	
	public void updateTreeNodes() {
		FileSystemBean fileSystemBean = (FileSystemBean) WebUtilities.getBean("fileSystemBean", request);
		fileSystemBean.setNodes(null);
	}
	
	public void updateCodePath(String path, String newPath) {
		CodeBean codeBean = (CodeBean) WebUtilities.getBean("codeBean", request);
		
		if(codeBean.getPath() != null) {
			if(codeBean.getPath().startsWith(path)) {
				newPath += FILESEPARATOR + codeBean.getPath().substring(path.length());
			    codeBean.setPath(newPath);
			}
		}
	}
	
	public void clearCodeInfo(String path) {
		CodeBean codeBean = (CodeBean) WebUtilities.getBean("codeBean", request);
		
		if(codeBean.getPath() != null) {
			if(codeBean.getPath().equals(path))
			    codeBean.clearFields();
		}
	}
	
	public void menuItemListener(ActionEvent event) {
		HtmlAjaxSupport htmlAjaxSupport = (HtmlAjaxSupport) event.getComponent();
		HtmlMenuItem htmlMenuItem = (HtmlMenuItem) htmlAjaxSupport.getParent();
		ContextMenu contextMenu = (ContextMenu) htmlMenuItem.getParent();
		HtmlTreeNode htmlTreeNode = (HtmlTreeNode) contextMenu.getParent();
		information = (Information) htmlTreeNode.getData();
	}
	
}
