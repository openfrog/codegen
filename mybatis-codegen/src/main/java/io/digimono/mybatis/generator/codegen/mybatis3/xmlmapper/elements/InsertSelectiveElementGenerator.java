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

package io.digimono.mybatis.generator.codegen.mybatis3.xmlmapper.elements;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.config.GeneratedKey;

import java.util.Optional;

import static io.digimono.mybatis.generator.constants.Constants.INSERT_SELECTIVE_CLAUSE_ID;

/**
 * @author yangyanju
 * @version 1.0
 */
public class InsertSelectiveElementGenerator extends BaseXmlElementGenerator {

  public InsertSelectiveElementGenerator() {
    super();
  }

  @Override
  public void addElements(XmlElement parentElement) {
    XmlElement answer = new XmlElement("insert");

    answer.addAttribute(new Attribute("id", introspectedTable.getInsertSelectiveStatementId()));

    FullyQualifiedJavaType parameterType = introspectedTable.getRules().calculateAllFieldsClass();

    answer.addAttribute(new Attribute("parameterType", parameterType.getFullyQualifiedName()));

    context.getCommentGenerator().addComment(answer);

    GeneratedKey gk = introspectedTable.getGeneratedKey();
    if (gk != null) {
      Optional<IntrospectedColumn> introspectedColumn = introspectedTable.getColumn(gk.getColumn());
      // if the column is null, then it's a configuration error. The
      // warning has already been reported
      if (introspectedColumn.isPresent()) {
        IntrospectedColumn column = introspectedColumn.get();
        if (gk.isJdbcStandard()) {
          answer.addAttribute(new Attribute("useGeneratedKeys", "true"));
          answer.addAttribute(new Attribute("keyProperty", column.getJavaProperty()));
          answer.addAttribute(new Attribute("keyColumn", column.getActualColumnName()));
        } else {
          answer.addElement(getSelectKey(column, gk));
        }
      }
    }

    StringBuilder sb = new StringBuilder();

    sb.append("insert into ");
    sb.append(introspectedTable.getFullyQualifiedTableNameAtRuntime());
    answer.addElement(new TextElement(sb.toString()));

    answer.addElement(getSqlClauseElement(INSERT_SELECTIVE_CLAUSE_ID));

    if (context.getPlugins().sqlMapInsertSelectiveElementGenerated(answer, introspectedTable)) {
      parentElement.addElement(answer);
    }
  }
}
