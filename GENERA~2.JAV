package org.camunda.bpm.myproject.electronic_equipment_competitions;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.value.FileValue;
import org.camunda.spin.plugin.variable.value.JsonValue;
import org.camunda.spin.plugin.variable.value.impl.JsonValueImpl;
import org.camunda.spin.impl.json.jackson.JacksonJsonNode;
import org.camunda.spin.json.*;
import static org.camunda.spin.Spin.*;
import static org.camunda.spin.Spin.*;
import org.camunda.spin.SpinList;
import static org.camunda.spin.DataFormats.*;
import java.util.List;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.IOException;





//import org.camunda.bpm.engine.impl.pvm.delegate.TaskListener;



public class GenerationOrdersDocumentDelegate implements TaskListener {

    @Override
    public void notify(DelegateTask delegateTask)  {
        JsonValue json  = (delegateTask.getVariableTyped("selectedProduct"));
        SpinJsonNode jsont = S(delegateTask.getVariable("selectedProduct"), json());
        System.out.println("test "+jsont.toString());
        SpinList products = jsont.elements();
        String pdfHTMLtrs = "";
        double totalPrice = 0.0;
        int totalQuantity = 0;
        for (int i = 0; i < products.size(); i++) {
            SpinJsonNode product = S(products.get(i), json());
            System.out.println("product "+product.toString());
            double price = Double.parseDouble(product.prop("Price").toString());
            int quantity = Integer.parseInt(product.prop("Quantity").toString());
            totalQuantity += quantity;
            double priceInnerTotal = price*quantity;
            totalPrice += priceInnerTotal;
            pdfHTMLtrs = pdfHTMLtrs + "<tr>"+
                    // "<td>"+selection.prop("user")+"</td>"+
                    "<td>"+product.prop("Category")+"</td>"+
                    "<td>"+product.prop("Description")+"</td>"+
                    "<td>"+product.prop("Price")+"</td>"+
                    "<td>"+product.prop("Details")+"</td>"+
                    "<td>"+product.prop("Quantity")+"</td>"+
                    "<td>"+Double.toString(priceInnerTotal)+"</td>"+
                    "</tr>";
        }
        System.out.println("test2 ");
        pdfHTMLtrs = pdfHTMLtrs + "<tr>"+
                // "<td>-</td>"+
                "<td>-</td>"+
                "<td>-</td>"+
                "<td>-</td>"+
                "<td>-</td>"+
                "<td>"+Integer.toString(totalQuantity)+"</td>"+
                "<td>"+Double.toString(totalPrice)+"</td>"+
                "</tr>";
        try (OutputStream os = new FileOutputStream("/tmp/myOrdersDocument.pdf")) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.useFont(new File("/tmp/SourceSansPro-Regular.ttf"), "source-sans");
            builder.withHtmlContent("<?xml version='1.0' encoding='UTF-8'?><!DOCTYPE html><html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/><meta charset=\"UTF-8\" />"+
                    "<style>"+
                    "@font-face {"+
                    "font-family: 'source-sans';"+
                    "font-style: normal;"+
                    "font-weight: 400;"+
                    "src: url('file:///tmp/SourceSansPro-Regular.ttf');"+
                    "-fs-font-subset: complete-font;"+
                    "} *{font-family: 'source-sans';} "+
                    "</style></head><body><div></div><table style=\"width:100%;\" border=\"1\">"+
                    "<tr>"+
                        // "<td>Τοπικός Διαχειριστής</td>"+
                        "<td>Είδος</td>"+
                        "<td>Περιγραφή</td>"+
                        "<td>Κόστος (€) (με ΦΠΑ)</td>"+
                        "<td>URL Ενδεικτικού Αρχείου Προδιαγραφών</td>"+
                        "<td>Ποσότητα</td>"+
                        "<td>Σύνολο</td>"+
                    "</tr>"+pdfHTMLtrs+
                    "</table></body></html>","");
            builder.toStream(os);
            builder.run();
            FileValue typedFileValue = Variables
                    .fileValue("myOrdersDocument.pdf")
                    .file(new File("/tmp/myOrdersDocument.pdf"))
                    .mimeType("application/pdf")
                    .encoding("UTF-8")
                    .create();
            delegateTask.setVariable("orderList", typedFileValue);

            FileValue retrievedTypedFileValue = delegateTask.getVariableTyped("orderList");
            InputStream fileContent = retrievedTypedFileValue.getValue(); // a byte stream of the file contents
            String fileName = retrievedTypedFileValue.getFilename(); // equals "myOrdersDocument.pdf"
            String mimeType = retrievedTypedFileValue.getMimeType(); // equals "application/pdf"
            String encoding = retrievedTypedFileValue.getEncoding(); // equals "UTF-8"
            System.out.println(fileName.toString());
            System.out.println(mimeType.toString());
            System.out.println(encoding.toString());
        } catch(Exception e) {

        }

    }

    //@Override
    //public void complete(DelegateTask delegateTask) {

    //}
}
