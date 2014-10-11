package blossom.test;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.eclipse.persistence.jaxb.MarshallerProperties;

import blossom.restful.service.stat.Stat;
import blossom.restful.service.stat.StatItem;

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

        JAXBContext jc;
        try {
            jc = JAXBContext.newInstance(Stat.class);
            final Marshaller marshaller = jc.createMarshaller();
            marshaller.setProperty(MarshallerProperties.MEDIA_TYPE, "application/json");
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, false);
            marshaller.marshal(stat, System.out);
        } catch (final JAXBException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }
}
