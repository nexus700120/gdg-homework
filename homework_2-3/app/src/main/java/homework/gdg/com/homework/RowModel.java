package homework.gdg.com.homework;

/**
 * Created by vkirillov on 15.08.2017.
 */

class RowModel {

    private String mCountry;
    private String mCapital;
    private int mLikes;
    private boolean mIsSelected;

    RowModel(String country, String capital) {
        mCountry = country;
        mCapital = capital;
    }

    String getCountry() {
        return mCountry;
    }

    String getCapital() {
        return mCapital;
    }

    boolean isSelected() {
        return mIsSelected;
    }

    void setIsSelected(boolean mIsSelected) {
        this.mIsSelected = mIsSelected;
    }

    int getLikes() {
        return mLikes;
    }

    void like() {
        mLikes++;
    }

    void dislike() {
        if (mLikes > 0) mLikes--;
    }

    void resetLikes() {
        mLikes = 0;
    }
}
