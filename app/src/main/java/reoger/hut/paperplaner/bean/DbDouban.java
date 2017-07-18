package reoger.hut.paperplaner.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by 24540 on 2017/5/11.
 *
 */

@Entity
public class DbDouban {

    @Id
    private Long douban_id;

    private String douban_news;

    private  float douban_time;

    private String douban_content;

    private boolean isBookMark;

    @Generated(hash = 1646685847)
    public DbDouban(Long douban_id, String douban_news, float douban_time,
            String douban_content, boolean isBookMark) {
        this.douban_id = douban_id;
        this.douban_news = douban_news;
        this.douban_time = douban_time;
        this.douban_content = douban_content;
        this.isBookMark = isBookMark;
    }

    @Generated(hash = 2059416205)
    public DbDouban() {
    }

    public Long getDouban_id() {
        return this.douban_id;
    }

    public void setDouban_id(Long douban_id) {
        this.douban_id = douban_id;
    }

    public String getDouban_news() {
        return this.douban_news;
    }

    public void setDouban_news(String douban_news) {
        this.douban_news = douban_news;
    }

    public float getDouban_time() {
        return this.douban_time;
    }

    public void setDouban_time(float douban_time) {
        this.douban_time = douban_time;
    }

    public String getDouban_content() {
        return this.douban_content;
    }

    public void setDouban_content(String douban_content) {
        this.douban_content = douban_content;
    }

    public boolean getIsBookMark() {
        return this.isBookMark;
    }

    public void setIsBookMark(boolean isBookMark) {
        this.isBookMark = isBookMark;
    }



}
