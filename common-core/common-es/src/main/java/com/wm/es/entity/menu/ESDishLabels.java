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
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ESDishLabels implements Serializable {
  /**
   * 用于ES菜肴搜索的标签列表
   */
  private static final long serialVersionUID = 1L;
  //所有食材加上这道菜本身的时令交集
  private List<Integer> months;

  //烹饪方式
  @Field(type = Keyword)
  private String cooking;

  //菜式（是否为汤）
  @Field(type = Keyword)
  private String dishType;

  //口味（咸鲜）
  @Field(type = Keyword)
  private String flavor;

  //菜系、地域（家常菜，浙菜）
  @Field(type = Keyword)
  private String cuisine;

  //选菜标签（荤菜，素菜，主食等）
  @Field(type = Keyword)
  private String selectLabel;


  //菜肴只有不宜
  @Field(type = Keyword)
  private List<String> eatGroup;
}
