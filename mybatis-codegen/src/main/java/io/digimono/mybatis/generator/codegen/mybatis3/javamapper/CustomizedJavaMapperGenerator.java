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

package io.digimono.mybatis.generator.codegen.mybatis3.javamapper;

import io.digimono.mybatis.generator.codegen.mybatis3.xmlmapper.CustomizedXMLMapperGenerator;
import io.digimono.mybatis.generator.plugins.EmptyJavaMapperPlugin;
import io.digimono.mybatis.generator.utils.ReflectUtils;
import io.digimono.mybatis.generator.utils.Utils;
import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.CompositePlugin;
import org.mybatis.generator.api.Plugin;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.codegen.AbstractXmlGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.JavaMapperGenerator;
import org.mybatis.generator.config.PropertyRegistry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;
import static org.mybatis.generator.internal.util.messages.Messages.getString;

/** @author yangyanju */
public class CustomizedJavaMapperGenerator extends JavaMapperGenerator {

  public CustomizedJavaMapperGenerator(String project) {
    super(project);
  }

  public CustomizedJavaMapperGenerator(String project, boolean requiresMatchedXMLGenerator) {
    super(project, requiresMatchedXMLGenerator);
  }

  @Override
  public List<CompilationUnit> getCompilationUnits() {
    progressCallback.startTask(
        getString("Progress.17", introspectedTable.getFullyQualifiedTable().toString()));
    CommentGenerator commentGenerator = context.getCommentGenerator();

    boolean hasEmptyJavaMapperPlugin = false;
    CompositePlugin compositePlugin = (CompositePlugin) context.getPlugins();
    try {
      Object value = ReflectUtils.getValue(compositePlugin, CompositePlugin.class, "plugins");
      if (value instanceof ArrayList) {
        @SuppressWarnings("unchecked")
        List<Plugin> plugins = (ArrayList<Plugin>) value;

        hasEmptyJavaMapperPlugin =
            plugins.stream().anyMatch(plugin -> plugin instanceof EmptyJavaMapperPlugin);
      }
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }

    String mapperType;
    String originalMapperType = introspectedTable.getMyBatis3JavaMapperType();

    if (hasEmptyJavaMapperPlugin) {
      int lastDotIndex = originalMapperType.lastIndexOf(".");
      mapperType =
          String.format(
              "%s.%s.%s%s",
              originalMapperType.substring(0, lastDotIndex),
              Utils.getGeneratedMapperSubpackage(context),
              originalMapperType.substring(lastDotIndex + 1),
              Utils.getGeneratedMapperSuffix(context));
    } else {
      mapperType = originalMapperType;
    }

    FullyQualifiedJavaType type = new FullyQualifiedJavaType(mapperType);
    Interface interfaze = new Interface(type);
    interfaze.setVisibility(JavaVisibility.PUBLIC);
    commentGenerator.addJavaFileComment(interfaze);

    String rootInterface =
        introspectedTable.getTableConfigurationProperty(PropertyRegistry.ANY_ROOT_INTERFACE);
    if (!stringHasValue(rootInterface)) {
      rootInterface =
          context
              .getJavaClientGeneratorConfiguration()
              .getProperty(PropertyRegistry.ANY_ROOT_INTERFACE);
    }

    if (stringHasValue(rootInterface)) {
      FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(rootInterface);
      interfaze.addSuperInterface(fqjt);
      interfaze.addImportedType(fqjt);
    }

    addCountByExampleMethod(interfaze);
    addDeleteByExampleMethod(interfaze);
    addDeleteByPrimaryKeyMethod(interfaze);
    addInsertMethod(interfaze);
    addInsertSelectiveMethod(interfaze);
    addSelectByExampleWithBLOBsMethod(interfaze);
    addSelectByExampleWithoutBLOBsMethod(interfaze);
    addSelectByPrimaryKeyMethod(interfaze);
    addUpdateByExampleSelectiveMethod(interfaze);
    addUpdateByExampleWithBLOBsMethod(interfaze);
    addUpdateByExampleWithoutBLOBsMethod(interfaze);
    addUpdateByPrimaryKeySelectiveMethod(interfaze);
    addUpdateByPrimaryKeyWithBLOBsMethod(interfaze);
    addUpdateByPrimaryKeyWithoutBLOBsMethod(interfaze);

    List<CompilationUnit> answer = new ArrayList<>();
    if (context.getPlugins().clientGenerated(interfaze, introspectedTable)) {
      answer.add(interfaze);
    }

    List<CompilationUnit> extraCompilationUnits = getExtraCompilationUnits();
    if (extraCompilationUnits != null) {
      answer.addAll(extraCompilationUnits);
    }

    return answer;
  }

  @Override
  public List<CompilationUnit> getExtraCompilationUnits() {
    return Collections.emptyList();
  }

  @Override
  protected void addInsertMethod(Interface interfaze) {
    if (Utils.onlyGenerateInsertSelectiveMethod(context, introspectedTable)) {
      return;
    }

    super.addInsertMethod(interfaze);
  }

  @Override
  protected void addUpdateByPrimaryKeyWithBLOBsMethod(Interface interfaze) {
    if (Utils.onlyGenerateUpdateSelectiveMethod(context, introspectedTable)) {
      return;
    }

    super.addUpdateByPrimaryKeyWithBLOBsMethod(interfaze);
  }

  @Override
  protected void addUpdateByPrimaryKeyWithoutBLOBsMethod(Interface interfaze) {
    if (Utils.onlyGenerateUpdateSelectiveMethod(context, introspectedTable)) {
      return;
    }

    super.addUpdateByPrimaryKeyWithoutBLOBsMethod(interfaze);
  }

  @Override
  protected void addUpdateByExampleWithBLOBsMethod(Interface interfaze) {
    if (Utils.onlyGenerateUpdateSelectiveMethod(context, introspectedTable)) {
      return;
    }

    super.addUpdateByExampleWithBLOBsMethod(interfaze);
  }

  @Override
  protected void addUpdateByExampleWithoutBLOBsMethod(Interface interfaze) {
    if (Utils.onlyGenerateUpdateSelectiveMethod(context, introspectedTable)) {
      return;
    }

    super.addUpdateByExampleWithoutBLOBsMethod(interfaze);
  }

  @Override
  public AbstractXmlGenerator getMatchedXMLGenerator() {
    return new CustomizedXMLMapperGenerator();
  }
}
