package ipn.escom.webide.userfiles;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.stereotype.Component;

@Component
public class FileTypes {
	
	private List<SelectItem> names = new ArrayList<SelectItem>();

	public FileTypes() {
		names.add(new SelectItem("c"));
		names.add(new SelectItem("java"));
		names.add(new SelectItem("txt"));
	}

	public List<SelectItem> getNames() {
		return names;
	}

	public void setNames(List<SelectItem> names) {
		this.names = names;
	}

}
