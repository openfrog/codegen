// package io.lollipok.mybatis.generator.plugins;
//
// import io.lollipok.mybatis.generator.plugins.base.BasePlugin;
// import java.util.Collections;
// import java.util.List;
// import org.mybatis.generator.api.CommentGenerator;
// import org.mybatis.generator.api.GeneratedJavaFile;
// import org.mybatis.generator.api.IntrospectedTable;
// import org.mybatis.generator.api.IntrospectedTable.TargetRuntime;
// import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
// import org.mybatis.generator.api.dom.java.Interface;
// import org.mybatis.generator.api.dom.java.JavaVisibility;
// import org.mybatis.generator.config.PropertyRegistry;
//
// /**
//  * @author yangyanju
//  * @version 1.0
//  * @date 2019-03-30
//  */
// public class EmptyJavaMapperPlugin extends BasePlugin {
//
//   @Override
//   public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(
//       IntrospectedTable introspectedTable) {
//     CommentGenerator commentGenerator = context.getCommentGenerator();
//
//     String originalMapperType = introspectedTable.getMyBatis3JavaMapperType();
//
//     int lastDotIndex = originalMapperType.lastIndexOf(".");
//     String rootInterface =
//         String.format(
//             "%s.Generated%s",
//             originalMapperType.substring(0, lastDotIndex),
//             originalMapperType.substring(lastDotIndex + 1));
//
//     FullyQualifiedJavaType type = new FullyQualifiedJavaType(originalMapperType);
//     Interface interfaze = new Interface(type);
//     interfaze.setVisibility(JavaVisibility.PUBLIC);
//     commentGenerator.addJavaFileComment(interfaze);
//
//     FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(rootInterface);
//     interfaze.addSuperInterface(fqjt);
//     interfaze.addImportedType(fqjt);
//
//     if (introspectedTable.getTargetRuntime() == TargetRuntime.MYBATIS3) {
//       interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Mapper"));
//       interfaze.addAnnotation("@Mapper");
//     }
//
//     GeneratedJavaFile gjf =
//         new GeneratedJavaFile(
//             interfaze,
//             context.getJavaClientGeneratorConfiguration().getTargetProject(),
//             context.getProperty(PropertyRegistry.CONTEXT_JAVA_FILE_ENCODING),
//             context.getJavaFormatter());
//
//     return Collections.singletonList(gjf);
//   }
// }
