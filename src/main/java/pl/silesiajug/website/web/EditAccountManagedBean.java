/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.silesiajug.website.web;

import java.util.List;
import javax.annotation.PostConstruct;
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
public class EditAccountManagedBean {



    private String password;
    private String newPassword;
    private String retypeNewPassword;
    private String email;
    private Users currentUser;
    private List<String> groupNames;

    public List<String> getGroupNames() {
        return groupNames;
    }

    public Users getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(Users currentUser) {
        this.currentUser = currentUser;
    }

    @EJB UsersSessionBean usersSessionBean;

    /** Creates a new instance of EditAccountManagedBean */
    public EditAccountManagedBean() {

    }

    @PostConstruct
    public void init() {
        currentUser = usersSessionBean.getUserByName(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser());
        email = currentUser.getEmail();
        groupNames = usersSessionBean.getUSerRoles(currentUser);
    }

    public String saveUser() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        
        if(!currentUser.getEmail().equals(email)) {
            boolean valid = UsersUtils.isValidEmail(email);
            if(!valid) {
                facesContext.addMessage("idInputEmail", new FacesMessage("Podany adres email nie jest poprawny"));
            }
        }
        // haslo
        if("".equals(password) && "".equals(newPassword) && "".equals(retypeNewPassword)) {
            // uzytkownik nie chcial zmienic haslaa
        } else {
            // uzytkownik probuje zmienic haslo
            if(!currentUser.getPassword().equals(UsersUtils.getMD5(password))) {
                // uzytkownik niepoprawnie podal dotychczasowe haslo
                facesContext.addMessage("idInputPassword", new FacesMessage("Niepoprawnie podałeś dotychczasowe hasło"));
            }
            if(!newPassword.equals(retypeNewPassword)) {
                facesContext.addMessage(null, new FacesMessage("Niepoprawnie podałeś nowe hasło"));
            }
        }

        // jezeli nie ma zadnych błędów
        // zapisz wyniki
        // w przeciwienstwie zwróć null
        if(facesContext.getMessageList().isEmpty()) {
            currentUser.setPassword(newPassword);
            currentUser.setEmail(email);
            usersSessionBean.editUser(currentUser);
           return "index";
        } else {
            return null;
        }
        
    }

        public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRetypeNewPassword() {
        return retypeNewPassword;
    }

    public void setRetypeNewPassword(String retypeNewPassword) {
        this.retypeNewPassword = retypeNewPassword;
    }

}
