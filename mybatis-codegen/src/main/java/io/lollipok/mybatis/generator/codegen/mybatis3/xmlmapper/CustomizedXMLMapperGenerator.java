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

package io.lollipok.mybatis.generator.codegen.mybatis3.xmlmapper;

import io.lollipok.mybatis.generator.codegen.mybatis3.xmlmapper.elements.InsertClauseElementGenerator;
import io.lollipok.mybatis.generator.codegen.mybatis3.xmlmapper.elements.InsertSelectiveElementGenerator;
import io.lollipok.mybatis.generator.codegen.mybatis3.xmlmapper.elements.UpdateByIdSelectiveElementGenerator;
import io.lollipok.mybatis.generator.codegen.mybatis3.xmlmapper.elements.UpdateClauseElementGenerator;
import io.lollipok.mybatis.generator.utils.Utils;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.XMLMapperGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

/**
 * @author yangyanju
 * @version 1.0
 * @date 2019-03-30
 */
public class CustomizedXMLMapperGenerator extends XMLMapperGenerator {

  @Override
  protected void addInsertElement(XmlElement parentElement) {
    if (Utils.onlyGenerateInsertSelectiveMethod(context, introspectedTable)) {
      return;
    }

    super.addInsertElement(parentElement);
  }

  @Override
  protected void addInsertSelectiveElement(XmlElement parentElement) {
    if (introspectedTable.getRules().generateInsertSelective()) {
      addInsertClauseElement(parentElement);

      AbstractXmlElementGenerator elementGenerator = new InsertSelectiveElementGenerator();
      initializeAndExecuteGenerator(elementGenerator, parentElement);
    }
  }

  @Override
  protected void addUpdateByPrimaryKeySelectiveElement(XmlElement parentElement) {
    if (introspectedTable.getRules().generateUpdateByPrimaryKeySelective()) {
      addUpdateClauseElement(parentElement);

      AbstractXmlElementGenerator elementGenerator = new UpdateByIdSelectiveElementGenerator();
      initializeAndExecuteGenerator(elementGenerator, parentElement);
    }
  }

  @Override
  protected void addUpdateByPrimaryKeyWithBLOBsElement(XmlElement parentElement) {
    if (Utils.onlyGenerateUpdateSelectiveMethod(context, introspectedTable)) {
      return;
    }

    super.addUpdateByPrimaryKeyWithBLOBsElement(parentElement);
  }

  @Override
  protected void addUpdateByPrimaryKeyWithoutBLOBsElement(XmlElement parentElement) {
    if (Utils.onlyGenerateUpdateSelectiveMethod(context, introspectedTable)) {
      return;
    }

    super.addUpdateByPrimaryKeyWithoutBLOBsElement(parentElement);
  }

  @Override
  protected void addUpdateByExampleWithBLOBsElement(XmlElement parentElement) {
    if (Utils.onlyGenerateUpdateSelectiveMethod(context, introspectedTable)) {
      return;
    }

    super.addUpdateByExampleWithBLOBsElement(parentElement);
  }

  @Override
  protected void addUpdateByExampleWithoutBLOBsElement(XmlElement parentElement) {
    if (Utils.onlyGenerateUpdateSelectiveMethod(context, introspectedTable)) {
      return;
    }

    super.addUpdateByExampleWithoutBLOBsElement(parentElement);
  }

  protected void addInsertClauseElement(XmlElement parentElement) {
    AbstractXmlElementGenerator elementGenerator = new InsertClauseElementGenerator();
    initializeAndExecuteGenerator(elementGenerator, parentElement);
  }

  protected void addUpdateClauseElement(XmlElement parentElement) {
    AbstractXmlElementGenerator elementGenerator = new UpdateClauseElementGenerator();
    initializeAndExecuteGenerator(elementGenerator, parentElement);
  }
}
