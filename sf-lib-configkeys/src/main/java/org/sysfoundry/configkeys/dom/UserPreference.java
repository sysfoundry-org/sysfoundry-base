package org.sysfoundry.configkeys.dom;

import lombok.Getter;
import lombok.Setter;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.services.message.MessageService;
import org.apache.isis.applib.services.repository.RepositoryService;
import org.apache.isis.applib.services.title.TitleService;

import javax.inject.Inject;
import javax.jdo.annotations.*;


@DomainObject
@DomainObjectLayout(cssClassFa = "fa-sliders-h")
@PersistenceCapable(identityType = IdentityType.DATASTORE,schema = "configkeys")
@DatastoreIdentity(strategy = IdGeneratorStrategy.IDENTITY,column = "id")
@Version(strategy = VersionStrategy.DATE_TIME,column = "version")
@Unique(name = "UserPreference_configKey_userId_UNQ",members = {"configKey","userId"})
public class UserPreference {

    @Getter @Setter
    @Property(editing = Editing.DISABLED)
    private ConfigKey configKey;

    @Getter @Setter
    @Property(editing = Editing.ENABLED)
    private String value;

    @Getter @Setter
    @Property(editing = Editing.DISABLED)
    private String userId;

    @Inject
    TitleService titleService;
    @Inject
    MessageService messageService;

    @Inject
    RepositoryService repositoryService;


    public UserPreference(ConfigKey configKey,String value,String userId){
        this.configKey = configKey;
        this.value = value;
        this.userId = userId;
    }

    public UserPreference(){
    }

    public static UserPreference from(String userId,ConfigKey configKey,String value){
        return new UserPreference(configKey,value,userId);
    }

    @Action(semantics = SemanticsOf.NON_IDEMPOTENT_ARE_YOU_SURE)
    public void delete(){
        final String title = titleService.titleOf(this);
        messageService.informUser(String.format("'%s' deleted", title));
        repositoryService.removeAndFlush(this);
    }

    public String title(){
        return String.format("User Preference : %s",configKey.getName());
    }

    @Property(editing = Editing.DISABLED)
    public String getDefaultValue(){
        return configKey.getDefaultValue();
    }

    @Property(editing = Editing.DISABLED)
    public Boolean getMultiValued(){
        return configKey.getMultiValued();
    }

    @Property(editing = Editing.DISABLED)
    public String getType(){
        return configKey.getType();
    }
}
