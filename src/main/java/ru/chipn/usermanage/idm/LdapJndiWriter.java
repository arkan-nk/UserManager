package ru.chipn.usermanage.idm;

import org.picketlink.idm.model.basic.Group;
import org.picketlink.idm.model.basic.User;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.*;
import javax.naming.ldap.LdapContext;
import java.util.Hashtable;

import static org.picketlink.common.constants.LDAPConstants.CN;
import static org.picketlink.common.constants.LDAPConstants.UID;

/**
 * Created by arkan on 29.08.2016.
 * It is realisation to write into LDAP and will replace to be use PicketLink
 */
class LdapJndiWriter {
    private LdapContext dtx;

    public LdapJndiWriter(LdapContext ld) throws NamingException {
        dtx = ld;
        Hashtable ht = dtx.getEnvironment();
        StringBuilder sb0 = new StringBuilder();
        String adminPrfx = (String) ht.get("adminPrfx");
        String adminDN = (String) ht.get("adminDN");
        String adminSfx = (String) ht.get("adminSfx");
        String adminPsw = (String) ht.get("adminPsw");
        if (adminPrfx==null || adminPrfx.length()<1) adminPrfx=CN;
        sb0.append(adminPrfx);
        sb0.append("=");
        sb0.append(adminDN!=null && adminDN.length()>1 ? adminDN : "admin");
        sb0.append(",");
        sb0.append(adminSfx!=null && adminSfx.length()>1 ? adminSfx : "dc=a2,dc=chipn,dc=ru");
        dtx.addToEnvironment(Context.SECURITY_PRINCIPAL, sb0.toString());
        dtx.addToEnvironment(Context.SECURITY_CREDENTIALS, adminPsw!=null ? adminPsw : "admin");
    }

    public void removeUserFromGroup(User currentUser, Group group1) throws NamingException {
        System.out.println("removeUserFromGroup");
        Hashtable ht = dtx.getEnvironment();
        StringBuilder groupSb = new StringBuilder(CN);
        groupSb.append("=").append(group1.getName());
        groupSb.append(",").append(ht.get("groupsDN"));
        Attribute attr = new BasicAttribute((String) ht.get("attrMember"));
        attr.add(currentUser.getAttribute("org.picketlink.idm.ldap.entry.dn").getValue());
        Attributes attrs = new BasicAttributes();
        attrs.put(attr);
        dtx.modifyAttributes(groupSb.toString(), DirContext.REMOVE_ATTRIBUTE, attrs);
    }
}
