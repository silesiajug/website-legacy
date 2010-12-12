
package pl.silesiajug.website.logic;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pl.silesiajug.website.model.Groups;
import pl.silesiajug.website.model.RegistrationKey;
import pl.silesiajug.website.model.Users;


/**
 *
 * @author Adam Szecowka
 */
@Stateless
@LocalBean
public class UsersSessionBean {
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    @PersistenceContext(unitName="SilesiaJUGPU")
    EntityManager em;

    @EJB SendToJmsBean sendToJmsBean;

    public Users getUserByName(String name) {
        return em.createNamedQuery("Users.findByName", Users.class).setParameter("name", name).getSingleResult();
    }

    public boolean checkIfUserExist(String userName) {

        return em.createNamedQuery("Users.findByName", Users.class).setParameter("name", userName).getResultList().size() > 0;
    }

    public void editUser(Users user) {
        em.merge(user);
    }

    public void removeGroups(Groups g) {
        g = em.merge(g);
        em.remove(g);
    }

    public void addUser(Users user) {

        em.persist(user);
        RegistrationKey rk = new RegistrationKey();
        rk.setUsers(user);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 3);
        Date date3MonthsAfterNow = calendar.getTime();
        rk.setRequestDate(date3MonthsAfterNow);
        Random r = new Random();
        long key = r.nextLong();
        key = Math.abs(key);
        rk.setUserKey(("" + key));
        em.persist(rk);
        sendToJmsBean.wyslijWiadomoscDoKolejki(user, rk);
    }

    public Users tryConfirmRegistration(String key) {
        try {
            RegistrationKey regKey = em.createNamedQuery("RegistrationKey.findByUserKey", RegistrationKey.class)
                    .setParameter("userKey", key)
                    .getSingleResult();
            Users user = regKey.getUsers();
            user.setEvidenced(true);
            Groups groups = new Groups();
            groups.setGroupName(TypeOfGroups.USER.getName());
            groups.setUsers(user);
            em.persist(groups);
            em.remove(regKey);
            return user;

        } catch(Exception ex) {
            return null;
        }
        
    }

    public List<String> getUSerRoles(Users user) {
       
        List<String> gNames = new ArrayList<String>();

        for(Groups g:em.createNamedQuery("Groups.findByUser", Groups.class).setParameter("user", user).getResultList()) {
            gNames.add(g.getGroupName());
        }

        return gNames;
    }

    public List<Users> getAllUsers() {
        return em.createNamedQuery("Users.findAll", Users.class).getResultList();
    }
 
}
