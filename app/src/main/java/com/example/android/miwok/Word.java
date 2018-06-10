package com.example.android.miwok;

public class Word {
    private String miwokWord,defaultWord;
    private static final int No_IMAGE_PROVIDED=-1;
    private int imageSource=No_IMAGE_PROVIDED, mAudioSource;

    public Word(String miwokWord,String defaultWord,int imageSource,int audioSource){
        this.miwokWord=miwokWord;
        this.defaultWord=defaultWord;
        this.imageSource=imageSource;
        this.mAudioSource=audioSource;
    }
    public Word(String miwokWord,String defaultWord,int mAudioSource){
        this.mAudioSource=mAudioSource;
        this.miwokWord=miwokWord;
        this.defaultWord=defaultWord;
    }

    public String getMiwokWord(){
        return miwokWord;
    }
    public String getDefaultWord(){
        return defaultWord;
    }
    public int getImageSource(){
        return imageSource;
    }
    public boolean hasImage(){
        return imageSource!=No_IMAGE_PROVIDED;
    }
    public int getAudioSource(){
        return mAudioSource;
    }
}
