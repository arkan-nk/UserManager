package ru.chipn.usermanage.idm;

import org.picketlink.idm.model.Attribute;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Map;

/**
 * Created by arkan on 26.07.2016.
 */
@Named
@ViewScoped
public class UserAttributeBean implements Serializable{
    public void changeAttribute(ValueChangeEvent event){
        LDAPATTRS la = null;
        String attributeLabel = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("attributeLabel");
        if (attributeLabel==null || attributeLabel.trim().length()<1) return;
        switch (attributeLabel.trim()){
            case "telephoneNumber" : la = LDAPATTRS.TELEPHONENUMBER; break;
            case "homePhone" : la = LDAPATTRS.HOMEPHONE; break;
            case "title" : la = LDAPATTRS.TITLE; break;
            case "organization" : la = LDAPATTRS.ORGANIZATIONNAME; break;
            case "description" : la = LDAPATTRS.DESCRIPTION; break;
            case "postalCode" : la = LDAPATTRS.POSTALCODE; break;
            case "postalAddress" : la = LDAPATTRS.POSTALADDRESS;
        }
        String tn = (String) event.getNewValue();
        if (tn==null || tn.trim().length()<1) tn=" ";
        userManagerBean.getCurrentUser().setAttribute(new Attribute<String>(la.getTxt(), tn.trim()));
    }

    @Inject
    private UserManagerBean userManagerBean;

    private String organization;
    private String telephoneNumber;
    private String description;
    private String title;
    private String postalCode;
    private String postalAddress;
    private String mobile;

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
        if (mapUserAttr.containsKey(LDAPATTRS.ORGANIZATIONNAME.getTxt())) {
            this.organization = mapUserAttr.get(LDAPATTRS.ORGANIZATIONNAME.getTxt()).getValue().toString();
        }
        if (mapUserAttr.containsKey(LDAPATTRS.TELEPHONENUMBER.getTxt())) {
            this.telephoneNumber = mapUserAttr.get(LDAPATTRS.TELEPHONENUMBER.getTxt()).getValue().toString();
        }
        if (mapUserAttr.containsKey(LDAPATTRS.TITLE.getTxt())) {
            this.title = mapUserAttr.get(LDAPATTRS.TITLE.getTxt()).getValue().toString();
        }
        if (mapUserAttr.containsKey(LDAPATTRS.DESCRIPTION.getTxt())) {
            this.description = mapUserAttr.get(LDAPATTRS.DESCRIPTION.getTxt()).getValue().toString();
        }
        if (mapUserAttr.containsKey(LDAPATTRS.POSTALCODE.getTxt())) {
            this.postalCode = mapUserAttr.get(LDAPATTRS.POSTALCODE.getTxt()).getValue().toString();
        }
        if (mapUserAttr.containsKey(LDAPATTRS.POSTALADDRESS.getTxt())) {
            this.postalAddress = mapUserAttr.get(LDAPATTRS.POSTALADDRESS.getTxt()).getValue().toString();
        }
        if (mapUserAttr.containsKey(LDAPATTRS.HOMEPHONE.getTxt())) {
            this.mobile = mapUserAttr.get(LDAPATTRS.HOMEPHONE.getTxt()).getValue().toString();
        }
    }
}
