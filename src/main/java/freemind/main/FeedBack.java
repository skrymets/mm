package freemind.main;

public interface FeedBack {

    void progress(int act, String messageId, Object[] pMessageParameters);

    int getActualValue();

    void setMaximumValue(int max);

    void increase(String messageId, Object[] pMessageParameters);
}