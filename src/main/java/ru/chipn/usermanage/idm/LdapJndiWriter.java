package ru.chipn.usermanage.idm;

import org.picketlink.idm.model.basic.Group;
import org.picketlink.idm.model.basic.User;
import ru.chipn.usermanage.login.Resources;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.ldap.LdapContext;
import java.util.Hashtable;

import static org.picketlink.common.constants.LDAPConstants.CN;

/**
 * Класс в котором скрыты операции записи в LDAP. Средства записи фреймворка
 * PicketLink не могут корректно работать при нестандартной структуре домена,
 * которая используется в приложениях АСКУПЭ-2.0. Поэтому запись в LDAP производится
 * с помощью JNDI службы сервера приложений.
 */

class LdapJndiWriter {
    private LdapContext dtx;

    public LdapJndiWriter(LdapContext ld) throws NamingException {
        dtx = ld;
        Hashtable ht = dtx.getEnvironment();
        StringBuilder sb0 = new StringBuilder();
        String adminPrfx = (String) ht.get("adminPrfx");
        if (adminPrfx != null && adminPrfx.length() > 1) {
            String adminDN = (String) ht.get("adminDN");
            String adminSfx = (String) ht.get("adminSfx");
            sb0.append(adminPrfx);
            sb0.append("=");
            sb0.append(adminDN);
            sb0.append(",");
            sb0.append(adminSfx);
        }
        if (sb0.length()<1) sb0.append(Resources.getParam("bindDNConfiguration"));
        String adminPsw = (String) ht.getOrDefault("adminPsw", Resources.getParam("bindCredential"));
        dtx.addToEnvironment(Context.SECURITY_PRINCIPAL, sb0.toString());
        dtx.addToEnvironment(Context.SECURITY_CREDENTIALS, adminPsw);
    }

    public void operate(User user, Group group, final int dirContextAttribute) throws NamingException{
        Hashtable ht = dtx.getEnvironment();
        StringBuilder groupSb = new StringBuilder(CN);
        groupSb.append("=").append(group.getName());
        groupSb.append(",").append(ht.get("groupsDN"));
        Attribute attr = new BasicAttribute((String) ht.get("attrMember"));
        attr.add(user.getAttribute("org.picketlink.idm.ldap.entry.dn").getValue());
        Attributes attrs = new BasicAttributes();
        attrs.put(attr);
        dtx.modifyAttributes(groupSb.toString(), dirContextAttribute, attrs);
    }
}
