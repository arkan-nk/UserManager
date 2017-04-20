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
 *
 */
@Named
@SessionScoped
public class AppBean implements Serializable {
    /**
     * Список приложений - выбиралка в интерфейсе
     */
    private List<SelectItem> appSelectList;
    /**
     * Все Функциональные группы всех приложений, сгруппированы
     */
    private Map<ModuleEnum, List<Group>> moduleFgOptions;
    /**
     * Все территориальные группы всех приложений сгруппированы
     */
    private Map<ModuleEnum, List<Group>> moduleTgOptions;
    @Inject
    private AuthorizationManager authorizationManager;

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
        final List<Group> groupCuListFg = takeGroups(ModuleEnum.CU_DN, "fg");
        final List<Group> groupInvList = takeGroups(ModuleEnum.INV_DN, "fg");
        final List<Group> groupDispList = takeGroups(ModuleEnum.DISP_DN, "fg");
        final List<Group> groupRepairList = takeGroups(ModuleEnum.REPAIR_DN, "fg");
        moduleFgOptions.put(ModuleEnum.CU_DN, groupCuListFg);
        moduleFgOptions.put(ModuleEnum.INV_DN, groupInvList);
        moduleFgOptions.put(ModuleEnum.DISP_DN, groupDispList);
        moduleFgOptions.put(ModuleEnum.REPAIR_DN, groupRepairList);
        final List<Group> groupCuListTg = takeGroups(ModuleEnum.CU_DN, "tg");
        moduleTgOptions.put(ModuleEnum.CU_DN, groupCuListTg);
    }
    private List<SelectItem> takeSelectItemsOptions(final List<Group> listGroup){
        return listGroup.stream()
                .map(g->new SelectItem(g.getId(),
                        g.getAttribute("description").getValue().toString()
                                .replace("Пользователи", "").replace("района","")
                ))
                .collect(Collectors.toList());
    }
    private List<Group> takeGroups(ModuleEnum moduleEnum, final String businessCategoryValue){
        final IdentityQueryBuilder iqb = authorizationManager.getIdentityManager().getQueryBuilder();
        final IdentityQuery<Group> query = iqb.createIdentityQuery(Group.class);
        final AttributeParameter objectClassParameter = Group.QUERY_ATTRIBUTE.byName(OBJECT_CLASS);
        final AttributeParameter oParameter = Group.QUERY_ATTRIBUTE.byName(LDAPATTRS.ORGANIZATIONNAME.getTxt());
        final AttributeParameter bc = Group.QUERY_ATTRIBUTE.byName("businessCategory");
        return query.where(
                iqb.equal(objectClassParameter, GROUP_OF_UNIQUE_NAMES),
                iqb.equal(oParameter, moduleEnum.getModule()),
                iqb.equal(bc, businessCategoryValue)
        ).getResultList();
    }
    public List<SelectItem> getGroupInvOptions(){
        return this.takeSelectItemsOptions(moduleFgOptions.get(ModuleEnum.INV_DN));
    }
    public List<SelectItem> getGroupCuOptions(){
        return this.takeSelectItemsOptions(moduleFgOptions.get(ModuleEnum.CU_DN));
    }
    public List<SelectItem> getGroupCuTgOptions(){
        return this.takeSelectItemsOptions(moduleTgOptions.get(ModuleEnum.CU_DN));
    }
    public List<SelectItem> getGroupDispOptions(){
        return this.takeSelectItemsOptions(moduleFgOptions.get(ModuleEnum.DISP_DN));
    }
    public List<SelectItem> getGroupRepairOptions(){
        return this.takeSelectItemsOptions(moduleFgOptions.get(ModuleEnum.REPAIR_DN));
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
}
