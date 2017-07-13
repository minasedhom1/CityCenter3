package com.av.lenovo.sa3edny.classes;

import java.util.ArrayList;

/**
 * Created by Mina on 7/13/2017.
 */

public class LoyaltyClass  {
private class PointLevel{
String LevelNumber,LevelPoint,Description;
    boolean HaveLevel;

    public String getLevelNumber() {
        return LevelNumber;
    }

    public void setLevelNumber(String levelNumber) {
        LevelNumber = levelNumber;
    }

    public String getLevelPoint() {
        return LevelPoint;
    }

    public void setLevelPoint(String levelPoint) {
        LevelPoint = levelPoint;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public boolean isHaveLevel() {
        return HaveLevel;
    }

    public void setHaveLevel(boolean haveLevel) {
        HaveLevel = haveLevel;
    }
}
    ArrayList<PointLevel> pointsLevel;
    String Total_point,Total_Visite,Promo_Code,Promo_Code_Description;
    boolean IsVisite,IsPromoCode,IsPoint;


    public ArrayList<PointLevel> getPointsLevel() {
        return pointsLevel;
    }

    public void setPointsLevel(ArrayList<PointLevel> pointsLevel) {
        this.pointsLevel = pointsLevel;
    }

    public String getTotal_point() {
        return Total_point;
    }

    public void setTotal_point(String total_point) {
        Total_point = total_point;
    }

    public String getTotal_Visite() {
        return Total_Visite;
    }

    public void setTotal_Visite(String total_Visite) {
        Total_Visite = total_Visite;
    }

    public String getPromo_Code() {
        return Promo_Code;
    }

    public void setPromo_Code(String promo_Code) {
        Promo_Code = promo_Code;
    }

    public String getPromo_Code_Description() {
        return Promo_Code_Description;
    }

    public void setPromo_Code_Description(String promo_Code_Description) {
        Promo_Code_Description = promo_Code_Description;
    }

    public boolean isVisite() {
        return IsVisite;
    }

    public void setVisite(boolean visite) {
        IsVisite = visite;
    }

    public boolean isPromoCode() {
        return IsPromoCode;
    }

    public void setPromoCode(boolean promoCode) {
        IsPromoCode = promoCode;
    }

    public boolean isPoint() {
        return IsPoint;
    }

    public void setPoint(boolean point) {
        IsPoint = point;
    }
}
