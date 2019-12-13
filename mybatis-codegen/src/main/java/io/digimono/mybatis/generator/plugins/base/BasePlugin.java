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

package io.digimono.mybatis.generator.plugins.base;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.IntrospectedTable.TargetRuntime;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.internal.util.StringUtility;

import java.util.List;

/**
 * @author yangyanju
 * @version 1.0
 */
public abstract class BasePlugin extends PluginAdapter {

  private static final String RUNTIME_MYBATIS3 = "MyBatis3";

  protected List<String> warnings;

  @Override
  public boolean validate(List<String> warnings) {
    this.warnings = warnings;

    String runtime = getContext().getTargetRuntime();

    if (!StringUtility.stringHasValue(runtime)) {
      return true;
    }

    if (RUNTIME_MYBATIS3.equalsIgnoreCase(runtime)) {
      return true;
    }

    try {
      Class<?> clazz = Class.forName(runtime);
      Object obj = clazz.newInstance();

      if (obj instanceof IntrospectedTable) {
        if (((IntrospectedTable) obj).getTargetRuntime().equals(TargetRuntime.MYBATIS3)) {
          return true;
        }
      }

    } catch (Exception ignored) {
    }

    warnings.add(String.format("The TargetRuntime must be %s", RUNTIME_MYBATIS3));
    return false;
  }
}
