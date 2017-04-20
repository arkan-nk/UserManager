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
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Массовые операции с пользователями в группе
 */
@Named
@RequestScoped
@Transactional
public class GroupOldSchoolBean implements Serializable {
    @Inject
    private FacesContext facesContext;

    /**
     * Массово добавить пользователей в группу
     * @param selectedFgroup
     * @param selectedUsers
     * @param jmxConnStr
     * @throws NamingException
     */
    public void massGrant(final Group selectedFgroup, final List<User> selectedUsers,
        final String jmxConnStr) throws NamingException{
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
            initialContext.close();
        }
    }

    /**
     * Массово удалить пользователей из группы
     * @param selectedFgroup
     * @param selectedUsers
     * @param jmxConnStr
     * @throws NamingException
     */
    public void massRevoke(final Group selectedFgroup, final List<User> selectedUsers,
        final String jmxConnStr) throws NamingException{
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
            initialContext.close();
        }
    }

    /**
     * добавить пользователя в группы
     * @param selectedFgroup выбранная ФГ
     * @param selectedTGroupList выбранные ТГ
     * @param user
     * @param jmxConnStr
     * @throws NamingException
     */
    public void addUserToSelectedGroups(final Group selectedFgroup,
        final List<Group> selectedTGroupList, final User user, final String jmxConnStr)
            throws NamingException{
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
            initialContext.close();
        }
    }

    /**
     * Удалить пользователя из группы
     * @param group
     * @param user
     * @param jmxConnStr
     * @throws NamingException
     */
    public void doGetOut(final Group group, final User user, final String jmxConnStr) throws NamingException{
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
            initialContext.close();
        }
    }

    private void operate(InitialContext initialContext, final Group group, final User user,
            final String jmxConnStr, final int dirContextOperation) throws NamingException {
        InitialDirContext initalDirContext = (InitialDirContext) initialContext.lookup(jmxConnStr);
        LdapContext ldapContext = new InitialLdapContext(initalDirContext.getEnvironment(), null);
        LdapJndiWriter ldapWriter = new LdapJndiWriter(ldapContext);
        ldapWriter.operate(user,group,dirContextOperation);
    }
}
