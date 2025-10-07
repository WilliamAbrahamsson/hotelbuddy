package javaproject.com.UI.components.PDF;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class PDF {

  String name;

  public PDF(String date, double price) {
    this.name = name;
    Document document = new Document(PageSize.A4, 50, 50, 50, 50);

    try {
      PdfWriter.getInstance(document, new FileOutputStream("Booking.pdf"));

      document.open();

      Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 28, Font.BOLD);
      Paragraph title = new Paragraph("Hotel Booking Confirmation", titleFont);
      title.setAlignment(Element.ALIGN_CENTER);
      document.add(title);

      document.add(new Paragraph("\n"));

      Font bodyFont = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.NORMAL);
      document.add(new Paragraph("Dear Customer" + ",", bodyFont));
      document.add(
        new Paragraph(
          "We are pleased to confirm your booking at our hotel.",
          bodyFont
        )
      );
      document.add(
        new Paragraph("Your stay is scheduled for " + date + ".", bodyFont)
      );
      document.add(
        new Paragraph(
          "The total price for your stay is $" + price + ".",
          bodyFont
        )
      );

      document.close();
    } catch (FileNotFoundException | DocumentException e) {
      e.printStackTrace();
    }
  }

  public PDF(double price) {
    this.name = name;
    Document document = new Document(PageSize.A4, 50, 50, 50, 50);

    try {
      PdfWriter.getInstance(document, new FileOutputStream("Booking.pdf"));

      document.open();

      Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 28, Font.BOLD);
      Paragraph title = new Paragraph("Hotel Booking Invoice", titleFont);
      title.setAlignment(Element.ALIGN_CENTER);
      document.add(title);

      document.add(new Paragraph("\n"));

      Font bodyFont = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.NORMAL);
      Font headerFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
      document.add(new Paragraph("Dear Customer" + ",", bodyFont));
      document.add(
        new Paragraph("Thank you for your booking at our hotel.", bodyFont)
      );
      document.add(
        new Paragraph(
          "The total price for your stay is $" + price + ".",
          bodyFont
        )
      );

      Paragraph paymentDetails = new Paragraph();
      paymentDetails.add(new Chunk("\nPayment Details:\n", headerFont));
      paymentDetails.add(
        new Chunk(
          "  Payment should be made by bank transfer to the following account:\n",
          bodyFont
        )
      );
      paymentDetails.add(new Chunk("  Account name: [HotelBuddy]\n", bodyFont));
      paymentDetails.add(
        new Chunk("  Account number: [4435-423-9674-234]\n", bodyFont)
      );
      paymentDetails.add(new Chunk("  Bank name: [Bank Supreme]\n", bodyFont));
      paymentDetails.add(new Chunk("  Bank address: [Bankstreet]\n", bodyFont));
      paymentDetails.add(
        new Chunk("  SWIFT/BIC code: [CHASUS33XXX]\n", bodyFont)
      );
      paymentDetails.add(
        new Chunk(
          "  Please include your invoice number in the payment reference.\n",
          bodyFont
        )
      );

      document.add(paymentDetails);
      document.close();
    } catch (FileNotFoundException | DocumentException e) {
      e.printStackTrace();
    }
  }

  public void open() {
    File file = new File("Booking.pdf");
    if (Desktop.isDesktopSupported()) {
      Desktop desktop = Desktop.getDesktop();
      if (file.exists()) {
        try {
          desktop.open(file);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }
}
