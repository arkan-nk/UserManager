package ru.chipn.usermanage.idm;

import org.junit.Test;
import org.picketbox.test.ldap.AbstractLDAPTest;

/**
 * Created by arkan on 20.06.2016.
 */
public class LDAPServer extends AbstractLDAPTest {
    private LDAPServer ldapServer;
    @Test
    public void startServer() throws Exception {
        super.importLDIF("domain.ldif");

        while (true) {
        }
    }
}

