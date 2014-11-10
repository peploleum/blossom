package blossom.restful.service.tech.table;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import blossom.exception.TopLevelBlossomException;
import blossom.persistence.EntityManagerFactorySingleton;
import blossom.persistence.entity.CharacterEntity;
import blossom.restful.service.tech.table.dto.TableRow;
import blossom.restful.service.tech.table.dto.TableRowCollection;

/**
 * Handles acess to persistence from service layer and the other way around.
 *
 * @author peploleum
 *
 */
public class TableDao {
    private static final Logger LOGGER = Logger.getLogger(TableDao.class.getName());

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
//                    tableRow.setSize(result.getSize());
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
        return tableRowCollection;
    }
}
