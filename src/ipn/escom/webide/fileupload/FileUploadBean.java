package ipn.escom.webide.fileupload;

import static ipn.escom.webide.constants.SystemConstants.FILESEPARATOR;

import ipn.escom.webide.tree.Information;
import ipn.escom.webide.tree.FileSystemBean;
import ipn.escom.webide.utilities.WebUtilities;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.faces.event.ActionEvent;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.ajax4jsf.component.html.HtmlAjaxSupport;
import org.richfaces.component.html.ContextMenu;
import org.richfaces.component.html.HtmlMenuItem;
import org.richfaces.component.html.HtmlTreeNode;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Scope("session")
@Component
public class FileUploadBean {
	
	static final int UPLOADS = 15;

	private HttpServletRequest request;
	private ArrayList<FileUpload> listFiles = new ArrayList<FileUpload>();
	private int uploadsAvailable = UPLOADS;
	private boolean autoUpload = false;
	private boolean useFlash = false;
	private String path = "";

	@Autowired(required = true)
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public ArrayList<FileUpload> getListFiles() {
		return listFiles;
	}

	public void setListFiles(ArrayList<FileUpload> listFiles) { 
		this.listFiles = listFiles;
	}

	public int getUploadsAvailable() {
		return uploadsAvailable;
	}

	public void setUploadsAvailable(int uploadsAvailable) {
		this.uploadsAvailable = uploadsAvailable;
	}

	public boolean isAutoUpload() {
		return autoUpload;
	}

	public void setAutoUpload(boolean autoUpload) {
		this.autoUpload = autoUpload;
	}

	public boolean isUseFlash() {
		return useFlash;
	}

	public void setUseFlash(boolean useFlash) {
		this.useFlash = useFlash;
	}

	public void menuItemListener(ActionEvent event) {
		HtmlAjaxSupport htmlAjaxSupport = (HtmlAjaxSupport) event.getComponent();
		HtmlMenuItem htmlMenuItem = (HtmlMenuItem) htmlAjaxSupport.getParent();
		ContextMenu contextMenu = (ContextMenu) htmlMenuItem.getParent();
		HtmlTreeNode htmlTreeNode = (HtmlTreeNode) contextMenu.getParent();
		Information information = (Information) htmlTreeNode.getData();
		path = information.getPath();
	}

	public void uploadListener(UploadEvent event) throws IOException {
	    UploadItem item = event.getUploadItem();
	    FileUpload fileUpload = new FileUpload();
	    fileUpload.setName(item.getFileName());
	    fileUpload.setLength(item.getData().length);
	    fileUpload.setData(item.getData());

	    FileOutputStream fileOutputStream = new FileOutputStream(path + FILESEPARATOR + fileUpload.getName());
	    fileOutputStream.write(fileUpload.getData());
	    fileOutputStream.close();
	    
	    listFiles.add(fileUpload);
	    uploadsAvailable--;
	}

	public void paint(OutputStream stream, Object object) throws IOException {
		FileUpload fileUpload= getListFiles().get((Integer) object);
		String mime = fileUpload.getMime();
		
		byte[] data = null;
		
		if(mime.equals("c") || mime.equals("java") || mime.equals("txt")) {
			File file = new File(WebUtilities.getRealPath("/images/file_upload", request) + FILESEPARATOR + mime + ".png");
			BufferedImage bufferedImage = ImageIO.read(file);
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
			data = byteArrayOutputStream.toByteArray();
		} else {
			data = getListFiles().get((Integer) object).getData();
		}
		
		stream.write(data);
	}
	
	public long getTimeStamp(){
		return System.currentTimeMillis();
	}
	
	public int getSize() {
		if(getListFiles().size() > 0)
			return getListFiles().size();
		else
			return 0;
	}
	  
	public String clearUploadData() {
		listFiles.clear();
		setUploadsAvailable(UPLOADS);
		
		return null;
	}
	
	public String close() {
		FileSystemBean fileSystemBean = (FileSystemBean) WebUtilities.getBean("fileSystemBean", request);
		fileSystemBean.setNodes(null);
		
		return "close";
	}
	
}
