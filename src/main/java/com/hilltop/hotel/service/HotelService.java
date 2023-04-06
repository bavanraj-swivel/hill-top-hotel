package com.hilltop.hotel.service;

import com.hilltop.hotel.domain.entity.Hotel;
import com.hilltop.hotel.domain.entity.Room;
import com.hilltop.hotel.domain.request.HotelRequestDto;
import com.hilltop.hotel.domain.request.UpdateHotelRequestDto;
import com.hilltop.hotel.exception.DataNotFoundException;
import com.hilltop.hotel.exception.HillTopHotelApplicationException;
import com.hilltop.hotel.repository.HotelRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Hotel service
 */
@Service
@Slf4j
public class HotelService {

    private final HotelRepository hotelRepository;

    public HotelService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    /**
     * This method is used to add hotel detail.
     *
     * @param hotelRequestDto hotelRequestDto
     */
    public void addHotel(HotelRequestDto hotelRequestDto) {
        try {
            hotelRepository.save(new Hotel(hotelRequestDto));
            log.debug("Successfully added hotel data.");
        } catch (DataAccessException e) {
            throw new HillTopHotelApplicationException("Failed to save hotel info in database.", e);
        }
    }

    /**
     * This method is used to update hotel detail.
     *
     * @param updateHotelRequestDto updateHotelRequestDto
     */
    public void updateHotel(UpdateHotelRequestDto updateHotelRequestDto) {
        try {
            Hotel hotel = getHotelById(updateHotelRequestDto.getId());
            hotel.updateHotel(updateHotelRequestDto);
            hotelRepository.save(hotel);
            log.debug("Successfully updated hotel data.");
        } catch (DataAccessException e) {
            throw new HillTopHotelApplicationException("Failed to update hotel info in database.", e);
        }
    }

    /**
     * This method is used to get hotel list.
     *
     * @return hotel list.
     */
    public List<Hotel> getHotelList() {
        try {
            return hotelRepository.findAll();
        } catch (DataAccessException e) {
            throw new HillTopHotelApplicationException("Failed to get all hotel data from database.", e);
        }
    }

    /**
     * This method is used to get hotel detail by id.
     *
     * @param id hotelId
     * @return hotel detail.
     */
    public Hotel getHotelById(String id) {
        try {
            return hotelRepository.findById(id)
                    .orElseThrow(() -> new DataNotFoundException("Hotel not found for id: " + id));
        } catch (DataAccessException e) {
            throw new HillTopHotelApplicationException("Failed to get hotel info from database.", e);
        }
    }

    /**
     * This method is used to search hotels by location and pax count.
     *
     * @param location location
     * @param paxCount paxCount
     * @return hotel & rooms map.
     */
    public Map<Hotel, List<Room>> getHotelsByLocationAndPaxCount(String location, int paxCount) {
        try {
            List<Hotel> hotelList = hotelRepository.findByLocation(location);
            Map<Hotel, List<Room>> hotelAndRoomsMap = new HashMap<>();
            for (Hotel hotel : hotelList) {
                List<Room> sortedRoomList = hotel.getRooms().stream()
                        .filter(room -> room.getMaxPeople() == paxCount).collect(Collectors.toList());
                if (sortedRoomList.isEmpty())
                    sortedRoomList = getPossibleRoomsForPaxCount(hotel.getRooms(), paxCount);
                if (!sortedRoomList.isEmpty())
                    hotelAndRoomsMap.put(hotel, sortedRoomList);
            }
            return hotelAndRoomsMap;
        } catch (DataAccessException e) {
            throw new HillTopHotelApplicationException("Failed to get hotels from database.", e);
        }
    }

    /**
     * This method is used to get possible rooms for pax count.
     * If pax count is 5 method will return rooms with pax count 6 and 7.
     * Else method will return multiple rooms to fulfill pax count (e.g. two rooms with pax count 3 & 2 ).
     *
     * @param roomSet  roomSet
     * @param paxCount paxCount
     * @return list of rooms.
     */
    private List<Room> getPossibleRoomsForPaxCount(Set<Room> roomSet, int paxCount) {
        List<Room> sortedRoomList = roomSet.stream()
                .filter(room -> room.getMaxPeople() > paxCount && room.getMaxPeople() <= paxCount + 2)
                .collect(Collectors.toList());
        if (sortedRoomList.isEmpty()) {
            Optional<Room> optionalRoom = roomSet.stream().filter(room -> room.getMaxPeople() < paxCount)
                    .max(Comparator.comparing(Room::getMaxPeople));
            if (optionalRoom.isPresent())
                sortedRoomList = getRoomCombination(optionalRoom.get(), roomSet, paxCount);
        }
        return sortedRoomList;
    }

    /**
     * This method is used to return multiple rooms to fulfill pax count.
     * e.g. if required pax count is 5 method will return two rooms with pax count 3 & 2.
     *
     * @param possibleMaximumPaxRoom possibleMaximumPaxRoom
     * @param roomSet                roomSet
     * @param paxCount               paxCount
     * @return list of rooms.
     */
    private List<Room> getRoomCombination(Room possibleMaximumPaxRoom, Set<Room> roomSet, int paxCount) {
        List<Room> sortedRoomList = new ArrayList<>();
        sortedRoomList.add(possibleMaximumPaxRoom);
        int totalPaxCount = possibleMaximumPaxRoom.getMaxPeople();

        List<Room> paxCountDecendingRoomList = roomSet.stream()
                .sorted(Comparator.comparing(Room::getMaxPeople).reversed()).collect(Collectors.toList());
        paxCountDecendingRoomList.remove(possibleMaximumPaxRoom);

        for (Room room : paxCountDecendingRoomList) {
            if (totalPaxCount + room.getMaxPeople() <= paxCount) {
                sortedRoomList.add(room);
                totalPaxCount += room.getMaxPeople();
                if (totalPaxCount == paxCount)
                    break;
            }
        }
        if (totalPaxCount != paxCount)
            return List.of();
        return sortedRoomList;
    }
}
