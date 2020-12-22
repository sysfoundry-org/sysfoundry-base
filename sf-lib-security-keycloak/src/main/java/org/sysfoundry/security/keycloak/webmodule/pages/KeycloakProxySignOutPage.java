package org.sysfoundry.security.keycloak.webmodule.pages;

import lombok.extern.slf4j.Slf4j;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.common.util.KeycloakUriBuilder;

import javax.inject.Inject;

@Slf4j
public class KeycloakProxySignOutPage extends WebPage {

    private static final long serialVersionUID = 1L;

    //public static final String LOGOUT_URL = "http://localhost:8080/auth/realms/Example/protocol/openid-connect/logout";

    @Inject
    private KeycloakDeployment keycloakDeployment;

    public KeycloakProxySignOutPage() {
    }

    @Override
    protected void onConfigure() {
        super.onConfigure();
        /*Session activeSession = getSession();
        HttpServletRequest request = (HttpServletRequest)getRequest().getContainerRequest();
        HttpSession session = request.getSession(false);
        Enumeration<String> attributeNames = session.getAttributeNames();

        while(attributeNames.hasMoreElements()){
            log.info("Attribute : "+attributeNames.nextElement());
        }

        String issuer = (String)activeSession.getAttribute("identity-provider");

        //PageParameters pageParameters = new PageParameters();
        //RequestUtils.toAbsolutePath()
        */
        String fullSignInUrl = RequestCycle.get().getUrlRenderer().renderFullUrl(
                Url.parse(urlFor(KeycloakProxySignInPage.class,null).toString()));
        KeycloakUriBuilder logoutUrl = keycloakDeployment.getLogoutUrl();
        logoutUrl = logoutUrl.queryParam("redirect_uri",fullSignInUrl);

        //Map<String,String> paramMap = new HashMap<>();
        //paramMap.put("redirect_uri",fullSignInUrl);


        String redirectURL = logoutUrl.build().toString();//logoutUrl.buildFromMap(paramMap,true).toString();//String.format("%s?redirect_uri=%s",,fullSignInUrl);
       //try {
            //String encodedRedirectURL = URLEncoder.encode(fullSignInUrl,"UTF-8");
            //String redirectWithIssuer = String.format("%s/protocol/openid-connect/logout?redirect_uri=%s",issuer, encodedRedirectURL);
            log.info("Redirecting to "+redirectURL);
            //log.info("Identity Provider "+issuer);
            //log.info("Redirect With Issuer "+redirectWithIssuer);
            throw new RedirectToUrlException(redirectURL);

        //} catch (UnsupportedEncodingException e) {
          //  e.printStackTrace();
        //}

        //pageParameters.add("redirect_uri",fullSignInUrl);

        //activeSession.
        /*HttpServletRequest request = (HttpServletRequest)getRequest().getContainerRequest();
        KeycloakSecurityContext securityContext = (KeycloakSecurityContext)request.getAttribute(KeycloakSecurityContext.class.getName());
        log.info("Security Context : "+securityContext);

        String issuer = securityContext.getToken().getIssuer();
        log.info("Issuer on logout : "+issuer);
        getSession().invalidate();
        try {
            request.logout();
            setResponsePage(getSignInPage());
        } catch (ServletException e) {
            e.printStackTrace();
        }*/


    }

    /*private Class<? extends Page> getSignInPage() {
        return pageClassRegistry.getPageClass(PageType.SIGN_IN);
    }

    @Inject
    PageClassRegistry pageClassRegistry;*/

}
