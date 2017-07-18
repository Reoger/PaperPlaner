package hut.reoger.gen;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import reoger.hut.paperplaner.bean.DbZhihu;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "DB_ZHIHU".
*/
public class DbZhihuDao extends AbstractDao<DbZhihu, Long> {

    public static final String TABLENAME = "DB_ZHIHU";

    /**
     * Properties of entity DbZhihu.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, long.class, "id", true, "_id");
        public final static Property Zhihu_news = new Property(1, String.class, "zhihu_news", false, "ZHIHU_NEWS");
        public final static Property Zhihu_content = new Property(2, String.class, "zhihu_content", false, "ZHIHU_CONTENT");
        public final static Property Date = new Property(3, java.util.Date.class, "date", false, "DATE");
        public final static Property IsBookMark = new Property(4, boolean.class, "isBookMark", false, "IS_BOOK_MARK");
    }


    public DbZhihuDao(DaoConfig config) {
        super(config);
    }
    
    public DbZhihuDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"DB_ZHIHU\" (" + //
                "\"_id\" INTEGER PRIMARY KEY NOT NULL ," + // 0: id
                "\"ZHIHU_NEWS\" TEXT," + // 1: zhihu_news
                "\"ZHIHU_CONTENT\" TEXT," + // 2: zhihu_content
                "\"DATE\" INTEGER," + // 3: date
                "\"IS_BOOK_MARK\" INTEGER NOT NULL );"); // 4: isBookMark
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"DB_ZHIHU\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, DbZhihu entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getId());
 
        String zhihu_news = entity.getZhihu_news();
        if (zhihu_news != null) {
            stmt.bindString(2, zhihu_news);
        }
 
        String zhihu_content = entity.getZhihu_content();
        if (zhihu_content != null) {
            stmt.bindString(3, zhihu_content);
        }
 
        java.util.Date date = entity.getDate();
        if (date != null) {
            stmt.bindLong(4, date.getTime());
        }
        stmt.bindLong(5, entity.getIsBookMark() ? 1L: 0L);
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, DbZhihu entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getId());
 
        String zhihu_news = entity.getZhihu_news();
        if (zhihu_news != null) {
            stmt.bindString(2, zhihu_news);
        }
 
        String zhihu_content = entity.getZhihu_content();
        if (zhihu_content != null) {
            stmt.bindString(3, zhihu_content);
        }
 
        java.util.Date date = entity.getDate();
        if (date != null) {
            stmt.bindLong(4, date.getTime());
        }
        stmt.bindLong(5, entity.getIsBookMark() ? 1L: 0L);
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.getLong(offset + 0);
    }    

    @Override
    public DbZhihu readEntity(Cursor cursor, int offset) {
        DbZhihu entity = new DbZhihu( //
            cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // zhihu_news
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // zhihu_content
            cursor.isNull(offset + 3) ? null : new java.util.Date(cursor.getLong(offset + 3)), // date
            cursor.getShort(offset + 4) != 0 // isBookMark
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, DbZhihu entity, int offset) {
        entity.setId(cursor.getLong(offset + 0));
        entity.setZhihu_news(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setZhihu_content(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setDate(cursor.isNull(offset + 3) ? null : new java.util.Date(cursor.getLong(offset + 3)));
        entity.setIsBookMark(cursor.getShort(offset + 4) != 0);
     }
    
    @Override
    protected final Long updateKeyAfterInsert(DbZhihu entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(DbZhihu entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(DbZhihu entity) {
        throw new UnsupportedOperationException("Unsupported for entities with a non-null key");
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}