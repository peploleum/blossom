package blossom.restful.service.table.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TableRowCollection {
    private List<TableRow> rows;

    public List<TableRow> getRows() {
        return this.rows;
    }

    public void setRows(final List<TableRow> rows) {
        this.rows = rows;
    }

}
