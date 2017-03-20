package ru.chipn.usermanage.idm;

import org.picketlink.idm.model.Attribute;
import org.picketlink.idm.model.basic.User;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Map;

import static org.picketlink.common.constants.LDAPConstants.OBJECT_CLASS;
import static ru.chipn.usermanage.idm.LDAPATTRS.*;

/**
 * Created by arkan on 26.07.2016.
 */
@Named
@ViewScoped
public class UserAttributeBean implements Serializable{
    @Inject
    private UserManagerBean userManagerBean;
    private String organization;
    private String telephoneNumber;
    private String description;
    private String title;
    private String postalCode;
    private String postalAddress;
    private String mobile;
    private Boolean registered=false;

    public void changeEmail(ValueChangeEvent event){
        String emailString = (String) event.getNewValue();
        if (emailString==null || emailString.trim().length()<1) return;
        registered=false;
        if (userManagerBean.getCurrentUser().getId()==null || userManagerBean.getCurrentUser().getId().length()<1) {
            registered=true;
            User user1 = userManagerBean.getUser(emailString.toLowerCase());
            if (user1 == null) {
                userManagerBean.getCurrentUser().setLoginName(emailString.toLowerCase());
                userManagerBean.getCurrentUser().setAttribute(new Attribute<>(OBJECT_CLASS, new String[]{"top", LDAPATTRS.PERSON.getTxt(), LDAPATTRS.INETORGPERSON.getTxt(), LDAPATTRS.POSIXACCOUNT.getTxt()}));
                userManagerBean.getCurrentUser().setAttribute(new Attribute<>("gidNumber", "0"));
                userManagerBean.getCurrentUser().setAttribute(new Attribute<>("uidNumber", "0"));
                userManagerBean.getCurrentUser().setAttribute(new Attribute<>("homeDirectory", "/home/" + emailString));
                init();
                registered=false;
            }
        }
    }
    public void changeFirstName(ValueChangeEvent event){
        String fN = (String) event.getNewValue();
        userManagerBean.getCurrentUser().setAttribute(new Attribute<>("givenName", fN));
    }
    public void changeAttribute(ValueChangeEvent event){
        LDAPATTRS la = null;
        String stub = " ";
        String attributeLabel = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("attributeLabel");
        if (attributeLabel==null || attributeLabel.trim().length()<1) return;
        switch (attributeLabel.trim()){
            case "telephoneNumber" : {la = TELEPHONENUMBER; stub = "0"; break;}
            case "homePhone" : {la = HOMEPHONE; stub = "0"; break; }
            case "title" : la = TITLE; break;
            case "organization" : la = ORGANIZATIONNAME; break;
            case "description" : la = DESCRIPTION; break;
            case "postalCode" : la = POSTALCODE; break;
            case "postalAddress" : la = POSTALADDRESS; break;
        }
        if (la==null) return;
        String tn = (String) event.getNewValue();
        if (tn==null || tn.trim().length()<1) tn=stub;
        userManagerBean.getCurrentUser().setAttribute(new Attribute<>(la.getTxt(), tn));
    }
    public Boolean getSaveDisabled(){
        return userManagerBean.getCurrentUser().getFirstName()==null ||
               userManagerBean.getCurrentUser().getLastName()==null ||
               userManagerBean.getCurrentUser().getEmail()==null ||
               userManagerBean.getCurrentUser().getFirstName().trim().length()<1 ||
               userManagerBean.getCurrentUser().getLastName().trim().length()<1 ||
               userManagerBean.getCurrentUser().getEmail().trim().length()<1 ||
               (
                  (userManagerBean.getCurrentUser().getId()==null ||
                   userManagerBean.getCurrentUser().getId().length()<1) &&
                   registered
               );
    }



    public String getMobile(){
        return mobile;
    }
    public void setMobile(String txt){
        mobile=txt;
    }
    public String getPostalCode(){
        return postalCode;
    }
    public void setPostalCode(String txt){
        postalCode = txt;
    }
    public String getPostalAddress(){
        return postalAddress;
    }
    public void setPostalAddress(String txt){
        postalAddress = txt;
    }
    public String getTitle(){
        return title;
    }
    public void setTitle(String txt){
        title = txt;
    }
    public Boolean getRegistered(){
        return registered;
    }

    public String getOrganization(){
        return this.organization;
    }
    public String getTelephoneNumber(){
        return this.telephoneNumber;
    }
    public String getDescription(){
        return this.description;
    }

    public void setDescription(String txt) {
        this.description = txt;
    }

    public void setTelephoneNumber(String txt) {
        this.telephoneNumber = txt;
    }
    public void setOrganization(String txt){
        this.organization = txt;
    }

    @PostConstruct
    public void init() {
        Map<String, Attribute<? extends Serializable>> mapUserAttr = userManagerBean.getCurrentUser().getAttributesMap();
        this.organization = this.fill(" ", ORGANIZATIONNAME, mapUserAttr);
        this.title = this.fill(" ", TITLE, mapUserAttr);
        this.description = this.fill(" ", DESCRIPTION, mapUserAttr);
        this.postalCode = this.fill(" ", POSTALCODE, mapUserAttr);
        this.postalAddress = this.fill(" ", POSTALADDRESS, mapUserAttr);
        this.mobile = this.fill("0", HOMEPHONE, mapUserAttr);
        this.telephoneNumber = this.fill("0", TELEPHONENUMBER, mapUserAttr);
    }
    private String fill(String defaultValue, LDAPATTRS la, Map<String, Attribute<? extends Serializable>> mapUserAttr){
        String value;
        if (mapUserAttr.containsKey(la.getTxt())) {
            value = mapUserAttr.get(la.getTxt()).getValue().toString();
        } else {
            userManagerBean.getCurrentUser().setAttribute(new Attribute<>(la.getTxt(), defaultValue));
            value=defaultValue;
        }
        return value;
    }
}
