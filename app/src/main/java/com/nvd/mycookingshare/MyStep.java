package com.nvd.mycookingshare;

import java.io.Serializable;

public class MyStep implements Serializable {
    public String imgStep;
    public String step;

    public MyStep() {
    }

    public MyStep(String imgStep, String step) {
        this.imgStep = imgStep;
        this.step = step;
    }

    public void setImgStep(String imgStep) {
        this.imgStep = imgStep;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getImgStep() {
        return imgStep;
    }

    public String getStep() {
        return step;
    }
}
