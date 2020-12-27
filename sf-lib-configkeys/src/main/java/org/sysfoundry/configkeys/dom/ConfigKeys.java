package org.sysfoundry.configkeys.dom;

import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.services.repository.RepositoryService;
import org.apache.isis.applib.services.user.UserService;
import org.apache.isis.persistence.jdo.applib.services.IsisJdoSupport_v3_2;

import javax.inject.Inject;
import javax.inject.Named;
import javax.jdo.JDOQLTypedQuery;
import java.util.List;

@DomainService(nature = NatureOfService.VIEW,objectType = "sf.solution.ConfigKeys")
@lombok.RequiredArgsConstructor(onConstructor_ = {@Inject} )
public class ConfigKeys {

    @Inject
    private final RepositoryService repositoryService;

    @Inject
    private final UserService userService;

    @Inject
    IsisJdoSupport_v3_2 jdoSupport;

    @Action(semantics = SemanticsOf.NON_IDEMPOTENT)
    public ConfigKey create(String name,String type,Boolean multiValued,String defaultValue,@ParameterLayout(named="Is Preference") boolean preference){
        return repositoryService.persist(ConfigKey.from(name,type,multiValued,defaultValue,preference));
    }

    public boolean hideCreate(){
        return !userService.getUser().hasRoleName("admin");
    }
    public List<String> choices1Create(){
        return ConfigKey.dataTypes;
    }

    @Action(semantics = SemanticsOf.SAFE)
    public List<ConfigKey> findByName(String name){
        JDOQLTypedQuery<ConfigKey> configKeyJDOQLTypedQuery = jdoSupport.newTypesafeQuery(ConfigKey.class);
        QConfigKey candidate = QConfigKey.candidate();
        configKeyJDOQLTypedQuery = configKeyJDOQLTypedQuery.filter(
                candidate.name.eq(configKeyJDOQLTypedQuery.stringParameter("name"))
        );
        return configKeyJDOQLTypedQuery.setParameter("name", name)
                .executeList();

    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(named = "List of Config Keys")
    public List<ConfigKey> listAll(){
        return repositoryService.allInstances(ConfigKey.class);
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(named = "List of Preference Keys")
    public List<ConfigKey> listPreferences() {
        JDOQLTypedQuery<ConfigKey> configKeyJDOQLTypedQuery = jdoSupport.newTypesafeQuery(ConfigKey.class);
        QConfigKey candidate = QConfigKey.candidate();
        configKeyJDOQLTypedQuery = configKeyJDOQLTypedQuery.filter(
                candidate.preference.eq(true)
        );
        return configKeyJDOQLTypedQuery
                .executeList();
    }
}
