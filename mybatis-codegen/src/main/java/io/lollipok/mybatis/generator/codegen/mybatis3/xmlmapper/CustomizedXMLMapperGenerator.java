package io.lollipok.mybatis.generator.codegen.mybatis3.xmlmapper;

import io.lollipok.mybatis.generator.utils.Utils;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.XMLMapperGenerator;

/**
 * @author yangyanju
 * @version 1.0
 * @date 2019-03-30
 */
public class CustomizedXMLMapperGenerator extends XMLMapperGenerator {

  @Override
  protected void addInsertElement(XmlElement parentElement) {
    if (Utils.onlyGenerateInsertSelectiveMethod(context, introspectedTable)) {
      return;
    }

    super.addInsertElement(parentElement);
  }

  @Override
  protected void addUpdateByPrimaryKeyWithBLOBsElement(XmlElement parentElement) {
    if (Utils.onlyGenerateUpdateSelectiveMethod(context, introspectedTable)) {
      return;
    }

    super.addUpdateByPrimaryKeyWithBLOBsElement(parentElement);
  }

  @Override
  protected void addUpdateByPrimaryKeyWithoutBLOBsElement(XmlElement parentElement) {
    if (Utils.onlyGenerateUpdateSelectiveMethod(context, introspectedTable)) {
      return;
    }

    super.addUpdateByPrimaryKeyWithoutBLOBsElement(parentElement);
  }

  @Override
  protected void addUpdateByExampleWithBLOBsElement(XmlElement parentElement) {
    if (Utils.onlyGenerateUpdateSelectiveMethod(context, introspectedTable)) {
      return;
    }

    super.addUpdateByExampleWithBLOBsElement(parentElement);
  }

  @Override
  protected void addUpdateByExampleWithoutBLOBsElement(XmlElement parentElement) {
    if (Utils.onlyGenerateUpdateSelectiveMethod(context, introspectedTable)) {
      return;
    }

    super.addUpdateByExampleWithoutBLOBsElement(parentElement);
  }
}
