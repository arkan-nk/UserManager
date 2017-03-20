package ru.chipn.usermanage.idm;

import org.picketlink.idm.credential.Password;
import org.picketlink.idm.credential.UsernamePasswordCredentials;
import org.picketlink.idm.model.basic.Agent;
import org.picketlink.idm.model.basic.BasicModel;
import org.picketlink.idm.model.basic.User;
import org.picketlink.idm.query.IdentityQuery;
import org.picketlink.idm.query.IdentityQueryBuilder;
import ru.chipn.usermanage.login.AuthorizationManager;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Named
@SessionScoped
public class UserManagerBean implements UserManagerIf, Serializable{

    private User currentUser;
    private List<User> users;
    private List<User> foundUsers;
    private String pageName;
    private String password;
    @Inject
    private AuthorizationManager authorizationManager;
    public UserManagerBean(){}

    @Override
    public List<User> getAll() {
        IdentityQueryBuilder iqb = authorizationManager.getIdentityManager().getQueryBuilder();
        IdentityQuery<User> query = iqb.createIdentityQuery(User.class);
        List<User> list1 = query.getResultList();
        return list1.stream().filter(user-> !user.getLoginName().equals("nobody"))
                .sorted(Comparator.comparing(Agent::getLoginName))
                .collect(Collectors.toList());
    }
    @Override
    @Transactional
    public void saveUser(){
        Objects.requireNonNull(currentUser.getLoginName());
        boolean isNew = currentUser.getId()==null || currentUser.getId().length()<1;
        if (isNew){
            authorizationManager.getIdentityManager().add(this.currentUser);
        }
        if (password!=null && password.trim().length()>0) {
            UsernamePasswordCredentials credential = new UsernamePasswordCredentials();
            credential.setUsername(currentUser.getLoginName());
            Password password1 = new Password(this.password);
            credential.setPassword(password1);
            authorizationManager.getIdentityManager().updateCredential(currentUser, password1);
            authorizationManager.getIdentityManager().validateCredentials(credential);
        }
        authorizationManager.getIdentityManager().update(currentUser);
        if (isNew) init();
    }
    @Override
    public User getUser(final String loginName){
        return BasicModel.getUser(authorizationManager.getIdentityManager(), loginName);
    }
    @Override
    @Transactional
    public void dropUser(){
        Objects.requireNonNull(currentUser);
        if (currentUser.getId()!=null || currentUser.getId().length()>1) {
            authorizationManager.getIdentityManager().remove(currentUser);
            init();
        }
        currentUser=null;
        this.password=null;
    }
    @Override
    public void newUser(){
        currentUser = new User();
        this.password=null;
    }


    public String getPageName(){
        return pageName;
    }
    public void setPageName(String pn){
        pageName = pn;
    }
    public String getPassword(){
        return password;
    }
    public void setPassword(String p){
        password = p;
    }

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
    public void setFoundUsers(final List<User> foundUsers){
        this.foundUsers = foundUsers;
    }

}