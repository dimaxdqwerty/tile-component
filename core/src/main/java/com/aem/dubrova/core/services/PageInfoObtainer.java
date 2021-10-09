package com.aem.dubrova.core.services;

import com.aem.dubrova.core.beans.Page;
import com.aem.dubrova.core.utils.PageConverter;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.jcr.api.SlingRepository;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aem.dubrova.core.constants.Constants;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;

@Component(immediate = true, service = PageInfoObtainer.class)
@ServiceDescription("Page Info Obtainer Service")
public class PageInfoObtainer {

    private static final Logger LOGGER = LoggerFactory.getLogger(PageInfoObtainer.class);

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    @Reference
    private SlingRepository repository;

    public List<Page> getDynamicPages(String dynamicPath, int limit) {
        String title, image, text;
        List<Page> pages = new ArrayList<>();
        ResourceResolver resourceResolver = null;
        try {
            Map<String, Object> params = new HashMap<>();
            params.put(ResourceResolverFactory.SUBSERVICE, Constants.PAGE_INFO_OBTAINER);
            resourceResolver = resourceResolverFactory.getServiceResourceResolver(params);
            Resource rootResource = resourceResolver.getResource(dynamicPath);
            List<Resource> resources = getChildPages(rootResource);
            for (Resource resource : resources) {
                com.day.cq.wcm.api.Page cqPage = resource.adaptTo(com.day.cq.wcm.api.Page.class);
                if (cqPage != null) {
                    title = cqPage.getTitle();
                    text = cqPage.getDescription();
                    Resource jcrContent = resource.getChild(Constants.JCR_CONTENT);
                    if (jcrContent != null) {
                        Resource imageProperty = jcrContent.getChild(Constants.IMAGE_PROPERTY);
                        if (imageProperty != null) {
                            image = imageProperty.getPath();
                            pages.add(new Page(title, image, text, PageConverter.convertPath(resource.getPath())));
                        }
                    }
                }
            }
            if (pages.size() >= limit) {
                pages = pages.subList(0, limit);
            }
        } catch (LoginException e) {
            LOGGER.error("Login exception " + e);
        } finally {
            if (resourceResolver != null) {
                resourceResolver.close();
            }
        }
        return pages;
    }

    public List<Page> getStaticPages(String[] staticPath) {
        String title, text, image;
        List<Page> pages = new ArrayList<>();
        ResourceResolver resourceResolver = null;
        try {
            for (String path : staticPath) {
                Map<String, Object> params = new HashMap<>();
                params.put(ResourceResolverFactory.SUBSERVICE, Constants.PAGE_INFO_OBTAINER);
                resourceResolver = resourceResolverFactory.getServiceResourceResolver(params);
                Resource resource = resourceResolver.resolve(path);
                Resource resourceChild = resource.getChild(Constants.JCR_CONTENT);
                if (resourceChild != null) {
                    Node node = resourceChild.adaptTo(Node.class);
                    if (node != null) {
                        title = node.getProperty(Constants.TITLE_PROPERTY).getString();
                        text = node.getProperty(Constants.TEXT_PROPERTY).getString();
                        image = node.getNode(Constants.IMAGE_PROPERTY).getPath();
                        pages.add(new Page(title, image, text, PageConverter.convertPath(path)));
                    }
                }
            }
        } catch (LoginException e) {
            LOGGER.error("Login exception " + e);
        } catch (PathNotFoundException e) {
            LOGGER.error("Path not found " + e);
        } catch (RepositoryException e) {
            LOGGER.error("Repository exception " + e);
        } finally {
            if (resourceResolver != null) {
                resourceResolver.close();
            }
        }
        return pages;
    }

    private List<Resource> getChildPages(Resource resource) {
        List<Resource> resources = new ArrayList<>();
        resources.add(resource);
        if (resource.hasChildren()) {
            Iterable<Resource> chidren = resource.getChildren();
            for (Resource child : chidren) {
                if (child.isResourceType(Constants.CQ_PAGE)) {
                    resources.addAll(getChildPages(child));
                }
            }
        }
        return resources;
    }

}
