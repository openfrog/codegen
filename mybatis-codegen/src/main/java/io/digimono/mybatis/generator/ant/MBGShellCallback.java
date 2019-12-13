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

package io.digimono.mybatis.generator.ant;

import org.mybatis.generator.exception.ShellException;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.mybatis.generator.internal.util.StringUtility;
import org.mybatis.generator.internal.util.messages.Messages;

import java.io.File;
import java.util.Properties;
import java.util.StringTokenizer;

/** @author yangyanju */
public class MBGShellCallback extends DefaultShellCallback {

  private final Properties properties;

  public MBGShellCallback(Properties properties, boolean overwrite) {
    super(overwrite);
    this.properties = properties;
  }

  @Override
  public File getDirectory(String targetProject, String targetPackage) throws ShellException {
    if (!"MAVEN".equals(targetProject) && !"GRADLE".equals(targetProject)) {
      return super.getDirectory(targetProject, targetPackage);
    }

    if (properties == null) {
      return super.getDirectory(targetProject, targetPackage);
    }

    String outputDirectory = properties.getProperty("mybatis.generator.outputDirectory");
    if (!StringUtility.stringHasValue(outputDirectory)) {
      return super.getDirectory(targetProject, targetPackage);
    }

    File project = new File(outputDirectory);
    if (!project.exists()) {
      // noinspection ResultOfMethodCallIgnored
      project.mkdirs();
    }

    if (!project.isDirectory()) {
      throw new ShellException(Messages.getString("Warning.9", project.getAbsolutePath()));
    }

    StringBuilder sb = new StringBuilder();
    StringTokenizer st = new StringTokenizer(targetPackage, ".");
    while (st.hasMoreTokens()) {
      sb.append(st.nextToken());
      sb.append(File.separatorChar);
    }

    File directory = new File(project, sb.toString());
    if (!directory.isDirectory()) {
      boolean rc = directory.mkdirs();
      if (!rc) {
        throw new ShellException(Messages.getString("Warning.10", directory.getAbsolutePath()));
      }
    }

    return directory;
  }
}
