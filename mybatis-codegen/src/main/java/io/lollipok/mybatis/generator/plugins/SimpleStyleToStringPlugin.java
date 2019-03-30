package io.lollipok.mybatis.generator.plugins;

import io.lollipok.mybatis.generator.plugins.base.BaseToStringPlugin;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;

/**
 * @author yangyanju
 * @version 1.0
 * @date 2019-03-29
 */
public class SimpleStyleToStringPlugin extends BaseToStringPlugin {

  @Override
  protected boolean doGenerateToString(
      IntrospectedTable introspectedTable, TopLevelClass topLevelClass, Method method) {
    method.addBodyLine("StringBuilder sb = new StringBuilder();");
    method.addBodyLine("sb.append(getClass().getSimpleName());");
    method.addBodyLine("sb.append(\" [\");");

    StringBuilder sb = new StringBuilder();
    for (Field field : topLevelClass.getFields()) {
      if (ignoreField(field)) {
        continue;
      }

      String property = field.getName();

      sb.setLength(0);
      sb.append("sb.append(\"")
          .append(", ")
          .append(property)
          .append("=\")")
          .append(".append(")
          .append(property)
          .append(");");
      method.addBodyLine(sb.toString());
    }

    method.addBodyLine("sb.append(\"]\");");
    if (useToStringFromRoot && topLevelClass.getSuperClass() != null) {
      method.addBodyLine("sb.append(\", from super class \");");
      method.addBodyLine("sb.append(super.toString());");
    }
    method.addBodyLine("return sb.toString();");

    topLevelClass.addMethod(method);

    return true;
  }
}
