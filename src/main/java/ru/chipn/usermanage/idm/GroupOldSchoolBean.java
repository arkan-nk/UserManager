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
import java.util.Objects;

/**
 * Created by arkan on 09.09.2016.
 */
@Named
@RequestScoped
public class GroupOldSchoolBean implements Serializable {
    @Inject
    private FacesContext facesContext;

    public void addUserToGroup(Group group, User user, String jmxConnStr) throws Exception {
        Objects.requireNonNull(group);
        Objects.requireNonNull(user);
        Objects.requireNonNull(jmxConnStr);
        Objects.requireNonNull(jmxConnStr.length()<1? null : jmxConnStr);
        operate(group,user,jmxConnStr,DirContext.ADD_ATTRIBUTE);
    }
    public void doGetOut(Group group, User user, String jmxConnStr) throws Exception{
        Objects.requireNonNull(group);
        Objects.requireNonNull(user);
        Objects.requireNonNull(jmxConnStr);
        Objects.requireNonNull(jmxConnStr.length()<1? null : jmxConnStr);
        operate(group,user,jmxConnStr,DirContext.REMOVE_ATTRIBUTE);
    }

    private void operate(Group group, User user, String jmxConnStr, final int dirContextOperation) throws Exception {
        InitialContext initialContext = null;
        InitialDirContext initalDirContext = null;
        LdapContext ldapContext = null;
        try {
            initialContext = new InitialContext();
            initalDirContext = (InitialDirContext) initialContext.lookup(jmxConnStr);
            ldapContext = new InitialLdapContext(initalDirContext.getEnvironment(), null);
            LdapJndiWriter ldapWriter = new LdapJndiWriter(ldapContext);
            ldapWriter.operate(user,group,dirContextOperation);
        } catch (NamingException e) {
            facesContext.addMessage(null, new FacesMessage(e.getExplanation()));
        }finally{
            if (ldapContext!=null) ldapContext.close();
            if (initalDirContext!=null) initalDirContext.close();
            if (initialContext!=null) initialContext.close();
        }
    }
}
