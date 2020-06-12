package com.company.sensorsAPI.simulation;

import com.company.sensorsAPI.entities.Sensor;
import com.company.sensorsAPI.entities.StatisticsData;
import com.company.sensorsAPI.repositories.SensorRepository;
import com.company.sensorsAPI.repositories.StatisticsDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

@Controller
@RequestMapping(path = "/database")
public class DataGenerator {

    @Autowired
    private StatisticsDataRepository statisticsDataRepository;
    @Autowired
    private SensorRepository sensorRepository;

    public static boolean[] controlledRandom(int count, int trueCount) {
        Random random = new Random();
        boolean[] result = new boolean[count];
        for (int i = 0; i < count; i++) {
            result[i] = i < trueCount;
        }
        for (int i = 0; i < result.length; i++) {
            boolean temp = result[i];
            int randomNumber = random.nextInt(result.length);
            result[i] = result[randomNumber];
            result[randomNumber] = temp;
        }
        return result;
    }

    @GetMapping(path = "/fill")
    public @ResponseBody
    void fill() {

        long timeNow = System.currentTimeMillis();

        Timestamp startTimestamp = new Timestamp(2020 - 1900, 0, 1, 12, 0, 0, 0);
        Timestamp endTimestamp = new Timestamp(2020 - 1900, 12, 30, 12, 0, 0, 0);
        Timestamp currentTimestamp = startTimestamp;

        Random random = new Random();

        Sensor sensor1 = sensorRepository.findById(4).get();
        Sensor sensor2 = sensorRepository.findById(2).get();
        Sensor sensor3 = sensorRepository.findById(6).get();
        Sensor sensor4 = sensorRepository.findById(8).get();
        Sensor sensor5 = sensorRepository.findById(10).get();


        boolean[] booleans1 = controlledRandom(10000, 1000);
        boolean[] booleans2 = controlledRandom(10000, 2000);
        boolean[] booleans3 = controlledRandom(10000, 3000);
        boolean[] booleans4 = controlledRandom(10000, 4000);
        boolean[] booleans5 = controlledRandom(10000, 5000);
        boolean[] booleans6 = controlledRandom(10000, 6000);
        boolean[] booleans7 = controlledRandom(10000, 7000);
        boolean[] booleans8 = controlledRandom(10000, 8000);

        int counter = 0;
        Calendar c = Calendar.getInstance();

        while (currentTimestamp.getTime() < endTimestamp.getTime()) {
            if (counter >= 10000) counter = 0;

            c.setTime(new Date(currentTimestamp.getTime()));
            c.add(Calendar.MINUTE, 5);
            currentTimestamp = new Timestamp(c.getTimeInMillis());


            StatisticsData sd1 = new StatisticsData("Kuchyne_sensor", random.nextInt((22 - 18) + 1) + 18, currentTimestamp,
                    random.nextInt((25 - 15) + 1) + 15, booleans7[counter], booleans6[counter], sensor1);
            StatisticsData sd2 = new StatisticsData("Obyvak_sensor", random.nextInt((22 - 18) + 1) + 18, currentTimestamp,
                    random.nextInt((25 - 15) + 1) + 15, booleans8[counter], booleans7[counter], sensor2);
            StatisticsData sd3 = new StatisticsData("Svetnice_sensor", random.nextInt((22 - 18) + 1) + 18, currentTimestamp,
                    random.nextInt((25 - 15) + 1) + 15, booleans4[counter], booleans1[counter], sensor3);
            StatisticsData sd4 = new StatisticsData("Koupelna_sensor", random.nextInt((22 - 18) + 1) + 18, currentTimestamp,
                    random.nextInt((25 - 15) + 1) + 15, booleans5[counter], booleans4[counter], sensor4);
            StatisticsData sd5 = new StatisticsData("Loznice_sensor", random.nextInt((22 - 18) + 1) + 18, currentTimestamp,
                    random.nextInt((25 - 15) + 1) + 15, booleans3[counter], booleans7[counter], sensor5);
            statisticsDataRepository.save(sd1);
            statisticsDataRepository.save(sd2);
            statisticsDataRepository.save(sd3);
            statisticsDataRepository.save(sd4);
            statisticsDataRepository.save(sd5);
            counter++;
        }

        System.out.println((System.currentTimeMillis() - timeNow) / 1000);

    }
}
