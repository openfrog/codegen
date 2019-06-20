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

package io.openfrog.gradle.plugin

import groovy.transform.ToString

/**
 * @author yangyanju
 */
@ToString(includeNames = true)
class MBGeneratorExtension {

    def skip = false
    def verbose = false
    def overwrite = true
    def outputDirectory
    def configFile
    def configPropertiesFile

    MBGeneratorExtension(def project) {
        this.outputDirectory = "${project.buildDir}/generated-sources/mybatis-generator"
        this.configFile = "${project.projectDir}/src/main/resources/META-INF/mybatis/mybatis-generator.xml"
        this.configPropertiesFile = "${project.projectDir}/src/main/resources/META-INF/mybatis/generator.properties"
    }
}
