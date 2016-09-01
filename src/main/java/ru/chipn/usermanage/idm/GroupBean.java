package ru.chipn.usermanage.idm;

import org.picketlink.idm.model.basic.Group;
import org.picketlink.idm.query.AttributeParameter;
import org.picketlink.idm.query.IdentityQuery;
import org.picketlink.idm.query.IdentityQueryBuilder;
import ru.chipn.usermanage.login.AuthorizationManager;
import ru.chipn.usermanage.login.ModuleEnum;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

import static org.picketlink.common.constants.LDAPConstants.GROUP_OF_UNIQUE_NAMES;
import static org.picketlink.common.constants.LDAPConstants.OBJECT_CLASS;

/**
 * Created by arkan on 02.08.2016.
 */
@Named
@SessionScoped
public class GroupBean implements Serializable{

    public String getTxt() {return "Управление пользователями!";}
    private List<Group> groupCuList;
    private List<Group> groupInvList;
    private List<Group> groupDispList;
    private List<Group> groupRepairList;
    @Inject
    private AuthorizationManager authorizationManager;
    @PostConstruct
    public void init(){
        groupCuList = (List<Group>) getListGroups(ModuleEnum.CU_DN);
        groupInvList = (List<Group>)getListGroups(ModuleEnum.INV_DN);
        groupDispList = (List<Group>)getListGroups(ModuleEnum.DISP_DN);
        groupRepairList = (List<Group>)getListGroups(ModuleEnum.REPAIR_DN);
    }
    private List<Group> getListGroups(ModuleEnum moduleEnum){
        IdentityQueryBuilder iqb = authorizationManager.getIdentityManager().getQueryBuilder();
        AttributeParameter objectClassParameter = Group.QUERY_ATTRIBUTE.byName(OBJECT_CLASS);
        AttributeParameter oParameter = Group.QUERY_ATTRIBUTE.byName(LDAPATTRS.ORGANIZATIONNAME.getTxt());
        IdentityQuery<Group> query = iqb.createIdentityQuery(Group.class);
        List<Group> group = query
                .where(
                    iqb.equal(objectClassParameter, GROUP_OF_UNIQUE_NAMES),
                    iqb.equal(oParameter, moduleEnum.getModule())
                )
                .getResultList();
        return group;
    }
    public List<Group> getGroupCuList() {
        return groupCuList;
    }
    public List<Group> getGroupInvList() {
        return groupInvList;
    }
    public List<Group> getGroupDispList() {
        return groupDispList;
    }
    public List<Group> getGroupRepairList() {
        return groupRepairList;
    }
    public String toHome(){
        return "home.xhtml?faces-redirect=true";
    }
    public String toUsers(){
        return "users.xhtml?faces-redrect=true";
    }
}
