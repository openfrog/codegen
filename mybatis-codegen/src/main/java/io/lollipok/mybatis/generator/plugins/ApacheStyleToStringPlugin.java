package io.lollipok.mybatis.generator.plugins;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import io.lollipok.mybatis.generator.plugins.base.BaseToStringPlugin;

/**
 * @author yangyanju
 * @version 1.0
 * @date 2019-03-29
 */
public class ApacheStyleToStringPlugin extends BaseToStringPlugin {

  @Override
  protected boolean doGenerateToString(
      IntrospectedTable introspectedTable, TopLevelClass topLevelClass, Method method) {
    topLevelClass.addImportedType("org.apache.commons.lang3.builder.ToStringBuilder");
    topLevelClass.addStaticImport(
        "org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE");

    method.addBodyLine("return ToStringBuilder.reflectionToString(this, SHORT_PREFIX_STYLE);");

    topLevelClass.addMethod(method);

    return true;
  }
}
