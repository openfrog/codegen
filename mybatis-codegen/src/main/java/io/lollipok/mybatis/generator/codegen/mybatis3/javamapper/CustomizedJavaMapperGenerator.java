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

package io.lollipok.mybatis.generator.codegen.mybatis3.javamapper;

import io.lollipok.mybatis.generator.codegen.mybatis3.xmlmapper.CustomizedXMLMapperGenerator;
import io.lollipok.mybatis.generator.utils.Utils;
import java.util.Collections;
import java.util.List;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.codegen.AbstractXmlGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.JavaMapperGenerator;

/**
 * @author yangyanju
 * @version 1.0
 * @date 2019-03-30
 */
public class CustomizedJavaMapperGenerator extends JavaMapperGenerator {

  // @Override
  // public List<CompilationUnit> getCompilationUnits() {
  //   progressCallback.startTask(
  //       getString("Progress.17", introspectedTable.getFullyQualifiedTable().toString()));
  //   CommentGenerator commentGenerator = context.getCommentGenerator();
  //
  //   String originalMapperType = introspectedTable.getMyBatis3JavaMapperType();
  //
  //   int lastDotIndex = originalMapperType.lastIndexOf(".");
  //   String mapperType =
  //       String.format(
  //           "%s.Generated%s",
  //           originalMapperType.substring(0, lastDotIndex),
  //           originalMapperType.substring(lastDotIndex + 1));
  //
  //   FullyQualifiedJavaType type = new FullyQualifiedJavaType(mapperType);
  //   Interface interfaze = new Interface(type);
  //   interfaze.setVisibility(JavaVisibility.PUBLIC);
  //   commentGenerator.addJavaFileComment(interfaze);
  //
  //   String rootInterface =
  //       introspectedTable.getTableConfigurationProperty(PropertyRegistry.ANY_ROOT_INTERFACE);
  //   if (!stringHasValue(rootInterface)) {
  //     rootInterface =
  //         context
  //             .getJavaClientGeneratorConfiguration()
  //             .getProperty(PropertyRegistry.ANY_ROOT_INTERFACE);
  //   }
  //
  //   if (stringHasValue(rootInterface)) {
  //     FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(rootInterface);
  //     interfaze.addSuperInterface(fqjt);
  //     interfaze.addImportedType(fqjt);
  //   }
  //
  //   addCountByExampleMethod(interfaze);
  //   addDeleteByExampleMethod(interfaze);
  //   addDeleteByPrimaryKeyMethod(interfaze);
  //   addInsertMethod(interfaze);
  //   addInsertSelectiveMethod(interfaze);
  //   addSelectByExampleWithBLOBsMethod(interfaze);
  //   addSelectByExampleWithoutBLOBsMethod(interfaze);
  //   addSelectByPrimaryKeyMethod(interfaze);
  //   addUpdateByExampleSelectiveMethod(interfaze);
  //   addUpdateByExampleWithBLOBsMethod(interfaze);
  //   addUpdateByExampleWithoutBLOBsMethod(interfaze);
  //   addUpdateByPrimaryKeySelectiveMethod(interfaze);
  //   addUpdateByPrimaryKeyWithBLOBsMethod(interfaze);
  //   addUpdateByPrimaryKeyWithoutBLOBsMethod(interfaze);
  //
  //   List<CompilationUnit> answer = new ArrayList<>();
  //   if (context.getPlugins().clientGenerated(interfaze, null, introspectedTable)) {
  //     answer.add(interfaze);
  //   }
  //
  //   List<CompilationUnit> extraCompilationUnits = getExtraCompilationUnits();
  //   if (extraCompilationUnits != null) {
  //     answer.addAll(extraCompilationUnits);
  //   }
  //
  //   return answer;
  // }

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
