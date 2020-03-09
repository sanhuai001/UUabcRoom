package com.uuabc.classroomlib.model;


import android.os.Parcel;

/**
 * Created by user on 2018/4/25.
 */

public class UserLoginModel extends RCommonResult {

    /**
     * status : 1
     * userInfo : {"userId":374,"name":"豆豆","englishName":"doudou","phoneNum":18321580780,"userHead":"https://uutest2.uuabc.com/Ft4cZtWiOUHCcICbhsvbegRxUA8s","age":5,"email":"","grade":2,"school":"阿斯顿发","diamondCount":979,"leaveCount":52,"situation":"","level":4,"province":310000,"city":311200,"experience":"0","uCoinCount":2812,"birthday":"2013-12-31","address":"","sex":"男"}
     * token : MPzAcD0_
     */

    private UserInfoModel userInfo;
    private String token;

    protected UserLoginModel(Parcel in) {
        super(in);
    }

    public UserInfoModel getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfoModel userInfo) {
        this.userInfo = userInfo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public static class UserInfoModel {
        /**
         * userId : 374
         * name : 豆豆
         * englishName : doudou
         * phoneNum : 18321580780
         * userHead : https://uutest2.uuabc.com/Ft4cZtWiOUHCcICbhsvbegRxUA8s
         * age : 5
         * email :
         * grade : 2
         * school : 阿斯顿发
         * diamondCount : 979
         * leaveCount : 52
         * situation :
         * level : 4
         * province : 310000
         * city : 311200
         * experience : 0
         * uCoinCount : 2812
         * birthday : 2013-12-31
         * address :
         * sex : 男
         */

        private int userId;
        private String name;
        private String englishName;
        private long phoneNum;
        private String userHead;
        private int age;
        private String email;
        private int grade;
        private String school;
        private int diamondCount;
        private int leaveCount;
        private String situation;
        private int level;
        private int province;
        private int city;
        private String experience;
        private int uCoinCount;
        private String birthday;
        private String address;
        private String sex;

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEnglishName() {
            return englishName;
        }

        public void setEnglishName(String englishName) {
            this.englishName = englishName;
        }

        public long getPhoneNum() {
            return phoneNum;
        }

        public void setPhoneNum(long phoneNum) {
            this.phoneNum = phoneNum;
        }

        public String getUserHead() {
            return userHead;
        }

        public void setUserHead(String userHead) {
            this.userHead = userHead;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public int getGrade() {
            return grade;
        }

        public void setGrade(int grade) {
            this.grade = grade;
        }

        public String getSchool() {
            return school;
        }

        public void setSchool(String school) {
            this.school = school;
        }

        public int getDiamondCount() {
            return diamondCount;
        }

        public void setDiamondCount(int diamondCount) {
            this.diamondCount = diamondCount;
        }

        public int getLeaveCount() {
            return leaveCount;
        }

        public void setLeaveCount(int leaveCount) {
            this.leaveCount = leaveCount;
        }

        public String getSituation() {
            return situation;
        }

        public void setSituation(String situation) {
            this.situation = situation;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public int getProvince() {
            return province;
        }

        public void setProvince(int province) {
            this.province = province;
        }

        public int getCity() {
            return city;
        }

        public void setCity(int city) {
            this.city = city;
        }

        public String getExperience() {
            return experience;
        }

        public void setExperience(String experience) {
            this.experience = experience;
        }

        public int getUCoinCount() {
            return uCoinCount;
        }

        public void setUCoinCount(int uCoinCount) {
            this.uCoinCount = uCoinCount;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }
    }
}
