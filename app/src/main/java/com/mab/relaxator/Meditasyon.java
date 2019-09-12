package com.mab.relaxator;

public class Meditasyon {
    private String Id;
    private String Title;
    private String Description;
    private String Image;
    private String Sound;
    private String Favorite;
    private String Date;
    private String Category;

    public String getId() {
        return Id;
    }

    public String getTitle() {
        return Title;
    }

    public String getDescription() {
        return Description;
    }

    public String getImage() {
        return Image;
    }

    public String getSound() {
        return Sound;
    }

    public String getFavorite() {
        return Favorite;
    }

    public String getDate() {
        return Date;
    }

    public String getCategory() {
        return Category;
    }

    public Meditasyon(String m_Id, String m_Title, String m_Description, String m_Image, String m_Sound, String m_Favorite, String m_Date, String m_Category) {

        this.Id          = m_Id;
        this.Title       = m_Title;
        this.Description = m_Description;
        this.Image       = m_Image;
        this.Sound       = m_Sound;
        this.Favorite    = m_Favorite;
        this.Date        = m_Date;
        this.Category    = m_Category;
    }

}
