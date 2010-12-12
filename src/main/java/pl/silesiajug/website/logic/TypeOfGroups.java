/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.silesiajug.website.logic;

/**
 *
 * @author user
 */
public enum TypeOfGroups {
    ADMIN("ADMIN"), EDITOR("EDITOR"),USER("USER");
    private String groupName;
    TypeOfGroups(String name) {
        groupName = name;
    }

    public String getName() {
        return groupName;
    }


}
