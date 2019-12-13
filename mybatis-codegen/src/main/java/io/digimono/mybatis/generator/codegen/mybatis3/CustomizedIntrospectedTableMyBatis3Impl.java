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

package io.digimono.mybatis.generator.codegen.mybatis3;

import io.digimono.mybatis.generator.codegen.mybatis3.javamapper.CustomizedJavaMapperGenerator;
import io.digimono.mybatis.generator.codegen.mybatis3.xmlmapper.CustomizedXMLMapperGenerator;
import io.digimono.mybatis.generator.constants.Constants;
import org.mybatis.generator.api.ProgressCallback;
import org.mybatis.generator.codegen.AbstractJavaClientGenerator;
import org.mybatis.generator.codegen.mybatis3.IntrospectedTableMyBatis3Impl;
import org.mybatis.generator.internal.util.StringUtility;

import java.util.List;

/** @author yangyanju */
public class CustomizedIntrospectedTableMyBatis3Impl extends IntrospectedTableMyBatis3Impl {

  public CustomizedIntrospectedTableMyBatis3Impl() {
    super();
  }

  // region Set StatementId

  @Override
  public void setDeleteByPrimaryKeyStatementId(String s) {
    internalAttributes.put(InternalAttribute.ATTR_DELETE_BY_PRIMARY_KEY_STATEMENT_ID, "deleteById");
  }

  @Override
  public void setSelectAllStatementId(String s) {
    super.setSelectAllStatementId("findAll");
  }

  @Override
  public void setSelectByExampleStatementId(String s) {
    super.setSelectByExampleStatementId("findByExample");
  }

  @Override
  public void setSelectByExampleWithBLOBsStatementId(String s) {
    super.setSelectByExampleWithBLOBsStatementId("findByExampleWithBLOBs");
  }

  @Override
  public void setSelectByPrimaryKeyStatementId(String s) {
    super.setSelectByPrimaryKeyStatementId("findById");
  }

  @Override
  public void setUpdateByPrimaryKeyStatementId(String s) {
    super.setUpdateByPrimaryKeyStatementId("updateById");
  }

  @Override
  public void setUpdateByPrimaryKeySelectiveStatementId(String s) {
    super.setUpdateByPrimaryKeySelectiveStatementId("updateByIdSelective");
  }

  @Override
  public void setUpdateByPrimaryKeyWithBLOBsStatementId(String s) {
    super.setUpdateByPrimaryKeyWithBLOBsStatementId("updateByIdWithBLOBs");
  }

  // endregion

  @Override
  protected String calculateMyBatis3XmlMapperFileName() {
    StringBuilder sb = new StringBuilder();
    if (StringUtility.stringHasValue(tableConfiguration.getMapperName())) {
      String mapperName = tableConfiguration.getMapperName();
      int ind = mapperName.lastIndexOf('.');
      if (ind == -1) {
        sb.append(mapperName);
      } else {
        sb.append(mapperName.substring(ind + 1));
      }
      sb.append(Constants.CUSTOMIZED_XML_MAPPER_SUFFIX);
    } else {
      sb.append(fullyQualifiedTable.getDomainObjectName());
      sb.append(Constants.DEFAULT_XML_MAPPER_SUFFIX);
    }
    return sb.toString();
  }

  @Override
  protected AbstractJavaClientGenerator createJavaClientGenerator() {
    if (context.getJavaClientGeneratorConfiguration() == null) {
      return null;
    }

    String type = context.getJavaClientGeneratorConfiguration().getConfigurationType();

    AbstractJavaClientGenerator javaGenerator;

    if ("XMLMAPPER".equalsIgnoreCase(type)) {
      javaGenerator = new CustomizedJavaMapperGenerator(getClientProject());
    } else if ("MAPPER".equalsIgnoreCase(type)) {
      javaGenerator = new CustomizedJavaMapperGenerator(getClientProject());
    } else {
      javaGenerator = super.createJavaClientGenerator();
    }

    return javaGenerator;
  }

  @Override
  protected void calculateXmlMapperGenerator(
      AbstractJavaClientGenerator javaClientGenerator,
      List<String> warnings,
      ProgressCallback progressCallback) {
    if (javaClientGenerator == null) {
      if (context.getSqlMapGeneratorConfiguration() != null) {
        xmlMapperGenerator = new CustomizedXMLMapperGenerator();
      }
    } else {
      xmlMapperGenerator = javaClientGenerator.getMatchedXMLGenerator();
    }

    initializeAbstractGenerator(xmlMapperGenerator, warnings, progressCallback);
  }
}
