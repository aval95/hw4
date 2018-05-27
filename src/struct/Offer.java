/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package struct;

/**
 *
 * @author Alex
 */
public class Offer {
    private int offer_id;
    private int reward_cost;
    private String description;
    private String shop;

    public Offer(int offer_id, int reward_cost, String description, String shop) {
        this.offer_id = offer_id;
        this.reward_cost = reward_cost;
        this.description = description;
        this.shop = shop;
    }

    public int getOffer_id() {
        return offer_id;
    }

    public int getReward_cost() {
        return reward_cost;
    }

    public String getDescription() {
        return description;
    }

    public String getShop() {
        return shop;
    }

    public void setOffer_id(int offer_id) {
        this.offer_id = offer_id;
    }

    public void setReward_cost(int reward_cost) {
        this.reward_cost = reward_cost;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    @Override
    public String toString() {
        return "Offer{" + "offer_id=" + offer_id + ", reward_cost=" + reward_cost + ", description=" + description + ", shop=" + shop + '}';
    }
    
}
