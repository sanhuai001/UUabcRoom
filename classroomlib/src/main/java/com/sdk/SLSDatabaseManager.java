package com.sdk;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.uuabc.classroomlib.model.db.DaoMaster;
import com.uuabc.classroomlib.model.db.DaoSession;
import com.uuabc.classroomlib.model.db.LogEntity;
import com.uuabc.classroomlib.model.db.LogEntityDao;

import org.greenrobot.greendao.DbUtils;
import org.greenrobot.greendao.query.DeleteQuery;
import org.greenrobot.greendao.query.Query;

import java.util.ArrayList;
import java.util.List;

public class SLSDatabaseManager {
    private static volatile SLSDatabaseManager sInstance;
    private DaoSession daoSession;

    //private constructor.
    private SLSDatabaseManager() {
        //Prevent form the reflection api.
        if (sInstance != null) {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
    }

    public static SLSDatabaseManager getInstance() {
        //Double check locking pattern
        if (sInstance == null) { //Check for the first time
            synchronized (SLSDatabaseManager.class) {   //Check for the second time.
                //if there is no instance available... create new one
                if (sInstance == null) {
                    sInstance = new SLSDatabaseManager();
                }
            }
        }

        return sInstance;
    }

    public void setupDB(Context context) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, Constants.DB_NAME);
        //获取可写数据库
        SQLiteDatabase db = helper.getWritableDatabase();

        //限制数据库最大能存储30M的数据
        db.setMaximumSize(1024 * 1024 * 30);
        Log.i("MyApplication", "pageSize: " + db.getPageSize() + " MaximumSize: " + db.getMaximumSize());

        //获取数据库对象
        DaoMaster daoMaster = new DaoMaster(db);

        //获取Dao对象管理者
        daoSession = daoMaster.newSession();
    }

    public void insertRecordIntoDB(LogEntity entity) {
        try {
            daoSession.getLogEntityDao().insert(entity);
        } catch (SQLiteException e) {
            deleteTwoThousandRecords();
        }
    }

    public void deleteRecordFromDB(LogEntity entity) {
        daoSession.getLogEntityDao().delete(entity);
    }

    public List<LogEntity> queryRecordFromDB() {
        Query query = daoSession.getLogEntityDao().queryBuilder().where(LogEntityDao.Properties.Timestamp.le(System.currentTimeMillis())).orderAsc(LogEntityDao.Properties.Timestamp).limit(30).build();
        return query.list();
    }

    public List<LogEntity> queryRecordFromDB2() {
        Query query = daoSession.getLogEntityDao().queryBuilder().where(LogEntityDao.Properties.Timestamp.le(System.currentTimeMillis())).orderDesc(LogEntityDao.Properties.Timestamp).build();
        return query.list();
    }

    public void deleteTwoThousandRecords() {
        Query tableSelectQuery = daoSession.getLogEntityDao().queryBuilder().where(LogEntityDao.Properties.Timestamp.le(System.currentTimeMillis())).orderAsc(LogEntityDao.Properties.Timestamp).limit(2000).build();
        List<LogEntity> records = tableSelectQuery.list();
        List ids = new ArrayList();
        for (LogEntity log : records) {
            ids.add(log.getId());
        }

        DeleteQuery tableDeleteQuery = daoSession.getLogEntityDao().queryBuilder().where(LogEntityDao.Properties.Id.in(ids)).buildDelete();
        tableDeleteQuery.executeDeleteWithoutDetachingEntities();
        daoSession.clear();
        DbUtils.vacuum(daoSession.getDatabase());
    }

    public void deleteTwoDaysRecords() {
        Log.i("aliyunLog", "--开始检测删除超过48小时日志--");
        long millTimes = System.currentTimeMillis() - 1000 * 60 * 60 * 48;
        Query tableSelectQuery = daoSession.getLogEntityDao().queryBuilder().where(LogEntityDao.Properties.Timestamp.le(millTimes)).orderAsc(LogEntityDao.Properties.Timestamp).build();
        List<LogEntity> records = tableSelectQuery.list();
        if (records.size() == 0) {
            Log.i("aliyunLog", "没有超过48小时日志");
            return;
        }
        List ids = new ArrayList();
        for (LogEntity log : records) {
            ids.add(log.getId());
        }
        Log.i("aliyunLog", "删除超过48小时日志条数:" + ids.size());
        DeleteQuery tableDeleteQuery = daoSession.getLogEntityDao().queryBuilder().where(LogEntityDao.Properties.Id.in(ids)).buildDelete();
        tableDeleteQuery.executeDeleteWithoutDetachingEntities();
        daoSession.clear();
        DbUtils.vacuum(daoSession.getDatabase());
    }

    /**
     * 删除超过3000条部分日志
     */
    public void deleteSurpassRecords() {
        Log.i("aliyunLog", "--开始检测删除超过3000条的日志--");
        Query tableSelectQuery = daoSession.getLogEntityDao().queryBuilder().where(LogEntityDao.Properties.Timestamp.le(System.currentTimeMillis())).orderDesc(LogEntityDao.Properties.Timestamp).build();
        List<LogEntity> records = tableSelectQuery.list();
        Log.i("aliyunLog", "当前日志条数为:" + records.size());
        if (records.size() <= 3000) {
            return;
        }
        List ids = new ArrayList();
        for (int i = 3000; i < records.size(); i++) {
            ids.add(records.get(i).getId());
        }
        Log.i("aliyunLog", "删除日志条数:" + ids.size());
        DeleteQuery tableDeleteQuery = daoSession.getLogEntityDao().queryBuilder().where(LogEntityDao.Properties.Id.in(ids)).buildDelete();
        tableDeleteQuery.executeDeleteWithoutDetachingEntities();
        daoSession.clear();
        DbUtils.vacuum(daoSession.getDatabase());
    }
}
