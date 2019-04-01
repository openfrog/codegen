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

package io.lollipok.mybatis.generator.plugins;

import io.lollipok.mybatis.generator.plugins.base.BasePlugin;
import java.util.Iterator;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;

/**
 * @author yangyanju
 * @version 1.0
 * @date 2019-03-29
 */
public class AliasColumnListPlugin extends BasePlugin {

  @Override
  public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
    XmlElement rootElement = document.getRootElement();

    XmlElement answer = new XmlElement("sql");
    answer.addAttribute(new Attribute("id", "Alias_Column_List"));

    context.getCommentGenerator().addComment(answer);

    StringBuilder sb = new StringBuilder();
    Iterator<IntrospectedColumn> iter = introspectedTable.getNonBLOBColumns().iterator();
    while (iter.hasNext()) {
      sb.append("${alias}.").append(MyBatis3FormattingUtilities.getSelectListPhrase(iter.next()));

      if (iter.hasNext()) {
        sb.append(", ");
      }

      if (sb.length() > 80) {
        answer.addElement(new TextElement(sb.toString()));
        sb.setLength(0);
      }
    }

    if (sb.length() > 0) {
      answer.addElement(new TextElement(sb.toString()));
    }

    rootElement.addElement(answer);
    return true;
  }
}
