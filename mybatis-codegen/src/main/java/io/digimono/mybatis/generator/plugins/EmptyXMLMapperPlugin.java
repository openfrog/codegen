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

import java.util.ArrayList;
import java.util.List;
import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.XmlConstants;
import org.mybatis.generator.config.TableConfiguration;
import org.mybatis.generator.internal.util.StringUtility;

/**
 * @author yangyanju
 * @version 1.0
 */
public class EmptyXMLMapperPlugin extends BasePlugin {

  @Override
  public List<GeneratedXmlFile> contextGenerateAdditionalXmlFiles(
      IntrospectedTable introspectedTable) {
    XmlElement answer = new XmlElement("mapper");
    String namespace = introspectedTable.getMyBatis3SqlMapNamespace();
    answer.addAttribute(new Attribute("namespace", namespace));

    context.getCommentGenerator().addRootComment(answer);

    Document document =
        new Document(
            XmlConstants.MYBATIS3_MAPPER_PUBLIC_ID, XmlConstants.MYBATIS3_MAPPER_SYSTEM_ID);
    document.setRootElement(answer);

    GeneratedXmlFile emptyXml =
        new GeneratedXmlFile(
            document,
            calculateMyBatis3XmlMapperFileName(introspectedTable),
            introspectedTable.getMyBatis3XmlMapperPackage(),
            context.getSqlMapGeneratorConfiguration().getTargetProject(),
            true,
            context.getXmlFormatter());

    List<GeneratedXmlFile> list = new ArrayList<>();
    list.add(emptyXml);

    return list;
  }

  private String calculateMyBatis3XmlMapperFileName(IntrospectedTable introspectedTable) {
    TableConfiguration tableConfiguration = introspectedTable.getTableConfiguration();
    StringBuilder sb = new StringBuilder();

    if (StringUtility.stringHasValue(tableConfiguration.getMapperName())) {
      String mapperName = tableConfiguration.getMapperName();
      int ind = mapperName.lastIndexOf('.');
      if (ind == -1) {
        sb.append(mapperName);
      } else {
        sb.append(mapperName.substring(ind + 1));
      }
      sb.append(".xml");
    } else {
      sb.append(introspectedTable.getFullyQualifiedTable().getDomainObjectName());
      sb.append("Mapper.xml");
    }
    return sb.toString();
  }
}
