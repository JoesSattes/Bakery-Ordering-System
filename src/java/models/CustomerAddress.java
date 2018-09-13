package models;

public class CustomerAddress {
    private String address;
    private String district;
    private String province;
    private String postcode;

    public CustomerAddress() {
    }

    public CustomerAddress(String address, String district, String province, String postcode) {
        this.address = address;
        this.district = district;
        this.province = province;
        this.postcode = postcode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }
    
}
