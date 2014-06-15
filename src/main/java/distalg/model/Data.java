package distalg.model;

import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;

/**
 * Created by home on 13.06.14.
 */
@Entity
@Table(name = "data")
public class Data implements Comparable<Data>{
    private int id;
    private String data;
    //@JsonIgnore
    private int size;
    @JsonIgnore
    private int isProcessed;
    @JsonIgnore
    private String processedData;

    @JsonIgnore
    private Algorithm algorithmByAlgorithmId;

    @Transient
    public double size_vidnosn;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "data")
    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Basic
    @Column(name = "size")
    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Basic
    @Column(name = "is_processed")
    public int getIsProcessed() {
        return isProcessed;
    }

    public void setIsProcessed(int isProcessed) {
        this.isProcessed = isProcessed;
    }

    @Basic
    @Column(name = "processed_data")
    public String getProcessedData() {
        return processedData;
    }

    public void setProcessedData(String processedData) {
        this.processedData = processedData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Data data1 = (Data) o;

        if (id != data1.id) return false;
        if (isProcessed != data1.isProcessed) return false;
        if (size != data1.size) return false;
        if (data != null ? !data.equals(data1.data) : data1.data != null) return false;
        if (processedData != null ? !processedData.equals(data1.processedData) : data1.processedData != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (data != null ? data.hashCode() : 0);
        result = 31 * result + size;
        result = 31 * result + isProcessed;
        result = 31 * result + (processedData != null ? processedData.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "algorithm_id", referencedColumnName = "id", nullable = false)
    public Algorithm getAlgorithmByAlgorithmId() {
        return algorithmByAlgorithmId;
    }

    public void setAlgorithmByAlgorithmId(Algorithm algorithmByAlgorithmId) {
        this.algorithmByAlgorithmId = algorithmByAlgorithmId;
    }


    @Override
    public int compareTo(Data data_compare) {
        if(this.size > data_compare.size)
            return -1;
        else if(this.size < data_compare.size)
            return 1;
        return 0;
    }
}
