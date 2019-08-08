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

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.PropertySet;
import org.mybatis.generator.ant.AntProgressCallback;
import org.mybatis.generator.ant.GeneratorAntTask;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.api.ShellCallback;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.exception.XMLParserException;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;
import static org.mybatis.generator.internal.util.messages.Messages.getString;

/**
 * @author yangyanju
 * @version 1.0
 */
public class MBGeneratorAntTask extends GeneratorAntTask {

  private PropertySet propertyset;

  public MBGeneratorAntTask() {
    super();
  }

  @Override
  public void execute() throws BuildException {
    if (!stringHasValue(getConfigfile())) {
      throw new BuildException(getString("RuntimeError.0"));
    }

    List<String> warnings = new ArrayList<>();

    File configurationFile = new File(getConfigfile());
    if (!configurationFile.exists()) {
      throw new BuildException(getString("RuntimeError.1", getConfigfile()));
    }

    Set<String> fullyQualifiedTables = new HashSet<>();
    if (stringHasValue(getFullyQualifiedTableNames())) {
      StringTokenizer st = new StringTokenizer(getFullyQualifiedTableNames(), ",");
      while (st.hasMoreTokens()) {
        String s = st.nextToken().trim();
        if (s.length() > 0) {
          fullyQualifiedTables.add(s);
        }
      }
    }

    Set<String> contexts = new HashSet<>();
    if (stringHasValue(getContextIds())) {
      StringTokenizer st = new StringTokenizer(getContextIds(), ",");
      while (st.hasMoreTokens()) {
        String s = st.nextToken().trim();
        if (s.length() > 0) {
          contexts.add(s);
        }
      }
    }

    try {
      Properties properties = propertyset == null ? null : propertyset.getProperties();

      ConfigurationParser cp = new ConfigurationParser(properties, warnings);
      Configuration config = cp.parseConfiguration(configurationFile);

      ShellCallback callback = new MBGShellCallback(properties, isOverwrite());

      MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);

      myBatisGenerator.generate(
          new AntProgressCallback(this, isVerbose()), contexts, fullyQualifiedTables);

    } catch (XMLParserException e) {
      for (String error : e.getErrors()) {
        log(error, Project.MSG_ERR);
      }

      throw new BuildException(e.getMessage());
    } catch (SQLException | IOException e) {
      throw new BuildException(e.getMessage());
    } catch (InvalidConfigurationException e) {
      for (String error : e.getErrors()) {
        log(error, Project.MSG_ERR);
      }

      throw new BuildException(e.getMessage());
    } catch (InterruptedException e) {
      // ignore (will never happen with the DefaultShellCallback)
    } catch (Exception e) {
      log(e, Project.MSG_ERR);
      throw new BuildException(e.getMessage());
    }

    for (String error : warnings) {
      log(error, Project.MSG_WARN);
    }
  }

  @Override
  public PropertySet createPropertyset() {
    if (propertyset == null) {
      propertyset = new PropertySet();
    }

    return propertyset;
  }
}
