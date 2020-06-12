package com.company.sensorsAPI.controllers;

import com.company.sensorsAPI.entities.Room;
import com.company.sensorsAPI.entities.StatisticsData;
import com.company.sensorsAPI.repositories.RoomRepository;
import com.company.sensorsAPI.repositories.SensorRepository;
import com.company.sensorsAPI.repositories.StatisticsDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(path = "/statistics_data")
public class StatisticsDataController {

    public static int daysHeated;
    public static Map<String, Map<Integer, Integer>> lightData;
    public static Map<Integer, Double> totalEnergyConsumption;
    public static boolean initialized = false;
    @Autowired
    private StatisticsDataRepository statisticsDataRepository;
    @Autowired
    private SensorRepository sensorRepository;
    @Autowired
    private RoomRepository roomRepository;

    @GetMapping(path = "/init")
    public @ResponseBody
    void initialize() {
        init();
    }

    @GetMapping(path = "/heatingInYear")
    public @ResponseBody
    int getHeatingInYear() {

        return daysHeated;
    }

    @GetMapping(path = "/lightTimeInMonths")
    public @ResponseBody
    Map<String, Map<Integer, Integer>> getLightTimeInMonths() {

        return lightData;
    }

    @GetMapping(path = "/totalConsumption")
    public @ResponseBody
    Map<Integer, Double> getTotalConsumption() {

        return totalEnergyConsumption;
    }

    public void init() {
        System.out.println("Počítám statistiky");
        Iterable<StatisticsData> data = statisticsDataRepository.findAll();
        daysHeated = getDaysHeated(data);
        lightData = getLightInMonth(data);
        totalEnergyConsumption = getTotalEnergyConsumption(data);
        System.out.println("days heated: " + daysHeated);
        System.out.println("light data: " + lightData.toString());
        System.out.println("total energy: " + totalEnergyConsumption.toString());
        System.out.println("Konec statistik");
    }

    public static void initNewRoom(Room room) {
        Map<Integer, Integer> monthsLight = new HashMap<>();
        for (int i = 0; i < 12; i++) {
            monthsLight.put(i, 0);
        }

        lightData.put(room.getName(), monthsLight);

    }

    private int getDaysHeated(Iterable<StatisticsData> data) {

        int ret = 0;
        ArrayList<Integer> daysAdded = new ArrayList<>();
        Calendar cal = Calendar.getInstance();

        for (StatisticsData statisticsData : data) {//projde všechny statistiky
            long timestamp = statisticsData.getTimestamp().getTime(); //konkrétní timestamp záznamu z DB
            cal.setTimeInMillis(timestamp); //nastaví instanci kalendáře na čas timestampu

            int statisticsDay = cal.get(Calendar.DAY_OF_YEAR);

            if (statisticsData.isWasHeated() && !daysAdded.contains(statisticsDay)) {  //pokud se v tento den topilo a zároveň tento den nebyl již připočítán
                ret += 1;
                daysAdded.add(statisticsDay);
            }
        }
        return ret;
    }

    private Map<String, Map<Integer, Integer>> getLightInMonth(Iterable<StatisticsData> data) { // projede všechny záznamy z statistics data a vrací hash mapu, ve které je na každou místnost namapovaná hashmapa s hodnotami svícení v daném měsíci
        Map<String, Map<Integer, Integer>> ret = new HashMap<>();
        Iterable<Room> rooms = roomRepository.findAll();
        Calendar cal = Calendar.getInstance();

        Map<Integer, Integer> monthsLight = new HashMap<>(); //naplní mapu měsící a nulovými hodnotami
        for (int i = 0; i < 12; i++) {
            monthsLight.put(i, 0);
        }

        for (Room room : rooms) {
            ret.put(room.getName(), monthsLight);
        }

        for (StatisticsData statisticsData : data) {

            if (!statisticsData.getlightsOn()) continue;

            long timestamp = statisticsData.getTimestamp().getTime();
            cal.setTimeInMillis(timestamp);
            int month = cal.get(Calendar.MONTH);
            Room room = null;
            for (Room room1 : rooms) {
                if (room1.getName().equals(statisticsData.getSensor().getRoom().getName())) room = room1;
            }
            if (room == null) continue;
            Map<Integer, Integer> values = new HashMap<>();
            for (Integer i : ret.get(room.getName()).keySet()) {
                values.put(i, ret.get(room.getName()).get(i));
            }
            int previousLightValue = ret.get(room.getName()).get(month);
            values.put(month, previousLightValue + 300); // přičítá 300 sekund za každý záznam kde je lights on

            ret.put(room.getName(), values);
        }

        return ret;
    }

    private Map<Integer, Double> getTotalEnergyConsumption(Iterable<StatisticsData> data) {
        Map<Integer, Double> ret = new HashMap<>();
        Calendar cal = Calendar.getInstance();

        for (int i = 0; i < 12; i++) {
            ret.put(i, 0.0);
        }

        for (StatisticsData statisticsData : data) {

            long timestamp = statisticsData.getTimestamp().getTime();
            cal.setTimeInMillis(timestamp);
            int month = cal.get(Calendar.MONTH);

            double previousValue = ret.get(month);
            ret.replace(month, previousValue + statisticsData.getCurrentConsumption());
        }

        return ret;
    }

}
