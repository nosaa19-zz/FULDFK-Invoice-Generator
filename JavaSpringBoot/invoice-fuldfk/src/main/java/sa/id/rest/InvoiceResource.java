package sa.id.rest;


import io.micrometer.core.instrument.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sa.id.InvoiceApp;
import sa.id.domain.Invoice;
import sa.id.service.InvoiceUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/report")
public class InvoiceResource {

    protected Logger logger = Logger.getLogger(InvoiceResource.class.getName());

    @Autowired
    InvoiceUtils invoiceUtils;

    @PostMapping("/pdf/staff")
    public ResponseEntity<InputStreamResource> getReportStaff(@RequestBody Invoice invoice) throws IOException {
        return invoiceUtils.generatePDF("e-kwitansi-"+invoice.getNo().replaceAll("[^a-zA-Z0-9]", ""), invoice, "staff");
    }

    @PostMapping("/pdf/ldfk")
    public ResponseEntity<InputStreamResource> getReportLDFK(@RequestBody Invoice invoice) throws IOException {
        return invoiceUtils.generatePDF("e-kwitansi-"+invoice.getNo().replaceAll("[^a-zA-Z0-9]", ""), invoice, "ldfk");
    }


}
