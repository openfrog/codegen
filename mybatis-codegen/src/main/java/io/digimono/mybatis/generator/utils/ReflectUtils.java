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

package io.digimono.mybatis.generator.utils;

import org.mybatis.generator.internal.PluginAggregator;

import java.lang.reflect.Field;

/** @author yangyanju */
public final class ReflectUtils {

  private ReflectUtils() {}

  public static Object getValue(Object object, Class<?> clazz, String fieldName) {
    Field field = ReflectUtils.getField(object, clazz, fieldName);

    try {
      boolean wasAccessible = field.isAccessible();
      if (!wasAccessible) {
        field.setAccessible(true);
      }

      Object value = field.get(object);

      if (!wasAccessible) {
        field.setAccessible(false);
      }

      return value;
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  public static Field getField(Object object, Class<?> clazz, String fieldName) {
    try {
      return clazz.getDeclaredField(fieldName);
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }
}
