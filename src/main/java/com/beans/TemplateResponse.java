package beans;

import java.util.List;

public class TemplateResponse extends PostResponse{

	private List<Template> templateList;

	public List<Template> getTemplateList() {
		return templateList;
	}

	public void setTemplateList(List<Template> templateList) {
		this.templateList = templateList;
	}
	
	
}
