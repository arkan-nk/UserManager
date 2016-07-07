package ru.chipn.manage.login;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.picketlink.Identity;

@Named
@RequestScoped
public class LoginController {

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
        this.identity.logout();
        return "/login.xhtml";
    }
}