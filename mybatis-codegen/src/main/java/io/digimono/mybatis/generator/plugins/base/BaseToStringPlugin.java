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

import io.digimono.mybatis.generator.constants.Constants;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.IntrospectedTable.TargetRuntime;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.util.List;
import java.util.Properties;

import static org.mybatis.generator.internal.util.StringUtility.isTrue;

/**
 * @author yangyanju
 * @version 1.0
 */
public abstract class BaseToStringPlugin extends BasePlugin {

  protected boolean useToStringFromRoot;

  @Override
  public boolean validate(List<String> warnings) {
    return true;
  }

  @Override
  public void setProperties(Properties properties) {
    super.setProperties(properties);
    useToStringFromRoot = isTrue(properties.getProperty("useToStringFromRoot"));
  }

  @Override
  public boolean modelBaseRecordClassGenerated(
      TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
    return generateToString(introspectedTable, topLevelClass);
  }

  @Override
  public boolean modelRecordWithBLOBsClassGenerated(
      TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
    return generateToString(introspectedTable, topLevelClass);
  }

  @Override
  public boolean modelPrimaryKeyClassGenerated(
      TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
    return generateToString(introspectedTable, topLevelClass);
  }

  protected boolean generateToString(
      IntrospectedTable introspectedTable, TopLevelClass topLevelClass) {
    Method method = new Method("toString");
    method.setVisibility(JavaVisibility.PUBLIC);
    method.setReturnType(FullyQualifiedJavaType.getStringInstance());
    method.addAnnotation("@Override");

    if (introspectedTable.getTargetRuntime() == TargetRuntime.MYBATIS3_DSQL) {
      context
          .getCommentGenerator()
          .addGeneralMethodAnnotation(method, introspectedTable, topLevelClass.getImportedTypes());
    } else {
      context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
    }

    return doGenerateToString(introspectedTable, topLevelClass, method);
  }

  protected boolean ignoreField(Field field) {
    String property = field.getName();
    return property.equalsIgnoreCase(Constants.SERIAL_VERSION_UID_FIELD_NAME);
  }

  protected abstract boolean doGenerateToString(
      IntrospectedTable introspectedTable, TopLevelClass topLevelClass, Method method);
}
