package blossom.restful.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.eclipse.persistence.jaxb.MarshallerProperties;

import blossom.exception.TopLevelBlossomException;
import blossom.persistence.EntityManagerFactorySingleton;
import blossom.persistence.entity.CharacterEntity;
import blossom.restful.table.TableRow;
import blossom.restful.table.TableRowCollection;

@Path("/table")
public class TableService {

    private static final Logger LOGGER = Logger.getLogger(TableService.class.getName());

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getdata")
    public TableRowCollection getRows() throws TopLevelBlossomException {

        final EntityManager entityManager = EntityManagerFactorySingleton.getInstance().getEntityManagerFactory().createEntityManager();
        final TableRowCollection tableRowCollection = new TableRowCollection();
        try {
            final List<TableRow> tableRows = new ArrayList<TableRow>();
            final Query nameQuery = entityManager.createNamedQuery("CharacterEntity.findAll");
            try {
                @SuppressWarnings("unchecked")
                final List<CharacterEntity> resultList = nameQuery.getResultList();
                for (final CharacterEntity result : resultList) {
                    final TableRow tableRow = new TableRow();
                    tableRow.setId(result.getId());
                    tableRow.setCatchphrase(result.getCatchphrase());
                    tableRow.setName(result.getName());
                    tableRow.setSize(result.getSize());
                    tableRows.add(tableRow);
                }
                tableRowCollection.setRows(tableRows);

            } catch (final Exception e) {
                if (entityManager.getTransaction().isActive()) {
                    entityManager.getTransaction().rollback();
                }
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
                throw new TopLevelBlossomException(e, "Failed to load table rows from database.");
            }
        } finally {
            entityManager.close();
        }
        JAXBContext jc;
        try {
            jc = JAXBContext.newInstance(TableRowCollection.class);
            final Marshaller marshaller = jc.createMarshaller();
            marshaller.setProperty(MarshallerProperties.MEDIA_TYPE, "application/json");
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(MarshallerProperties.JSON_VALUE_WRAPPER, "coordinates");
            marshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, false);
            marshaller.marshal(tableRowCollection, System.out);
        } catch (final JAXBException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
        return tableRowCollection;
    }
}
