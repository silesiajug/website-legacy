package pl.silesiajug.website.web;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import pl.silesiajug.website.logic.UsersSessionBean;
import pl.silesiajug.website.logic.UsersUtils;
import pl.silesiajug.website.model.Users;


/**
 *
 * @author Adam Szecowka
 */
@ManagedBean
@RequestScoped
public class RegisterManagedBean {

    /** Creates a new instance of RegisterManagedBean */
    public RegisterManagedBean() {
    }
    @EJB UsersSessionBean usersBean;
    Users newUser = new Users();
    private String retypePassword;

    public String doRegister() {
        validate();
        if(FacesContext.getCurrentInstance().getMessageList().size() > 0) {
            return null;
        } else {
            // dodaj uzytkownika
            usersBean.addUser(newUser);
            // wyslij mail z linkiem aktywujacym
            return "registerSendMailMessage";
        }
    }

    public String getEmail() {
        return newUser.getEmail();
    }

    public void setEmail(String email) {
        newUser.setEmail(email);
    }

    public String getPassword() {
        return newUser.getPassword();
    }

    public void setPassword(String password) {
        newUser.setPassword(password);
    }

    public String getRetypePassword() {
        return retypePassword;
    }

    public void setRetypePassword(String s) {
        retypePassword = s;
    }

    public String getUserName() {
        return newUser.getName();
    }

    public void setUserName(String userName) {
        newUser.setName(userName);
    }

    private void validate() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if(usersBean.checkIfUserExist(newUser.getName())) {
            facesContext.addMessage("idInputUserName", new FacesMessage("W systemie istnieje już użytkownik o podanej nazwie"));
        }

        if(!UsersUtils.isValidEmail(newUser.getEmail())) {
            facesContext.addMessage("idInputEmail", new FacesMessage("Podany adres email nie jest poprawny"));
        }

        if(newUser.getPassword().equals(UsersUtils.getMD5(retypePassword)) == false) {
            facesContext.addMessage(null, new FacesMessage("Pomyliłeś się podczas wpisywania hasła"));
        }
    }
}
