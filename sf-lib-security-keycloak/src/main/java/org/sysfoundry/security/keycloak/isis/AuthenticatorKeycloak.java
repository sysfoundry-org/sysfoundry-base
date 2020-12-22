package org.sysfoundry.security.keycloak.isis;

import lombok.extern.slf4j.Slf4j;
import org.apache.isis.applib.annotation.OrderPrecedence;
import org.apache.isis.applib.services.user.ResourceMemento;
import org.apache.isis.applib.services.user.RoleMemento;
import org.apache.isis.applib.services.user.UserMemento;
import org.apache.isis.core.security.authentication.Authentication;
import org.apache.isis.core.security.authentication.AuthenticationRequest;
import org.apache.isis.core.security.authentication.standard.Authenticator;
import org.apache.isis.core.security.authentication.standard.SimpleAuthentication;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.*;
import java.util.stream.Stream;

@Service
@Named("sfSecurityKeycloak.AuthenticatorKeycloak")
@Order(OrderPrecedence.EARLY)
@Qualifier("Keycloak")
@Singleton
@Slf4j
public class AuthenticatorKeycloak implements Authenticator {

    @Inject
    private Environment environment;

    @Override
    public final boolean canAuthenticate(final Class<? extends AuthenticationRequest> authenticationRequestClass) {
        return true;
    }

    @Override
    public Authentication authenticate(final AuthenticationRequest request, final String code) {
        AuthenticatedWebSession unAuthenticatedSession = AuthenticatedWebSession.get();
        //AccessToken.Access accessInfo = (AccessToken.Access)unAuthenticatedSession.getAttribute("keycloak-accessinfo");
        //String issuer = (String)unAuthenticatedSession.getAttribute("keycloak-issuer");
        //UserMemento userMemento = UserMemento.ofNameAndRoleNames(request.getName(), getRoles(accessInfo.getRoles()));

        AccessToken accessToken = (AccessToken)unAuthenticatedSession.getAttribute("keycloak-access-token");

        UserMemento userMemento = buildUserMementoFromAccessToken(request,code,accessToken);

        log.debug("UserMemento --> {}",userMemento);

        SimpleAuthentication simpleAuthentication = SimpleAuthentication.of(userMemento,code);

        return simpleAuthentication;
    }

    private UserMemento buildUserMementoFromAccessToken(AuthenticationRequest request, String code,
                                                        AccessToken accessToken) {
        String appName = environment.getProperty("spring.application.name","unknown-app");
        String userName = request.getName();

        log.info("Looking up roles for app : {}",appName);
        Map<String, AccessToken.Access> accessInfoMap = accessToken.getResourceAccess();
        Set<String> appRoles = new HashSet<>();

        if(accessInfoMap.containsKey(appName)){
            AccessToken.Access accessInfoForApp = accessInfoMap.get(appName);
            appRoles = accessInfoForApp.getRoles();
        }else{
            log.info("No Role access found for user : {} @ {}",userName,appName);
        }

        Stream<ResourceMemento> resources = buildResourceStream(accessInfoMap);

        return new UserMemento(userName,getRoles(appRoles),resources);
    }

    private Stream<ResourceMemento> buildResourceStream(Map<String,AccessToken.Access> accessInfoMap){
        List<ResourceMemento> resourceMementoList = new ArrayList<>();
        accessInfoMap.forEach((resourceName,accessDetail)->{
            resourceMementoList.add(new ResourceMemento(resourceName,resourceName,getRoles(accessDetail.getRoles())));
        });
        return resourceMementoList.stream();
    }

    private Stream<RoleMemento> getRoles(Set<String> existingRoles) {
        Set<String> defaultRoles = new HashSet<>();
        defaultRoles.add("org.apache.isis.viewer.wicket.roles.USER");
        defaultRoles.addAll(existingRoles);
        return defaultRoles.stream().map(RoleMemento::new);
    }

    @Override
    public void logout(final Authentication session) {
        log.info("About to logout of session "+session.getUserName());

        //SimpleSession actualSession = (SimpleSession)session;
        //AuthenticatedWebSession.get().invalidateNow();
    }

}
