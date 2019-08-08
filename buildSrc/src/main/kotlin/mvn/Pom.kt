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

package mvn

import groovy.util.Node
import groovy.util.NodeList
import org.gradle.api.Project
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.extra

fun MavenPublication.customizePom(project: Project, rootProject: Project) {
    val properties = rootProject.extra.properties

    pom {
        val projectInceptionYear: String by properties
        val projectURL: String by properties

        name.set(project.name)
        description.set(project.description)
        inceptionYear.set(projectInceptionYear)
        url.set(projectURL)

        groupId = project.group as String
        artifactId = project.name
        version = project.version as String

        packaging = "jar"

        scm {
            connection.set("scm:git:https://github.com/digimono/codegen.git")
            developerConnection.set("scm:git:https://github.com/digimono/codegen.git")
            url.set("https://github.com/digimono/codegen")
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
                id.set("digimono")
                name.set("yangyanju")
                email.set("yanjuyang@outlook.com")
                url.set("https://github.com/digimono")
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
                        dependency["scope"].asNodeList()
                            .forEach { dep -> dep.asNode().setValue("compile") }
                        dependency.appendNode("optional", true)
                    }
                }
            }
        }
    }
}
