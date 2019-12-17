package ipn.escom.webide.userfiles;

import static ipn.escom.webide.constants.SystemConstants.FILESEPARATOR;

import java.io.IOException;

import ipn.escom.webide.tree.Information;
import ipn.escom.webide.tree.FileSystemBean;
import ipn.escom.webide.utilities.FileUtilities;
import ipn.escom.webide.utilities.WebUtilities;
import ipn.escom.webide.web.development.CodeBean;

import javax.faces.component.html.HtmlCommandButton;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;

import org.ajax4jsf.component.html.HtmlAjaxSupport;
import org.primefaces.model.StreamedContent;
import org.richfaces.component.html.ContextMenu;
import org.richfaces.component.html.HtmlDragSupport;
import org.richfaces.component.html.HtmlDropSupport;
import org.richfaces.component.html.HtmlMenuItem;
import org.richfaces.component.html.HtmlTreeNode;
import org.richfaces.event.DragEvent;
import org.richfaces.event.DropEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Scope("session")
@Component
public class FileBean {
	
	private HttpServletRequest request;

	private String name;
	private String extension;
	private String newName;
	private Information information;
	private Information dragInformation;
	private Information dropInformation;

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

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
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
	
	public StreamedContent getStreamedContent() throws IOException {
		String name = information.getName() + "." + information.getExtension();
		String contentType = WebUtilities.getMimeType(name, request);

		return FileUtilities.getStreamedContent(information.getPath(), contentType, name);
	}

	public void create() throws IOException {
		if(!name.equals("")) {
			FileUtilities.create(information.getPath() + FILESEPARATOR + name + "." + extension);
			updateTreeNodes();
			
			name = "";
		}
	}

	public String rename() {
		if(!newName.equals("")) {
			String path = information.getPath();
			String newPath = path.substring(0, path.lastIndexOf(FILESEPARATOR) + 1) + newName + "." + information.getExtension();
			FileUtilities.rename(path, newPath);
			updateTreeNodes();
			updateCodePath(path, newPath);
			
		    newName = "";
		}
	    
	    return "rename";
	}
	
	public String delete() {
		String path = information.getPath();
		FileUtilities.delete(path);
		updateTreeNodes();
		clearCodeInfo(path);
	    
	    return "delete";
	}
	
	public String move() {
		String path = dropInformation.getPath();
		String newPath = path + FILESEPARATOR + dragInformation.getName() + "." + dragInformation.getExtension();
		FileUtilities.rename(path, newPath);
		updateTreeNodes();
		updateCodePath(path, newPath);
		
		return "move";
	}
	
	public void updateTreeNodes() {
		FileSystemBean fileSystemBean = (FileSystemBean) WebUtilities.getBean("fileSystemBean", request);
		fileSystemBean.setNodes(null);
	}
	
	public void updateCodePath(String path, String newPath) {
		CodeBean codeBean = (CodeBean) WebUtilities.getBean("codeBean", request);
		
		if(codeBean.getPath() != null) {
			if(codeBean.getPath().equals(path))
			    codeBean.setPath(newPath);
		}
	}
	
	public void clearCodeInfo(String path) {
		CodeBean codeBean = (CodeBean) WebUtilities.getBean("codeBean", request);
		
		if(codeBean.getPath() != null) {
			if(codeBean.getPath().equals(path))
			    codeBean.clearFields();
		}
	}
	
	public void dragListener(DragEvent event) {
		HtmlDragSupport htmlDragSupport = (HtmlDragSupport) event.getComponent();
		HtmlTreeNode htmlTreeNode = (HtmlTreeNode) htmlDragSupport.getParent();
		dragInformation = (Information) htmlTreeNode.getData();
	}
	
	public void dropListener(DropEvent event) {
		HtmlDropSupport htmlDropSupport = (HtmlDropSupport) event.getComponent();
		HtmlTreeNode htmlTreeNode = (HtmlTreeNode) htmlDropSupport.getParent();
		dropInformation = (Information) htmlTreeNode.getData();
	}
	
	public void menuItemListener(ActionEvent event) {
		Object object = event.getComponent();
		HtmlMenuItem htmlMenuItem = null;
		
		if(object instanceof HtmlAjaxSupport)
			htmlMenuItem = (HtmlMenuItem) ((HtmlAjaxSupport) object).getParent();
		else if(object instanceof HtmlCommandButton)
			htmlMenuItem = (HtmlMenuItem) ((HtmlCommandButton) object).getParent();

		ContextMenu contextMenu = (ContextMenu) htmlMenuItem.getParent();
		HtmlTreeNode htmlTreeNode = (HtmlTreeNode) contextMenu.getParent();
		information = (Information) htmlTreeNode.getData();
	}

}
