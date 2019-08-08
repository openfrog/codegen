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
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.ListUtilities;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

import static io.digimono.mybatis.generator.constants.Constants.INSERT_SELECTIVE_CLAUSE_ID;

/**
 * @author yangyanju
 * @version 1.0
 */
public class InsertClauseElementGenerator extends AbstractXmlElementGenerator {

  public InsertClauseElementGenerator() {
    super();
  }

  @Override
  public void addElements(XmlElement parentElement) {
    XmlElement answer = new XmlElement("sql");
    answer.addAttribute(new Attribute("id", INSERT_SELECTIVE_CLAUSE_ID));

    context.getCommentGenerator().addComment(answer);

    StringBuilder sb = new StringBuilder();

    XmlElement insertTrimElement = new XmlElement("trim");
    insertTrimElement.addAttribute(new Attribute("prefix", "("));
    insertTrimElement.addAttribute(new Attribute("suffix", ")"));
    insertTrimElement.addAttribute(new Attribute("suffixOverrides", ","));
    answer.addElement(insertTrimElement);

    XmlElement valuesTrimElement = new XmlElement("trim");
    valuesTrimElement.addAttribute(new Attribute("prefix", "values ("));
    valuesTrimElement.addAttribute(new Attribute("suffix", ")"));
    valuesTrimElement.addAttribute(new Attribute("suffixOverrides", ","));
    answer.addElement(valuesTrimElement);

    for (IntrospectedColumn introspectedColumn :
        ListUtilities.removeIdentityAndGeneratedAlwaysColumns(introspectedTable.getAllColumns())) {

      if (introspectedColumn.isSequenceColumn()
          || introspectedColumn.getFullyQualifiedJavaType().isPrimitive()) {
        // if it is a sequence column, it is not optional
        // This is required for MyBatis3 because MyBatis3 parses
        // and calculates the SQL before executing the selectKey

        // if it is primitive, we cannot do a null check
        sb.setLength(0);
        sb.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
        sb.append(',');
        insertTrimElement.addElement(new TextElement(sb.toString()));

        sb.setLength(0);
        sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn));
        sb.append(',');
        valuesTrimElement.addElement(new TextElement(sb.toString()));

        continue;
      }

      sb.setLength(0);
      sb.append(introspectedColumn.getJavaProperty());
      sb.append(" != null");
      XmlElement insertNotNullElement = new XmlElement("if");
      insertNotNullElement.addAttribute(new Attribute("test", sb.toString()));

      sb.setLength(0);
      sb.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
      sb.append(',');
      insertNotNullElement.addElement(new TextElement(sb.toString()));
      insertTrimElement.addElement(insertNotNullElement);

      sb.setLength(0);
      sb.append(introspectedColumn.getJavaProperty());
      sb.append(" != null");
      XmlElement valuesNotNullElement = new XmlElement("if");
      valuesNotNullElement.addAttribute(new Attribute("test", sb.toString()));

      sb.setLength(0);
      sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn));
      sb.append(',');
      valuesNotNullElement.addElement(new TextElement(sb.toString()));
      valuesTrimElement.addElement(valuesNotNullElement);
    }

    if (context.getPlugins().sqlMapInsertSelectiveElementGenerated(answer, introspectedTable)) {
      parentElement.addElement(answer);
    }
  }
}
