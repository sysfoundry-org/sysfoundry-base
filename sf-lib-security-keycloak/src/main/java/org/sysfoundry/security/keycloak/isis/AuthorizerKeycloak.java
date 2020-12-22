package org.sysfoundry.security.keycloak.isis;

import lombok.extern.slf4j.Slf4j;
import org.apache.isis.applib.Identifier;
import org.apache.isis.applib.annotation.OrderPrecedence;
import org.apache.isis.core.security.authorization.standard.Authorizor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import javax.inject.Named;

@Service
@Named("sfSecurityKeycloak.AuthorizorKeycloak")
@Order(OrderPrecedence.EARLY)
@Qualifier("Keycloak")
@Slf4j
public class AuthorizerKeycloak implements Authorizor {

    @Override
    public boolean isVisibleInRole(String role, Identifier identifier) {
        log.info(String.format("isVisibleInRole - %s , %s",role,identifier));
        return isVisibleInAnyRole(identifier);
    }

    @Override
    public boolean isUsableInRole(String role, Identifier identifier) {
        log.info(String.format("isUsableInRole - %s , %s",role,identifier));
        return isUsableInAnyRole(identifier);
    }

    @Override
    public boolean isVisibleInAnyRole(Identifier identifier) {
        return isPermitted(identifier, "r");
    }

    @Override
    public boolean isUsableInAnyRole(Identifier identifier) {
        return isPermitted(identifier, "w");
    }

    private boolean isPermitted(Identifier identifier, String qualifier) {
        //log.info("isPermitted "+identifier+" - Qualifier "+qualifier);
        return true;
    }


}
