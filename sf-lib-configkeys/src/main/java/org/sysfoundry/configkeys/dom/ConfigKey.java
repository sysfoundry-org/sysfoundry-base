package org.sysfoundry.configkeys.dom;

import lombok.Getter;
import lombok.Setter;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.services.message.MessageService;
import org.apache.isis.applib.services.repository.RepositoryService;
import org.apache.isis.applib.services.title.TitleService;

import javax.inject.Inject;
import javax.jdo.annotations.*;
import java.util.Arrays;
import java.util.List;

import static org.apache.isis.applib.annotation.SemanticsOf.NON_IDEMPOTENT_ARE_YOU_SURE;

@DomainObject
@DomainObjectLayout(cssClassFa = "fa-tools")
@PersistenceCapable(identityType = IdentityType.DATASTORE,schema = "configkeys")
@DatastoreIdentity(strategy = IdGeneratorStrategy.IDENTITY,column = "id")
@Version(strategy = VersionStrategy.DATE_TIME,column = "version")
@Unique(name = "ConfigKey_name_UNQ",members = {"name"})
public class ConfigKey {

    @Getter @Setter
    @Property(editing = Editing.ENABLED)
    private String name;

    @Getter @Setter
    @Property(editing = Editing.ENABLED)
    private String type;

    @Getter @Setter
    @Property(editing = Editing.ENABLED)
    private String defaultValue;

    @Getter @Setter
    @Property(editing = Editing.ENABLED)
    @PropertyLayout(named = "Is Preference?")
    private Boolean preference;

    @Getter @Setter
    @Property(editing = Editing.ENABLED)
    @PropertyLayout(named = "Is Multivalued?")
    private Boolean multiValued;

    public static final List<String> dataTypes = Arrays.asList("Text","Number");

    @Inject
    TitleService titleService;
    @Inject
    MessageService messageService;

    @Inject
    RepositoryService repositoryService;


    public ConfigKey(){
        this("unknown","unknown",false,"none",false);
    }

    public ConfigKey(String name, String type,Boolean multiValued, String defaultValue,boolean preference) {
        this.name = name;
        this.type = type;
        this.defaultValue = defaultValue;
        this.preference = preference;
        this.multiValued = multiValued;
    }

    public static ConfigKey from(String name, String type,Boolean multiValued, String defaultValue,boolean preference) {
        return new ConfigKey(name,type,multiValued,defaultValue,preference);
    }

    @Action(semantics = NON_IDEMPOTENT_ARE_YOU_SURE)
    public void delete(){
        final String title = titleService.titleOf(this);
        messageService.informUser(String.format("'%s' deleted", title));
        repositoryService.removeAndFlush(this);
    }

    /*Choices for properties*/
    public List<String> choicesType(){
        return dataTypes;
    }


    public String title(){
        return String.format("Config Key : %s",name);
    }
}
