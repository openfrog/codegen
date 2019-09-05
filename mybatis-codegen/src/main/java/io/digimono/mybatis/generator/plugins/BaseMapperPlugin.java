///*
// * Copyright 2019 the original author or authors.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *    http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package io.digimono.mybatis.generator.plugins;
//
//import io.digimono.mybatis.generator.plugins.base.BasePlugin;
//import org.mybatis.generator.api.CommentGenerator;
//import org.mybatis.generator.api.GeneratedJavaFile;
//import org.mybatis.generator.api.IntrospectedTable;
//import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
//import org.mybatis.generator.internal.util.StringUtility;
//
//import java.util.List;
//
///**
// * @author yangyanju
// * @version 1.0
// */
//public class BaseMapperPlugin extends BasePlugin {
//
//  private String targetPackage;
//
//  @Override
//  public void initialized(IntrospectedTable introspectedTable) {
//    super.initialized(introspectedTable);
//
//    this.targetPackage = getProperties().getProperty("targetPackage");
//
//    if (!StringUtility.stringHasValue(this.targetPackage)) {
//      warnings.add("The BaseMapper targetPackage must not be empty.");
//    }
//  }
//
//  @Override
//  public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(
//      IntrospectedTable introspectedTable) {
//    CommentGenerator commentGenerator = context.getCommentGenerator();
//    String originalMapperType = introspectedTable.getMyBatis3JavaMapperType();
//
//    FullyQualifiedJavaType type = new FullyQualifiedJavaType(originalMapperType);
//
//    return super.contextGenerateAdditionalJavaFiles(introspectedTable);
//  }
//}
