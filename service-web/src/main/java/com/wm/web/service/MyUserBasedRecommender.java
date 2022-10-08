package com.wm.web.service;

import org.apache.mahout.cf.taste.recommender.RecommendedItem;

import java.util.List;

public interface MyUserBasedRecommender{
    List<RecommendedItem> userBasedRecommender(long userID, int size);

    List<RecommendedItem> myItemBasedRecommender(long userID, int size);

    List<RecommendedItem> mySlopeOneRecommender(long userID, int size);
}
