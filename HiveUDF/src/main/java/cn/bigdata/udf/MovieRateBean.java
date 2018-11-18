package cn.bigdata.udf;

/**
 * Created by qian on 2017/4/11.
 */
public class MovieRateBean {
    private String movie;
    private String rate;
    private String ts;
    private String uid;

    public String getMovie() {
        return movie;
    }

    public void setMovie(String movie) {
        this.movie = movie;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public String

    toString() {
        return  "movie=" + movie + "\t" +
                "rate=" + rate + '\t' +
                "ts=" + ts + '\t' +
                "uid=" + uid ;
    }
}
