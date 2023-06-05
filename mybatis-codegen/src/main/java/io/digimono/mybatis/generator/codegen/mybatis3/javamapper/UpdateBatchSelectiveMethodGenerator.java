package io.digimono.mybatis.generator.codegen.mybatis3.javamapper;

import static io.digimono.mybatis.generator.constants.Constants.STATEMENT_ID_UPDATE_BATCH_SELECTIVE;

import java.util.Set;
import java.util.TreeSet;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.AbstractJavaMapperMethodGenerator;

/**
 * @author yangyj
 * @since 2023/6/5
 */
public class UpdateBatchSelectiveMethodGenerator extends AbstractJavaMapperMethodGenerator {

  public UpdateBatchSelectiveMethodGenerator() {
    super();
  }

  @Override
  public void addInterfaceElements(Interface interfaze) {
    Set<FullyQualifiedJavaType> importedTypes = new TreeSet<>();
    FullyQualifiedJavaType parameterType;

    if (introspectedTable.getRules().generateRecordWithBLOBsClass()) {
      parameterType = FullyQualifiedJavaType.getNewListInstance();
      parameterType.addTypeArgument(
          new FullyQualifiedJavaType(introspectedTable.getRecordWithBLOBsType()));
    } else {
      parameterType = FullyQualifiedJavaType.getNewListInstance();
      parameterType.addTypeArgument(
          new FullyQualifiedJavaType(introspectedTable.getBaseRecordType()));
    }

    importedTypes.add(parameterType);

    Method method = new Method(STATEMENT_ID_UPDATE_BATCH_SELECTIVE);
    method.setVisibility(JavaVisibility.PUBLIC);
    method.setAbstract(true);
    method.setReturnType(FullyQualifiedJavaType.getIntInstance());
    method.addParameter(new Parameter(parameterType, "list")); // $NON-NLS-1$

    context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);

    addMapperAnnotations(method);

    addExtraImports(interfaze);
    interfaze.addImportedTypes(importedTypes);
    interfaze.addMethod(method);
  }

  public void addMapperAnnotations(Method method) {}

  public void addExtraImports(Interface interfaze) {}
}
