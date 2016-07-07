package ru.chipn.manage.idm;

import org.picketlink.idm.model.basic.User;
import org.picketlink.idm.query.IdentityQuery;
import org.picketlink.idm.query.IdentityQueryBuilder;
import ru.chipn.manage.login.AuthorizationManager;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Named
@SessionScoped
public class UserManagerBean implements UserManagerIf, Serializable{
    private User currentUser;
    private List<User> users;
    private List<User> foundUsers;

    @Override
    public List<User> getAll() {
        IdentityQueryBuilder iqb = authorizationManager.getIdentityManager().getQueryBuilder();
        IdentityQuery query = iqb.createIdentityQuery(User.class);
        List<User> result = query.getResultList();
        return result;
    }

    @Inject
    private AuthorizationManager authorizationManager;
    public UserManagerBean(){}
    @PostConstruct
    public void init(){
        users = Collections.unmodifiableList(getAll());
    }
    public User getCurrentUser() {
        return currentUser;
    }
    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
    public List<User> getUsers(){
        return users;
    }
    public List<User> getFoundUsers(){
        return foundUsers;
    }
    public void setFoundUsers(List<User> foundUsers){
        this.foundUsers = foundUsers;
    }
}