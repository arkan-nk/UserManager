package ru.chipn.usermanage.login;

import org.picketlink.Identity;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.PartitionManager;
import org.picketlink.idm.RelationshipManager;
import org.picketlink.idm.model.Account;
import org.picketlink.idm.model.Partition;
import org.picketlink.idm.model.basic.BasicModel;
import org.picketlink.idm.model.basic.Realm;
import org.picketlink.idm.model.basic.Role;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

import static org.picketlink.idm.model.basic.BasicModel.getRole;

@Named
@RequestScoped
public class AuthorizationManager implements Serializable{
    @Inject
    private Instance<Identity> identityInstance;
    @Inject
    private PartitionManager partitionManager;
    @Inject
    private IdentityManager identityManager;
    @Inject
    private RelationshipManager relationshipManager;
    /*
    @Inject
    private AttributedTypeManager attributedTypeManager;
    /*
    public boolean isAdmin0() {
        return hasRole(ApplicationRole.FG_ADMIN0);
    }
    */
    public PartitionManager getPartitionManager(){
        return partitionManager;
    }
    public RelationshipManager getRelationshipManager(){
        return relationshipManager;
    }
    public IdentityManager getIdentityManager(){
        return identityManager;
    }
    public boolean isOperator0() {
        return hasRole(ApplicationRole.FG_OPERATOR0);
    }
    /*
    public boolean isViewer0() {
        return hasRole(ApplicationRole.FG_VIEWER0);
    }
    */
    private Identity getIdentity() {
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
