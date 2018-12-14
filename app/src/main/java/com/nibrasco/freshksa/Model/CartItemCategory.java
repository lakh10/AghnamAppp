package com.nibrasco.freshksa.Model;

public class CartItemCategory {
    private String Category;
    private int image_drawable;
    private String Packaging;
    private String Slicing;
    private String Weight;
    private String Intestine;
    private String Quantity;

    public CartItemCategory(Cart.Item item) {
        image_drawable = item.getCategory().Value();
        Category = item.getCategory().name();
        Packaging = item.getPackaging().name();
        Slicing = item.getSlicing().name();
        Weight = Cart.WeightLists.GetName(item.getCategory(), item.getWeight());
        Intestine = item.isIntestine() ? "نعم" : "لا";
        Quantity = String.valueOf(item.getQuantity());
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String Category) {
        this.Category = Category;
    }

    public int getImage_drawable() {
        return image_drawable;
    }

    public void setImage_drawable(int image_drawable) {
        this.image_drawable = image_drawable;
    }

    public String getPackaging() {
        return Packaging;
    }

    public void setPackaging(String packaging) {
        Packaging = packaging;
    }

    public String getSlicing() {
        return Slicing;
    }

    public void setSlicing(String slicing) {
        Slicing = slicing;
    }

    public String getWeight() {
        return Weight;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public void setWeight(String weight) {
        Weight = weight;
    }

    public String getIntestine() {
        return Intestine;
    }

    public void setIntestine(String intestine) {
        Intestine = intestine;
    }
}
