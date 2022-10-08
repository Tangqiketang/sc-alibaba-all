package com.wm.web.controller;


import com.wm.core.model.vo.base.BaseResp;
import com.wm.web.service.MyUserBasedRecommender;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;


@Controller
@RequestMapping("/test")
@Api(value = "推荐算法",tags={"推荐算法"})
@Slf4j
public class RecommenderController {

    @Resource
    private MyUserBasedRecommender myUserBasedRecommender;

    //查询详情
    @ApiOperation(value = "推荐算法查询", notes = "推荐算法查询")
    @GetMapping("/a")
    @ResponseBody
    public BaseResp<Object> a(long id,int size,int type){
        long startTime = System.currentTimeMillis()/1000;
        long end;
        List<RecommendedItem> recommendedItems;
        if(type==0){
            //用户协同
            recommendedItems = myUserBasedRecommender.userBasedRecommender(id, size);
            end = System.currentTimeMillis()/1000;
            log.info("type0,userBased.cost.time:"+(end-startTime));
        }else if(type==1){
            recommendedItems = myUserBasedRecommender.myItemBasedRecommender(id,size);
            end = System.currentTimeMillis()/1000;
            log.info("type1,myItemBased.cost.time:"+(end-startTime));
        }else{
            recommendedItems = myUserBasedRecommender.mySlopeOneRecommender(id,size);
            end = System.currentTimeMillis()/1000;
            log.info("type2,slopeone.cost.time:"+(end-startTime));
        }
        return BaseResp.ok(recommendedItems);
    }


}

