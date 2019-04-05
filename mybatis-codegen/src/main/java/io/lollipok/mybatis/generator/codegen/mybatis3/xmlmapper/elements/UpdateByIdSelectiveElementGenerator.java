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

package io.lollipok.mybatis.generator.codegen.mybatis3.xmlmapper.elements;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

/**
 * @author yangyanju
 * @version 1.0
 * @date 2019-04-03
 */
public class UpdateByIdSelectiveElementGenerator extends AbstractXmlElementGenerator {

  public UpdateByIdSelectiveElementGenerator() {
    super();
  }

  @Override
  public void addElements(XmlElement parentElement) {
    XmlElement answer = new XmlElement("update");

    answer.addAttribute(
        new Attribute("id", introspectedTable.getUpdateByPrimaryKeySelectiveStatementId()));

    String parameterType;

    if (introspectedTable.getRules().generateRecordWithBLOBsClass()) {
      parameterType = introspectedTable.getRecordWithBLOBsType();
    } else {
      parameterType = introspectedTable.getBaseRecordType();
    }

    answer.addAttribute(new Attribute("parameterType", parameterType));

    context.getCommentGenerator().addComment(answer);

    StringBuilder sb = new StringBuilder();

    sb.append("update ");
    sb.append(introspectedTable.getFullyQualifiedTableNameAtRuntime());
    answer.addElement(new TextElement(sb.toString()));

    answer.addElement(getSqlClauseElement("Update_Clause"));

    boolean and = false;
    for (IntrospectedColumn introspectedColumn : introspectedTable.getPrimaryKeyColumns()) {
      sb.setLength(0);
      if (and) {
        sb.append("  and ");
      } else {
        sb.append("where ");
        and = true;
      }

      sb.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
      sb.append(" = ");
      sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn));
      answer.addElement(new TextElement(sb.toString()));
    }

    if (context
        .getPlugins()
        .sqlMapUpdateByPrimaryKeySelectiveElementGenerated(answer, introspectedTable)) {
      parentElement.addElement(answer);
    }
  }

  private XmlElement getSqlClauseElement(String refid) {
    XmlElement answer = new XmlElement("include");
    answer.addAttribute(new Attribute("refid", refid));
    return answer;
  }
}