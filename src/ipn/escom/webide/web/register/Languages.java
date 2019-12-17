package ipn.escom.webide.web.register;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.stereotype.Component;

@Component
public class Languages {
	
	private List<SelectItem> names = new ArrayList<SelectItem>();

	public Languages() {
		names.add(new SelectItem("Español"));
	}

	public List<SelectItem> getNames() {
		return names;
	}

	public void setNames(List<SelectItem> names) {
		this.names = names;
	}
	
}
