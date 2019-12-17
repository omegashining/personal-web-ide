package ipn.escom.webide.tree;

import static ipn.escom.webide.constants.SystemConstants.FILESEPARATOR;

public class Information {

	private String path;
	private String name;
	private String extension;
	private String type;
	
	public Information() {
	}
	
	public Information(String path) {
		this.path = path;

		String completeName = path.substring(path.lastIndexOf(FILESEPARATOR) + 1);
		int extensionDot = completeName.lastIndexOf('.');
		
		if(extensionDot > 0) {
			name = completeName.substring(0, extensionDot);
			extension = completeName.substring(extensionDot + 1);

			if("bmp".equals(extension)) {
				type = "image";
			} else if("jpg".equals(extension)) {
				type = "image";
			} else if("gif".equals(extension)) {
				type = "image";
			} else if("png".equals(extension)) {
				type = "image";
			} else if("c".equals(extension)) {
				type = "file";
			} else if("java".equals(extension)) {
				type = "file";
			} else if("txt".equals(extension)) {
				type = "file";
			} else if("exe".equals(extension)) {
				type = "exec";
			} else if("class".equals(extension)) {
				type = "exec";
			} else if("root".equals(extension)) {
				type = "root";
			}
		} else {
			name = completeName;
			extension = "folder";
			type = "folder";
		}
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

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
