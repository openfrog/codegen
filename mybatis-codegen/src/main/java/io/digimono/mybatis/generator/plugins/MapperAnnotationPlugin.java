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

package io.digimono.mybatis.generator.plugins;

import io.digimono.mybatis.generator.utils.PluginUtils;

import java.util.List;

/** @author yangyanju */
public class MapperAnnotationPlugin extends org.mybatis.generator.plugins.MapperAnnotationPlugin {

  @Override
  public boolean validate(List<String> warnings) {
    try {
      boolean hasEmptyJavaMapperPlugin =
          PluginUtils.hasPlugin(context, EmptyJavaMapperPlugin.class);
      if (hasEmptyJavaMapperPlugin) {
        return false;
      }
    } catch (Exception ex) {
      warnings.add(
          String.format(
              "Plugin %s error. Msg: %s", MapperAnnotationPlugin.class.getName(), ex.getMessage()));

      return false;
    }

    return super.validate(warnings);
  }
}
