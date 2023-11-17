package io.digimono.mybatis.generator.plugins;

import io.digimono.mybatis.generator.plugins.base.BasePlugin;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.IntrospectedTable.TargetRuntime;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.util.Objects;

/**
 * @author yangyanju
 */
public class LombokPlugin extends BasePlugin {

  private boolean useLombokAccessors = false;

  @Override
  public void initialized(IntrospectedTable introspectedTable) {
    super.initialized(introspectedTable);

    String value = properties.getProperty("useLombokAccessors", "false");
    this.useLombokAccessors = Objects.equals(value, "true");
  }

  @Override
  public boolean modelBaseRecordClassGenerated(
      TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
    addLombokAnnotations(topLevelClass, introspectedTable);
    return true;
  }

  @Override
  public boolean modelRecordWithBLOBsClassGenerated(
      TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
    addLombokAnnotations(topLevelClass, introspectedTable);
    return true;
  }

  @Override
  public boolean modelGetterMethodGenerated(
      Method method,
      TopLevelClass topLevelClass,
      IntrospectedColumn introspectedColumn,
      IntrospectedTable introspectedTable,
      ModelClassType modelClassType) {
    return false;
  }

  @Override
  public boolean modelSetterMethodGenerated(
      Method method,
      TopLevelClass topLevelClass,
      IntrospectedColumn introspectedColumn,
      IntrospectedTable introspectedTable,
      ModelClassType modelClassType) {
    return false;
  }

  private void addLombokAnnotations(
      TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
    if (introspectedTable.getTargetRuntime() == TargetRuntime.MYBATIS3) {

      topLevelClass.addImportedType(new FullyQualifiedJavaType("lombok.Getter"));
      topLevelClass.addImportedType(new FullyQualifiedJavaType("lombok.Setter"));
      topLevelClass.addImportedType(new FullyQualifiedJavaType("lombok.ToString"));

      if (useLombokAccessors) {
        topLevelClass.addImportedType(new FullyQualifiedJavaType("lombok.experimental.Accessors"));
      }

      topLevelClass.addAnnotation("@Getter");
      topLevelClass.addAnnotation("@Setter");

      if (topLevelClass.getSuperClass().isPresent()) {
        topLevelClass.addAnnotation("@ToString(callSuper = true)");
      } else {
        topLevelClass.addAnnotation("@ToString");
      }

      if (useLombokAccessors) {
        topLevelClass.addAnnotation("@Accessors(chain = true)");
      }
    }
  }
}
