package io.digimono.mybatis.generator.plugins;

import static io.digimono.mybatis.generator.constants.Constants.IGNORED_COLUMNS_ON_BATCH_UPDATE;

import io.digimono.mybatis.generator.codegen.mybatis3.javamapper.UpdateBatchSelectiveMethodGenerator;
import io.digimono.mybatis.generator.codegen.mybatis3.xmlmapper.elements.UpdateBatchSelectiveElementGenerator;
import io.digimono.mybatis.generator.plugins.base.BasePlugin;
import io.digimono.mybatis.generator.utils.PluginUtils;
import java.util.List;

import io.digimono.mybatis.generator.utils.Utils;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.AbstractJavaMapperMethodGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

/**
 * @author yangyj
 * @since 2023/6/5
 */
public class UpdateBatchSelectivePlugin extends BasePlugin {

  private List<String> ignoredColumns;

  @Override
  public void initialized(IntrospectedTable introspectedTable) {
    super.initialized(introspectedTable);

    this.ignoredColumns =
        PluginUtils.getProperties(properties, introspectedTable, IGNORED_COLUMNS_ON_BATCH_UPDATE);
  }

  @Override
  public boolean clientGenerated(Interface interfaze, IntrospectedTable introspectedTable) {
    if (Utils.generateEmptyJavaMapper(context, introspectedTable)) {
      return true;
    }

    AbstractJavaMapperMethodGenerator methodGenerator = new UpdateBatchSelectiveMethodGenerator();
    methodGenerator.setContext(context);
    methodGenerator.setIntrospectedTable(introspectedTable);
    methodGenerator.addInterfaceElements(interfaze);
    return super.clientGenerated(interfaze, introspectedTable);
  }

  @Override
  public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
    AbstractXmlElementGenerator elementGenerator =
        new UpdateBatchSelectiveElementGenerator(ignoredColumns);
    elementGenerator.setContext(context);
    elementGenerator.setIntrospectedTable(introspectedTable);
    elementGenerator.addElements(document.getRootElement());
    return super.sqlMapDocumentGenerated(document, introspectedTable);
  }
}
