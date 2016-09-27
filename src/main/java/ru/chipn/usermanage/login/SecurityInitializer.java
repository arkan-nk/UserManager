package ru.chipn.usermanage.login;

import org.picketlink.idm.PartitionManager;
import org.picketlink.idm.config.IdentityConfiguration;
import org.picketlink.idm.config.IdentityConfigurationBuilder;
import org.picketlink.idm.credential.handler.PasswordCredentialHandler;
import org.picketlink.idm.internal.DefaultPartitionManager;
import org.picketlink.idm.model.IdentityType;
import org.picketlink.idm.model.basic.*;
import ru.chipn.usermanage.idm.AppBean;
import ru.chipn.usermanage.idm.LDAPATTRS;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.util.List;

import static org.picketlink.common.constants.LDAPConstants.*;
import static ru.chipn.usermanage.login.ConfigurationEnum.*;

/**
 * Created by arkan on 30.08.2016.
 */
@Startup
@Singleton
public class SecurityInitializer {
    @Inject
    private PartitionManager partitionManager;
    @PostConstruct
    public void init(){

    }
}
