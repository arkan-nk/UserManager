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

import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import static org.picketlink.common.constants.LDAPConstants.*;

/**
 * Configuration of the Application. This class works on the application start-phase
 */
@Startup
@ApplicationScoped
public class IdentityManagementConfiguration {

    @Produces
    public IdentityConfiguration configure() {
        final String configurationName = Resources.getParam("configurationName");
        final String baseDNConfiguration=Resources.getParam("baseDNConfiguration");
        final String bindDNConfiguration=Resources.getParam("bindDNConfiguration");
        final String bindCredential=Resources.getParam("bindCredential");
        final String ldapUrl=Resources.getParam("ldapUrl");
        final String usersBase=Resources.getParam("usersBase");
        final String rolesBase=Resources.getParam("rolesBase");
        final String groupsBase=Resources.getParam("groupsBase");
        IdentityConfigurationBuilder builder = new IdentityConfigurationBuilder();
        builder.named(configurationName)
                .stores()
                .ldap()
                .baseDN(baseDNConfiguration)
                .bindDN(bindDNConfiguration)
                .bindCredential(bindCredential)
                .url(ldapUrl)
                .supportCredentials(true)
                .supportType(IdentityType.class)
                .supportGlobalRelationship(Grant.class, GroupMembership.class)
                .setCredentialHandlerProperty(PasswordCredentialHandler.PASSWORD_ENCODER, new SHAPasswordEncoder(512))
                .mapping(User.class)
                .baseDN(usersBase)
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
                .baseDN(rolesBase)
                .objectClasses("top", GROUP_OF_UNIQUE_NAMES)
                .attribute(LDAPATTRS.NAME.getTxt(), CN, true)
                .readOnlyAttribute(LDAPATTRS.CREATEDDATE.getTxt(), CREATE_TIMESTAMP)

                .mapping(Group.class)
                .baseDN(groupsBase)
                .hierarchySearchDepth(8)
                .objectClasses("top", GROUP_OF_UNIQUE_NAMES)
                .attribute(LDAPATTRS.NAME.getTxt(), CN, true)
                .attribute("businessCategory", "businessCategory")
                .attribute(LDAPATTRS.DESCRIPTION.getTxt(), LDAPATTRS.DESCRIPTION.getTxt())
                .attribute(LDAPATTRS.ORGANIZATIONNAME.getTxt(), LDAPATTRS.ORGANIZATIONNAME.getTxt())
                .attribute("member", LDAPATTRS.UNIQUEMEMBER.getTxt())
                .readOnlyAttribute(LDAPATTRS.CREATEDDATE.getTxt(), CREATE_TIMESTAMP)
                .attribute("ou", "ou")
                .parentMembershipAttributeName("member")


                .mapping(Grant.class).forMapping(Role.class)
                //.baseDN(ROLES_OU.getTxt() + ModuleEnum.MANAGE_DN.getTxt() + BASE_DN.getTxt() + ROOT_DN.getTxt())
                //.objectClasses("top", GROUP_OF_UNIQUE_NAMES, LDAPATTRS.INETORGPERSON.getTxt(), LDAPATTRS.PERSON.getTxt(), LDAPATTRS.POSIXACCOUNT.getTxt())
                //.objectClasses("top", GROUP_OF_UNIQUE_NAMES)
                .attribute("assignee", LDAPATTRS.UNIQUEMEMBER.getTxt())

                .mapping(GroupMembership.class).forMapping(Group.class)
                //.hierarchySearchDepth(8)
                //.baseDN(BASE_DN.getTxt() + ROOT_DN.getTxt())
                //.objectClasses("top", GROUP_OF_UNIQUE_NAMES, LDAPATTRS.INETORGPERSON.getTxt(), LDAPATTRS.PERSON.getTxt(), LDAPATTRS.POSIXACCOUNT.getTxt())
                //.objectClasses("top", GROUP_OF_UNIQUE_NAMES)
                .attribute("member", LDAPATTRS.UNIQUEMEMBER.getTxt());


        return builder.build();
    }

}