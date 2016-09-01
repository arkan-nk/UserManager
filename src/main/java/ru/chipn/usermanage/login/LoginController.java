package ru.chipn.usermanage.login;

import org.picketlink.Identity;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.PartitionManager;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@RequestScoped
public class LoginController {
    @Inject
    private PartitionManager partitionManager;
    @Inject
    private IdentityManager identityManager;


    @Inject
    private Identity identity;
    @Inject
    private FacesContext facesContext;

    public String login() {
        this.identity.login();
        String result = null;
        if (this.identity.isLoggedIn()) {
            result = "idm/home.xhtml?faces-redirect=true";
        } else {
            this.facesContext.addMessage(null,
                new FacesMessage("Authentication was unsuccessful.  Please check your username and password " + "before trying again."));
        }
        return result;
    }

    public String logout() {
        if (this.identity.isLoggedIn()) this.identity.logout();
        this.facesContext.getExternalContext().invalidateSession();
        return "/login.xhtml";
    }
}