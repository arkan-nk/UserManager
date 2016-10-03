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
    @Inject
    private GroupBean groupBean;
    @Inject
    private FacesContext facesContext;
    public void addUserToSelectedGroups(User user, String jmxConnStr) throws Exception{
        Objects.requireNonNull(user);
        Objects.requireNonNull(jmxConnStr);
        Objects.requireNonNull(jmxConnStr.length()<1? null : jmxConnStr);
        InitialContext initialContext = new InitialContext();
        try {
            List<Group> groups = new ArrayList<>();
            if (groupBean.getSelectedFgroup()!=null) groups.add(groupBean.getSelectedFgroup());
            if (groupBean.getSelectedTGroupList()!=null && !groupBean.getSelectedTGroupList().isEmpty()) groups.addAll(groupBean.getSelectedTGroupList());
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
            groupBean.clearSelected();
        }
    }
    /*
    public void addUserToGroup(Group group, User user, String jmxConnStr) throws Exception {
        Objects.requireNonNull(group);
        Objects.requireNonNull(user);
        Objects.requireNonNull(jmxConnStr);
        Objects.requireNonNull(jmxConnStr.length()<1? null : jmxConnStr);
        operate(group,user,jmxConnStr,DirContext.ADD_ATTRIBUTE);
    }
    */

    public void doGetOut(Group group, User user, String jmxConnStr) throws Exception{
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
}
