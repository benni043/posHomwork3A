package country;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CountryTest {

    @Test
    public void serialize() throws IOException, ClassNotFoundException {
        List<Country> list = Country.readCSV("src/main/resources/country/countries.csv");

        try (
                FileOutputStream fileOutputStream = new FileOutputStream("src/main/resources/country/countriesNew.csv");
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        ) {
            for (Country country : list) {
                objectOutputStream.writeObject(country);
                country.setName(country.getName().toUpperCase());
                country.setHauptstadt(country.getHauptstadt().toUpperCase());
            }

        }
        try (FileInputStream fileInputStream = new FileInputStream("src/main/resources/country/countriesNew.csv");
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {

            List<Country> newList = new ArrayList<>();

            for (int i = 0; i < list.size(); i++) {
                newList.add((Country) objectInputStream.readObject());
            }

            assertEquals(list, newList);
        }
    }

}