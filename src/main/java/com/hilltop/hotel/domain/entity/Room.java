package com.hilltop.hotel.domain.entity;

import com.hilltop.hotel.domain.request.RoomRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

/**
 * Room entity
 */
@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
public class Room {

    @Transient
    private static final String ROOM_ID_PREFIX = "rid-";

    @Id
    private String id;
    private String roomNo;
    @ManyToOne
    private RoomType roomType;
    private int maxPeople;
    private double cost;
    private double price;
    @ManyToOne
    @JoinColumn(nullable = false)
    private Hotel hotel;

    public Room(RoomRequestDto roomRequestDto, Hotel hotel, RoomType roomType) {
        this.id = ROOM_ID_PREFIX + UUID.randomUUID();
        updateRoom(roomRequestDto, hotel, roomType);
    }

    /**
     * This method is used to update room details.
     *
     * @param roomRequestDto roomRequestDto
     * @param hotel          hotel
     * @param roomType       roomType
     */
    public void updateRoom(RoomRequestDto roomRequestDto, Hotel hotel, RoomType roomType) {
        this.roomType = roomType;
        this.roomNo = roomRequestDto.getRoomNo();
        this.maxPeople = roomRequestDto.getMaxPeople();
        this.cost = roomRequestDto.getCost();
        this.hotel = hotel;
        this.price = calculateRoomPrice(roomType);
    }

    /**
     * This method is used to calculate room price.
     *
     * @param roomType roomType
     * @return room price.
     */
    private double calculateRoomPrice(RoomType roomType) {
        return cost * (100 + roomType.getMarkupPercentage()) / 100;
    }
}
