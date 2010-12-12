
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
import pl.silesiajug.website.model.Group;
import pl.silesiajug.website.model.RegistrationKey;
import pl.silesiajug.website.model.User;


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

    public User getUserByName(String name) {
        return em.createNamedQuery("Users.findByName", User.class).setParameter("name", name).getSingleResult();
    }

    public boolean checkIfUserExist(String userName) {

        return em.createNamedQuery("Users.findByName", User.class).setParameter("name", userName).getResultList().size() > 0;
    }

    public void editUser(User user) {
        em.merge(user);
    }

    public void removeGroups(Group g) {
        g = em.merge(g);
        em.remove(g);
    }

    public void addUser(User user) {

        em.persist(user);
        RegistrationKey rk = new RegistrationKey();
        rk.setUser(user);
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

    public User tryConfirmRegistration(String key) {
        try {
            RegistrationKey regKey = em.createNamedQuery("RegistrationKey.findByUserKey", RegistrationKey.class)
                    .setParameter("userKey", key)
                    .getSingleResult();
            User user = regKey.getUser();
            user.setEvidenced(true);
            Group groups = new Group();
            groups.setGroupName(TypeOfGroups.USER.getName());
            groups.setUser(user);
            em.persist(groups);
            em.remove(regKey);
            return user;

        } catch(Exception ex) {
            return null;
        }
        
    }

    public List<String> getUSerRoles(User user) {
       
        List<String> gNames = new ArrayList<String>();

        for(Group g:em.createNamedQuery("Groups.findByUser", Group.class).setParameter("user", user).getResultList()) {
            gNames.add(g.getGroupName());
        }

        return gNames;
    }

    public List<User> getAllUsers() {
        return em.createNamedQuery("Users.findAll", User.class).getResultList();
    }
 
}
