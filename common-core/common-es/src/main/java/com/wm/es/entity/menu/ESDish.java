package com.wm.es.entity.menu;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

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
@Document(indexName = "dish_library", type = "dish")
public class ESDish implements Serializable {
  /**
   * 用于在ES中搜索菜肴
   */
  private static final long serialVersionUID = 1L;
  @Id
  private String id;
  //菜肴码
  private String dishKey;
  //菜肴名称
  @Field(type = Keyword)
  private String dish;
  //主料
  @Field(type = FieldType.Nested, includeInParent = true)
  private List<ESFood> mainFoods;
  @Field(type = FieldType.Object, includeInParent = true)
  private ESDishLabels esDishLabels;
  @Field(type = Keyword)
  private List<String> foods;
}

