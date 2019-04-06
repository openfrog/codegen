/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.lollipok.gradle.plugin

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Before
import org.junit.Test

import static org.junit.Assert.assertTrue

/**
 * @author yangyanju
 */
class MBGeneratorPluginTest {

    Project project

    @Before
    void setUp() {
        project = ProjectBuilder.builder().build()
    }

    @Test
    void applyMBGeneratorPlugin() {
        project.pluginManager.apply 'io.lollipok.mybatis-generator-plugin'
        assertTrue(project.tasks.mybatisGenerate instanceof MBGeneratorTask)
    }
}
