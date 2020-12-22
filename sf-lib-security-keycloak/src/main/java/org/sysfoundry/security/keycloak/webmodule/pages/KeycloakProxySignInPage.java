package org.sysfoundry.security.keycloak.webmodule.pages;

import lombok.extern.slf4j.Slf4j;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.representations.AccessToken;
import org.keycloak.representations.IDToken;
import org.springframework.beans.factory.annotation.Value;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;


@Slf4j
public class KeycloakProxySignInPage extends WebPage {

    //public static final String APP_NAME = "simpleapp";

    @Inject
    private KeycloakDeployment keycloakDeployment;

    public KeycloakProxySignInPage(){
        add(new Label("not-signed-in-label","You are not signed In!"));
    }

    @Override
    protected void onConfigure() {
        super.onConfigure();

        if(!AuthenticatedWebSession.get().isSignedIn()){
            HttpServletRequest httpServletRequest = (HttpServletRequest)getRequest().getContainerRequest();

            HttpServletResponse httpServletResponse = (HttpServletResponse)getResponse().getContainerResponse();

            KeycloakSecurityContext securityContext = (KeycloakSecurityContext)httpServletRequest.getAttribute(KeycloakSecurityContext.class.getName());

            if(securityContext != null){
                IDToken idToken = securityContext.getIdToken();

                String userName = idToken.getPreferredUsername();
                log.info("App Name : "+keycloakDeployment.getResourceName());
                log.info("Issuer : "+idToken.getIssuer());
                log.info("Authenticated Successfully.... user id : "+idToken.getPreferredUsername());

                AccessToken accessToken = securityContext.getToken();

                //AccessToken.Access accessInfo = accessToken.getResourceAccess(keycloakDeployment.getResourceName());
                //SimpleSession simpleSession = new SimpleSession(idToken.getPreferredUsername(),computeRoles(accessInfo));

                /*if(accessInfo != null){
                    Set<String> roles = accessInfo.getRoles();
                    log.info("Roles for app -> "+keycloakDeployment.getResourceName());
                    roles.forEach(role->{
                        log.info(role);
                    });
                }*/

                AuthenticatedWebSession unAuthenticatedSession = AuthenticatedWebSession.get();

                unAuthenticatedSession.setAttribute("keycloak-access-token",accessToken);
                //unAuthenticatedSession.setAttribute("keycloak-accessinfo",accessInfo);
                //unAuthenticatedSession.setAttribute("keycloak-issuer",idToken.getIssuer());

                boolean result = AuthenticatedWebSession.get().signIn(userName, "already-authenticated");



                log.info("External Security ? : "+System.getProperty("isis.security.logout.external"));
                //getSession().setAttribute("identity-provider",idToken.getIssuer());
                if(result){
                    //update the security context in the session
                    AuthenticatedWebSession.get().setAttribute("keycloak-security-context",securityContext);
                    //proceed to the homepage
                    setResponsePage(getApplication().getHomePage());
                }else{
                    httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }


            }else{
                log.info("Not yet authenticated....");
                httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

        }else{
            //just go to the home page
            setResponsePage(getApplication().getHomePage());
        }


        //AuthenticatedWebApplication app = (AuthenticatedWebApplication) Application.get();

        //setResponsePage(getApplication().getHomePage());

        /*if(AuthenticatedWebSession.get().isSignedIn()){
            log.info("You are signed in....");

        }else{
            log.info("You are not signed in. Please check@@@@@@@@@@@@@@@@@");
        }*/
    }

    private Iterable<String> computeRoles(AccessToken.Access accessInfo) {
        accessInfo.addRole("org.apache.isis.viewer.wicket.roles.USER");

        return accessInfo.getRoles();
    }

}
