package com.company.sensorsAPI.controllers;

import com.company.sensorsAPI.entities.Room;
import com.company.sensorsAPI.entities.StatisticsData;
import com.company.sensorsAPI.entities.StatisticsDataCache;
import com.company.sensorsAPI.repositories.RoomRepository;
import com.company.sensorsAPI.repositories.StatisticsDataCacheRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
@RequestMapping(path = "/statistics_data_cache")
public class StatisticsDataCacheController {

    public static int daysHeated = 0;
    public static Map<String, Map<Integer, Integer>> lightData = new HashMap<>();
    public static Map<Integer, Double> totalEnergyConsumption = new HashMap<>();
    public static boolean initialized = false;

    public static StatisticsData currentStatisticsData;

    ArrayList<Integer> daysAdded = new ArrayList<>();

    @Autowired
    private StatisticsDataCacheRepository statisticsDataCacheRepository;
    @Autowired
    private RoomRepository roomRepository;

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

    @GetMapping(path = "/init")
    public @ResponseBody
    void initialize() {

        if(!initialized) {

            Iterable<Room> rooms = roomRepository.findAll();

            Map<Integer, Integer> monthsLight = new HashMap<>(); //naplní submapu měsící a nulovými hodnotami
            for (int i = 0; i < 12; i++) {
                monthsLight.put(i, 0);
                totalEnergyConsumption.put(i, 0.0);
            }

            for (Room room : rooms) {
                lightData.put(room.getName(), monthsLight);  //naplni existujícími místnostmi a nulovými hodnotami
            }

            Iterable<StatisticsDataCache> cacheToLoad = statisticsDataCacheRepository.findAll();
            for (StatisticsDataCache statisticsDataCache : cacheToLoad) {
                System.out.println("days heated" + statisticsDataCache.getDaysHeated());
                daysHeated = statisticsDataCache.getDaysHeated();
                Map<Integer, Integer> monthsData = lightData.get(statisticsDataCache.getRoomName());
                monthsData.put(statisticsDataCache.getMonthNumber(), Integer.parseInt(String.valueOf(statisticsDataCache.getLightOnSeconds())));
                lightData.put(statisticsDataCache.getRoomName(), monthsData);
                totalEnergyConsumption.put(statisticsDataCache.getMonthNumber(), statisticsDataCache.getTotalPowerConsumption());

            }
            initialized = true;
        }
        else {
            if(currentStatisticsData != null) {
                init();
            }
        }
    }

    private void init() {
        Calendar c = Calendar.getInstance();
        System.out.println(currentStatisticsData.getSensor().getRoom().getName());
        c.setTimeInMillis(currentStatisticsData.getTimestamp().getTime());
        Integer currentYear = c.get(Calendar.YEAR);
        Integer currentMonth = c.get(Calendar.MONTH);

        StatisticsDataCache cacheData = new StatisticsDataCache(currentStatisticsData.getSensor().getRoom().getName(), currentYear,
                currentMonth, 0, 0, 0, currentStatisticsData.getSensor());  //pokud záznam s touto místností a měsícem ještě neexistuje, tak je vytvořen

        Iterable<StatisticsDataCache> existingCacheData = statisticsDataCacheRepository.findAll();

        for (StatisticsDataCache existingCacheDatum : existingCacheData) { // prohledá všechny existující cache data a když najde záznam se stejnou místností a měsícem, tak jenom upravuje hodnoty, jinak ukládá nový cache záznam

            if(!currentYear.equals(existingCacheDatum.getYear())) break; // pokud se záznamy liší rokem, tak je třeba záznam ze starého roku převést na nový rok a začít počítat záznamy nanovo
            if(existingCacheDatum.getRoomName().equals(currentStatisticsData.getSensor().getRoom().getName())) {

                if(existingCacheDatum.getMonthNumber().equals(currentMonth)) {

                    cacheData = existingCacheDatum;

                }
            }
        }

        daysHeated = getDaysHeated(currentStatisticsData, cacheData);
        lightData = getLightInMonth(currentStatisticsData, cacheData);
        totalEnergyConsumption = getTotalEnergyConsumption(currentStatisticsData, cacheData);

        System.out.println("days heated: " + daysHeated);
        System.out.println("light data: " + lightData.toString());
        System.out.println("total energy: " + totalEnergyConsumption.toString());

        Iterable<StatisticsDataCache> loadedCache = statisticsDataCacheRepository.findAll();
        for (StatisticsDataCache statisticsDataCache : loadedCache) {
            if(statisticsDataCache.getRoomName().equals(cacheData.getRoomName())) {
                cacheData.setId(statisticsDataCache.getId());
                break;
            }
        }

        StatisticsDataCache finalCacheData = cacheData;

        if(cacheData.getId() == null) {
            statisticsDataCacheRepository.save(finalCacheData);
            return;
        }

        statisticsDataCacheRepository.findById(cacheData.getId())
                .map(statisticsDataCache -> {
                    statisticsDataCache.setLightOnSeconds(finalCacheData.getLightOnSeconds());
                    statisticsDataCache.setDaysHeated(finalCacheData.getDaysHeated());
                    statisticsDataCache.setTotalPowerConsumption(finalCacheData.getTotalPowerConsumption());
                    statisticsDataCache.setMonthNumber(finalCacheData.getMonthNumber());
                    statisticsDataCache.setRoomName(finalCacheData.getRoomName());
                    statisticsDataCache.setYear(finalCacheData.getYear());
                    statisticsDataCache.setLastUsedDayOfYear(finalCacheData.getLastUsedDayOfYear());

                    return statisticsDataCacheRepository.save(statisticsDataCache);
                })
                .orElseGet(() -> {
                    return statisticsDataCacheRepository.save(finalCacheData);
                });
    }

    public static void initNewRoom(Room room) {
        Map<Integer, Integer> monthsLight = new HashMap<>();
        for (int i = 0; i < 12; i++) {
            monthsLight.put(i, 0);
        }

        lightData.put(room.getName(), monthsLight);

    }

    private int getDaysHeated(StatisticsData data, StatisticsDataCache cache) {

        int ret = 0;
        Calendar cal = Calendar.getInstance();

        long timestamp = data.getTimestamp().getTime(); //konkrétní timestamp záznamu z DB
        cal.setTimeInMillis(timestamp); //nastaví instanci kalendáře na čas timestampu

        int statisticsDay = cal.get(Calendar.DAY_OF_YEAR);
        daysAdded.clear();
        daysAdded.add(cache.getLastUsedDayOfYear());
        if (data.isWasHeated() && !daysAdded.contains(statisticsDay)) {  //pokud se v tento den topilo a zároveň tento den nebyl již připočítán
            ret += 1;
            cache.setLastUsedDayOfYear(statisticsDay);
        }
        cache.setDaysHeated(cache.getDaysHeated() + ret); // nastaví hodnotu daysHeated na součet hodnoty minulé s hodnotou novou
        ret = cache.getDaysHeated() + ret;

        return ret;
    }

    private Map<String, Map<Integer, Integer>> getLightInMonth(StatisticsData data, StatisticsDataCache cache) {

        if (data.getlightsOn()) {

            Map<Integer, Integer> lightDataMonths = lightData.get(cache.getRoomName());
            lightDataMonths.put(cache.getMonthNumber(), lightDataMonths.get(cache.getMonthNumber()) + 300);

            lightData.put(cache.getRoomName(), lightDataMonths);
            cache.setLightOnSeconds(lightDataMonths.get(cache.getMonthNumber()) + 300); // přičte 300 sekund k minulému záznamu
        }

        return lightData;
    }

    private Map<Integer, Double> getTotalEnergyConsumption(StatisticsData data, StatisticsDataCache cache) {

        System.out.println(totalEnergyConsumption.toString());
        double previousValue = totalEnergyConsumption.get(cache.getMonthNumber());
        totalEnergyConsumption.replace(cache.getMonthNumber(), previousValue + data.getCurrentConsumption());
        cache.setTotalPowerConsumption(previousValue + data.getCurrentConsumption());

        return totalEnergyConsumption;
    }
}
