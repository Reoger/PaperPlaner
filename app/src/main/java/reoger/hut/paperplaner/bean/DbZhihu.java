package reoger.hut.paperplaner.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;

/**
 * Created by 24540 on 2017/5/11.
 * 知乎日报的数据库存储对象
 */

@Entity
public class DbZhihu {

    @Id
    private  long id;

    private String zhihu_news;

    private String zhihu_content;

    private Date date;

    private boolean isBookMark;

    @Generated(hash = 830648147)
    public DbZhihu(long id, String zhihu_news, String zhihu_content, Date date,
            boolean isBookMark) {
        this.id = id;
        this.zhihu_news = zhihu_news;
        this.zhihu_content = zhihu_content;
        this.date = date;
        this.isBookMark = isBookMark;
    }

    @Generated(hash = 1285108570)
    public DbZhihu() {
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getZhihu_news() {
        return this.zhihu_news;
    }

    public void setZhihu_news(String zhihu_news) {
        this.zhihu_news = zhihu_news;
    }

    public String getZhihu_content() {
        return this.zhihu_content;
    }

    public void setZhihu_content(String zhihu_content) {
        this.zhihu_content = zhihu_content;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean getIsBookMark() {
        return this.isBookMark;
    }

    public void setIsBookMark(boolean isBookMark) {
        this.isBookMark = isBookMark;
    }



}
