/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.digimono.mybatis.generator.internal.types;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.internal.types.JavaTypeResolverDefaultImpl;
import org.mybatis.generator.internal.util.StringUtility;

import java.sql.Types;
import java.util.Properties;

/** @author yangyanju */
public class JavaTypeResolverImpl extends JavaTypeResolverDefaultImpl {

  private boolean shortMapToInteger;

  public JavaTypeResolverImpl() {
    super();
  }

  @Override
  public void addConfigurationProperties(Properties properties) {
    super.addConfigurationProperties(properties);
    this.shortMapToInteger = StringUtility.isTrue(properties.getProperty("shortMapToInteger"));
  }

  @Override
  protected FullyQualifiedJavaType overrideDefaultType(
      IntrospectedColumn column, FullyQualifiedJavaType defaultType) {
    this.typeMap.put(
        Types.TINYINT,
        new JdbcTypeInformation("TINYINT", new FullyQualifiedJavaType(Integer.class.getName())));

    super.typeMap.put(
        Types.OTHER,
        new JdbcTypeInformation("NVARCHAR", new FullyQualifiedJavaType(String.class.getName())));

    return super.overrideDefaultType(column, defaultType);
  }

  @Override
  protected FullyQualifiedJavaType calculateBigDecimalReplacement(
      IntrospectedColumn column, FullyQualifiedJavaType defaultType) {
    // 1~4	  Short
    // 5~9	  Integer
    // 10~18  Long
    // 18+    BigDecimal

    if (!this.forceBigDecimals) {
      if (column.getScale() > 0 && column.getLength() <= 18) {
        return new FullyQualifiedJavaType(Double.class.getName());
      } else if (column.getScale() <= 0 && column.getLength() <= 4 && this.shortMapToInteger) {
        return new FullyQualifiedJavaType(Integer.class.getName());
      }
    }

    return super.calculateBigDecimalReplacement(column, defaultType);
  }
}
