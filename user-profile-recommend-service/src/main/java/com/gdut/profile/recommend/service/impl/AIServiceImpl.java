package com.gdut.profile.recommend.service.impl;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.airec.model.v20181012.PushDocumentRequest;
import com.aliyuncs.airec.model.v20181012.PushDocumentResponse;
import com.aliyuncs.http.FormatType;
import com.gdut.profile.recommend.aiclient.AIClient;
import com.gdut.profile.recommend.service.AIService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author liujunliang
 * @date 2025/12/2
 */
@Service
public class AIServiceImpl implements AIService {
    /**
     * 推送单个用户的详细信息到 AIRec 的user表，用于初始化或更新单个用户数据。
     */
    @Override
    public void pushOneUser(){
        DefaultAcsClient client = AIClient.getAcsClient();


        PushDocumentRequest request = new PushDocumentRequest();
        request.setAcceptFormat(FormatType.JSON);
        //填入实例id
        request.setInstanceId(AIClient.getInstanceId());
        //填入要上报的数据表名：user/item/behavior
        request.setTableName("user");

        String content = JSON.toJSONString(buildOneUser("add"));
        request.setHttpContent(content.getBytes(), "UTF-8", FormatType.JSON);

        try {
            PushDocumentResponse response = client.getAcsResponse(request);
            System.out.println(response.getResult());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static  List<Map<String,Object>> buildOneUser(String cmd){
        List<Map<String,Object>> pushList = Lists.newArrayList();
        Map<String,Object> pushMap = Maps.newHashMap();
        Map<String,Object> userMap = Maps.newHashMap();

        userMap.put("user_id", "hltest_001");
        userMap.put("user_id_type", "update");
        userMap.put("third_user_name", "third_user_name");
        userMap.put("third_user_type", "third_user_type");
        userMap.put("phone_md5", "phone_md5");
        userMap.put("imei", "");
        userMap.put("content", "content");
        userMap.put("gender", "gender");
        userMap.put("age", "1");
        userMap.put("age_group", "age_group");
        userMap.put("country", "country");
        userMap.put("city", "city");
        userMap.put("ip", "ip");
        userMap.put("device_model", "device_model");
        userMap.put("register_time", System.currentTimeMillis()/1000);
        userMap.put("last_login_time", System.currentTimeMillis()/1000);
        userMap.put("last_modify_time", System.currentTimeMillis()/1000);
        userMap.put("tags", "tags");
        userMap.put("source", "source");
        userMap.put("features", "features");
        userMap.put("num_features", "num_features");
        pushMap.put("fields",userMap);
        pushMap.put("cmd",cmd);
        pushList.add(pushMap);
        return pushList;
    }

    /**
     * 批量推送 1000 个用户的基础信息到 AIRec 的user表，用于生成测试用的用户数据集。
     */
    @Override
    public void pushRecommendUser(){
        DefaultAcsClient client = AIClient.getAcsClient();


        PushDocumentRequest request = new PushDocumentRequest();
        request.setAcceptFormat(FormatType.JSON);
        //填入实例id
        request.setInstanceId(AIClient.getInstanceId());
        //填入要上报的数据表名：user/item/behavior
        request.setTableName("user");

        String content = JSON.toJSONString(buildRecommendUser());
        request.setHttpContent(content.getBytes(), "UTF-8", FormatType.JSON);

        try {
            PushDocumentResponse response = client.getAcsResponse(request);
            System.out.println(response.getResult());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static  List<Map<String,Object>> buildRecommendUser(){
        List<Map<String,Object>> pushList = Lists.newArrayList();
        for(int i=0;i<1000;i++){
            Map<String,Object> pushMap = Maps.newHashMap();
            Map<String,Object> userMap = Maps.newHashMap();
            userMap.put("user_id", "user_id"+i);
            userMap.put("user_id_type", "user_id_type");
            userMap.put("third_user_name", "third_user_name");
            userMap.put("third_user_type", "third_user_type");
            userMap.put("phone_md5", "phone_md5");
            userMap.put("imei", "");
            userMap.put("content", "content");
            userMap.put("gender", "gender");
            userMap.put("age", "1");
            userMap.put("age_group", "age_group");
            userMap.put("country", "country");
            userMap.put("city", "city");
            userMap.put("ip", "ip");
            userMap.put("device_model", "device_model");
            userMap.put("register_time", System.currentTimeMillis()/1000);
            userMap.put("last_login_time", System.currentTimeMillis()/1000);
            userMap.put("last_modify_time", System.currentTimeMillis()/1000);
            userMap.put("tags", "tags");
            userMap.put("source", "source");
            userMap.put("features", "features");
            userMap.put("num_features", "num_features");
            pushMap.put("fields",userMap);
            pushMap.put("cmd","add");
            pushList.add(pushMap);
        }

        return pushList;
    }

    /**
     * 推送单个内容类物品（如图片、文章）的详细信息到 AIRec 的item表，用于初始化或更新单个内容物品。
     */
    @Override
    public void pushOneContentItem(){
        DefaultAcsClient client = AIClient.getAcsClient();


        PushDocumentRequest request = new PushDocumentRequest();
        request.setAcceptFormat(FormatType.JSON);
        //填入实例id
        request.setInstanceId(AIClient.getInstanceId());
        //填入要上报的数据表名：user/item/behavior
        request.setTableName("item");

        String content = JSON.toJSONString(buildOneContentsItem());
        request.setHttpContent(content.getBytes(), "UTF-8", FormatType.JSON);

        try {
            PushDocumentResponse response = client.getAcsResponse(request);
            System.out.println(response.getResult());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static  List<Map<String,Object>> buildOneContentsItem(){
        List<Map<String,Object>> pushList = Lists.newArrayList();
        Map<String,Object> pushMap = Maps.newHashMap();
        Map<String,Object> itemMap = Maps.newHashMap();

        itemMap.put("item_id","hl162981_test_element_01");
        itemMap.put("item_type", "image");
        itemMap.put("title", "title");
        itemMap.put("content", "content");
        itemMap.put("user_id", "user_id1");
        itemMap.put("pub_time", System.currentTimeMillis()/1000);
        itemMap.put("status", 1);
        itemMap.put("expire_time", System.currentTimeMillis()/1000+3600*24);
        itemMap.put("last_modify_time", System.currentTimeMillis()/1000);
        itemMap.put("scene_id", "scene_id1");
        itemMap.put("duration", "10");
        itemMap.put("category_level", "2");
        itemMap.put("category_path", "1_1");
        itemMap.put("tags", "tags");
        itemMap.put("channel", "channel");
        itemMap.put("organization", "organization");
        itemMap.put("author", "author");
        itemMap.put("pv_cnt", "1");
        itemMap.put("click_cnt", "1");
        itemMap.put("like_cnt", "1");
        itemMap.put("unlike_cnt", "1");
        itemMap.put("comment_cnt", "1");
        itemMap.put("collect_cnt", "1");
        itemMap.put("share_cnt", "1");
        itemMap.put("download_cnt", "1");
        itemMap.put("tip_cnt", "1");
        itemMap.put("subscribe_cnt", "1");
        itemMap.put("source_id", "1");
        itemMap.put("country", "country");
        itemMap.put("city", "city");
        itemMap.put("features", "features");
        itemMap.put("num_features", "num_features");
        pushMap.put("fields",itemMap);
        pushMap.put("cmd","add");
        pushList.add(pushMap);
        return pushList;
    }

    /**
     * 针对内容行业（如图文、视频等），批量推送 1000 条物品数据到 AIRec 的item表，用于测试物品质量分布功能。
     */
    @Override
    public void pusDistributeContent() {
        DefaultAcsClient client = AIClient.getAcsClient();


        PushDocumentRequest request = new PushDocumentRequest();
        request.setAcceptFormat(FormatType.JSON);
        //填入实例id
        request.setInstanceId(AIClient.getInstanceId());
        //填入要上报的数据表名：user/item/behavior
        request.setTableName("item");
        List<List<Map<String, Object>>> res = builContentItemDistribute();
        for (List<Map<String, Object>> list : res) {
            String content = JSON.toJSONString(list);
            request.setHttpContent(content.getBytes(), "UTF-8", FormatType.JSON);
            try {
                PushDocumentResponse response = client.getAcsResponse(request);
                System.out.println(response.getResult());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static List<List<Map<String, Object>>> builContentItemDistribute() {
        String[] scene = new String[]{"scene_id1", "scene_id2"};
        String[] type = new String[]{"image", "item", "recipe", "shortvideo", "video", "audio", "article"};
        String[] content = new String[]{"There are moments in life when you miss someone so much that you just want " +
                "to pick them from your dreams and hug them for real! Dream what you want to dream;go where you want " +
                "to go;be what you want to be,because you have only one life and one chance to do all the things you " +
                "want to do", "May you have enough happiness to make you sweet,enough trials to make you strong," +
                "enough sorrow to keep you human,enough hope to make you happy? Always put yourself in others’shoes" +
                ".If you feel that it hurts you,it probably hurts the other person, too", "The happiest of people " +
                "don’t necessarily have the best of everything;they just make the most of everything that comes along" +
                " their way.Happiness lies for those who cry,those who hurt, those who have searched,and those who " +
                "have tried,for only they can appreciate the importance of people", "who have touched their lives" +
                ".Love begins with a smile,grows with a kiss and ends with a tear.The brightest future will always be" +
                " based on a forgotten past, you can’t go on well in lifeuntil you let go of your past failures and " +
                "heartaches.",
                "When you were born,you were crying and everyone around you was smiling.Live your life so that when " +
                        "you die,you're the one who is smiling and everyone around you is crying.", "Please send this" +
                " message to those people who mean something to you,to those who have touched your life in one way or" +
                " another,to those who make you smile when you really need it,to those that make you see the brighter" +
                " side of things when you are really down", "to those who you want to let them know that you " +
                "appreciate their friendship.And if you don’t, don’t worry,nothing bad will happen to you,you will " +
                "just miss out on the opportunity to brighten someone’s day with this message."};
        String[] title = new String[]{"The power of smiles", "The meaning of life", "Value every minute", "Never give" +
                " up", "The greatest pain in life", "Yesterday, today and tomorrow", "Love is understanding"};

        List<List<Map<String, Object>>> resList = Lists.newArrayList();
        for (int i = 0; i < 7; i++) {
            List<Map<String, Object>> pushList = Lists.newArrayList();
            for (int j = 0; j < 1000; j++) {
                Map<String, Object> pushMap = Maps.newHashMap();
                Map<String, Object> itemMap = Maps.newHashMap();
                itemMap.put("item_id", "item_id" + j);
                itemMap.put("item_type", type[i]);
                itemMap.put("title", title[j%7]);
                itemMap.put("content", content[(i+j)%7]);
                itemMap.put("user_id", "user_id" + j);
                itemMap.put("pub_time", System.currentTimeMillis() / 1000 + (3600 * 24) * (10 - j % 20));
                itemMap.put("status", 1);
                itemMap.put("expire_time", System.currentTimeMillis() / 1000 + (3600 * 24) * (5 - j % 10));
                itemMap.put("last_modify_time", System.currentTimeMillis() / 1000);
                itemMap.put("scene_id", scene[j % 2]);
                itemMap.put("duration", "10");
                itemMap.put("category_level", "3");
                itemMap.put("category_path", i + "_" + j % 10 + "_" + j % 10);
                itemMap.put("tags", "tag" + j % 10);
                itemMap.put("channel", "channel" + j % 10);
                itemMap.put("organization", "organization" + j % 10);
                itemMap.put("author", "user_id" + j % 10);
                itemMap.put("pv_cnt", "1");
                itemMap.put("click_cnt", "1");
                itemMap.put("like_cnt", "1");
                itemMap.put("unlike_cnt", "1");
                itemMap.put("comment_cnt", "1");
                itemMap.put("collect_cnt", "1");
                itemMap.put("share_cnt", "1");
                itemMap.put("download_cnt", "1");
                itemMap.put("tip_cnt", "1");
                itemMap.put("subscribe_cnt", "1");
                itemMap.put("source_id", "1");
                itemMap.put("country", "country" + j % 10);
                itemMap.put("city", "city" + j % 10);
                itemMap.put("features", "features");
                if(j%2==0){
                    itemMap.put("weight", 1);
                }else {
                    itemMap.put("weight", 100);
                }
                itemMap.put("num_features", "num_features");
                pushMap.put("fields", itemMap);
                pushMap.put("cmd", "add");
                pushList.add(pushMap);
            }
            resList.add(pushList);
        }

        return resList;
    }

    /**
     * 推送单个通用物品（如商品、新闻）的信息到 AIRec 的item表，适用于非特定行业的物品初始化。
     */
    @Override
    public void pushOneGeneralItem() {
        DefaultAcsClient client = AIClient.getAcsClient();


        PushDocumentRequest request = new PushDocumentRequest();
        request.setAcceptFormat(FormatType.JSON);
        //填入实例id
        request.setInstanceId(AIClient.getInstanceId());
        //填入要上报的数据表名：user/item/behavior
        request.setTableName("item");

        String content = JSON.toJSONString(buildOneGeneralItem());
        request.setHttpContent(content.getBytes(), "UTF-8", FormatType.JSON);

        try {
            PushDocumentResponse response = client.getAcsResponse(request);
            System.out.println(response.getResult());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static List<Map<String, Object>> buildOneGeneralItem() {
        List<Map<String, Object>> pushList = Lists.newArrayList();
        Map<String, Object> pushMap = Maps.newHashMap();
        Map<String, Object> itemMap = Maps.newHashMap();

        itemMap.put("scene_id", "scene_id1");
        itemMap.put("item_id", "new_element_001");
        itemMap.put("item_type", "title");
        itemMap.put("category_level", "2");
        itemMap.put("category_path", "1_1");
        itemMap.put("title", "title");
        itemMap.put("content", "content");
        itemMap.put("pub_time", System.currentTimeMillis() / 1000);
        itemMap.put("tags", "tag_update");
        itemMap.put("share_cnt", "1");
        itemMap.put("collect_cnt", "1");
        itemMap.put("pv_cnt", "1");
        itemMap.put("status", 1);
        itemMap.put("expire_time", System.currentTimeMillis() / 1000 + (3600 * 24));
        itemMap.put("last_modify_time", System.currentTimeMillis() / 1000);
        itemMap.put("origin_price", "");
        itemMap.put("cur_price", "");
        itemMap.put("buy_cnt", "");
        itemMap.put("source_buy_cnt", "");
        itemMap.put("comment_cnt", "");
        itemMap.put("brand_id", "brand_id");
        itemMap.put("shop_id", "shop_id");
        itemMap.put("source_id", "1");
        itemMap.put("add_fee", "");
        itemMap.put("features", "features");
        itemMap.put("num_features", "num_features");
        itemMap.put("weight", 1);
        pushMap.put("fields", itemMap);
        pushMap.put("cmd", "add");
        pushList.add(pushMap);
        return pushList;
    }

    /**
     * 针对新闻行业，批量推送 1000 条物品数据到item表，用于测试新闻行业的物品质量分布。
     */
    @Override
    public void pusDistributeItem() {
        DefaultAcsClient client = AIClient.getAcsClient();


        PushDocumentRequest request = new PushDocumentRequest();
        request.setAcceptFormat(FormatType.JSON);
        //填入实例id
        request.setInstanceId(AIClient.getInstanceId());
        //填入要上报的数据表名：user/item/behavior
        request.setTableName("item");
        List<List<Map<String, Object>>> res = builItemItemDistribute();
        for (List<Map<String, Object>> list : res) {
            String content = JSON.toJSONString(list);
            request.setHttpContent(content.getBytes(), "UTF-8", FormatType.JSON);
            try {
                PushDocumentResponse response = client.getAcsResponse(request);
                System.out.println(response.getResult());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static List<List<Map<String, Object>>> builItemItemDistribute() {
        String[] scene = new String[]{"scene_id1", "scene_id2"};
        String[] type = new String[]{"image", "item", "recipe", "shortvideo", "video", "audio", "article"};
        String[] content = new String[]{"There are moments in life when you miss someone so much that you just want " +
                "to pick them from your dreams and hug them for real! Dream what you want to dream;go where you want " +
                "to go;be what you want to be,because you have only one life and one chance to do all the things you " +
                "want to do", "May you have enough happiness to make you sweet,enough trials to make you strong," +
                "enough sorrow to keep you human,enough hope to make you happy? Always put yourself in others’shoes" +
                ".If you feel that it hurts you,it probably hurts the other person, too", "The happiest of people " +
                "don’t necessarily have the best of everything;they just make the most of everything that comes along" +
                " their way.Happiness lies for those who cry,those who hurt, those who have searched,and those who " +
                "have tried,for only they can appreciate the importance of people", "who have touched their lives" +
                ".Love begins with a smile,grows with a kiss and ends with a tear.The brightest future will always be" +
                " based on a forgotten past, you can’t go on well in lifeuntil you let go of your past failures and " +
                "heartaches.",
                "When you were born,you were crying and everyone around you was smiling.Live your life so that when " +
                        "you die,you're the one who is smiling and everyone around you is crying.", "Please send this" +
                " message to those people who mean something to you,to those who have touched your life in one way or" +
                " another,to those who make you smile when you really need it,to those that make you see the brighter" +
                " side of things when you are really down", "to those who you want to let them know that you " +
                "appreciate their friendship.And if you don’t, don’t worry,nothing bad will happen to you,you will " +
                "just miss out on the opportunity to brighten someone’s day with this message."};
        String[] title = new String[]{"The power of smiles", "The meaning of life", "Value every minute", "Never give" +
                " up", "The greatest pain in life", "Yesterday, today and tomorrow", "Love is understanding"};


        List<List<Map<String, Object>>> resList = Lists.newArrayList();
        for (int i = 0; i < 7; i++) {
            List<Map<String, Object>> pushList = Lists.newArrayList();
            for (int j = 0; j < 1000; j++) {
                Map<String, Object> pushMap = Maps.newHashMap();
                Map<String, Object> itemMap = Maps.newHashMap();
                itemMap.put("scene_id", scene[j % 2]);


                itemMap.put("item_id", "item_id" + j);
                itemMap.put("item_type", type[i]);
                itemMap.put("category_level", "3");
                itemMap.put("category_path", i + "_" + j % 10 + "_" + j % 10);

                itemMap.put("title", title[j%7]);
                itemMap.put("content", content[(i+j)%7]);
                itemMap.put("pub_time", System.currentTimeMillis() / 1000 + (3600 * 24) * (10 - j % 20));
                itemMap.put("tags", "tag" + j % 10);
                itemMap.put("share_cnt", "1");
                itemMap.put("collect_cnt", "1");
                itemMap.put("pv_cnt", "1");
                itemMap.put("status", 1);
                itemMap.put("expire_time", System.currentTimeMillis() / 1000 + (3600 * 24) * (5 - j % 10));
                itemMap.put("last_modify_time", System.currentTimeMillis() / 1000);
                itemMap.put("origin_price", "");
                itemMap.put("cur_price", "");
                itemMap.put("buy_cnt", "");
                itemMap.put("source_buy_cnt", "");
                itemMap.put("comment_cnt", "");
                itemMap.put("brand_id", "brand_id" + j % 5);
                itemMap.put("shop_id", "shop_id" + j % 3);
                itemMap.put("source_id", "1");
                itemMap.put("add_fee", "");
                itemMap.put("features", "features");
                itemMap.put("num_features", "num_features");
                if (j % 2 == 0) {
                    itemMap.put("weight", 1);
                } else {
                    itemMap.put("weight", 100);
                }
                pushMap.put("fields", itemMap);
                pushMap.put("cmd", "add");
                pushList.add(pushMap);
            }
            resList.add(pushList);
        }

        return resList;
    }

    /**
     * 推送单个新闻物品的详细信息到item表，用于初始化或更新单条新闻数据。
     */
    @Override
    public void pushOneNewsItem(){
        DefaultAcsClient client = AIClient.getAcsClient();


        PushDocumentRequest request = new PushDocumentRequest();
        request.setAcceptFormat(FormatType.JSON);
        //填入实例id
        request.setInstanceId(AIClient.getInstanceId());
        //填入要上报的数据表名：user/item/behavior
        request.setTableName("item");

        String content = JSON.toJSONString(buildOneNewsItem());
        request.setHttpContent(content.getBytes(), "UTF-8", FormatType.JSON);

        try {
            PushDocumentResponse response = client.getAcsResponse(request);
            System.out.println(response.getResult());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static  List<Map<String,Object>> buildOneNewsItem(){
        List<Map<String,Object>> pushList = Lists.newArrayList();
        Map<String,Object> pushMap = Maps.newHashMap();
        Map<String,Object> itemMap = Maps.newHashMap();

        itemMap.put("item_id","item_id1");
        itemMap.put("item_type", "image");
        itemMap.put("title", "title");
        itemMap.put("content", "content");
        itemMap.put("user_id", "user_id1");
        itemMap.put("pub_time", System.currentTimeMillis()/1000);
        itemMap.put("status", 1);
        itemMap.put("expire_time", System.currentTimeMillis()/1000+3600*24);
        itemMap.put("last_modify_time", System.currentTimeMillis()/1000);
        itemMap.put("scene_id", "scene_id1");
        itemMap.put("duration", "10");
        itemMap.put("category_level", "2");
        itemMap.put("category_path", "1_1");
        itemMap.put("tags", "tags");
        itemMap.put("channel", "channel");
        itemMap.put("organization", "organization");
        itemMap.put("author", "author");
        itemMap.put("pv_cnt", "1");
        itemMap.put("click_cnt", "1");
        itemMap.put("like_cnt", "1");
        itemMap.put("unlike_cnt", "1");
        itemMap.put("comment_cnt", "1");
        itemMap.put("collect_cnt", "1");
        itemMap.put("share_cnt", "1");
        itemMap.put("download_cnt", "1");
        itemMap.put("tip_cnt", "1");
        itemMap.put("subscribe_cnt", "1");
        itemMap.put("source_id", "1");
        itemMap.put("country", "country");
        itemMap.put("city", "city");
        itemMap.put("features", "features");
        itemMap.put("num_features", "num_features");
        pushMap.put("fields",itemMap);
        pushMap.put("cmd","add");
        pushList.add(pushMap);
        return pushList;
    }


    /**
     * 批量推送新闻行业的物品数据（共 1000 条）到item表，用于测试新闻行业的物品质量分布。
     */
    @Override
    public void pusDistributeNews() {
        DefaultAcsClient client = AIClient.getAcsClient();


        PushDocumentRequest request = new PushDocumentRequest();
        request.setAcceptFormat(FormatType.JSON);
        //填入实例id
        request.setInstanceId(AIClient.getInstanceId());
        //填入要上报的数据表名：user/item/behavior
        request.setTableName("item");
        List<List<Map<String, Object>>> res = builNewsItemDistribute();
        for (List<Map<String, Object>> list : res) {
            String content = JSON.toJSONString(list);
            request.setHttpContent(content.getBytes(), "UTF-8", FormatType.JSON);
            try {
                PushDocumentResponse response = client.getAcsResponse(request);
                System.out.println(response.getResult());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static List<List<Map<String, Object>>> builNewsItemDistribute() {
        String[] scene = new String[]{"scene_id1", "scene_id2"};
        String[] type = new String[]{"image", "item", "recipe", "shortvideo", "video", "audio", "article"};
        String[] content = new String[]{"There are moments in life when you miss someone so much that you just want " +
                "to pick them from your dreams and hug them for real! Dream what you want to dream;go where you want " +
                "to go;be what you want to be,because you have only one life and one chance to do all the things you " +
                "want to do", "May you have enough happiness to make you sweet,enough trials to make you strong," +
                "enough sorrow to keep you human,enough hope to make you happy? Always put yourself in others’shoes" +
                ".If you feel that it hurts you,it probably hurts the other person, too", "The happiest of people " +
                "don’t necessarily have the best of everything;they just make the most of everything that comes along" +
                " their way.Happiness lies for those who cry,those who hurt, those who have searched,and those who " +
                "have tried,for only they can appreciate the importance of people", "who have touched their lives" +
                ".Love begins with a smile,grows with a kiss and ends with a tear.The brightest future will always be" +
                " based on a forgotten past, you can’t go on well in lifeuntil you let go of your past failures and " +
                "heartaches.",
                "When you were born,you were crying and everyone around you was smiling.Live your life so that when " +
                        "you die,you're the one who is smiling and everyone around you is crying.", "Please send this" +
                " message to those people who mean something to you,to those who have touched your life in one way or" +
                " another,to those who make you smile when you really need it,to those that make you see the brighter" +
                " side of things when you are really down", "to those who you want to let them know that you " +
                "appreciate their friendship.And if you don’t, don’t worry,nothing bad will happen to you,you will " +
                "just miss out on the opportunity to brighten someone’s day with this message."};
        String[] title = new String[]{"The power of smiles", "The meaning of life", "Value every minute", "Never give" +
                " up", "The greatest pain in life", "Yesterday, today and tomorrow", "Love is understanding"};

        List<List<Map<String, Object>>> resList = Lists.newArrayList();
        for (int i = 0; i < 7; i++) {
            List<Map<String, Object>> pushList = Lists.newArrayList();
            for (int j = 0; j < 1000; j++) {
                Map<String, Object> pushMap = Maps.newHashMap();
                Map<String, Object> itemMap = Maps.newHashMap();
                itemMap.put("item_id", "item_id" + j);
                itemMap.put("item_type", type[i]);
                itemMap.put("title", title[j%7]);
                itemMap.put("content", content[(i+j)%7]);
                itemMap.put("user_id", "user_id" + j);
                itemMap.put("pub_time", System.currentTimeMillis() / 1000 + (3600 * 24) * (10 - j % 20));
                itemMap.put("status", 1);
                itemMap.put("expire_time", System.currentTimeMillis() / 1000 + (3600 * 24) * (5 - j % 10));
                itemMap.put("last_modify_time", System.currentTimeMillis() / 1000);
                itemMap.put("scene_id", scene[j % 2]);
                itemMap.put("duration", "10");
                itemMap.put("category_level", "3");
                itemMap.put("category_path", i + "_" + j % 10 + "_" + j % 10);
                itemMap.put("tags", "tag" + j % 10);
                itemMap.put("channel", "channel" + j % 10);
                itemMap.put("organization", "organization" + j % 10);
                itemMap.put("author", "user_id" + j % 10);
                itemMap.put("pv_cnt", "1");
                itemMap.put("click_cnt", "1");
                itemMap.put("like_cnt", "1");
                itemMap.put("unlike_cnt", "1");
                itemMap.put("comment_cnt", "1");
                itemMap.put("collect_cnt", "1");
                itemMap.put("share_cnt", "1");
                itemMap.put("download_cnt", "1");
                itemMap.put("tip_cnt", "1");
                itemMap.put("subscribe_cnt", "1");
                itemMap.put("source_id", "1");
                itemMap.put("country", "country" + j % 10);
                itemMap.put("city", "city" + j % 10);
                itemMap.put("features", "features");
                if (j % 2 == 0) {
                    itemMap.put("weight", 1);
                } else {
                    itemMap.put("weight", 100);
                }
                itemMap.put("num_features", "num_features");
                pushMap.put("fields", itemMap);
                pushMap.put("cmd", "add");
                pushList.add(pushMap);
            }
            resList.add(pushList);
        }

        return resList;
    }

    /**
     * 批量推送历史用户行为数据（如曝光、点击）到 AIRec 的behavior表，为推荐模型提供用户交互数据。
     */
    @Override
    public void pushRecommendBhv() {
        DefaultAcsClient client = AIClient.getAcsClient();


        PushDocumentRequest request = new PushDocumentRequest();
        request.setAcceptFormat(FormatType.JSON);
        //填入实例id
        request.setInstanceId(AIClient.getInstanceId());
        //填入要上报的数据表名：user/item/behavior
        request.setTableName("behavior");
        List<List<Map<String, Object>>> pushList = buildRecommendBhv();
        for (List<Map<String, Object>> list : pushList) {
            String content = JSON.toJSONString(list);
            request.setHttpContent(content.getBytes(), "UTF-8", FormatType.JSON);
            try {
                PushDocumentResponse response = client.getAcsResponse(request);
                System.out.println(response.getResult());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static List<List<Map<String, Object>>> buildRecommendBhv() {
        String[] scene = new String[]{"scene_id1", "scene_id2"};
        String[] type = new String[]{"image", "item", "recipe", "shortvideo", "video", "audio", "article"};
        List<List<Map<String, Object>>> pushList = Lists.newArrayList();
        for (int i = 0; i < 1000; i++) {
            List<Map<String, Object>> recordList = Lists.newArrayList();
            for (int j = 0; j < 100; j++) {
                Map<String, Object> pushMap = Maps.newHashMap();
                Map<String, Object> bhvMap = Maps.newHashMap();
                bhvMap.put("trace_id", "selfhold");
                bhvMap.put("trace_info", "");
                bhvMap.put("platform", "platform" + j % 2);
                bhvMap.put("device_model", "device_model" + j % 2);
                bhvMap.put("app_version", "app_version" + j % 10);
                bhvMap.put("net_type", "net_type");
                bhvMap.put("longitude", "longitude");
                bhvMap.put("latitude", "latitude");
                bhvMap.put("ip", "ip");
                bhvMap.put("login", "login");
                bhvMap.put("report_src", "report_src");
                bhvMap.put("scene_id", scene[j % 2]);
                bhvMap.put("user_id", "user_id" + i);
                bhvMap.put("item_id", "item_id" + j);
                bhvMap.put("item_type", type[(i + j) % 7]);
                bhvMap.put("module_id", "module_id");
                bhvMap.put("page_id", "page_id");
                bhvMap.put("position", "position");
                if (j % 10 == 0) {
                    bhvMap.put("bhv_type", "click");
                } else {
                    bhvMap.put("bhv_type", "expose");
                }
                bhvMap.put("bhv_value", "1");
                bhvMap.put("bhv_time", System.currentTimeMillis() / 1000 + j);
                pushMap.put("fields", bhvMap);
                pushMap.put("cmd", "add");
                recordList.add(pushMap);
            }
            pushList.add(recordList);
        }
        return pushList;
    }

    /**
     * 模拟实时用户行为触发，推送特定用户的实时行为数据（如点击、不喜欢）到behavior表，用于测试实时推荐反馈。
     */
    @Override
    public void pushRealTimeRecommendBhv() {
        DefaultAcsClient client = AIClient.getAcsClient();

        PushDocumentRequest request = new PushDocumentRequest();
        request.setAcceptFormat(FormatType.JSON);
        //填入实例id
        request.setInstanceId(AIClient.getInstanceId());
        //填入要上报的数据表名：user/item/behavior
        request.setTableName("behavior");
        List<List<Map<String, Object>>> pushList = buildRealTimeRecommendBhv("user_id1");
        for (List<Map<String, Object>> list : pushList) {
            String content = JSON.toJSONString(list);
            request.setHttpContent(content.getBytes(), "UTF-8", FormatType.JSON);
            try {
                PushDocumentResponse response = client.getAcsResponse(request);
                System.out.println(response.getResult());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static List<List<Map<String, Object>>> buildRealTimeRecommendBhv(String userId) {
        String[] scene = new String[]{"scene_id1", "scene_id2"};
        String[] type = new String[]{"image", "item", "recipe", "shortvideo", "video", "audio", "article"};
        List<List<Map<String, Object>>> pushList = Lists.newArrayList();
        List<Map<String, Object>> recordList = Lists.newArrayList();
        for (int i = 0; i < 100; i++) {
            Map<String, Object> pushMap = Maps.newHashMap();
            Map<String, Object> bhvMap = Maps.newHashMap();
            bhvMap.put("trace_id", "selfhold");
            bhvMap.put("trace_info", "");
            bhvMap.put("platform", "platform");
            bhvMap.put("device_model", "device_model");
            bhvMap.put("app_version", "app_version");
            bhvMap.put("net_type", "net_type");
            bhvMap.put("longitude", "longitude");
            bhvMap.put("latitude", "latitude");
            bhvMap.put("ip", "ip");
            bhvMap.put("scene_id", "scene_id1");
            bhvMap.put("login", "login");
            bhvMap.put("report_src", "report_src");
            bhvMap.put("user_id", userId);
            bhvMap.put("item_id", "item_id" + i);
            bhvMap.put("item_type", "image");
            bhvMap.put("module_id", "module_id");
            bhvMap.put("page_id", "page_id");
            bhvMap.put("position", "position");
            if(i%10==0){
                bhvMap.put("bhv_type", "dislike");
                bhvMap.put("bhv_value", "dislike_class:cagetory_path=12_4_5");
            }if(i%7==0){
                bhvMap.put("bhv_type", "dislike");
                bhvMap.put("bhv_value", "dislike_related");
            }else {
                bhvMap.put("bhv_type", "click");
                bhvMap.put("bhv_value", "1");
            }
            bhvMap.put("bhv_time", System.currentTimeMillis() / 1000);
            pushMap.put("fields", bhvMap);
            pushMap.put("cmd", "add");
            recordList.add(pushMap);
        }
        pushList.add(recordList);
        return pushList;
    }

    /**
     * 推送特定用户的实时更新数据到user表，用于模拟用户属性变化触发的实时推荐调整。
     */
    @Override
    public void pushRealTimeOneUser(){
        DefaultAcsClient client = AIClient.getAcsClient();


        PushDocumentRequest request = new PushDocumentRequest();
        request.setAcceptFormat(FormatType.JSON);
        //填入实例id
        request.setInstanceId(AIClient.getInstanceId());
        //填入要上报的数据表名：user/item/behavior
        request.setTableName("user");

        String content = JSON.toJSONString(buildOneUser("user_id1"));
        request.setHttpContent(content.getBytes(), "UTF-8", FormatType.JSON);

        try {
            PushDocumentResponse response = client.getAcsResponse(request);
            System.out.println(response.getResult());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static  List<Map<String,Object>> buildRealTimeOneUser(String userId){
        List<Map<String,Object>> pushList = Lists.newArrayList();
        Map<String,Object> pushMap = Maps.newHashMap();
        Map<String,Object> userMap = Maps.newHashMap();

        userMap.put("user_id", userId);
        userMap.put("user_id_type", "user_id_type1");
        userMap.put("third_user_name", "third_user_name");
        userMap.put("third_user_type", "third_user_type");
        userMap.put("phone_md5", "phone_md5");
        userMap.put("imei", "");
        userMap.put("content", "content");
        userMap.put("gender", "gender_trigger");
        userMap.put("age", "1");
        userMap.put("age_group", "age_trigger");
        userMap.put("country", "country");
        userMap.put("city", "city_trigger");
        userMap.put("channel", "channel1");
        userMap.put("ip", "ip");
        userMap.put("device_model", "device_model");
        userMap.put("register_time", System.currentTimeMillis()/1000);
        userMap.put("last_login_time", System.currentTimeMillis()/1000);
        userMap.put("last_modify_time", System.currentTimeMillis()/1000);
        userMap.put("tags", "tag_trigger");
        userMap.put("source", "source");
        userMap.put("features", "features");
        userMap.put("num_features", "num_features");
        pushMap.put("fields",userMap);
        pushMap.put("cmd","add");
        pushList.add(pushMap);
        return pushList;
    }

}
