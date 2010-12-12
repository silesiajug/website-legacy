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
import pl.silesiajug.website.model.Group;
import pl.silesiajug.website.model.User;

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

    private List<User> users = new ArrayList<User>();
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
        for(User u:users) {
            list.add(u.getName());
        }
        return list;
    }

    public String save() {
        
        User cu = null;
        for(User u:users) {
            if(u.getName().equals(selectedUser)) {
                cu = u;
                break;
            }
        }

        if(cu == null) {
            return null;
        }

        for(Group g:cu.getGroupsList()) {
            userSessionBean.removeGroups(g);
        }
        cu.getGroupsList().clear();
        
        if(adminRole) {
            Group gAdmin = new Group();
            gAdmin.setGroupName(TypeOfGroups.ADMIN.getName());
            gAdmin.setUser(cu);
            cu.getGroupsList().add(gAdmin);
        } 
        if(editorRole) {
            Group gEditor = new Group();
            gEditor.setGroupName(TypeOfGroups.EDITOR.getName());
            gEditor.setUser(cu);
            cu.getGroupsList().add(gEditor);
        } 
        if(userRole) {
            Group gUser = new Group();
            gUser.setGroupName(TypeOfGroups.USER.getName());
            gUser.setUser(cu);
            cu.getGroupsList().add(gUser);
        }

        userSessionBean.editUser(cu);
        return null;
    }

}
