package com.sustech.ooad.Utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Authored By 西凉的悲伤
 * https://blog.csdn.net/qq_33697094/article/details/111663292
 */
public class WorldAddressUtils {

    /**
     * 用于读取解析的地址路径
     */
    private final String addressFilePath = "TXTResources/worldAddress.txt";
    /**
     * 是否过滤A、B、C...之类的国家分割符号，true过滤，false不过滤
     */
    private final boolean fillterSingleChar = true;

    /**
     * 存放所有国家、省会、城市
     */
    private Map<String, Map<String, List<String>>> addressInfoMap = new HashMap<>();
    /**
     * 存放所有英文和中文，<英文，中文>
     */
    private Map<String, String> englishCnMap = new HashMap<>();
    private Map<String, String> cnEnglishMap = new HashMap<>();

    public Map<String, String> getCnEnglishMap() {
        return cnEnglishMap;
    }

    /**
     * 存放所有国家
     */
    private List<String> countrys = new ArrayList<>();
    /**
     * 存放所有省会
     */
    private List<String> province = new ArrayList<>();
    /**
     * 存放所有城市和省会<城市，省会>
     */
    private Map<String, String> citysToProvince = new HashMap<>();
    /**
     * 存放所有城市和省会<省会，国家>
     */
    private Map<String, String> provinceToCountrys = new HashMap<>();

    public WorldAddressUtils() {
        try (
                //读取文件为字节流
                FileInputStream file = new FileInputStream(addressFilePath);
                //字节流转化为字符流，以UTF-8读取防止中文乱码
                InputStreamReader in = new InputStreamReader(file, "UTF-8");
                //加入到缓存
                BufferedReader buf = new BufferedReader(in);
        ) {
            String str = "";
            //按行读取，到达最后一行返回null
            while ((str = buf.readLine()) != null) {
                String[] engCn = str.split("#");
                //enName是英文的地名
                String enName = engCn[0].trim();
                //enName是中文的地名
                String cnName = engCn[1].trim();
                englishCnMap.put(engCn[0].trim().toLowerCase(), cnName);
                cnEnglishMap.put(cnName, enName);
                char[] chars = engCn[0].toCharArray();
                //每一行前的空格数，0是国家，2是省会，4是城市
                int spaceCount = 0;
                for (int i = 0; i < chars.length; i++) {
                    if (chars[i] != ' ') {
                        break;
                    } else if (chars[i] == ' ') {
                        spaceCount += 1;
                    }
                }

                if (enName.length() == 1 && fillterSingleChar) {
                    continue;
                }

                if (0 == spaceCount) {
                    countrys.add(cnName);
                    //存放一个新的国家
                    addressInfoMap.put(cnName, new HashMap<>());
                }

                //从countrys获取最后一个国家的所有省会
                Map<String, List<String>> provinceMap = addressInfoMap.get(countrys.get(countrys.size() - 1));
                if (2 == spaceCount) {
                    province.add(cnName);
                    //存放一个新的省会
                    provinceMap.put(cnName, new ArrayList<>());
                    provinceToCountrys.put(cnName, countrys.get(countrys.size() - 1));
                }

                //存放一个新的城市
                if (spaceCount == 4) {
                    String provinceStr = province.get(province.size() - 1);
                    List<String> cityList = provinceMap.get(provinceStr);
                    cityList.add(cnName);
                    citysToProvince.put(cnName, provinceStr);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取所有的国家名称
     */
    public JSONObject getAllCountrys() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("countrys", JSONArray.parseArray(JSON.toJSONString(countrys)));
        return jsonObject;
    }


    /**
     * 获取所有的国家名称和对应的省会
     */
    public JSONObject getAllCountrysProvince() {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (Map.Entry<String, Map<String, List<String>>> entryMap : addressInfoMap.entrySet()) {
            JSONObject countrysProvince = new JSONObject();
            JSONArray provinceArray = new JSONArray();
            for (Map.Entry<String, List<String>> map : entryMap.getValue().entrySet()) {
                provinceArray.add(map.getKey());
            }
            countrysProvince.put("country", entryMap.getKey());//国家
            countrysProvince.put("province", provinceArray);//省会
            jsonArray.add(countrysProvince);
            jsonObject.put("countrysProvince", jsonArray);
        }
        return jsonObject;
    }


    /**
     * 获取所有的国家名称和对应的省会和城市
     */
    public JSONObject getAllCountrysProvinceCity() {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (Map.Entry<String, Map<String, List<String>>> entryMap : addressInfoMap.entrySet()) {
            JSONObject countrysProvince = new JSONObject();
            JSONArray provinceArray = new JSONArray();
            for (Map.Entry<String, List<String>> map : entryMap.getValue().entrySet()) {
                List<String> cityList = map.getValue();//城市
                JSONArray cityArray = JSONArray.parseArray(JSON.toJSONString(cityList));
                JSONObject provinceCity = new JSONObject();//存城市和省会
                provinceCity.put("province", map.getKey());
                provinceCity.put("city", cityArray);
                provinceArray.add(provinceCity);
            }
            countrysProvince.put("country", entryMap.getKey());//国家
            countrysProvince.put("provinceCity", provinceArray);//省会、城市
            jsonArray.add(countrysProvince);
            jsonObject.put("countrysProvince", jsonArray);
        }
        return jsonObject;
    }


    /**
     * 获取指定国家下的省会、城市
     */
    public JSONObject getProvinceFromCountry(String country) {
        if (englishCnMap.containsKey(country.toLowerCase())) {
            //如果输入的是英文转换成中文
            country = englishCnMap.get(country.toLowerCase());
        }
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (Map.Entry<String, Map<String, List<String>>> entryMap : addressInfoMap.entrySet()) {
            if (country.equals(entryMap.getKey())) {
                JSONObject countrysProvince = new JSONObject();
                JSONArray provinceArray = new JSONArray();
                for (Map.Entry<String, List<String>> map : entryMap.getValue().entrySet()) {
                    List<String> cityList = map.getValue();//城市
                    JSONArray cityArray = JSONArray.parseArray(JSON.toJSONString(cityList));
                    JSONObject provinceCity = new JSONObject();//存城市和省会
                    provinceCity.put("province", map.getKey());
                    provinceCity.put("city", cityArray);
                    provinceArray.add(provinceCity);
                }
                countrysProvince.put("country", entryMap.getKey());//国家
                countrysProvince.put("provinceCity", provinceArray);//省会、城市
                jsonArray.add(countrysProvince);
                jsonObject.put("countrysProvince", jsonArray);
            }
        }
        return jsonObject;
    }


    /**
     * 获取指定省会下的城市
     */
    public JSONObject getCityFromProvince(String province) {
        if (englishCnMap.containsKey(province.toLowerCase())) {
            //如果输入的是英文转换成中文
            province = englishCnMap.get(province.toLowerCase());
        }
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (Map.Entry<String, Map<String, List<String>>> entryMap : addressInfoMap.entrySet()) {
            //根据省会获取国家并判断相等
            if (provinceToCountrys.get(province).equals(entryMap.getKey())) {
                JSONObject countrysProvince = new JSONObject();
                JSONArray provinceArray = new JSONArray();
                for (Map.Entry<String, List<String>> map : entryMap.getValue().entrySet()) {
                    if (province.equals(map.getKey())) {
                        List<String> cityList = map.getValue();//城市
                        JSONArray cityArray = JSONArray.parseArray(JSON.toJSONString(cityList));
                        JSONObject provinceCity = new JSONObject();//存城市和省会
                        provinceCity.put("province", map.getKey());
                        provinceCity.put("city", cityArray);
                        provinceArray.add(provinceCity);
                    }
                }
                countrysProvince.put("country", entryMap.getKey());//国家
                countrysProvince.put("provinceCity", provinceArray);//省会、城市
                jsonArray.add(countrysProvince);
                jsonObject.put("countrysProvince", jsonArray);
            }
        }
        return jsonObject;
    }


    /**
     * 根据城市获取所属省会的城市和国家
     */
    public JSONObject getCityFromCountry(String city) {
        if (englishCnMap.containsKey(city.toLowerCase())) {
            //如果输入的是英文转换成中文
            city = englishCnMap.get(city.toLowerCase());
        }
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (Map.Entry<String, Map<String, List<String>>> entryMap : addressInfoMap.entrySet()) {
            String getProvince = citysToProvince.get(city);
            if (provinceToCountrys.get(getProvince).equals(entryMap.getKey())) {//根据省会获取国家过滤
                JSONObject countrysProvince = new JSONObject();
                JSONArray provinceArray = new JSONArray();
                for (Map.Entry<String, List<String>> map : entryMap.getValue().entrySet()) {
                    if (getProvince.equals(map.getKey())) {
                        List<String> cityList = map.getValue();//城市
                        JSONArray cityArray = JSONArray.parseArray(JSON.toJSONString(cityList));
                        JSONObject provinceCity = new JSONObject();//存城市和省会
                        provinceCity.put("province", map.getKey());
                        provinceCity.put("city", cityArray);
                        provinceArray.add(provinceCity);
                    }
                }
                countrysProvince.put("country", entryMap.getKey());//国家
                countrysProvince.put("provinceCity", provinceArray);//省会、城市
                jsonArray.add(countrysProvince);
                jsonObject.put("countrysProvince", jsonArray);
            }
        }
        return jsonObject;
    }
}
