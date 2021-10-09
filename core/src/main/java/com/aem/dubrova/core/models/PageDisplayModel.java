package com.aem.dubrova.core.models;

import com.aem.dubrova.core.beans.Page;
import com.aem.dubrova.core.constants.Constants;
import com.aem.dubrova.core.services.PageInfoObtainer;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

@Component
@Model(adaptables = Resource.class)
public class PageDisplayModel {

    private static final Logger LOGGER = LoggerFactory.getLogger(PageDisplayModel.class);

    @Inject
    private String behavior;

    @Inject
    @Optional
    private String dynamicPath;

    @Inject
    @Optional
    private String[] staticPath;

    @Inject
    private String number;

    private List<Page> pages;

    @Inject
    private PageInfoObtainer pageInfoObtainer;

    public static Logger getLOGGER() {
        return LOGGER;
    }

    public String getBehavior() {
        return behavior;
    }

    public String getDynamicPath() {
        return dynamicPath;
    }

    public String[] getStaticPath() {
        return staticPath;
    }

    public String getNumber() {
        return number;
    }

    public List<Page> getPages() {
        return pages;
    }

    @PostConstruct
    public void init() {
        LOGGER.info("Method PostConstruct init() started in PageDisplayModel");
        if (behavior.equals((Constants.STATIC_BEHAVIOR)) && staticPath != null) {
            LOGGER.info("Behavior is static");
            pages = pageInfoObtainer.getStaticPages(staticPath);
        }
        if (behavior.equals((Constants.DYNAMIC_BEHAVIOR)) && dynamicPath != null) {
            LOGGER.info("Behavior is dynamic");
            int limit = 0;
            try {
                limit = Integer.parseInt(number);
            } catch (NumberFormatException e) {
                LOGGER.info("Wrong number " + e);
            }
            pages = pageInfoObtainer.getDynamicPages(dynamicPath, limit);
        }
    }

}
