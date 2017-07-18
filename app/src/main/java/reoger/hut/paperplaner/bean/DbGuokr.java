package reoger.hut.paperplaner.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by 24540 on 2017/5/11.
 */

@Entity
public class DbGuokr {

    @Id(autoincrement = true)
    private Long id;

    private boolean isBookMark;

    @Unique
    @NotNull
    private int guokr_id;

    private String guolkr_news;
    private float guokr_time;
    private String guokr_content;
    @Generated(hash = 302599802)
    public DbGuokr(Long id, boolean isBookMark, int guokr_id, String guolkr_news,
            float guokr_time, String guokr_content) {
        this.id = id;
        this.isBookMark = isBookMark;
        this.guokr_id = guokr_id;
        this.guolkr_news = guolkr_news;
        this.guokr_time = guokr_time;
        this.guokr_content = guokr_content;
    }
    @Generated(hash = 1221109766)
    public DbGuokr() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public boolean getIsBookMark() {
        return this.isBookMark;
    }
    public void setIsBookMark(boolean isBookMark) {
        this.isBookMark = isBookMark;
    }
    public int getGuokr_id() {
        return this.guokr_id;
    }
    public void setGuokr_id(int guokr_id) {
        this.guokr_id = guokr_id;
    }
    public String getGuolkr_news() {
        return this.guolkr_news;
    }
    public void setGuolkr_news(String guolkr_news) {
        this.guolkr_news = guolkr_news;
    }
    public float getGuokr_time() {
        return this.guokr_time;
    }
    public void setGuokr_time(float guokr_time) {
        this.guokr_time = guokr_time;
    }
    public String getGuokr_content() {
        return this.guokr_content;
    }
    public void setGuokr_content(String guokr_content) {
        this.guokr_content = guokr_content;
    }

}
