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
package org.apache.causeway.incubator.viewer.vaadin.ui.pages.login;

import jakarta.inject.Inject;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import org.apache.causeway.core.config.CausewayConfiguration;
import org.apache.causeway.core.config.viewer.web.WebAppContextPath;
import org.apache.causeway.core.security.authentication.AuthenticationRequestPassword;
import org.apache.causeway.incubator.viewer.vaadin.ui.auth.VaadinAuthenticationHandler;
import org.apache.causeway.incubator.viewer.vaadin.ui.pages.main.MainViewVaa;

import lombok.val;

/**
 * Yet a minimal working version of a login page.
 */
@Route("login")
public class VaadinLoginView extends VerticalLayout {

    private final transient VaadinAuthenticationHandler vaadinAuthenticationHandler;

    @Inject
    public VaadinLoginView(
            final CausewayConfiguration causewayConfiguration,
            final WebAppContextPath webAppContextPath,
            final VaadinAuthenticationHandler vaadinAuthenticationHandler
    ) {
        this.vaadinAuthenticationHandler = vaadinAuthenticationHandler;

        addTitleAndLogo(causewayConfiguration, webAppContextPath);

        val usernameField = new TextField("Username"){{
            setMinWidth(20f, Unit.EM);
        }};
        val passwordField = new PasswordField("Password"){{
           setMinWidth(20f, Unit.EM);
        }};

        val loginButton = new Button("Login") {{
            addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        }};

        val loginAsSvenButton = new Button("Login (as Sven)") {{
            addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        }};

        val buttonsLayout = new HorizontalLayout(){{
            add(loginButton);
            add(loginAsSvenButton);
        }};

        val loginForm = new VerticalLayout(){{
            add(usernameField);
            add(passwordField);
            add(buttonsLayout);
            setAlignItems(Alignment.CENTER);
//            setAlignSelf(Alignment.START, buttonsLayout);
        }};
        add(loginForm);

        setAlignItems(Alignment.CENTER);
        // -- focus
        usernameField.focus();

        // -- binding
        loginButton.addClickListener(
                buttonClickEvent -> doLogin(
                        usernameField.getValue(),
                        passwordField.getValue())
        );

        loginAsSvenButton.addClickListener(
                buttonClickEvent -> doLoginAsSven()
        );
    }

    // -- HELPER

    private void doLogin(final String userName, final String secret) {
        val authenticationRequest = new AuthenticationRequestPassword(userName, secret);
        if (vaadinAuthenticationHandler.loginToSession(authenticationRequest)) {
            getUI().ifPresent(ui -> ui.navigate(MainViewVaa.class));
        } else {
            // TODO indicate to the user: login failed
        }
    }

    /**
     * @deprecated early development only
     */
    @Deprecated
    private void doLoginAsSven() {
        doLogin("sven", "pass");
    }

    private void addTitleAndLogo(final CausewayConfiguration causewayConfiguration, final WebAppContextPath webAppContextPath) {
        val applicationLogo = causewayConfiguration.getViewer().getCommon().getApplication().getBrandLogoSignin();
        applicationLogo.ifPresent(logoUrl ->
                add(new Image(webAppContextPath.prependContextPathIfLocal(logoUrl), "logo"))
        );

        val applicationName = causewayConfiguration.getViewer().getCommon().getApplication().getName();
        add(new H1(applicationName));
    }
}
