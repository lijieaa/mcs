package com.cat.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface McDao {

    int mergeInto(Map map);

   /* List<Map> findByLikeObjNo(Map map);*/
}
