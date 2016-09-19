package ru.chipn.usermanage.idm;

import org.picketlink.idm.model.basic.Group;
import org.picketlink.idm.query.AttributeParameter;
import org.picketlink.idm.query.IdentityQuery;
import org.picketlink.idm.query.IdentityQueryBuilder;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.Visibility;
import ru.chipn.usermanage.login.AuthorizationManager;
import ru.chipn.usermanage.login.ModuleEnum;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.picketlink.common.constants.LDAPConstants.GROUP_OF_UNIQUE_NAMES;
import static org.picketlink.common.constants.LDAPConstants.OBJECT_CLASS;

/**
 * Created by arkan on 02.08.2016.
 */
@Named
@SessionScoped
public class GroupBean implements Serializable{
    /*
    public GroupBean(){
        moduleTgOptions = new HashMap<>();
        moduleFgOptions = new HashMap<>();
    }

    @PostConstruct
    public void init(){
        groupCuListFg = loadListGroups(ModuleEnum.CU_DN, "fg");
        groupInvList = loadListGroups(ModuleEnum.INV_DN, "fg");
        groupDispList = loadListGroups(ModuleEnum.DISP_DN, "fg");
        groupRepairList = loadListGroups(ModuleEnum.REPAIR_DN, "fg");
        groupCuListTg = loadListGroups(ModuleEnum.CU_DN, "tg");
        this.fillGroupOptions(groupInvList, ModuleEnum.INV_DN, moduleFgOptions);
        this.fillGroupOptions(groupCuListFg, ModuleEnum.CU_DN, moduleFgOptions);
        this.fillGroupOptions(groupCuListTg, ModuleEnum.CU_DN, moduleTgOptions);
        this.fillGroupOptions(groupDispList, ModuleEnum.DISP_DN, moduleFgOptions);
        this.fillGroupOptions(groupRepairList, ModuleEnum.REPAIR_DN, moduleFgOptions);
    }
    */


    public void onCollapse(ToggleEvent event) {
        this.collapsed= event.getVisibility().equals(Visibility.VISIBLE);
    }
    public String getNameSelectedGroup(){
        if (groupId==null) return null;
        String nameGroup = this.findName(ModuleEnum.CU_DN);
        if (nameGroup==null) nameGroup=this.findName(ModuleEnum.INV_DN);
        if (nameGroup==null) nameGroup=this.findName(ModuleEnum.DISP_DN);
        if (nameGroup==null) nameGroup=this.findName(ModuleEnum.REPAIR_DN);
        return nameGroup;
    }
    private String findName(ModuleEnum moduleEnum){
        List<SelectItem> listToFind = appBean.getModuleFgOptions().get(moduleEnum);
        if (listToFind==null) appBean.getModuleTgOptions().get(moduleEnum);
        if (listToFind==null) return null;
        final SelectItem selectedItem = listToFind.stream()
                .filter(si->si.getValue().equals(groupId)).findFirst().get();
        return selectedItem!=null ? selectedItem.getLabel(): null;
    }
    public Group getSelectedGroup(){
        if (groupId==null) return null;
        Group selectedGroup = null;
        selectedGroup = appBean.getGroupCuListFg().stream().filter(gr->gr.getId().equals(groupId)).findFirst().get();
        if (selectedGroup!=null) return selectedGroup;
        selectedGroup = appBean.getGroupInvList().stream().filter(gr->gr.getId().equals(groupId)).findFirst().get();
        if (selectedGroup!=null) return selectedGroup;
        selectedGroup = appBean.getGroupDispList().stream().filter(gr->gr.getId().equals(groupId)).findFirst().get();
        if (selectedGroup!=null) return selectedGroup;
        selectedGroup = appBean.getGroupRepairList().stream().filter(gr->gr.getId().equals(groupId)).findFirst().get();
        if (selectedGroup!=null) return selectedGroup;
        selectedGroup = appBean.getGroupCuListTg().stream().filter(gr->gr.getId().equals(groupId)).findFirst().get();
        return selectedGroup;
    }


    /*
    public void fillGroupOptions(List<org.picketlink.idm.model.basic.Group> listGroup, ModuleEnum moduleEnum, Map<ModuleEnum, List<SelectItem>> moduleOptions){
        final List<SelectItem> list = listGroup.stream()
                .map(g->new SelectItem(g.getId(), g.getAttribute("description").getValue().toString()))
                .collect(Collectors.toList());
        moduleOptions.put(moduleEnum, list);
    }
    public List<org.picketlink.idm.model.basic.Group> loadListGroups(ModuleEnum moduleEnum, final String businessCategoryValue){
        final IdentityQueryBuilder iqb = authorizationManager.getIdentityManager().getQueryBuilder();
        final IdentityQuery<org.picketlink.idm.model.basic.Group> query = iqb.createIdentityQuery(org.picketlink.idm.model.basic.Group.class);
        final AttributeParameter objectClassParameter = org.picketlink.idm.model.basic.Group.QUERY_ATTRIBUTE.byName(OBJECT_CLASS);
        final AttributeParameter oParameter = org.picketlink.idm.model.basic.Group.QUERY_ATTRIBUTE.byName(LDAPATTRS.ORGANIZATIONNAME.getTxt());
        final AttributeParameter bc = org.picketlink.idm.model.basic.Group.QUERY_ATTRIBUTE.byName("businessCategory");
        final List<org.picketlink.idm.model.basic.Group> group = query.where(
             iqb.equal(objectClassParameter, GROUP_OF_UNIQUE_NAMES),
             iqb.equal(oParameter, moduleEnum.getModule()),
             iqb.equal(bc, businessCategoryValue)
        ).getResultList();
        return group;
    }
    */

    public String toHome(){
        return "home.xhtml?faces-redirect=true";
    }
    public String toUsers(){
        return "users.xhtml?faces-redrect=true";
    }
    public String getGroupId(){
        return groupId;
    }
    public void setGroupId(String groupId1){
        groupId=groupId1;
    }
    public Boolean getCollapsed(){
        return collapsed;
    }
    public void setCollapsed(Boolean collapsed) {
        this.collapsed = collapsed;
    }
    public String getTxt() {return "Управление пользователями!";}
    /*
    private Map<ModuleEnum, List<SelectItem>> moduleFgOptions;
    private Map<ModuleEnum, List<SelectItem>> moduleTgOptions;
    private List<org.picketlink.idm.model.basic.Group> groupCuListFg;
    private List<org.picketlink.idm.model.basic.Group> groupCuListTg;
    private List<org.picketlink.idm.model.basic.Group> groupInvList;
    private List<org.picketlink.idm.model.basic.Group> groupDispList;
    private List<org.picketlink.idm.model.basic.Group> groupRepairList;
    */
    private Boolean collapsed = false;
    private String groupId;
    @Inject
    private AppBean appBean;
}
