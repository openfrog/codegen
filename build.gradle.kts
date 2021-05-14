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

plugins {
    `java-library`
    id("com.gradle.plugin-publish") version Versions.gradlePluginPublishVersion
}

tasks.withType<Wrapper> {
    group = "build setup"
    description = "The Gradle Wrapper is the preferred way of starting a Gradle build."
    gradleVersion = System.getProperty("gradle.version")
    distributionType = Wrapper.DistributionType.BIN
}

val projectURL: String by extra { "https://github.com/digimono/codegen" }

val coreProjects: List<Project> by extra {
    subprojects.filter {
        !it.name.endsWith("-docs")
            && !it.name.endsWith("-gradle-plugin")
    }
}

val projectsToPublish: List<Project> by extra {
    subprojects.filter {
        listOf(
            "mybatis-codegen"
        ).contains(it.name)
    }
}

val gradlePluginProjects: List<Project> by extra {
    subprojects.filter {
        it.name.endsWith("-gradle-plugin")
    }
}

description = "MyBatis Generator Plugin"

allprojects {
    group = "io.github.digimono"

    repositories {
        mavenLocal()
        mavenCentral()
    }
}

subprojects {
    // val isGradlePlugin: Boolean by extra { gradlePluginProjects.contains(this) }
}

configure(coreProjects) {
    apply {
        plugin("java-library-convention")
    }
}

configure(projectsToPublish) {
    apply {
        plugin("maven-publish-convention")
    }
}

configure(gradlePluginProjects) {
    apply {
        plugin("groovy")
        plugin("java-gradle-plugin")
        plugin("com.gradle.plugin-publish")
        // plugin("maven-publish")
    }

    dependencies {
        implementation(gradleApi())
        implementation(localGroovy())

        testImplementation(TestLibs.junit)
        testImplementation(gradleTestKit())
    }
}
