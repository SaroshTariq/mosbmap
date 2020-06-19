package com.mosbmap.catalogservice;



import com.mosbmap.catalogservice.models.daos.Authority;
import com.mosbmap.catalogservice.repositories.AuthoritiesRepository;
import com.mosbmap.catalogservice.utils.LogUtil;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 *
 * @author Sarosh
 */
@Component
public class ListenerBean {

    @Autowired
    AuthoritiesRepository authoritiesRepository;

    @EventListener
    public void handleEvent(ContextRefreshedEvent event) {

        if (event instanceof ContextRefreshedEvent) {
            ApplicationContext applicationContext = ((ContextRefreshedEvent) event).getApplicationContext();
            Map<RequestMappingInfo, HandlerMethod> map1 = applicationContext.getBean(RequestMappingHandlerMapping.class).getHandlerMethods();

            map1.forEach((RequestMappingInfo requestMappingInfo, HandlerMethod handlerMethod) -> {
                try {
                    if (!handlerMethod.getMethod().getName().equalsIgnoreCase("error")
                            && !handlerMethod.getMethod().getName().equalsIgnoreCase("errorHtml")) {
                        Authority authority = new Authority();
                        authority.setId(requestMappingInfo.getName());
                        authority.setName(handlerMethod.getMethod().getName());
                        authority.setDescription(requestMappingInfo.toString());
                        authoritiesRepository.save(authority);

                    }

                } catch (Exception e) {
                    LogUtil.warn("", "handleEvent", "error inserting authority", "");
                }

            });
        }
    }
}
