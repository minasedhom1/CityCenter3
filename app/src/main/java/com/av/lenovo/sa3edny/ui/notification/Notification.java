package com.av.lenovo.sa3edny.ui.notification;

/**
 * Created by mido on 3/30/2017.
 */

public class Notification {
   String NotificationID,Description,ItemID,Item_Photo;

    public  Notification()
    {

    }
    public String getNotificationID() {
        return NotificationID;
    }

    public void setNotificationID(String notificationID) {
        NotificationID = notificationID;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getItemID() {
        return ItemID;
    }

    public void setItemID(String itemID) {
        ItemID = itemID;
    }

    public String getItem_Photo() {
        return Item_Photo;
    }

    public void setItem_Photo(String item_Photo) {
        Item_Photo = item_Photo;
    }
}
