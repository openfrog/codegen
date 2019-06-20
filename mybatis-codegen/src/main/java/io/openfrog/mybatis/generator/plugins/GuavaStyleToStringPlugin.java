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

package io.openfrog.mybatis.generator.plugins;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import io.openfrog.mybatis.generator.plugins.base.BaseToStringPlugin;

/**
 * @author yangyanju
 * @version 1.0
 */
public class GuavaStyleToStringPlugin extends BaseToStringPlugin {

  @Override
  protected boolean doGenerateToString(
      IntrospectedTable introspectedTable, TopLevelClass topLevelClass, Method method) {
    topLevelClass.addImportedType("com.google.common.base.MoreObjects");

    method.addBodyLine("return MoreObjects.toStringHelper(this)");

    StringBuilder sb = new StringBuilder();
    for (Field field : topLevelClass.getFields()) {
      if (ignoreField(field)) {
        continue;
      }

      String property = field.getName();

      sb.setLength(0);
      sb.append(".add(\"").append(property).append("\", ").append(property).append(")");
      method.addBodyLine(sb.toString());
    }
    method.addBodyLine(".toString();");

    topLevelClass.addMethod(method);

    return true;
  }
}
