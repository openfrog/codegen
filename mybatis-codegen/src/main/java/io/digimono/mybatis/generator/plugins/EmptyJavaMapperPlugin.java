package io.digimono.mybatis.generator.plugins;

import io.digimono.mybatis.generator.plugins.base.BasePlugin;
import io.digimono.mybatis.generator.utils.Utils;
import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.IntrospectedTable.TargetRuntime;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.config.PropertyRegistry;

import java.util.Collections;
import java.util.List;

/** @author yangyanju */
public class EmptyJavaMapperPlugin extends BasePlugin {

  @Override
  public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(
      IntrospectedTable introspectedTable) {
    CommentGenerator commentGenerator = context.getCommentGenerator();

    String originalMapperType = introspectedTable.getMyBatis3JavaMapperType();

    int lastDotIndex = originalMapperType.lastIndexOf(".");
    String rootInterface =
        String.format(
            "%s.%s.%s%s",
            originalMapperType.substring(0, lastDotIndex),
            Utils.getGeneratedMapperSubpackage(context),
            originalMapperType.substring(lastDotIndex + 1),
            Utils.getGeneratedMapperSuffix(context));

    FullyQualifiedJavaType type = new FullyQualifiedJavaType(originalMapperType);
    Interface interfaze = new Interface(type);
    interfaze.setVisibility(JavaVisibility.PUBLIC);
    commentGenerator.addJavaFileComment(interfaze);

    FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(rootInterface);
    interfaze.addSuperInterface(fqjt);
    interfaze.addImportedType(fqjt);

    if (introspectedTable.getTargetRuntime() == TargetRuntime.MYBATIS3) {
      interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Mapper"));
      interfaze.addAnnotation("@Mapper");
    }

    GeneratedJavaFile gjf =
        new GeneratedJavaFile(
            interfaze,
            context.getJavaClientGeneratorConfiguration().getTargetProject(),
            context.getProperty(PropertyRegistry.CONTEXT_JAVA_FILE_ENCODING),
            context.getJavaFormatter());

    return Collections.singletonList(gjf);
  }
}
