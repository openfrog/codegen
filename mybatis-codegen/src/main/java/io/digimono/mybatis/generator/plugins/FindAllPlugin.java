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

package io.digimono.mybatis.generator.plugins;

import io.digimono.mybatis.generator.plugins.base.BasePlugin;

import java.util.Set;
import java.util.TreeSet;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.config.PropertyRegistry;
import org.mybatis.generator.internal.util.StringUtility;

/**
 * @author yangyanju
 * @version 1.0
 */
public class FindAllPlugin extends BasePlugin {

  @Override
  public boolean sqlMapSelectAllElementGenerated(
      XmlElement element, IntrospectedTable introspectedTable) {
    return true;
  }

  @Override
  public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
    XmlElement rootElement = document.getRootElement();

    XmlElement answer = new XmlElement("select");
    answer.addAttribute(new Attribute("id", introspectedTable.getSelectAllStatementId()));
    answer.addAttribute(new Attribute("resultMap", introspectedTable.getBaseResultMapId()));

    context.getCommentGenerator().addComment(answer);

    StringBuilder sb = new StringBuilder();
    sb.append("select ");

    if (sb.length() > 0) {
      answer.addElement(new TextElement(sb.toString()));
    }

    XmlElement include = new XmlElement("include");
    include.addAttribute(new Attribute("refid", introspectedTable.getBaseColumnListId()));
    answer.addElement(include);

    sb.setLength(0);
    sb.append("from ");
    sb.append(introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime());
    answer.addElement(new TextElement(sb.toString()));

    String orderByClause =
        introspectedTable.getTableConfigurationProperty(
            PropertyRegistry.TABLE_SELECT_ALL_ORDER_BY_CLAUSE);
    boolean hasOrderBy = StringUtility.stringHasValue(orderByClause);
    if (hasOrderBy) {
      sb.setLength(0);
      sb.append("order by ");
      sb.append(orderByClause);
      answer.addElement(new TextElement(sb.toString()));
    }

    rootElement.addElement(answer);

    return true;
  }

  @Override
  public boolean clientGenerated(
      Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
    Set<FullyQualifiedJavaType> importedTypes = new TreeSet<>();
    importedTypes.add(FullyQualifiedJavaType.getNewListInstance());

    Method method = new Method();
    method.setVisibility(JavaVisibility.PUBLIC);

    FullyQualifiedJavaType returnType = FullyQualifiedJavaType.getNewListInstance();
    FullyQualifiedJavaType listType =
        new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());

    importedTypes.add(listType);
    returnType.addTypeArgument(listType);
    method.setReturnType(returnType);
    method.setName(introspectedTable.getSelectAllStatementId());

    context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);

    addMapperAnnotations(interfaze, method);

    if (context.getPlugins().clientSelectAllMethodGenerated(method, interfaze, introspectedTable)) {
      addExtraImports(interfaze);
      interfaze.addImportedTypes(importedTypes);
      interfaze.addMethod(method);
    }

    return true;
  }

  public void addMapperAnnotations(Interface interfaze, Method method) {}

  public void addExtraImports(Interface interfaze) {}
}
