package sa.id.service;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.*;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import sa.id.InvoiceApp;
import sa.id.domain.Invoice;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang3.StringEscapeUtils.escapeJava;

@Component
public class InvoiceUtils {

    public ResponseEntity<InputStreamResource> generatePDF(String reportName, Invoice invoice, String key) {
        FileInputStream fis = null;
        Map<String, Object> parameters = new HashMap<>();

        String path = ".";

        File ttd = new File(path+"/fuldfk_files/ttd.png");
        File stempel = new File(path+"/fuldfk_files/stempel.png");

        try {
            parameters.put("no", escapeJava(invoice.getNo()));
            parameters.put("receiptFrom", invoice.getReceiptFrom());

            parameters.put("nominal", "Rp. "+ invoice.getNominal());
            parameters.put("periode", invoice.getPeriode());
            parameters.put("nominalWords", numToIndonesianWord(Double.parseDouble(invoice.getNominal()))+"RUPIAH");
            parameters.put("background", InvoiceApp.class.getResource("/public/report/images/background-kwitansi.jpg").toString());
            parameters.put("ttd", ttd.getAbsolutePath());
            parameters.put("stempel", stempel.getAbsolutePath());
            parameters.put("finName", invoice.getFinName());
            parameters.put("finNIK", invoice.getFinNIK());
            parameters.put("cityAndDate", invoice.getCity()+", "+invoice.getDate());

            if(key.equals("staff")) parameters.put("mandate", invoice.getMandate());
            else if (key.equals("ldfk")) parameters.put("dew", invoice.getDew());

            JasperReport report = (JasperReport) JRLoader.loadObject(InvoiceApp.class.getResourceAsStream("/public/report/"+key+".jasper"));
            JasperPrint print = JasperFillManager.fillReport(report, parameters, new JREmptyDataSource(1));

            SimplePdfReportConfiguration reportConfig = new SimplePdfReportConfiguration();
            reportConfig.setSizePageToContent(true);
            reportConfig.setForceLineBreakPolicy(false);

            SimplePdfExporterConfiguration exportConfig = new SimplePdfExporterConfiguration();
            exportConfig.setMetadataAuthor("sa.id");
            exportConfig.setEncrypted(true);

            // exports report to pdf
            SimpleOutputStreamExporterOutput output = new SimpleOutputStreamExporterOutput(reportName+".pdf");

            JRPdfExporter exporter = new JRPdfExporter();
            exporter.setExporterInput(new SimpleExporterInput(print));
            exporter.setExporterOutput(output);
            exporter.setConfiguration(reportConfig);
            exporter.setConfiguration(exportConfig);
            exporter.exportReport();

            fis = new FileInputStream(reportName+".pdf");
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.setContentType(MediaType.valueOf("application/pdf"));
            responseHeaders.setContentDispositionFormData("inline", reportName);
            responseHeaders.setContentLength(fis.available());
            return new ResponseEntity<InputStreamResource>(new InputStreamResource(fis), responseHeaders, HttpStatus.OK);

        } catch (Exception e) {
            throw new RuntimeException("It's not possible to generate the pdf report.", e);
        }
    }

    private String numToIndonesianWord (double number) {

        String[] nominal={"","SATU","DUA","TIGA","EMPAT","LIMA","ENAM",
                "TUJUH","DELAPAN","SEMBILAN","SEPULUH","SEBELAS"};

        if(number<12)
        {
            return nominal[(int)number];
        }

        if(number>=12 && number <=19)
        {
            return nominal[(int)number%10] +" BELAS ";
        }

        if(number>=20 && number <=99)
        {
            return nominal[(int)number/10] +" PULUH "+nominal[(int)number%10];
        }

        if(number>=100 && number <=199)
        {
            return "SERATUS "+ numToIndonesianWord(number%100);
        }

        if(number>=200 && number <=999)
        {
            return nominal[(int)number/100]+" RATUS "+numToIndonesianWord(number%100);
        }

        if(number>=1000 && number <=1999)
        {
            return "SERIBU "+ numToIndonesianWord(number%1000);
        }

        if(number >= 2000 && number <=999999)
        {
            return numToIndonesianWord((int)number/1000)+" RIBU "+ numToIndonesianWord(number%1000);
        }

        if(number >= 1000000 && number <=999999999)
        {
            return numToIndonesianWord((int)number/1000000)+" JUTA "+ numToIndonesianWord(number%1000000);
        }

        return "";
    }

}
