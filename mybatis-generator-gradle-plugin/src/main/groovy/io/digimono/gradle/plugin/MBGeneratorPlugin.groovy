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

import org.gradle.api.Plugin
import org.gradle.api.internal.project.ProjectInternal

/**
 * @author yangyanju
 */
class MBGeneratorPlugin implements Plugin<ProjectInternal> {

    def MYBATIS_GENERATOR_CONFIGURATION = 'mybatisGenerator'
    def MYBATIS_GENERATOR_TASK = 'mybatisGenerate'
    def MYBATIS_GENERATOR_DEPENDENCY = 'org.mybatis.generator:mybatis-generator-core:1.3.7'

    @Override
    void apply(ProjectInternal project) {
        project.configurations
            .create(MYBATIS_GENERATOR_CONFIGURATION)
            .setDescription('MyBatis Generator')

        def extension = project.extensions.create(MYBATIS_GENERATOR_CONFIGURATION, MBGeneratorExtension, project)

        MBGeneratorTask task = project.tasks.create(MYBATIS_GENERATOR_TASK, MBGeneratorTask)
            .configure {
                group = "mybatis"
                description = 'Generate mybatis files'
            } as MBGeneratorTask

        task.conventionMapping.with {
            mybatisGeneratorClasspath = {
                def config = project.configurations[MYBATIS_GENERATOR_CONFIGURATION]
                if (config.dependencies.empty) {
                    project.dependencies {
                        mybatisGenerator MYBATIS_GENERATOR_DEPENDENCY
                    }
                }
                config
            }

            options = { extension }
        }
    }
}
