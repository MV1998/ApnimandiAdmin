package com.mohit.varma.apnimandiadmin.utilities;

import java.util.UUID;

public class Constant {
    //mime type for image : intent
    public static final String IMAGE_MIME_TYPE = "image/*";

    //Constant Product Category
    public static final String ITEMS = "Items";
    public static final String FRUIT = "Fruits";
    public static final String VEGETABLE = "Vegetables";
    public static final String SNACKS = "Snacks";
    public static final String PROTEIN = "Protein";
    public static final String BACKING = "Backing";
    public static final String ITEM_KEY = "item_key";
    public static final String MOST_POPULAR = "MostPopularItems";
    public static final String UITEM_KEY = "uitem";

    public static final String ORDER_PLACED = "Order Placed";
    public static final String SHIPPED = "Order Shipped";
    public static final String CANCELLED = "Order Cancelled";
    public static final String DELIVERED = "Order Delivered";
    public static final String PROCESSING = "Order Processing";

    public static int generateUniqueId() {
        UUID idOne = UUID.randomUUID();
        String str=""+idOne;
        int uid=str.hashCode();
        String filterStr=""+uid;
        str=filterStr.replaceAll("-", "");
        return Integer.parseInt(str);
    }
}
