package com.uuabc.classroomlib.model.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Unique;

@Entity
public class LottieRecord {
    @Id(autoincrement = true)
    @Unique
    private Long mainKey;       //主键自增长，不可重复,作为不同记录对象的标识，传入参数对象时不要传入
    @Property(nameInDb = "id")
    private String id;
    @Property(nameInDb = "code")
    private String code;
    @Property(nameInDb = "name")
    private String name;
    @Property(nameInDb = "groupName")
    private String groupName;
    @Property(nameInDb = "lottieJsonPath")
    private String lottieJsonPath;
    @Property(nameInDb = "smallImgPath")
    private String smallImgPath;
    @Property(nameInDb = "reserve1")
    private String reserve1;
    @Property(nameInDb = "reserve2")
    private String reserve2;
    @Property(nameInDb = "reserve3")
    private String reserve3;
    @Property(nameInDb = "reserve4")
    private boolean reserve4;
    @Property(nameInDb = "reserve5")
    private int reserve5;
    @Generated(hash = 1870829743)
    public LottieRecord(Long mainKey, String id, String code, String name,
            String groupName, String lottieJsonPath, String smallImgPath,
            String reserve1, String reserve2, String reserve3, boolean reserve4,
            int reserve5) {
        this.mainKey = mainKey;
        this.id = id;
        this.code = code;
        this.name = name;
        this.groupName = groupName;
        this.lottieJsonPath = lottieJsonPath;
        this.smallImgPath = smallImgPath;
        this.reserve1 = reserve1;
        this.reserve2 = reserve2;
        this.reserve3 = reserve3;
        this.reserve4 = reserve4;
        this.reserve5 = reserve5;
    }
    @Generated(hash = 1694589272)
    public LottieRecord() {
    }
    public Long getMainKey() {
        return this.mainKey;
    }
    public void setMainKey(Long mainKey) {
        this.mainKey = mainKey;
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getCode() {
        return this.code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getGroupName() {
        return this.groupName;
    }
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    public String getLottieJsonPath() {
        return this.lottieJsonPath;
    }
    public void setLottieJsonPath(String lottieJsonPath) {
        this.lottieJsonPath = lottieJsonPath;
    }
    public String getSmallImgPath() {
        return this.smallImgPath;
    }
    public void setSmallImgPath(String smallImgPath) {
        this.smallImgPath = smallImgPath;
    }
    public String getReserve1() {
        return this.reserve1;
    }
    public void setReserve1(String reserve1) {
        this.reserve1 = reserve1;
    }
    public String getReserve2() {
        return this.reserve2;
    }
    public void setReserve2(String reserve2) {
        this.reserve2 = reserve2;
    }
    public String getReserve3() {
        return this.reserve3;
    }
    public void setReserve3(String reserve3) {
        this.reserve3 = reserve3;
    }
    public boolean getReserve4() {
        return this.reserve4;
    }
    public void setReserve4(boolean reserve4) {
        this.reserve4 = reserve4;
    }
    public int getReserve5() {
        return this.reserve5;
    }
    public void setReserve5(int reserve5) {
        this.reserve5 = reserve5;
    }


}
