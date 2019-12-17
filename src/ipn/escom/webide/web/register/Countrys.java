package ipn.escom.webide.web.register;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.stereotype.Component;

@Component
public class Countrys {
	
	private List<SelectItem> names = new ArrayList<SelectItem>();

	public Countrys() {
		names.add(new SelectItem("México"));
	}

	public List<SelectItem> getNames() {
		return names;
	}

	public void setNames(List<SelectItem> names) {
		this.names = names;
	}

}
