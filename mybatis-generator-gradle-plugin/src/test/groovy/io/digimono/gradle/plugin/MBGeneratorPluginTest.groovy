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

package io.digimono.gradle.plugin

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

import static org.gradle.testkit.runner.TaskOutcome.SUCCESS
import static org.junit.Assert.assertTrue

/**
 * @author yangyanju
 */
class MBGeneratorPluginTest {

    @Rule
    public final TemporaryFolder testProjectDir = new TemporaryFolder();

    private Project project

    private File buildFile
    private GradleRunner gradleRunner

    @Before
    void setUp() {
        project = ProjectBuilder.builder().build()
        project.pluginManager.apply 'java-library'
        project.pluginManager.apply 'io.github.digimono.mybatis-generator'

        ClassLoader classLoader = getClass().getClassLoader();
        String configFile = classLoader.getResource("META-INF/mybatis/mybatis-generator.xml").getFile()
        String configPropertiesFile = classLoader.getResource("META-INF/mybatis/generator.properties").getFile()
        String buildDir = new File(classLoader.getResource(".").getFile())
            .getParentFile()
            .getParentFile()
            .getParentFile()
            .getAbsolutePath()

        buildFile = testProjectDir.newFile("build.gradle")
        buildFile << """
            plugins {
                id "java-library"
                id "io.github.digimono.mybatis-generator"
            }

            repositories {
                mavenLocal()
                mavenCentral()
            }

            dependencies {
                // mybatisGenerator(project(":mybatis-codegen"))
                mybatisGenerator("io.github.digimono:mybatis-codegen:1.0.4")
                mybatisGenerator("org.mybatis.generator:mybatis-generator-core:1.4.0")
                mybatisGenerator("com.h2database:h2:1.4.200")
            }

            mybatisGenerator.configFile = "${configFile}"
            mybatisGenerator.configPropertiesFile = "${configPropertiesFile}"
            mybatisGenerator.outputDirectory = "${buildDir}/mybatis-generator"
        """

        gradleRunner = GradleRunner.create()
            .withPluginClasspath()
            .withProjectDir(testProjectDir.root)
            .withTestKitDir(testProjectDir.newFolder())
    }

    @Test
    void applyMBGeneratorPlugin() {
        assertTrue(project.tasks.mybatisGenerate instanceof MBGeneratorTask)
    }

    @Test
    void testMybatisGenerate() {
        def result = gradleRunner.withArguments('mybatisGenerate').build()
        assert result.task(":mybatisGenerate").outcome == SUCCESS
    }

    private BuildResult gradle(boolean isSuccessExpected, String[] arguments = ['tasks']) {
        arguments += '--stacktrace'
        def runner = GradleRunner.create()
            .withArguments(arguments)
            .withProjectDir(project.rootDir)
            .withPluginClasspath()
            .withDebug(true)
        return isSuccessExpected ? runner.build() : runner.buildAndFail()
    }

    private BuildResult gradle(String[] arguments = ['tasks']) {
        gradle(true, arguments)
    }
}
