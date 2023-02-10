package team.waitingcatch.app.restaurant.entity;

import javax.persistence.Embeddable;

@Embeddable
public class Address {
    private String province;
    private String city;
    private String street;
    private float latitude;
    private float longitude;
}
