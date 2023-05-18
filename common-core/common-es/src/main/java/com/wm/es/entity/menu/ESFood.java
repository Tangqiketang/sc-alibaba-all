package com.wm.es.entity.menu;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.elasticsearch.annotations.Field;

import java.io.Serializable;
import java.util.List;

import static org.springframework.data.elasticsearch.annotations.FieldType.Keyword;

/**
 * 食材营养成分表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ESFood implements Serializable {

  private static final long serialVersionUID = 1L;

  //食材标签(这里把所有可用的食材标签罗列)
  @Field(type = Keyword)
  private List<String> foodFilterLabels;

  //食材名
  @Field(type = Keyword)
  private String foodName;

  //蔬菜类食材需要避免重复
  @Field(type = Keyword)
  private Integer isVeg;

  //用于区分鱼禽蛋肉
  @Field(type = Keyword)
  private Integer meat;

  //食材关于人群的慎吃，适宜, 禁用等
  @Field(type = Keyword)
  private List<String> cannotEatGroup;
  
  @Field(type = Keyword)
  private List<String> eatGroup;
  
  @Field(type = Keyword)
  private List<String> recommendEatGroup;
}
