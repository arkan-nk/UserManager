package ru.chipn.usermanage.idm;

import org.picketlink.idm.model.basic.Group;
import org.picketlink.idm.query.AttributeParameter;
import org.picketlink.idm.query.IdentityQuery;
import org.picketlink.idm.query.IdentityQueryBuilder;
import ru.chipn.usermanage.login.AuthorizationManager;
import ru.chipn.usermanage.login.ModuleEnum;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.picketlink.common.constants.LDAPConstants.GROUP_OF_UNIQUE_NAMES;
import static org.picketlink.common.constants.LDAPConstants.OBJECT_CLASS;

/**
 * Created by arkan on 16.09.2016.
 */
@Named
@ApplicationScoped
public class AppBean {
    public List<SelectItem> getAppSelectList(){
        return appSelectList;
    }
    public Map<ModuleEnum, List<SelectItem>> getModuleFgOptions(){
        return moduleFgOptions;
    }
    public Map<ModuleEnum, List<SelectItem>> getModuleTgOptions(){
        return moduleTgOptions;
    }

    @PostConstruct
    public void init(){
        appSelectList = Arrays.stream(ModuleEnum.values())
                .filter(moduleEnum-> ModuleEnum.MANAGE_DN!=moduleEnum)
                .map(moduleEnum->new SelectItem(moduleEnum.getModule(), moduleEnum.getDescr()))
                .collect(Collectors.toList());
        moduleTgOptions = new HashMap<>();
        moduleFgOptions = new HashMap<>();
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
    public void fillGroupOptions(List<org.picketlink.idm.model.basic.Group> listGroup, ModuleEnum moduleEnum, Map<ModuleEnum, List<SelectItem>> moduleOptions){
        final List<SelectItem> list = listGroup.stream()
                .map(g->new SelectItem(g.getId(),
                        g.getAttribute("description").getValue().toString()
                                .replace("Пользователи", "").replace("района","")
                ))
                .collect(Collectors.toList());
        moduleOptions.put(moduleEnum, list);
    }
    public List<org.picketlink.idm.model.basic.Group> loadListGroups(ModuleEnum moduleEnum, final String businessCategoryValue){
        final IdentityQueryBuilder iqb = authorizationManager.getIdentityManager().getQueryBuilder();
        final IdentityQuery<Group> query = iqb.createIdentityQuery(org.picketlink.idm.model.basic.Group.class);
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
    public List<SelectItem> getGroupInvOptions(){
        return moduleFgOptions.get(ModuleEnum.INV_DN);
    }
    public List<SelectItem> getGroupCuOptions(){
        return moduleFgOptions.get(ModuleEnum.CU_DN);
    }
    public List<SelectItem> getGroupCuTg(){
        return moduleTgOptions.get(ModuleEnum.CU_DN);
    }
    public List<SelectItem> getGroupDispOptions(){
        return moduleFgOptions.get(ModuleEnum.DISP_DN);
    }
    public List<SelectItem> getGroupRepairOptions(){
        return moduleFgOptions.get(ModuleEnum.REPAIR_DN);
    }
    public List<Group> getGroupCuListFg(){
        return groupCuListFg;
    }
    public List<Group> getGroupCuListTg(){
        return groupCuListTg;
    }
    public List<Group> getGroupInvList(){
        return groupInvList;
    }
    public List<Group> getGroupDispList(){
        return groupDispList;
    }
    public List<Group> getGroupRepairList(){
        return groupRepairList;
    }
    private List<SelectItem> appSelectList;
    private Map<ModuleEnum, List<SelectItem>> moduleFgOptions;
    private Map<ModuleEnum, List<SelectItem>> moduleTgOptions;
    private List<org.picketlink.idm.model.basic.Group> groupCuListFg;
    private List<org.picketlink.idm.model.basic.Group> groupCuListTg;
    private List<org.picketlink.idm.model.basic.Group> groupInvList;
    private List<org.picketlink.idm.model.basic.Group> groupDispList;
    private List<org.picketlink.idm.model.basic.Group> groupRepairList;
    @Inject
    private AuthorizationManager authorizationManager;
}
