/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package temp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Sattaya Singkul
 */

//Support add more username
public class StoragePath {

    private static Map<String, ArrayList<String>> pathAll;
    private static String username;
    private static ArrayList<String> list;
    private static String bannerPath;
    private static String profilePath;
    private static Map<String, ArrayList<String>> storePath;
    private static ArrayList<String> storeList;
    private static Map<String, ArrayList<String>> productDetail;
    private static ArrayList<String> productList;

    public StoragePath() {
        pathAll = new HashMap<String, ArrayList<String>>();
        storePath = new HashMap<String, ArrayList<String>>();
    }

    public static void setPath(String path) {
        if (pathAll.containsKey(username)) {
            list = pathAll.get(username);
            list.add(path);
        } else {
            list = new ArrayList<String>();
            list.add(path);
            pathAll.put(username, list);
        }
    }

    public static void clearAllPath() {
        pathAll.clear();
        list.clear();
    }

    public static Map<String, ArrayList<String>> getAllPath() {
        return pathAll;
    }
    
    public static ArrayList<String> getPath(String username){
        return pathAll.get(username);
    }
    
    public static String getPathIndex(String username, int index){
        return pathAll.get(username).get(index);
    }
    
    public void setAllPath(Map<String, ArrayList<String>> pathAll){
        this.pathAll = pathAll;
    }

    /**
     * @return the username
     */
    public static String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public static void setUsername(String username) {
        StoragePath.username = username;
    }
    
    public static void setBanner(String path){
        StoragePath.bannerPath = path;
    }
    
    public static String getBanner(){
        return StoragePath.bannerPath;
    }
    
    public static void setProfile(String path){
        StoragePath.profilePath = path;
    }
    
    public static String getProfile(){
        return StoragePath.profilePath;
    }
    
    public static void setStorePath(String path) {
        if (storePath.containsKey(username)) {
            storeList = storePath.get(username);
            storeList.add(path);
        } else {
            storeList = new ArrayList<String>();
            storeList.add(path);
            storePath.put(username, storeList);
        }
    }

    public static void clearAllStorePath() {
        storePath.clear();
        storeList.clear();
    }

    public static Map<String, ArrayList<String>> getAllStorePath() {
        return storePath;
    }
    
    public static ArrayList<String> getStorePath(String username){
        return storePath.get(username);
    }
    
    public static String getStorePathIndex(String username, int index){
        return storePath.get(username).get(index);
    }
    
    public void setAllStorePath(Map<String, ArrayList<String>> storePath){
        this.storePath = storePath;
    }
    
        public static void setProductDetail(String detail) {
        if (productDetail.containsKey(username)) {
            productList = productDetail.get(username);
            productList.add(detail);
        } else {
            productList = new ArrayList<String>();
            productList.add(detail);
            productDetail.put(username, productList);
        }
    }

    public static void clearAllProductDetail() {
        productDetail.clear();
        productList.clear();
    }

    public static Map<String, ArrayList<String>> getAllProductDetail() {
        return productDetail;
    }
    
    public static ArrayList<String> getProductDetail(String username){
        return productDetail.get(username);
    }
    
    public static String getProductDetailIndex(String username, int index){
        return productDetail.get(username).get(index);
    }
    
    public void setAllProductDetail(Map<String, ArrayList<String>> productDetail){
        this.productDetail = productDetail;
    }
}
