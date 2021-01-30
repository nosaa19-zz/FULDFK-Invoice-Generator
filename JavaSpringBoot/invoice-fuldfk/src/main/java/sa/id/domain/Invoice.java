package sa.id.domain;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;

public class Invoice {

    String no;

    String receiptFrom;

    String mandate;

    String dew;

    String nominal;

    String date;

    String periode;

    String finName;

    String finNIK;

    String city;

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getReceiptFrom() {
        return receiptFrom;
    }

    public void setReceiptFrom(String receiptFrom) {
        this.receiptFrom = receiptFrom;
    }

    public String getMandate() {
        return mandate;
    }

    public void setMandate(String mandate) {
        this.mandate = mandate;
    }

    public String getDew() { return dew; }

    public void setDew(String dew) { this.dew = dew; }

    public String getNominal() {
        return nominal;
    }

    public void setNominal(String nominal) {
        this.nominal = nominal;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPeriode() {
        return periode;
    }

    public void setPeriode(String periode) { this.periode = periode; }

    public String getFinName() { return finName; }

    public void setFinName(String finName) { this.finName = finName; }

    public String getFinNIK() {
        return finNIK;
    }

    public void setFinNIK(String finNIK) {
        this.finNIK = finNIK;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
