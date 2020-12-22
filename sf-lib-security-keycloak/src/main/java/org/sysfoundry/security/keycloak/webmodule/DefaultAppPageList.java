package org.sysfoundry.security.keycloak.webmodule;

import org.sysfoundry.security.keycloak.webmodule.pages.KeycloakProxySignInPage;
import org.sysfoundry.security.keycloak.webmodule.pages.KeycloakProxySignOutPage;
import org.apache.isis.viewer.wicket.viewer.registries.pages.PageClassListDefault;
import org.apache.wicket.Page;

public class DefaultAppPageList extends PageClassListDefault {

    @Override
    protected Class<? extends Page> getSignInPageClass() {
        return KeycloakProxySignInPage.class;
    }


    @Override
    protected Class<? extends Page> getSignOutPageClass() {
        return KeycloakProxySignOutPage.class;
    }
}
