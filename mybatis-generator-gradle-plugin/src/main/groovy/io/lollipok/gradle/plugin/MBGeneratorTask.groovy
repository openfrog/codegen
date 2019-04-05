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

import org.gradle.api.file.FileCollection
import org.gradle.api.internal.ConventionTask
import org.gradle.api.internal.project.IsolatedAntBuilder
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.TaskAction

/**
 * @author yangyanju
 */
class MBGeneratorTask extends ConventionTask {

    @Input
    MBGeneratorExtension options
    @InputFiles
    FileCollection mybatisGeneratorClasspath

    @TaskAction
    void generate() {
        MBGeneratorExtension opts = getOptions()
        if (opts.getSkip()) {
            project.logger.warn("MyBatis generator is skipped.")
            return
        }

        services.get(IsolatedAntBuilder)
            .withClasspath(getMybatisGeneratorClasspath())
            .execute {
                // ant.taskdef(name: 'mbgenerator', classname: 'org.mybatis.generator.ant.GeneratorAntTask')
                ant.taskdef(name: 'mbgenerator', classname: 'io.lollipok.mybatis.generator.ant.MBGeneratorAntTask')

                ant.properties.put('generated.source.dir', opts.getOutputDirectory())

                def mybatisProperties = getMyBatisProperties(opts)
                mybatisProperties.each {
                    ant.project.setProperty(it.key.toString(), it.value.toString())
                }

                ant.mbgenerator(
                    overwrite: opts.getOverwrite(),
                    configfile: opts.getConfigFile(),
                    verbose: opts.getVerbose()) {

                    propertyset {
                        mybatisProperties.each {
                            propertyref(name: it.key)
                        }
                    }
                }
            }
    }

    def getMyBatisProperties(MBGeneratorExtension extension) {
        def properties = new Properties()
        project.file(extension.getConfigPropertiesFile()).withInputStream { inputStream ->
            properties.load(inputStream)
        }
        properties.put("mybatis.generator.outputDirectory", extension.getOutputDirectory())

        properties
    }
}
