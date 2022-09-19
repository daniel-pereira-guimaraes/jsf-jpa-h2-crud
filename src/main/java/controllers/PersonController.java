package controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;

import entities.Person;
import services.PersonService;

@ManagedBean
@SessionScoped
public class PersonController {

	private List<Person> list = new ArrayList<Person>();
	private String searchText;
	
	private void addErrorMessage(Exception e) {
		FacesMessage message = new FacesMessage(e.getMessage());
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void loadList(ComponentSystemEvent cse) {
		list.clear();
		try {
			list = PersonService.getInstance().selectByText(searchText);
			System.out.println("loadList");
			System.out.println(list);
		} catch(Exception e) {
			addErrorMessage(e);
		}
	}
	
	public List<Person> getList() throws Exception {
		return list;
	}
	
	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}
	
	public String search() {
		loadList(null);
		return null;
	}

	public String edit(Long id) {
		try {
			Person person = PersonService.getInstance().selectById(id);
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> requestMap = externalContext.getRequestMap();
			requestMap.put("person", person);
		} catch(Exception e) {
			addErrorMessage(e);
			return null;
		}
		return "person_edit";
	}
	
	public String save(Person person) throws Exception {
		try {
			PersonService dao = PersonService.getInstance();
			if (person.getId() == null)
				dao.insert(person);
			else
				dao.update(person);
		} catch(Exception e) {
			addErrorMessage(e);
			return null;
		}
		return "person_list?faces-redirect=true";
	}
	
	public String delete(Long id) {
		try {
			PersonService.getInstance().delete(id);
		} catch(Exception e) {
			addErrorMessage(e);
		}
		return null;
	}


}
