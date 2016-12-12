package ru.chipn.usermanage.login;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.picketlink.Identity;
import org.picketlink.credential.DefaultLoginCredentials;

@Named
@RequestScoped
public class LoginController implements Serializable {
	private static final long serialVersionUID = -4562927943493722628L;
    @Inject
    private AuthorizationManager authorizationManager;

    @Inject
    private Identity identity;
    @Inject
    private FacesContext facesContext;
    @Inject
    private DefaultLoginCredentials loginCredentials;

    public String login() {
        this.identity.login();
        if (!this.identity.isLoggedIn()) {
            this.facesContext.addMessage(null,
                    new FacesMessage("Authentication was unsuccessful.  Please check your username and password " + "before trying again."));
            this.facesContext.getExternalContext().invalidateSession();
            return "";
        }
        boolean result=false;
        try {
            HttpServletRequest request = (HttpServletRequest) this.facesContext.getExternalContext().getRequest();
            request.login(loginCredentials.getUserId(), loginCredentials.getPassword());
            result = authorizationManager.isOperator0();
        } catch (ServletException e) {
            e.printStackTrace();
        }
        if (!result) {
            this.logoutPrivate();
            this.facesContext.addMessage(null,
                    new FacesMessage("Authorization was unsuccessful. Please get permission from administrator to work with the application."));
            this.facesContext.getExternalContext().invalidateSession();
            return "";
        }
        return "idm/users.xhtml?faces-redirect=true";
    }

    public String logout() {
        this.logoutPrivate();
        this.facesContext.getExternalContext().invalidateSession();
        return "/login.xhtml?faces-redirect=true";
    }
    private void logoutPrivate(){
        if (this.identity.isLoggedIn()) this.identity.logout();
        try {
            HttpServletRequest request = (HttpServletRequest) this.facesContext.getExternalContext().getRequest();
            request.logout();
        }catch (ServletException se){
            se.printStackTrace();
        }
    }
	//@Inject private PartitionManager partitionManager;
}