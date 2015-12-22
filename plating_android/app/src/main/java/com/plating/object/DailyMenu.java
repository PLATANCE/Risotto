package com.plating.object;

/**
 * Created by cheehoonha on 9/4/15.
 */
public class DailyMenu {
    public int menu_d_idx;
    public int idx;
    public String nameMenu;
    public String imageUrlMenu;
    public int price;
    public int altPrice;
    public int idxChef;
    public String nameChef;
    public String imageUrlChef;
    public String imageUrlChef2;
    public String career;
    public String careerSumm;
    public String ingredients;
    public String calories;
    public String story;
    public int stock;
    public double rating;
    public int numOfReviews;

//    public DailyMenu() {
//
//    }
//
//    public DailyMenu(int idx, String nameMenu, String imageUrlMenu, int price, int altPrice, int idxChef, String nameChef, String imageUrlChef, String imageUrlChef2, String career, String careerSumm, String ingredients, String calories, String story) {
//        this.idx = idx;
//        this.nameMenu = nameMenu;
//        this.imageUrlMenu = imageUrlMenu;
//        this.price = price;
//        this.altPrice = altPrice;
//        this.idxChef = idxChef;
//        this.nameChef = nameChef;
//        this.imageUrlChef = imageUrlChef;
//        this.imageUrlChef2 = imageUrlChef2;
//        this.career = career;
//        this.careerSumm = careerSumm;
//        this.ingredients = ingredients;
//        this.calories = calories;
//        this.story = story;
//    }

    public boolean equals(int menu_idx) {
        return this.idx == menu_idx;
    }
}
