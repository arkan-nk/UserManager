package ru.chipn.usermanage.idm;

import org.picketlink.idm.model.basic.Group;
import org.picketlink.idm.model.basic.User;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by arkan on 09.09.2016.
 */
@Named
@RequestScoped
public class GroupOldSchoolBean implements Serializable {
    public void massGrant(Group selectedFgroup, List<User> selectedUsers, String jmxConnStr) throws NamingException{
        Objects.requireNonNull(selectedFgroup);
        Objects.requireNonNull(selectedUsers);
        Objects.requireNonNull(jmxConnStr);
        Objects.requireNonNull(selectedUsers.isEmpty() ? null: selectedUsers);
        Objects.requireNonNull(jmxConnStr.isEmpty() ? null : jmxConnStr);
        InitialContext initialContext = new InitialContext();
        try{
            selectedUsers.forEach(userItem -> {
                try {
                    this.operate(initialContext, selectedFgroup, userItem, jmxConnStr, DirContext.ADD_ATTRIBUTE);
                } catch (NamingException ne) {
                    facesContext.addMessage(null, new FacesMessage(ne.getExplanation()));
                }
            });
        }finally{
            if (initialContext!=null) initialContext.close();
        }
    }

    public void massRevoke(Group selectedFgroup, List<User> selectedUsers, String jmxConnStr) throws NamingException{
        Objects.requireNonNull(selectedFgroup);
        Objects.requireNonNull(selectedUsers);
        Objects.requireNonNull(jmxConnStr);
        Objects.requireNonNull(selectedUsers.isEmpty() ? null: selectedUsers);
        Objects.requireNonNull(jmxConnStr.isEmpty() ? null : jmxConnStr);
        InitialContext initialContext = new InitialContext();
        try{
            selectedUsers.forEach(userItem ->{
                try {
                    this.operate(initialContext, selectedFgroup, userItem, jmxConnStr, DirContext.REMOVE_ATTRIBUTE);
                }catch(NamingException ne){
                    facesContext.addMessage(null, new FacesMessage(ne.getExplanation()));
                }
            });
        }finally{
            if (initialContext!=null) initialContext.close();
        }
    }
    public void addUserToSelectedGroups(Group selectedFgroup, List<Group> selectedTGroupList, User user, String jmxConnStr) throws NamingException{
        Objects.requireNonNull(user);
        Objects.requireNonNull(jmxConnStr);
        Objects.requireNonNull(jmxConnStr.length()<1? null : jmxConnStr);
        InitialContext initialContext = new InitialContext();
        try {
            List<Group> groups = new ArrayList<>();
            if (selectedFgroup!=null) groups.add(selectedFgroup);
            if (selectedTGroupList!=null && !selectedTGroupList.isEmpty()) groups.addAll(selectedTGroupList);
            if (!groups.isEmpty()) {
                groups.forEach(group->{
                    try {
                        operate(initialContext, group, user, jmxConnStr, DirContext.ADD_ATTRIBUTE);
                    } catch (NamingException ne) {
                        facesContext.addMessage(null, new FacesMessage(ne.getExplanation()));
                    }
                });
            }
        } finally{
            if (initialContext!=null) initialContext.close();
            //groupBean.clearSelected();
        }
    }

    public void doGetOut(Group group, User user, String jmxConnStr) throws NamingException{
        Objects.requireNonNull(group);
        Objects.requireNonNull(user);
        Objects.requireNonNull(jmxConnStr);
        Objects.requireNonNull(jmxConnStr.length()<1? null : jmxConnStr);
        InitialContext initialContext = new InitialContext();
        try{
            operate(initialContext, group,user,jmxConnStr,DirContext.REMOVE_ATTRIBUTE);
        }catch (NamingException ne){
            facesContext.addMessage(null, new FacesMessage(ne.getExplanation()));
        }finally{
            if (initialContext!=null) initialContext.close();
        }
    }

    private void operate(InitialContext initialContext, Group group, User user, String jmxConnStr, final int dirContextOperation) throws NamingException {
        InitialDirContext initalDirContext = null;
        LdapContext ldapContext = null;
        initalDirContext = (InitialDirContext) initialContext.lookup(jmxConnStr);
        ldapContext = new InitialLdapContext(initalDirContext.getEnvironment(), null);
        LdapJndiWriter ldapWriter = new LdapJndiWriter(ldapContext);
        ldapWriter.operate(user,group,dirContextOperation);
    }
    @Inject
    private FacesContext facesContext;
}
