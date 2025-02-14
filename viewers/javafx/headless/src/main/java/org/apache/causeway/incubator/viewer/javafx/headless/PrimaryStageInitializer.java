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
package org.apache.causeway.incubator.viewer.javafx.headless;

import org.springframework.context.ApplicationListener;

import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @since 3.0
 */
@FunctionalInterface
public interface PrimaryStageInitializer
extends ApplicationListener<PrimaryStageReadyEvent> {

    @Override
    public default void onApplicationEvent(final PrimaryStageReadyEvent event) {
        var primaryStage = event.getStage();
        onPrimaryStageReady(primaryStage);
        primaryStage.show();
    }

    /**
     * Implementing classes are expected to populate a {@link Scene} and set it on the primary {@link Stage}.
     */
    void onPrimaryStageReady(Stage primaryStage);

}
