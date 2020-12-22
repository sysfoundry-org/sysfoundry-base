package $package;

import org.sysfoundry.security.keycloak.webmodule.DefaultAppPageList;
import org.apache.isis.applib.annotation.OrderPrecedence;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import javax.inject.Named;

@Service
@Named("sfPortal.PageClassListDefault")
@Order(OrderPrecedence.EARLY)
@Qualifier("Default")
@Primary
public class CustomAppPageList extends DefaultAppPageList {
}
