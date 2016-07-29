package ru.chipn.usermanage.login;

/**
 * Created by arkan on 27.06.2016.
 */

import org.picketlink.idm.config.IdentityConfiguration;
import org.picketlink.idm.config.IdentityConfigurationBuilder;
import org.picketlink.idm.model.IdentityType;
import org.picketlink.idm.model.basic.Grant;
import org.picketlink.idm.model.basic.GroupMembership;
import org.picketlink.idm.model.basic.Role;
import org.picketlink.idm.model.basic.User;
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
                .mapping(User.class)
                  .baseDN(USERS_OU.getTxt() + BASE_DN.getTxt() + ROOT_DN.getTxt())
                  .objectClasses(LDAPATTRS.INETORGPERSON.getTxt(), LDAPATTRS.ORGANIZATIONALPERSON.getTxt())
                  .attribute("loginName", UID, true)
                  .attribute("firstName", CN)
                  .attribute("lastName", SN)
                  .attribute("email", EMAIL)
                  .attribute(LDAPATTRS.DESCRIPTION.getTxt(), LDAPATTRS.DESCRIPTION.getTxt())
                  .attribute(LDAPATTRS.TITLE.getTxt(), LDAPATTRS.TITLE.getTxt())
                  .attribute(LDAPATTRS.ORGANIZATIONNAME.getTxt(), LDAPATTRS.ORGANIZATIONNAME.getTxt())
                  .attribute(LDAPATTRS.TELEPHONENUMBER.getTxt(), LDAPATTRS.TELEPHONENUMBER.getTxt())
                  .attribute(LDAPATTRS.HOMEPHONE.getTxt(), LDAPATTRS.HOMEPHONE.getTxt())
                  .attribute(LDAPATTRS.POSTALCODE.getTxt(), LDAPATTRS.POSTALCODE.getTxt())
                  .attribute(LDAPATTRS.POSTALADDRESS.getTxt(), LDAPATTRS.POSTALADDRESS.getTxt())
                  .readOnlyAttribute(LDAPATTRS.CREATEDDATE.getTxt(), CREATE_TIMESTAMP)
                .mapping(Role.class)
                  .baseDN(GROUPS_OU.getTxt() + ModuleEnum.INV_DN.getTxt() + BASE_DN.getTxt() + ROOT_DN.getTxt())
                  .objectClasses(GROUP_OF_UNIQUE_NAMES)
                  .attribute(LDAPATTRS.NAME.getTxt(), CN, true)
                  .readOnlyAttribute(LDAPATTRS.CREATEDDATE.getTxt(), CREATE_TIMESTAMP)
                  .parentMembershipAttributeName(LDAPATTRS.UNIQUEMEMBER.getTxt())
                .mapping(Role.class)
                  .baseDN(GROUPS_OU.getTxt() + ModuleEnum.CU_DN.getTxt() + BASE_DN.getTxt() + ROOT_DN.getTxt())
                  .objectClasses(GROUP_OF_UNIQUE_NAMES)
                  .attribute(LDAPATTRS.NAME.getTxt(), CN, true)
                  .readOnlyAttribute(LDAPATTRS.CREATEDDATE.getTxt(), CREATE_TIMESTAMP)
                  .parentMembershipAttributeName(LDAPATTRS.UNIQUEMEMBER.getTxt())
                .mapping(Role.class)
                  .baseDN(GROUPS_OU.getTxt() + ModuleEnum.DISP_DN.getTxt() + BASE_DN.getTxt() + ROOT_DN.getTxt())
                  .objectClasses(GROUP_OF_UNIQUE_NAMES)
                  .attribute(LDAPATTRS.NAME.getTxt(), CN, true)
                  .readOnlyAttribute(LDAPATTRS.CREATEDDATE.getTxt(), CREATE_TIMESTAMP)
                  .parentMembershipAttributeName(LDAPATTRS.UNIQUEMEMBER.getTxt())
                .mapping(Role.class)
                  .baseDN(GROUPS_OU.getTxt() + ModuleEnum.REPAIR_DN.getTxt() + BASE_DN.getTxt() + ROOT_DN.getTxt())
                  .objectClasses(GROUP_OF_UNIQUE_NAMES)
                  .attribute(LDAPATTRS.NAME.getTxt(), CN, true)
                  .readOnlyAttribute(LDAPATTRS.CREATEDDATE.getTxt(), CREATE_TIMESTAMP)
                  .parentMembershipAttributeName(LDAPATTRS.UNIQUEMEMBER.getTxt())
                .mapping(Role.class)
                  .baseDN(ROLES_OU.getTxt() + ModuleEnum.MANAGE_DN.getTxt() + BASE_DN.getTxt() + ROOT_DN.getTxt())
                  .objectClasses(GROUP_OF_UNIQUE_NAMES)
                  .attribute(LDAPATTRS.NAME.getTxt(), CN, true)
                  .readOnlyAttribute(LDAPATTRS.CREATEDDATE.getTxt(), CREATE_TIMESTAMP)
                  .parentMembershipAttributeName(LDAPATTRS.UNIQUEMEMBER.getTxt())
                .mapping(Grant.class)
                   .forMapping(Role.class)
                   .attribute("assignee", LDAPATTRS.UNIQUEMEMBER.getTxt());
        return builder.build();
    }
}
