package io.lollipok.mybatis.generator.internal.types;

import java.sql.Types;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.internal.types.JavaTypeResolverDefaultImpl;

/**
 * @author yangyanju
 * @version 1.0
 * @date 2019-03-29
 */
public class JavaTypeResolverImpl extends JavaTypeResolverDefaultImpl {

  public JavaTypeResolverImpl() {
    super();
  }

  @Override
  protected FullyQualifiedJavaType overrideDefaultType(
      IntrospectedColumn column, FullyQualifiedJavaType defaultType) {
    if (column.getJdbcType() == Types.TINYINT) {
      return new FullyQualifiedJavaType(Integer.class.getName());
    }

    return super.overrideDefaultType(column, defaultType);
  }
}
