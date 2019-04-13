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

import org.gradle.api.Project
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.TaskProvider
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.register

// https://docs.gradle.org/current/userguide/publishing_maven.html
fun PublishingExtension.configurePublishing(
    project: Project,
    name: String,
    javaComponent: Boolean,
    artifacts: List<TaskProvider<Jar>>
) {
    repositories {
        maven {
            configureRepository(project.version)
        }
    }

    publications {
        register(name, MavenPublication::class) {
            customizePom(project, project.rootProject)

            if (javaComponent) {
                from(project.components["java"])
            }

            artifacts.forEach {
                artifact(it.get())
            }
        }
    }
}

fun MavenArtifactRepository.configureRepository(version: Any) {
    val releasesRepoUrl = "https://oss.sonatype.org/service/local/staging/deploy/maven2/"
    val snapshotsRepoUrl = "https://oss.sonatype.org/content/repositories/snapshots/"

    setUrl(if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl)

    credentials {
        username = System.getProperty("oss.username")
        password = System.getProperty("oss.password")
    }
}
