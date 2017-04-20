package ru.chipn.usermanage.login;

import org.picketlink.Identity;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.RelationshipManager;
import org.picketlink.idm.model.Account;
import org.picketlink.idm.model.basic.BasicModel;
import org.picketlink.idm.model.basic.Role;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

import static org.picketlink.idm.model.basic.BasicModel.getRole;

/**
 * Authorization. Authenticated User must be member of FG_OPERATOR0 role
 * This role in LDAP is cn=fg_operator0, ou=Roles, dc=a2,dc=chipn, dc=ru
 */
@Named
@RequestScoped
public class AuthorizationManager implements Serializable{
    @Inject
    private Instance<Identity> identityInstance;
    @Inject
    private IdentityManager identityManager;
    @Inject
    private RelationshipManager relationshipManager;

    public IdentityManager getIdentityManager(){
        return identityManager;
    }
    public RelationshipManager getRelationshipManager(){
        return relationshipManager;
    }
    public boolean isOperator0() {
        return hasRole(ApplicationRole.FG_OPERATOR0);
    }

    public Identity getIdentity() {
        return this.identityInstance.get();
    }

    /**
     * <p>Checks if the current user is granted with the given role.</p>
     *
     * @param applicationRole
     * @return boolean result
     */
    private boolean hasRole(ApplicationRole applicationRole) {
        Account agent = getIdentity().getAccount();
        Role role = getRole(this.identityManager, applicationRole.name());
        return BasicModel.hasRole(this.relationshipManager, agent, role);
    }
}
