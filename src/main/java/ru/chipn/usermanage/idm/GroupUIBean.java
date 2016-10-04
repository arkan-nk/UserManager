package ru.chipn.usermanage.idm;

import org.primefaces.event.ToggleEvent;
import org.primefaces.model.Visibility;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 * Created by arkan on 04.10.2016.
 */
@Named
@ViewScoped
public class GroupUIBean implements Serializable {
    private Boolean collapsed = false;
    public Boolean getCollapsed(){
        return collapsed;
    }
    public void setCollapsed(Boolean collapsed) {
        this.collapsed = collapsed;
    }
    public void onCollapse(ToggleEvent event) {
        this.collapsed= event.getVisibility().equals(Visibility.VISIBLE);
    }

}
