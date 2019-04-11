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

buildscript {
    repositories {
        gradlePluginPortal()
    }
    dependencies {
        classpath(BuildPlugins.gradlePluginPublishPlugin)
        classpath(BuildPlugins.gradleReleasePlugin)
    }
}

plugins {
    java
    signing
    `maven-publish`
    id("net.researchgate.release") version Versions.gradleReleaseVersion
}

tasks.withType<Wrapper> {
    group = "build setup"
    description = "The Gradle Wrapper is the preferred way of starting a Gradle build."
    gradleVersion = System.getProperty("gradle.version")
    distributionType = Wrapper.DistributionType.ALL
}

val projectInceptionYear: String by extra { "2019" }
val projectURL: String by extra { "https://github.com/lollipok/codegen" }

val projectNamesToPublish = listOf(
    "mybatis-codegen"
)

val projectsToPublish: List<Project> by extra {
    subprojects.filter {
        projectNamesToPublish.contains(it.name)
    }
}

val gradlePluginProjects: List<Project> by extra {
    subprojects.filter {
        it.name.endsWith("-gradle-plugin")
    }
}

allprojects {
    group = "io.github.lollipok"
    version = System.getProperty("version")

    description = "MyBatis Generator Plugin"

    repositories {
        mavenLocal()
        mavenCentral()
    }
}

subprojects {
    val javaVersion = JavaVersion.toVersion(Versions.javaVersion)

    apply {
        plugin("java")
    }

    configure<JavaPluginConvention> {
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
    }

    tasks {
        withType<JavaCompile> {
            options.isDeprecation = true
            options.encoding = Build.buildSourceEncoding
            options.compilerArgs.addAll(arrayOf("-Xlint:unchecked", "-parameters"))
        }

        withType<Jar> {
            afterEvaluate {
                manifest {
                    attributes(
                        "Manifest-Version" to "1.0",
                        "X-Compile-Source-JDK" to Versions.javaVersion,
                        "X-Compile-Target-JDK" to Versions.javaVersion,
                        "Build-Time" to helper.getBuildTime(),
                        "Specification-Version" to "1.0",
                        "Specification-Title" to project.name,
                        "Implementation-Title" to project.name,
                        "Implementation-Version" to project.version,
                        "Implementation-Vendor-Id" to project.group,
                        "Built-By" to helper.getUsername(),
                        "Build-Jdk" to helper.getBuildJdk(),
                        "Build-OS" to helper.getBuildOS(),
                        "Created-By" to "Gradle ${gradle.gradleVersion}"
                    )
                }
            }
        }

        withType<Javadoc> {
            options {
                encoding = Build.buildJavadocEncoding
                isFailOnError = false
            }

            (options as? StandardJavadocDocletOptions)?.also {
                it.charSet = Build.buildJavadocEncoding
                it.isAuthor = true
                it.isVersion = true

                if (JavaVersion.current().isJava9Compatible) {
                    it.addBooleanOption("html5", true)
                }
            }
        }

        whenTaskAdded {
            if (this.name.contains("signMavenJavaPublication")) {
                this.enabled = File(project.property("signing.secretKeyRingFile") as String).isFile
            }
        }
    }
}

configure(projectsToPublish) {

    // val isGradlePlugin: Boolean by extra { gradlePluginProjects.contains(this) }

    apply {
        plugin("signing")
        plugin("maven")
        plugin("maven-publish")
        plugin("net.researchgate.release")
    }

    val sourcesJar by tasks.registering(Jar::class) {
        archiveClassifier.set("sources")
        from(sourceSets.main.get().allSource)
        // from(sourceSets["main"].allJava)
        dependsOn(JavaPlugin.CLASSES_TASK_NAME)
    }

    val javadocJar by tasks.registering(Jar::class) {
        archiveClassifier.set("javadoc")
        from(tasks[JavaPlugin.JAVADOC_TASK_NAME])
        dependsOn(JavaPlugin.JAVADOC_TASK_NAME)
    }

    tasks {
        artifacts {
            add("archives", sourcesJar)
            add("archives", javadocJar)
        }

        whenTaskAdded {
            if (this.name.contains("signMavenJavaPublication")) {
                this.enabled = File(project.property("signing.secretKeyRingFile") as String).isFile
            }
        }

        afterReleaseBuild {

        }
    }

    // https://docs.gradle.org/current/userguide/publishing_maven.html
    publishing {
        apply(from = "$rootDir/gradle/maven-repo.gradle.kts")

        publications {
            register("mavenJava", MavenPublication::class) {
                maven.customizePom(this, project, rootProject)

                from(components["java"])
                artifact(sourcesJar.get())
                artifact(javadocJar.get())
            }
        }
    }

    signing {
        useGpgCmd()
        sign(publishing.publications["mavenJava"])
    }

    release {
        tagTemplate = "v$version"
        pushReleaseVersionBranch = false
    }
}

configure(gradlePluginProjects) {

    apply {
        plugin("groovy")
        plugin("signing")
        plugin("maven")
        plugin("maven-publish")
        plugin("java-gradle-plugin")
        plugin("com.gradle.plugin-publish")
        plugin("net.researchgate.release")
    }

    dependencies {
        compile(gradleApi())
        compile(localGroovy())

        testCompile(gradleTestKit())
        testCompile(TestLibs.junit)
    }

    val sourcesJar by tasks.registering(Jar::class) {
        archiveClassifier.set("sources")
        from(sourceSets.main.get().allSource)
        dependsOn(JavaPlugin.CLASSES_TASK_NAME)
    }

    val javadocJar by tasks.registering(Jar::class) {
        archiveClassifier.set("javadoc")
        from(tasks[JavaPlugin.JAVADOC_TASK_NAME])
        dependsOn(JavaPlugin.JAVADOC_TASK_NAME)
    }

    val groovydocJar by tasks.registering(Jar::class) {
        archiveClassifier.set("groovydoc")
        from(tasks[GroovyPlugin.GROOVYDOC_TASK_NAME])
        dependsOn(GroovyPlugin.GROOVYDOC_TASK_NAME)
    }

    tasks {
        whenTaskAdded {
            if (this.name.contains("signPluginMavenPublication")) {
                this.enabled = File(project.property("signing.secretKeyRingFile") as String).isFile
            }
        }

        afterReleaseBuild {

        }
    }

    publishing {
        apply(from = "$rootDir/gradle/maven-repo.gradle.kts")

        publications {
            register("pluginMaven", MavenPublication::class) {
                maven.customizePom(this, project, rootProject)

                artifact(sourcesJar.get())
                artifact(javadocJar.get())
                artifact(groovydocJar.get())
            }
        }
    }

    signing {
        useGpgCmd()
        sign(publishing.publications["pluginMaven"])
    }

    release {
        tagTemplate = "v$version"
        pushReleaseVersionBranch = false
    }
}
