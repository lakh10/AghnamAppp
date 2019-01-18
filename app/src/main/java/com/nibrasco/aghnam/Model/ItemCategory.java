package com.nibrasco.aghnam.Model;

import android.content.res.Resources;
import com.nibrasco.aghnam.R;

public class ItemCategory {
    private String name;
    private int image_drawable;
    public ItemCategory(Resources r, Cart.eCategory category)
    {
        switch (category)
        {
            case Goat:
                name = r.getString(R.string.recyclerItemGoat);
                break;
            case Sheep:
                name = r.getString(R.string.recyclerItemSheep);
                break;
            case Camel:
                name = r.getString(R.string.recyclerItemCamel);
                break;
            case GroundMeat:
                name = r.getString(R.string.recyclerItemGroundMeat);
                break;
            case HalfSheep:
                name = r.getString(R.string.recyclerItemHalfSheep);
                break;
        }
        image_drawable = category.Value();
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage_drawable() {
        return image_drawable;
    }

    public void setImage_drawable(int image_drawable) {
        this.image_drawable = image_drawable;
    }
}
