package org.sysfoundry.configkeys.dom;

import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.services.repository.RepositoryService;
import org.apache.isis.applib.services.user.UserService;
import org.apache.isis.persistence.jdo.applib.services.IsisJdoSupport_v3_2;

import javax.inject.Inject;
import javax.jdo.JDOQLTypedQuery;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@DomainService(nature = NatureOfService.VIEW,objectType = "sf.solution.UserPreferences")
@lombok.RequiredArgsConstructor(onConstructor_ = {@Inject} )
public class UserPreferences {

    @Inject
    private final RepositoryService repositoryService;

    @Inject
    private final ConfigKeys configKeysDomainService;

    @Inject
    IsisJdoSupport_v3_2 jdoSupport;

    @Inject
    private UserService userService;

    @Action(semantics = SemanticsOf.NON_IDEMPOTENT)
    public UserPreference create(String keyName,String value){
        Optional<String> currentUserNameOptional = userService.currentUserName();
        if(currentUserNameOptional.isPresent()){
            String currentUserId = currentUserNameOptional.get();
            List<ConfigKey> matchingKeys = configKeysDomainService.findByName(keyName);
            if(matchingKeys.size() > 0) {
                ConfigKey configKeyObj = matchingKeys.get(0);
                return repositoryService.persist(UserPreference.from(currentUserId,configKeyObj,value));
            }else{
                String errorMessage = String.format("ConfigKey %s is not a valid one!",keyName);
                throw new RuntimeException(errorMessage);
            }
        }else{
            String errorMessage = String.format("Current User not identified!");
            throw new RuntimeException(errorMessage);
        }
    }

    public List<String> choices0Create(){
        List<ConfigKey> preferences = configKeysDomainService.listPreferences();
        //get a list of the current user preferences for this user
        List<UserPreference> userPreferences = this.listAll();

        List<ConfigKey> userPreferenceConfigKeys = userPreferences.stream().map(UserPreference::getConfigKey)
                .collect(Collectors.toList());

        return preferences.stream()
                .filter(configKey -> {
                    //remove the existing user preferences
                    return !userPreferenceConfigKeys.contains(configKey);
                })
                .map(ConfigKey::getName).collect(Collectors.toList());
    }

    public String validate1Create(Object val){
        String value = (String)val;
        if(value == null || value.isEmpty()){
            return "value cannot be empty!";
        }
        return null;
    }

    //list all user preferences for the given user
    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(named = "List of your preferences")
    public List<UserPreference> listAll(){
        JDOQLTypedQuery<UserPreference> typedQuery = jdoSupport.newTypesafeQuery(UserPreference.class);
        QUserPreference candidate = QUserPreference.candidate();
        typedQuery = typedQuery.filter(
                candidate.userId.eq(typedQuery.stringParameter("userId"))
        );
        Optional<String> currentUserNameOptional = userService.currentUserName();
        if(currentUserNameOptional.isPresent()) {
            return typedQuery.setParameter("userId",currentUserNameOptional.get()).executeList();

        }else{
            String errorMessage = String.format("Current User not identified!");
            throw new RuntimeException(errorMessage);
        }
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(named = "List of User's preferences")
    public List<UserPreference> findPreferencesOfUser(String user){
        JDOQLTypedQuery<UserPreference> typedQuery = jdoSupport.newTypesafeQuery(UserPreference.class);
        QUserPreference candidate = QUserPreference.candidate();
        typedQuery = typedQuery.filter(
                candidate.userId.eq(typedQuery.stringParameter("userId"))
        );
        return typedQuery.setParameter("userId",user).executeList();
    }

    public boolean hideFindPreferencesOfUser(){
        return !userService.getUser().hasRoleName("admin");
    }

}
