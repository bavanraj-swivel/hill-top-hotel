package com.hilltop.hotel.domain.entity;

import com.hilltop.hotel.domain.request.HotelRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

/**
 * Hotel entity
 */
@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
public class Hotel {

    @Transient
    private static final String HOTEL_ID_PREFIX = "hid-";

    @Id
    private String id;
    private String name;
    private String location;
    private int roomCount;
    @OneToMany(mappedBy = "hotel", fetch = FetchType.LAZY)
    private Set<Room> rooms;

    public Hotel(HotelRequestDto hotelRequestDto) {
        this.id = HOTEL_ID_PREFIX + UUID.randomUUID();
        this.name = hotelRequestDto.getName();
        this.location = hotelRequestDto.getLocation();
        this.roomCount = hotelRequestDto.getRoomCount();
    }

    /**
     * This method is used to update hotel details.
     *
     * @param hotelRequestDto hotelRequestDto
     */
    public void updateHotel(HotelRequestDto hotelRequestDto) {
        this.name = hotelRequestDto.getName();
        this.location = hotelRequestDto.getLocation();
        this.roomCount = hotelRequestDto.getRoomCount();
    }
}
