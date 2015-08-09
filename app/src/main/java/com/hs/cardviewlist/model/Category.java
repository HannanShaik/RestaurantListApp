package com.hs.cardviewlist.model;

/**
 * Created by Hannan on 06/08/15.
 */
public class Category {
    public String OfflineCategoryID;
    public String Name;
    public String ParentCategoryID;
    public String CategoryType;

    public Category(){
        super();
    }

    @Override
    public String toString() {
        return "Category{" +
                "OfflineCategoryID='" + OfflineCategoryID + '\'' +
                ", Name='" + Name + '\'' +
                ", ParentCategoryID='" + ParentCategoryID + '\'' +
                ", CategoryType='" + CategoryType + '\'' +
                '}';
    }
}
