package ru.chipn.usermanage.idm;

import org.picketlink.idm.model.basic.Group;
import org.picketlink.idm.query.AttributeParameter;
import org.picketlink.idm.query.IdentityQuery;
import org.picketlink.idm.query.IdentityQueryBuilder;
import ru.chipn.usermanage.login.AuthorizationManager;
import ru.chipn.usermanage.login.ModuleEnum;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import static org.picketlink.common.constants.LDAPConstants.GROUP_OF_UNIQUE_NAMES;
import static org.picketlink.common.constants.LDAPConstants.OBJECT_CLASS;

/**
 * Created by arkan on 16.09.2016.
 */
@Named
@SessionScoped
public class AppBean implements Serializable {
    public List<SelectItem> getAppSelectList(){
        return appSelectList;
    }

    @PostConstruct
    public void init(){
        appSelectList = Arrays.stream(ModuleEnum.values())
                .filter(moduleEnum-> ModuleEnum.MANAGE_DN!=moduleEnum)
                .map(moduleEnum->new SelectItem(moduleEnum.getModule(), moduleEnum.getDescr()))
                .collect(Collectors.toList());
        moduleFgOptions = new EnumMap<>(ModuleEnum.class);
        moduleTgOptions = new EnumMap<>(ModuleEnum.class);
        final List<Group> groupCuListFg = loadListGroups(ModuleEnum.CU_DN, "fg");
        final List<Group> groupInvList = loadListGroups(ModuleEnum.INV_DN, "fg");
        final List<Group> groupDispList = loadListGroups(ModuleEnum.DISP_DN, "fg");
        final List<Group> groupRepairList = loadListGroups(ModuleEnum.REPAIR_DN, "fg");
        moduleFgOptions.put(ModuleEnum.CU_DN, groupCuListFg);
        moduleFgOptions.put(ModuleEnum.INV_DN, groupInvList);
        moduleFgOptions.put(ModuleEnum.DISP_DN, groupDispList);
        moduleFgOptions.put(ModuleEnum.REPAIR_DN, groupRepairList);
        final List<Group> groupCuListTg = loadListGroups(ModuleEnum.CU_DN, "tg");
        moduleTgOptions.put(ModuleEnum.CU_DN, groupCuListTg);
    }
    public List<SelectItem> fillGroupOptions(final List<Group> listGroup){
        final List<SelectItem> list = listGroup.stream()
                .map(g->new SelectItem(g.getId(),
                        g.getAttribute("description").getValue().toString()
                                .replace("Пользователи", "").replace("района","")
                ))
                .collect(Collectors.toList());
        return list;
    }
    public List<Group> loadListGroups(ModuleEnum moduleEnum, final String businessCategoryValue){
        final IdentityQueryBuilder iqb = authorizationManager.getIdentityManager().getQueryBuilder();
        final IdentityQuery<Group> query = iqb.createIdentityQuery(Group.class);
        final AttributeParameter objectClassParameter = Group.QUERY_ATTRIBUTE.byName(OBJECT_CLASS);
        final AttributeParameter oParameter = Group.QUERY_ATTRIBUTE.byName(LDAPATTRS.ORGANIZATIONNAME.getTxt());
        final AttributeParameter bc = Group.QUERY_ATTRIBUTE.byName("businessCategory");
        final List<Group> group = query.where(
                iqb.equal(objectClassParameter, GROUP_OF_UNIQUE_NAMES),
                iqb.equal(oParameter, moduleEnum.getModule()),
                iqb.equal(bc, businessCategoryValue)
        ).getResultList();
        return group;
    }
    public List<SelectItem> getGroupInvOptions(){
        return this.fillGroupOptions(moduleFgOptions.get(ModuleEnum.INV_DN));
    }
    public List<SelectItem> getGroupCuOptions(){
        return this.fillGroupOptions(moduleFgOptions.get(ModuleEnum.CU_DN));
    }
    public List<SelectItem> getGroupCuTgOptions(){
        return this.fillGroupOptions(moduleTgOptions.get(ModuleEnum.CU_DN));
    }
    public List<SelectItem> getGroupDispOptions(){
        return this.fillGroupOptions(moduleFgOptions.get(ModuleEnum.DISP_DN));
    }
    public List<SelectItem> getGroupRepairOptions(){
        return this.fillGroupOptions(moduleFgOptions.get(ModuleEnum.REPAIR_DN));
    }
    public List<Group> getGroupCuListFg(){
        return moduleFgOptions.get(ModuleEnum.CU_DN);
    }
    public List<Group> getGroupCuListTg(){
        return moduleTgOptions.get(ModuleEnum.CU_DN);
    }
    public List<Group> getGroupInvList(){
        return moduleFgOptions.get(ModuleEnum.INV_DN);
    }
    public List<Group> getGroupDispList(){
        return moduleFgOptions.get(ModuleEnum.DISP_DN);
    }
    public List<Group> getGroupRepairList(){
        return moduleFgOptions.get(ModuleEnum.REPAIR_DN);
    }
    private List<SelectItem> appSelectList;
    private Map<ModuleEnum, List<org.picketlink.idm.model.basic.Group>> moduleFgOptions;
    private Map<ModuleEnum, List<org.picketlink.idm.model.basic.Group>> moduleTgOptions;
    /*
    private List<org.picketlink.idm.model.basic.Group> groupCuListFg;
    private List<org.picketlink.idm.model.basic.Group> groupCuListTg;
    private List<org.picketlink.idm.model.basic.Group> groupInvList;
    private List<org.picketlink.idm.model.basic.Group> groupDispList;
    private List<org.picketlink.idm.model.basic.Group> groupRepairList;
    */
    @Inject
    private AuthorizationManager authorizationManager;
}
