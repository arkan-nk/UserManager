package ru.chipn.usermanage.login;

/**
 * Created by arkan on 27.06.2016.
 */

import org.picketlink.idm.config.IdentityConfiguration;
import org.picketlink.idm.config.IdentityConfigurationBuilder;
import org.picketlink.idm.credential.encoder.SHAPasswordEncoder;
import org.picketlink.idm.credential.handler.PasswordCredentialHandler;
import org.picketlink.idm.model.IdentityType;
import org.picketlink.idm.model.basic.*;
import ru.chipn.usermanage.idm.LDAPATTRS;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import static org.picketlink.common.constants.LDAPConstants.*;
import static ru.chipn.usermanage.login.ConfigurationEnum.*;

@ApplicationScoped
public class IdentityManagementConfiguration {

    @Produces
    public IdentityConfiguration configure() {
        IdentityConfigurationBuilder builder = new IdentityConfigurationBuilder();
        builder.named("default")
                .stores()
                .ldap()
                .baseDN(BASE_DN.getTxt() + ROOT_DN.getTxt())
                .bindDN("cn=admin," + BASE_DN.getTxt() + ROOT_DN.getTxt())
                .bindCredential("admin")
                .url(LDAP_URL.getTxt())
                .supportCredentials(true)
                .supportType(IdentityType.class)
                .supportGlobalRelationship(Grant.class, GroupMembership.class)
                .setCredentialHandlerProperty(PasswordCredentialHandler.PASSWORD_ENCODER, new SHAPasswordEncoder(4))

                .mapping(User.class)
                .baseDN(USERS_OU.getTxt() + BASE_DN.getTxt() + ROOT_DN.getTxt())
                .objectClasses("top", LDAPATTRS.INETORGPERSON.getTxt(), LDAPATTRS.PERSON.getTxt(), LDAPATTRS.POSIXACCOUNT.getTxt())
                .attribute("loginName", UID, true)
                .attribute("firstName", CN)
                .attribute("lastName", SN)
                .attribute("email", EMAIL)
                .attribute("gidNumber", "gidNumber")
                .attribute("uidNumber", "uidNumber")
                .attribute("homeDirectory", "homeDirectory")
                .attribute(LDAPATTRS.DESCRIPTION.getTxt(), LDAPATTRS.DESCRIPTION.getTxt())
                .attribute(LDAPATTRS.TITLE.getTxt(), LDAPATTRS.TITLE.getTxt())
                .attribute(LDAPATTRS.ORGANIZATIONNAME.getTxt(), LDAPATTRS.ORGANIZATIONNAME.getTxt())
                .attribute(LDAPATTRS.TELEPHONENUMBER.getTxt(), LDAPATTRS.TELEPHONENUMBER.getTxt())
                .attribute(LDAPATTRS.HOMEPHONE.getTxt(), LDAPATTRS.HOMEPHONE.getTxt())
                .attribute(LDAPATTRS.POSTALCODE.getTxt(), LDAPATTRS.POSTALCODE.getTxt())
                .attribute(LDAPATTRS.POSTALADDRESS.getTxt(), LDAPATTRS.POSTALADDRESS.getTxt())
                .readOnlyAttribute(LDAPATTRS.CREATEDDATE.getTxt(), CREATE_TIMESTAMP)
                .mapping(Role.class)
                .baseDN(ROLES_OU.getTxt() + ModuleEnum.MANAGE_DN.getTxt() + BASE_DN.getTxt() + ROOT_DN.getTxt())
                .objectClasses("top", GROUP_OF_UNIQUE_NAMES)
                .attribute(LDAPATTRS.NAME.getTxt(), CN, true)
                .readOnlyAttribute(LDAPATTRS.CREATEDDATE.getTxt(), CREATE_TIMESTAMP)
                .mapping(Grant.class).forMapping(Role.class)
                .attribute("assignee", LDAPATTRS.UNIQUEMEMBER.getTxt())

                .mapping(Group.class)
                .baseDN(BASE_DN.getTxt() + ROOT_DN.getTxt())
                /*
                .baseDN(GROUPS_OU.getTxt() + ModuleEnum.CU_DN.getTxt() + BASE_DN.getTxt() + ROOT_DN.getTxt())
                .baseDN(GROUPS_OU.getTxt() + ModuleEnum.INV_DN.getTxt() + BASE_DN.getTxt() + ROOT_DN.getTxt())
                .baseDN(GROUPS_OU.getTxt() + ModuleEnum.DISP_DN.getTxt() + BASE_DN.getTxt() + ROOT_DN.getTxt())
                .baseDN(GROUPS_OU.getTxt() + ModuleEnum.REPAIR_DN.getTxt() + BASE_DN.getTxt() + ROOT_DN.getTxt())
                */
                .objectClasses("top", GROUP_OF_UNIQUE_NAMES)
                .attribute(LDAPATTRS.NAME.getTxt(), CN, true)
                .attribute("businessCategory", "businessCategory")
                .attribute(LDAPATTRS.DESCRIPTION.getTxt(), LDAPATTRS.DESCRIPTION.getTxt())
                .attribute(LDAPATTRS.ORGANIZATIONNAME.getTxt(), LDAPATTRS.ORGANIZATIONNAME.getTxt())
                .attribute("member", LDAPATTRS.UNIQUEMEMBER.getTxt())
                .readOnlyAttribute(LDAPATTRS.CREATEDDATE.getTxt(), CREATE_TIMESTAMP)
                .attribute("ou", "ou")
                .parentMembershipAttributeName("member")
                .mapping(GroupMembership.class).forMapping(Group.class)
                .attribute("member", LDAPATTRS.UNIQUEMEMBER.getTxt());
        return builder.build();
    }

    private IdentityConfiguration configure(ModuleEnum moduleEnum) {
        IdentityConfigurationBuilder builder = new IdentityConfigurationBuilder();
        builder.named(moduleEnum.getModule())
                .stores()
                .ldap()
                .baseDN(BASE_DN.getTxt() + ROOT_DN.getTxt())
                .bindDN("cn=admin," + BASE_DN.getTxt() + ROOT_DN.getTxt())
                .bindCredential("admin")
                .url(LDAP_URL.getTxt())
                //.supportCredentials(true)
                .supportType(IdentityType.class)
                .supportGlobalRelationship(Grant.class, GroupMembership.class)
                .mapping(Group.class)
                .baseDN(GROUPS_OU.getTxt() + moduleEnum.getTxt() + BASE_DN.getTxt() + ROOT_DN.getTxt())
                .objectClasses("top", GROUP_OF_UNIQUE_NAMES)
                .attribute(LDAPATTRS.NAME.getTxt(), CN, true)
                .attribute("businessCategory", "businessCategory")
                .attribute(LDAPATTRS.DESCRIPTION.getTxt(), LDAPATTRS.DESCRIPTION.getTxt())
                .attribute(LDAPATTRS.ORGANIZATIONNAME.getTxt(), LDAPATTRS.ORGANIZATIONNAME.getTxt())
                .attribute("member", LDAPATTRS.UNIQUEMEMBER.getTxt())
                .readOnlyAttribute(LDAPATTRS.CREATEDDATE.getTxt(), CREATE_TIMESTAMP)
                .attribute("ou", "ou")
                .parentMembershipAttributeName("member")
                .mapping(GroupMembership.class).forMapping(Group.class)
                .attribute("member", LDAPATTRS.UNIQUEMEMBER.getTxt());
        return builder.build();
    }
}