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

import groovy.util.Node
import groovy.util.NodeList

plugins {
    java
    signing
    `maven-publish`
}

tasks.withType<Wrapper> {
    group = "build setup"
    description = "The Gradle Wrapper is the preferred way of starting a Gradle build."
    gradleVersion = System.getProperty("gradle.version")
    distributionType = Wrapper.DistributionType.ALL
}

val projectInceptionYear = "2019"
val projectURL = "https://github.com/lollipok/codegen"

val projectNamesToPublish = listOf("mybatis-codegen")
val projectsToPublish: List<Project> by extra {
    subprojects.filter {
        projectNamesToPublish.contains(it.name)
    }
}

allprojects {
    group = "io.github.lollipok"
    version = "0.0.2-SNAPSHOT"

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

    apply {
        plugin("signing")
        plugin("maven")
        plugin("maven-publish")
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
    }

    publishing {
        repositories {
            maven {
                val releasesRepoUrl = "https://oss.sonatype.org/service/local/staging/deploy/maven2/"
                val snapshotsRepoUrl = "https://oss.sonatype.org/content/repositories/snapshots/"

                setUrl(if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl)

                credentials {
                    username = System.getProperty("oss.username")
                    password = System.getProperty("oss.password")
                }
            }
        }

        publications {
            register("mavenJava", MavenPublication::class) {
                pom {
                    name.set(project.name)
                    description.set(project.description)
                    inceptionYear.set(projectInceptionYear)
                    url.set(projectURL)

                    groupId = project.group.toString()
                    artifactId = project.name
                    version = project.version.toString()
                    packaging = "jar"

                    scm {
                        connection.set("scm:git:https://github.com/lollipok/codegen.git")
                        developerConnection.set("scm:git:https://github.com/lollipok/codegen.git")
                        url.set("https://github.com/lollipok/codegen")
                    }

                    licenses {
                        license {
                            name.set("The Apache Software License, Version 2.0")
                            url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                            distribution.set("repo")
                        }
                    }

                    developers {
                        developer {
                            id.set("lollipok")
                            name.set("yangyanju")
                            email.set("yanjuyang@outlook.com")
                            url.set("https://github.com/lollipok")
                        }
                    }

                    withXml {
                        fun Any?.asNode() = this as Node
                        fun Any?.asNodeList() = this as NodeList

                        val root = asNode()
                        root["dependencies"].asNodeList().getAt("*").forEach {
                            val dependency = it.asNode()
                            if (dependency["scope"].asNodeList().text() == "runtime") {
                                if (project.configurations.findByName("implementation")?.allDependencies?.none { dep ->
                                        dep.name == dependency["artifactId"].asNodeList().text()
                                    } == false) {
                                    dependency["scope"].asNodeList().forEach { dep -> dep.asNode().setValue("compile") }
                                    dependency.appendNode("optional", true)
                                }
                            }
                        }
                    }
                }

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
}
