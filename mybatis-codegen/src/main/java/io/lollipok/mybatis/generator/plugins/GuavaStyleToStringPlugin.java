package io.lollipok.mybatis.generator.plugins;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import io.lollipok.mybatis.generator.plugins.base.BaseToStringPlugin;

/**
 * @author yangyanju
 * @version 1.0
 * @date 2019-03-29
 */
public class GuavaStyleToStringPlugin extends BaseToStringPlugin {

  @Override
  protected boolean doGenerateToString(
      IntrospectedTable introspectedTable, TopLevelClass topLevelClass, Method method) {
    topLevelClass.addImportedType("com.google.common.base.MoreObjects");

    method.addBodyLine("return MoreObjects.toStringHelper(this)");

    StringBuilder sb = new StringBuilder();
    for (Field field : topLevelClass.getFields()) {
      if (ignoreField(field)) {
        continue;
      }

      String property = field.getName();

      sb.setLength(0);
      sb.append(".add(\"").append(property).append("\", ").append(property).append(")");
      method.addBodyLine(sb.toString());
    }
    method.addBodyLine(".toString();");

    topLevelClass.addMethod(method);

    return true;
  }
}
