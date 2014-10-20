package blossom.test;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import blossom.restful.service.business.stat.Stat;
import blossom.restful.service.business.stat.StatItem;

import com.fasterxml.jackson.databind.ObjectMapper;

public class StatTest {

    private static final Logger LOGGER = Logger.getLogger(StatTest.class.getName());

    private StatTest() {

    }

    public static void main(final String[] args) {
        writeStat();
    }

    private static void writeStat() {

        final Stat stat = new Stat();
        final List<StatItem> statItems = new ArrayList<StatItem>();
        final StatItem statitem1 = new StatItem();
        statitem1.setValued("leela");
        statitem1.setValue(20);
        statItems.add(statitem1);
        final StatItem statitem2 = new StatItem();
        statitem2.setValued("fry");
        statitem2.setValue(120);
        statItems.add(statitem2);
        final StatItem statitem3 = new StatItem();
        statitem3.setValued("farnsworth");
        statitem3.setValue(160);
        statItems.add(statitem3);
        stat.setDataset(statItems);

        final ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(System.out, stat);
        } catch (final Exception e) {
            LOGGER.log(Level.SEVERE, "failed to write json", e);
        }
    }
}
