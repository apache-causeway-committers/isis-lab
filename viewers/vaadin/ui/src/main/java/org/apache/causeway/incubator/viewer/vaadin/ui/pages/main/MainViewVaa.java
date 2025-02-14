/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.apache.causeway.incubator.viewer.vaadin.ui.pages.main;

import jakarta.inject.Inject;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.tabs.TabSheetVariant;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.theme.lumo.LumoUtility;

import org.apache.causeway.applib.annotation.Where;
import org.apache.causeway.applib.services.title.TitleService;
import org.apache.causeway.commons.collections.Can;
import org.apache.causeway.commons.internal.exceptions._Exceptions;
import org.apache.causeway.core.metamodel.context.HasMetaModelContext;
import org.apache.causeway.core.metamodel.context.MetaModelContext;
import org.apache.causeway.core.metamodel.interactions.managed.ManagedAction;
import org.apache.causeway.core.metamodel.object.ManagedObject;
import org.apache.causeway.core.metamodel.object.ManagedObjects;
import org.apache.causeway.core.metamodel.tabular.interactive.DataTableInteractive;
import org.apache.causeway.incubator.viewer.vaadin.model.context.UiContextVaa;
import org.apache.causeway.incubator.viewer.vaadin.ui.components.UiComponentFactoryVaa;
import org.apache.causeway.incubator.viewer.vaadin.ui.components.collection.TableViewVaa;
import org.apache.causeway.incubator.viewer.vaadin.ui.components.object.ObjectViewVaa;
import org.apache.causeway.incubator.viewer.vaadin.ui.util.LocalResourceUtil;
import org.apache.causeway.viewer.commons.applib.services.header.HeaderUiService;
import org.apache.causeway.viewer.commons.model.decorators.IconDecorator;

import org.jspecify.annotations.NonNull;
import lombok.val;
import lombok.extern.log4j.Log4j2;

/**
 * top-level view
 */
@Route("main")
@RouteAlias("")
@Log4j2
public class MainViewVaa extends AppLayout
        implements
        HasMetaModelContext,
        BeforeEnterObserver,
        HasUrlParameter<String> {

    private final MetaModelContext metaModelContext;
    private final UiContextVaa uiContext;
    private final UiActionHandlerVaa uiActionHandler;
    private final UiComponentFactoryVaa uiComponentFactory;
    private final HeaderUiService headerUiService;
    private final MainViewVaaState state;
    private final TitleService titleService;

    private final VerticalLayout pageContent = new VerticalLayout() {
        {
            setId("main-view-content");
            setSizeFull();
            setPadding(false);
            setSpacing(false);
        }
    };

    /**
     * Constructs the main view of the web-application, with the menu-bar and page content.
     */
    @Inject
    public MainViewVaa(
            final @NonNull MetaModelContext metaModelContext,
            final @NonNull UiActionHandlerVaa uiActionHandler,
            final @NonNull HeaderUiService headerUiService,
            final @NonNull UiContextVaa uiContext,
            final @NonNull UiComponentFactoryVaa uiComponentFactory,
            final @NonNull TitleService titleService,
            final @NonNull MainViewVaaState state
    ) {
        this.metaModelContext = metaModelContext;
        this.uiActionHandler = uiActionHandler;
        this.headerUiService = headerUiService;
        this.uiContext = uiContext;
        this.uiComponentFactory = uiComponentFactory;
        this.titleService = titleService;
        this.state = state;
        // FIXME Alf: MainViewVaa and UiContextVaa should be live in one package and not abstracted away
        ((UiContextVaaDefault) uiContext).setMainView(this);

    }

    // FIXME Alf: How to handle deep links
    @Override
    public void setParameter(
            BeforeEvent event,
            @OptionalParameter String parameter
    ) {
        if (parameter == null) {
            // FIXME Alf: How to handle deep links
            log.info("no parameter");
        } else {
            log.warn("unknown parameter: {}", parameter);
        }
    }

    enum MenuVariant {
        DRAWER,
        MENU_BAR
    }

    private final MenuVariant menuVariant = MenuVariant.DRAWER;
    private final Div drawerContent = new Div() {{
        setId("main-view-drawer");
    }};
    private final HorizontalLayout navbarContent = new HorizontalLayout() {
        {
            setId("main-view-navbar");
            setPadding(false);
            setSpacing(false);
        }
    };

    private final TabSheet tabSheet = new CloseableTabSheet() {{
        addThemeVariants(TabSheetVariant.LUMO_TABS_MINIMAL, TabSheetVariant.LUMO_TABS_SMALL);
        setSizeFull();
    }};

    @Override
    public void beforeEnter(final BeforeEnterEvent event) {

        val faStyleSheet = LocalResourceUtil.ResourceDescriptor.webjars(IconDecorator.FONTAWESOME_RESOURCE);
        LocalResourceUtil.addStyleSheet(faStyleSheet);

        setPrimarySection(Section.DRAWER);

        if (menuVariant == MenuVariant.DRAWER) {
            setDrawerOpened(true);
            val toggle = new DrawerToggle();
            val appName = metaModelContext.getConfiguration().getViewer().getCommon().getApplication().getName();

            val title = new H1(appName) {{
                // FIXME Alf: move to css
                getStyle()
                        .set("font-size", "var(--lumo-font-size-l)")
                        .set("margin", "0")
                        .setMarginTop("var(--lumo-space-s)")
                        .setLineHeight("var(--lumo-line-height-l)");

            }};
            val drawer = MainView_createMenuAsDrawer.apply(
                    metaModelContext,
                    headerUiService.getHeader(),
                    uiActionHandler
            );

            val drawerScroller = new Scroller(drawer) {{
                setClassName(LumoUtility.Padding.SMALL);
            }};
            // FIXME Alf: add more logic to not replace the content
            drawerContent.removeAll();
            drawerContent.add(drawerScroller);
            addToDrawer(drawerContent);
            navbarContent.removeAll();
            navbarContent.add(toggle, title);
            addToNavbar(navbarContent);
        } else {
            val menuBarContainer = MainView_createMenuAsTopBar.apply(
                    metaModelContext,
                    headerUiService.getHeader(),
                    uiActionHandler::handleActionLinkClicked,
                    this::renderHomepage
            );

            addToNavbar(menuBarContainer);
            setDrawerOpened(false);
        }

        setContent(pageContent);

        pageContent.add(tabSheet);

        setDrawerOpened(true);
        renderHomepage();
    }

    private void addNewTab(final Component component, String tabTitle) {
        val tab = tabSheet.add(tabTitle, component);
        tabSheet.setSelectedTab(tab);
    }

    private void renderHomepage() {
        log.info("about to render homepage");
        val homepage = getInteractionService().callAnonymous(metaModelContext::getHomePageAdapter);
        uiContext.route(homepage);
    }

    public void handle(final ManagedObject object) {
        val objectState = state.addManagedObject(object);
        val component = createAndAddComponent(objectState);
        val tabTitle = titleService.titleOf(object);
        addNewTab(component, tabTitle);
    }

    private ObjectViewVaa createAndAddComponent(MainViewVaaState.NavigationState.ObjectState objectState) {
        return ObjectViewVaa.fromObject(
                uiContext,
                uiComponentFactory,
                titleService,
                uiActionHandler::handleActionLinkClicked,
                objectState.object()
        );
    }

    public void handle(final ManagedAction managedAction, final Can<ManagedObject> params, final ManagedObject actionResult) {

        if (ManagedObjects.isPacked(actionResult)) {
            val component = registerAndCreateComponentForResultList(managedAction, params, actionResult);
            val tabTitle = managedAction.getFriendlyName();//titleService.titleOf(actionResult);
            addNewTab(component, tabTitle);
        } else {
            handle(actionResult);
        }
    }

    private Component registerAndCreateComponentForResultList(ManagedAction managedAction, Can<ManagedObject> params, ManagedObject actionResult) {
        val actionState = state.addActionResult(actionResult, managedAction, params);
        val dataTableModel = DataTableInteractive.forAction(actionState.managedAction(), actionState.params(), actionState.actionResult());
        return TableViewVaa.forDataTableModel(uiContext, titleService, dataTableModel, Where.STANDALONE_TABLES);
    }

    @Override
    public MetaModelContext getMetaModelContext() {
        if (metaModelContext == null) {
            // TODO needs static recovery
            throw _Exceptions.notImplemented();
        }
        return metaModelContext;
    }
}
