package io.digimono.mybatis.kit.mapper;

import java.util.List;
import java.util.Optional;

/**
 * @author yangyj
 * @since 2023/11/18
 */
public interface BaseMapper<T, K> {

  int deleteByPrimaryKey(K id);

  int insertSelective(T entity);

  Optional<T> selectByPrimaryKey(K id);

  List<T> selectByEntity(T entity);

  int updateByPrimaryKeySelective(T entity);

  int updateBatchSelective(List<T> list);

  int markAsDeletedByPrimaryKey(K id);
}
