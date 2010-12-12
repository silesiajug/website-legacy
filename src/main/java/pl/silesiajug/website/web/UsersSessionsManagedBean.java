/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.silesiajug.website.web;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Adam Szecowka
 */
@ManagedBean
@RequestScoped
public class UsersSessionsManagedBean {

    public UsersSessionsManagedBean() {

    }

    public String getCurrentLoggedUserName() {
        
       return FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
    }

    public String logout() {
       ExternalContext ectx = FacesContext.getCurrentInstance().getExternalContext();
         HttpServletResponse response = (HttpServletResponse)ectx.getResponse();
         HttpSession session = (HttpSession)ectx.getSession(false);
        session.invalidate();
         return "logout";
    }
}
