/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.silesiajug.website.web;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import pl.silesiajug.website.logic.UsersSessionBean;
import pl.silesiajug.website.model.User;


/**
 *
 * @author Adam Szecowka
 */
@ManagedBean
@RequestScoped
public class ConfirmRegistrationManagedBean {

    @EJB UsersSessionBean usersBean;
    private String key;
    private String message;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getMessage() {
        if(key == null) {
            message = "";
            return message;
        }
        User user = usersBean.tryConfirmRegistration(key);
        if(user == null) {
            message = "Akcja zakończonoa niepowodzeniem";
            return message;
        } else {
            message = "Rejestracja użytkownika " + user.getName() + " dobiegła końca";
            return message;
        }



    }

    public void setMessage(String message) {
        this.message = message;
    }


    /** Creates a new instance of ConfirmRegistrationManagedBean */
    public ConfirmRegistrationManagedBean() {
    }

}
