package ru.chipn.manage.login;

/**
 * Created by arkan on 27.06.2016.
 */

import org.picketlink.idm.config.IdentityConfiguration;
import org.picketlink.idm.config.IdentityConfigurationBuilder;
import org.picketlink.idm.credential.UsernamePasswordCredentials;
import org.picketlink.idm.credential.handler.PasswordCredentialHandler;
import org.picketlink.idm.model.IdentityType;
import org.picketlink.idm.model.basic.*;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import static org.picketlink.common.constants.LDAPConstants.*;
import static ru.chipn.manage.login.ConfigurationEnum.*;

@ApplicationScoped
public class IdentityManagementConfiguration {
    /**
     * <p>
     * We use this method to produce a {@link IdentityConfiguration} configured with a LDAP store.
     * </p>
     *
     * @return
     */
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
                //.addCredentialHandler(PasswordCredentialHandler.class)
                .supportType(IdentityType.class)
                .supportGlobalRelationship(Grant.class, GroupMembership.class)
                .mapping(User.class)
                  .baseDN(USERS_OU.getTxt() + BASE_DN.getTxt() + ROOT_DN.getTxt())
                  .objectClasses(LDAPATTRS.INETORGPERSON.getTxt(), LDAPATTRS.ORGANIZATIONALPERSON.getTxt())
                  .attribute("loginName", UID, true)
                  .attribute("firstName", CN)
                  .attribute("lastName", SN)
                  .attribute("email", EMAIL)
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
    private enum LDAPATTRS{
        NAME("name"), INETORGPERSON("inetOrgPerson"), ORGANIZATIONALPERSON("organizationalPerson"),
        CREATEDDATE("createdDate"), UNIQUEMEMBER("uniqueMember");
        private String txt;
        String getTxt(){
            return txt;
        }
        LDAPATTRS(String tt){
            txt = tt;
        }
    }
}
