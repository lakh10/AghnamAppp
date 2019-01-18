package com.nibrasco.aghnam.Model;

public class Session {
    private User user;
    private Cart cart;
    private Cart.Item item;

    private static Session session = new Session();
    public static Session getInstance() { return session; }

    public Cart.Item Item() {
        return item;
    }

    public void Item(Cart.Item item) {
        this.item = item;
    }
    public User User() {
        return user;
    }

    public void User(User _user) {
        user = _user;
    }

    public Cart Cart() {
        return cart;
    }

    public void Cart(Cart _cart) {
        cart = _cart;
    }
}
