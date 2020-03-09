package com.uuabc.classroomlib.db;

import com.uuabc.classroomlib.RoomApplication;
import com.uuabc.classroomlib.model.db.LottieRecord;
import com.uuabc.classroomlib.model.db.LottieRecordDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

public class RoomDbManager {
    private static RoomDbManager dbManager;

    public static RoomDbManager getInstance() {
        if (dbManager == null) {
            synchronized (RoomDbManager.class) {
                if (dbManager == null) {
                    dbManager = new RoomDbManager();
                }
            }
        }
        return dbManager;
    }

    public synchronized void saveLottieRecords(List<LottieRecord> lottieRecords) {
        try {
            LottieRecordDao recordDao = RoomApplication.getInstance().getDaoSession().getLottieRecordDao();
            if (recordDao == null) return;
            recordDao.insertOrReplaceInTx(lottieRecords);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void saveLottieRecord(LottieRecord lottieRecord) {
        try {
            LottieRecordDao recordDao = RoomApplication.getInstance().getDaoSession().getLottieRecordDao();
            if (recordDao == null) return;
            recordDao.insertOrReplace(lottieRecord);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void deleteAllLottieRecords() {
        try {
            LottieRecordDao recordDao = RoomApplication.getInstance().getDaoSession().getLottieRecordDao();
            if (recordDao == null) return;
            recordDao.deleteAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void deleteLottieRecord(LottieRecord lottieRecord) {
        try {
            LottieRecordDao recordDao = RoomApplication.getInstance().getDaoSession().getLottieRecordDao();
            if (recordDao == null) return;
            recordDao.delete(lottieRecord);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized List<LottieRecord> queryAllLottieRecords() {
        try {
            LottieRecordDao recordDao = RoomApplication.getInstance().getDaoSession().getLottieRecordDao();
            if (recordDao == null) return null;
            QueryBuilder<LottieRecord> qb = recordDao.queryBuilder();
            return qb.list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public synchronized LottieRecord queryLottieRecord(String code) {
        try {
            LottieRecordDao recordDao = RoomApplication.getInstance().getDaoSession().getLottieRecordDao();
            if (recordDao == null) return null;
            QueryBuilder<LottieRecord> qb = recordDao.queryBuilder();
            qb.where(LottieRecordDao.Properties.Code.eq(code));
            return qb.list() == null || qb.list().size() == 0 ? null : qb.list().get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public synchronized List<LottieRecord> queryLottieRecord(int updateTime) {
        try {
            LottieRecordDao recordDao = RoomApplication.getInstance().getDaoSession().getLottieRecordDao();
            if (recordDao == null) return null;
            QueryBuilder<LottieRecord> qb = recordDao.queryBuilder();
            qb.where(LottieRecordDao.Properties.Reserve5.eq(updateTime));
            return qb.list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
