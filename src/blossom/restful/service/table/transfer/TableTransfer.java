package blossom.restful.service.table.transfer;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.eclipse.persistence.jaxb.MarshallerProperties;

import blossom.exception.TopLevelBlossomException;
import blossom.persistence.EntityManagerFactorySingleton;
import blossom.persistence.entity.CharacterEntity;
import blossom.restful.service.table.dto.TableRow;
import blossom.restful.service.table.dto.TableRowCollection;

/**
 * Handles acess to persistence from service layer and the other way around.
 *
 * @author peploleum
 *
 */
public class TableTransfer {
    private static final Logger LOGGER = Logger.getLogger(TableTransfer.class.getName());

    /**
     * Access persistence. Read all rows. return serialized model
     *
     * @return {@link TableRowCollection} rows
     * @throws TopLevelBlossomException
     *             if persistence acess fails
     */
    public TableRowCollection getTableRows() throws TopLevelBlossomException {
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