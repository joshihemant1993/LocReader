package com.example.hrjoshi.locreader;

import android.graphics.Bitmap;

/**
 * Created by iGuest on 10/31/2016.
 */

public class RowItem {

    private Bitmap image;
    private String title;
    //    private String desc;

        public RowItem(Bitmap image, String title, String desc) {
            this.image = image;
            this.title = title;
        //    this.desc = desc;
        }
        public Bitmap getImage() {
            return image;
        }
        public void setImageId(Bitmap imageId) {
            this.image = image;
        }
      /*  public String getDesc() {
            return desc;
        }
        public void setDesc(String desc) {
            this.desc = desc;
        }
        */
        public String getTitle() {
            return title;
        }
        public void setTitle(String title) {
            this.title = title;
        }
     /*   @Override
        public String toString() {
            return title + "\n" + desc;
    }*/

}
