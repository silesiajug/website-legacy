/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.silesiajug.website.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import pl.silesiajug.website.logic.TypeOfGroups;
import pl.silesiajug.website.logic.UsersSessionBean;
import pl.silesiajug.website.model.Groups;
import pl.silesiajug.website.model.Users;

/**
 *
 * @author Adam Szecowka
 */
@ManagedBean
@ViewScoped
public class GrantRoleManagedBean implements Serializable{

    @EJB UsersSessionBean userSessionBean;
    /** Creates a new instance of GrantRoleManagedBean */
    public GrantRoleManagedBean() {
    }

    private List<Users> users = new ArrayList<Users>();
    private String selectedUser;
    private boolean userRole, editorRole, adminRole;

    public boolean isAdminRole() {
        return adminRole;
    }

    public void setAdminRole(boolean adminRole) {
        this.adminRole = adminRole;
    }

    public boolean isEditorRole() {
        return editorRole;
    }

    public void setEditorRole(boolean editorRole) {
        this.editorRole = editorRole;
    }

    public boolean isUserRole() {
        return userRole;
    }

    public void setUserRole(boolean userRole) {
        this.userRole = userRole;
    }

    private final static String NO_USER_SELECTED = "--- Wybierz użytkownika ---";

    @PostConstruct
    public void init() {
        users = userSessionBean.getAllUsers();
        selectedUser = NO_USER_SELECTED;
        System.out.println("ZNOWU POSTCONSTRUCT");
    }

    public String getVisibilityForTableAndButton() {
        if (NO_USER_SELECTED.equals(selectedUser)) {
            return "hidden";
        } else {
            return "visible";
        }
    }

    public String getNoUserSelected() {
        return NO_USER_SELECTED;
    }

    public String getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(String u) {
           selectedUser = u;
           List<String> groups = userSessionBean.getUSerRoles(userSessionBean.getUserByName(u));
           userRole = editorRole = adminRole = false;
           for(String gName:groups) {
                if(gName.equals("USER")) {
                    userRole = true;
                } else if(gName.equals("EDITOR")) {
                    editorRole = true;
                } else if (gName.equals("ADMIN")) {
                    adminRole = true;
                }
           }

    }

    public List<String> getUsersName() {
        List<String> list = new ArrayList<String>();
        list.add(NO_USER_SELECTED);
        for(Users u:users) {
            list.add(u.getName());
        }
        return list;
    }

    public String save() {
        
        Users cu = null;
        for(Users u:users) {
            if(u.getName().equals(selectedUser)) {
                cu = u;
                break;
            }
        }

        if(cu == null) {
            return null;
        }

        for(Groups g:cu.getGroupsList()) {
            userSessionBean.removeGroups(g);
        }
        cu.getGroupsList().clear();
        
        if(adminRole) {
            Groups gAdmin = new Groups();
            gAdmin.setGroupName(TypeOfGroups.ADMIN.getName());
            gAdmin.setUsers(cu);
            cu.getGroupsList().add(gAdmin);
        } 
        if(editorRole) {
            Groups gEditor = new Groups();
            gEditor.setGroupName(TypeOfGroups.EDITOR.getName());
            gEditor.setUsers(cu);
            cu.getGroupsList().add(gEditor);
        } 
        if(userRole) {
            Groups gUser = new Groups();
            gUser.setGroupName(TypeOfGroups.USER.getName());
            gUser.setUsers(cu);
            cu.getGroupsList().add(gUser);
        }

        userSessionBean.editUser(cu);
        return null;
    }

}
