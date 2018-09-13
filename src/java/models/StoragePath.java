/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StoragePath {

    private Map<String, ArrayList<String>> pathAll;
    private String username;
    private ArrayList<String> list;
    private String bannerPath;
    private String profilePath;
    private Map<String, ArrayList<String>> storePath;
    private ArrayList<String> storeList;
    private Map<String, ArrayList<String>> productDetail;
    private ArrayList<String> productList;

    public StoragePath() {
        pathAll = new HashMap<String, ArrayList<String>>();
        storePath = new HashMap<String, ArrayList<String>>();
        productDetail = new HashMap<String, ArrayList<String>>();
    }

    public void setPath(String path) {
        if (pathAll.containsKey(username)) {
            list = pathAll.get(username);
            list.add(path);
        } else {
            list = new ArrayList<String>();
            list.add(path);
            pathAll.put(username, list);
        }
    }

    public void clearAllPath() {
        pathAll.clear();
        list.clear();
    }

    public Map<String, ArrayList<String>> getAllPath() {
        return pathAll;
    }

    public ArrayList<String> getPath(String username) {
        return pathAll.get(username);
    }

    public String getPathIndex(String username, int index) {
        return pathAll.get(username).get(index);
    }

    public void setAllPath(Map<String, ArrayList<String>> pathAll) {
        this.pathAll = pathAll;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    public void setBanner(String path) {
        this.bannerPath = path;
    }

    public String getBanner() {
        return bannerPath;
    }

    public void setProfile(String path) {
        this.profilePath = path;
    }

    public String getProfile() {
        return profilePath;
    }

    public void setStorePath(String path) {
        if (storePath.containsKey(username)) {
            storeList = storePath.get(username);
            storeList.add(path);
        } else {
            storeList = new ArrayList<String>();
            storeList.add(path);
            storePath.put(username, storeList);
        }
    }

    public void clearAllStorePath() {
        storePath.clear();
        storeList.clear();
    }

    public Map<String, ArrayList<String>> getAllStorePath() {
        return storePath;
    }

    public ArrayList<String> getStorePath(String username) {
        return storePath.get(username);
    }

    public String getStorePathIndex(String username, int index) {
        return storePath.get(username).get(index);
    }

    public void setAllStorePath(Map<String, ArrayList<String>> storePath) {
        this.storePath = storePath;
    }

    public void setProductDetail(String detail) {
        System.out.println("SET PRODUCT DETAIL : "+username);
        System.out.println("SET DETAIL : "+detail);
        if (productDetail.containsKey(username)) {
            productList = productDetail.get(username);
            productList.add(detail);
        } else {
            productList = new ArrayList<String>();
            productList.add(detail);
            productDetail.put(username, productList);
        }
        System.out.println("FINISH ADD");
    }

    public void clearAllProductDetail() {
        productDetail.clear();
        productList.clear();
    }

    public Map<String, ArrayList<String>> getAllProductDetail() {
        return productDetail;
    }

    public ArrayList<String> getProductDetail(String username) {
        return productDetail.get(username);
    }

    public String getProductDetailIndex(String username, int index) {
        return productDetail.get(username).get(index);
    }

    public void setAllProductDetail(Map<String, ArrayList<String>> productDetail) {
        this.productDetail = productDetail;
    }
}
