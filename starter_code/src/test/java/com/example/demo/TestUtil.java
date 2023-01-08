package com.example.demo;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.requests.ModifyCartRequest;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TestUtil {
    public static void injectObjects(Object target, String field, Object toInject) {
        boolean wasPrivate = false;

        try {
            Field field1 = target.getClass().getDeclaredField(field);
            if (!field1.isAccessible()) {
                field1.setAccessible(true);
                wasPrivate = true;
            }

            field1.set(target, toInject);
            if (wasPrivate) {
                field1.setAccessible(false);
            }

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static User getUser() {
        User user = new User();
        Cart cart = new Cart();
        user.setId(1);
        user.setPassword("PasswordTest");
        user.setUsername("Lamia");
        user.setCart(cart);
        cart.addItem(getItem());
        return user;
    }

    public static List<UserOrder> getOrders() {
        ArrayList<UserOrder> list = new ArrayList<>();
        list.add(new UserOrder());
        list.add(new UserOrder());
        list.add(new UserOrder());

        return list;
    }

    public static UserOrder getUserOrder() {
        return new UserOrder();
    }

    public static Item getItem() {
        Item item = new Item();
        item.setPrice(new BigDecimal(69));
        item.setDescription("desc");
        item.setName("name");
        return item;
    }

    public static ModifyCartRequest getModifyCartRequest() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(1);
        modifyCartRequest.setQuantity(2);
        modifyCartRequest.setUsername("Lamia");
        return modifyCartRequest;
    }
}

