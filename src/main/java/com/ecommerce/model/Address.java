package com.ecommerce.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "addresses")
@Data
@NoArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    @NotBlank
    @Size(min = 5, message = "Street must be at least 5 characters long")
    private String street;

    @NotBlank
    @Size(min = 5, message = "Building name must be at least 5 characters long")
    private String buildingName;

    @NotBlank
    @Size(min = 5, message = "City name must be at least 5 characters long")
    private String cityName;

    @NotBlank
    @Size(min = 2, message = "State name must be at least 2 characters long")
    private String stateName;

    @NotBlank
    @Size(min = 2, message = "Country name must be at least 2 characters long")
    private String countryName;

    @NotBlank
    @Size(min = 6, message = "Postal code must be at least 6 characters long")
    private String postalCode;

    @ManyToMany(mappedBy = "addresses")
    private List<User> users = new ArrayList<>();

    public Address(String street, String buildingName, String cityName, String stateName, String countryName, String postalCode) {
        this.street = street;
        this.buildingName = buildingName;
        this.cityName = cityName;
        this.stateName = stateName;
        this.countryName = countryName;
        this.postalCode = postalCode;
    }
}
