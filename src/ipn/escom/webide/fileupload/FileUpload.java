package ipn.escom.webide.fileupload;

public class FileUpload {

	private String name;
	private String mime;
	private long length;
	private byte[] data;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
		int extensionDot = name.lastIndexOf('.');
		
		if(extensionDot > 0){
			String extension = name.substring(extensionDot + 1);

			if("bmp".equals(extension)){
				this.mime="bmp";
			} else if("jpg".equals(extension)){
				this.mime="jpeg";
			} else if("gif".equals(extension)){
				this.mime="gif";
			} else if("png".equals(extension)){
				this.mime="png";
			} else if("c".equals(extension)){
				this.mime="c";
			} else if("java".equals(extension)){
				this.mime="java";
			} else if("txt".equals(extension)){
				this.mime="txt";
			}
		}
	}

	public String getMime() {
		return mime;
	}

	public void setMime(String mime) {
		this.mime = mime;
	}

	public long getLength() {
		return length;
	}
	
	public void setLength(long length) {
		this.length = length;
	}
	
	public byte[] getData() {
		return data;
	}
	
	public void setData(byte[] data) {
		this.data = data;
	}
	
}
