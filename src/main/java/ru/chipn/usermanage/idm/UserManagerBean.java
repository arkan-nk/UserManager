package ru.chipn.usermanage.idm;

import org.picketlink.idm.PartitionManager;
import org.picketlink.idm.config.IdentityConfiguration;
import org.picketlink.idm.credential.Credentials;
import org.picketlink.idm.credential.Password;
import org.picketlink.idm.credential.UsernamePasswordCredentials;
import org.picketlink.idm.model.basic.User;
import org.picketlink.idm.query.IdentityQuery;
import org.picketlink.idm.query.IdentityQueryBuilder;
import ru.chipn.usermanage.login.AuthorizationManager;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Named
@SessionScoped
public class UserManagerBean implements UserManagerIf, Serializable{
    private User currentUser;
    private List<User> users;
    private List<User> foundUsers;
    private String pageName;
    private String password;

    @Override
    public List<User> getAll() {
        IdentityQueryBuilder iqb = authorizationManager.getIdentityManager().getQueryBuilder();
        IdentityQuery query = iqb.createIdentityQuery(User.class);
        List<User> list1 =query.getResultList();
        return list1.stream().filter(user-> !user.getLoginName().equals("nobody"))
                .sorted((user1,user2)->user1.getLoginName().compareTo(user2.getLoginName()))
                .collect(Collectors.toList());
    }

    public void saveUser(ActionEvent event){
        if (password!=null && password.trim().length()>0) {
            UsernamePasswordCredentials credential = new UsernamePasswordCredentials();
            credential.setUsername(currentUser.getLoginName());
            Password password1 = new Password(this.password);
            credential.setPassword(password1);
            this.password=null;
            authorizationManager.getIdentityManager().updateCredential(currentUser, password1);
            authorizationManager.getIdentityManager().validateCredentials(credential);
        }
        authorizationManager.getIdentityManager().update(currentUser);
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