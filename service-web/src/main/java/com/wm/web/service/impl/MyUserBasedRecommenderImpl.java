package com.wm.web.service.impl;

import com.wm.web.mahout.model.MyDataModel;
import com.wm.web.service.MyUserBasedRecommender;
import lombok.extern.slf4j.Slf4j;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.CachingRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.slopeone.SlopeOneRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;

/**
 * 描述:用户协同,基于用户
 *
 * @auther WangMin
 * @create 2022-09-12 21:19
 */
@Service
@Slf4j
public class MyUserBasedRecommenderImpl implements MyUserBasedRecommender {


    @Resource
    private MyDataModel myDataModel;

    /**
     * kNN算法的优缺点
     * 优点：
     * 简单好用，容易理解，精度高，理论成熟，既可以用来做分类也可以用来做回归；
     * 可用于数值型数据和离散型数据；
     * 训练时间复杂度为O(n)；无数据输入假定；
     * 对异常值不敏感
     * 缺点：
     * 计算复杂性高；空间复杂性高；
     * 样本不平衡问题（即有些类别的样本数量很多，而其它样本的数量很少）；
     * 一般数值很大的时候不用这个，计算量太大。但是单个样本又不能太少，否则容易发生误分。
     * 最大的缺点是无法给出数据的内在含义。
     *
     * 需要用户先有打分.通过共同口味与偏好找相似邻居用户
     * @param userID
     * @param size
     * @return  2571/933/1997/2000/1275
     */
    @Override
    public List<RecommendedItem>   userBasedRecommender(long userID, int size){
        // step:1 构建模型 2 计算相似度 3 查找k紧邻 4 构造推荐引擎
        List<RecommendedItem> recommendations = null;
        try {
            DataModel model = myDataModel.myDataModel();//构造数据模型
            //用户相似度.使用1皮尔逊相关系数2.欧几里得距离3.塔尼莫托系数(谷本系数)
            UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
            //UserSimilarity similarity=new EuclideanDistanceSimilarity(model);
            //UserSimilarity similarity = new TanimotoCoefficientSimilarity(model);

            //将与该用户最近距离为 3 的用户设置为该用户的“邻居”。圆的半径=3
            UserNeighborhood neighborhood = new NearestNUserNeighborhood(3, similarity, model);
            ////采用 CachingRecommender 为 RecommendationItem 进行缓存
            Recommender recommender = new CachingRecommender(new GenericUserBasedRecommender(model, neighborhood, similarity));
            recommendations = recommender.recommend(userID, size);//得到推荐的结果，size是推荐结果的数目
        } catch (Exception e) {
            e.printStackTrace();
        }
        return recommendations;
    }

    /**
     * 发现物品之间的相似度，推荐类似的物品。
     * @param userID
     * @param size
     * @return
     */
    @Override
    public List<RecommendedItem> myItemBasedRecommender(long userID,int size){
        List<RecommendedItem> recommendations = null;
        try {
            DataModel model = new FileDataModel(new File("D:/movie_preferences.txt"));//构造数据模型
            //DataModel model = myDataModel.myDataModel();//构造数据模型
            //内容相似度
            ItemSimilarity similarity = new PearsonCorrelationSimilarity(model);
            Recommender recommender = new GenericItemBasedRecommender(model, similarity);//构造推荐引擎
            recommendations = recommender.recommend(userID, size);//得到推荐结果
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return recommendations;
    }

    //74s  3382/557/2438/3656/572

    /**
     * 轻量级
     * @param userID
     * @param size
     * @return
     */
    @Override
    public List<RecommendedItem> mySlopeOneRecommender(long userID,int size){
        List<RecommendedItem> recommendations = null;
        try {
            DataModel model = new FileDataModel(new File("D:/movie_preferences.txt"));//构造数据模型
            //DataModel model = myDataModel.myDataModel();//构造数据模型
            Recommender recommender = new CachingRecommender(new SlopeOneRecommender(model));//构造推荐引擎
            recommendations = recommender.recommend(userID, size);//得到推荐的结果，size是推荐接过的数目
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return recommendations;
    }


}   //标签tag。es这种。用户、商品打标签(青年、男、女、//包含哪些用户群体 )