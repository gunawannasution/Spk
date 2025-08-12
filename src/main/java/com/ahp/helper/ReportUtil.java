package com.ahp.helper;

import com.ahp.content.dao.PdfExportable;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.awt.Desktop;

import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ReportUtil {

    public static <T extends PdfExportable> void generatePdfReport(
            List<T> dataList,
            String[] headers,
            String reportTitle,
            String baseFileName,
            String kota,
            String namaDirektur
    ) throws Exception {

        // Siapkan folder laporan
        File reportsDir = new File("reports");
        if (!reportsDir.exists()) {
            reportsDir.mkdirs();
        }

        // Penamaan file otomatis
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String outputFilePath = "reports/" + baseFileName.replaceAll("\\s+", "_").toLowerCase() + "_" + timestamp + ".pdf";

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, new FileOutputStream(outputFilePath));
        document.open();

        addKopSurat(document);

        Paragraph title = new Paragraph(reportTitle,
                new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD));
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingBefore(20);
        title.setSpacingAfter(20);
        document.add(title);

        PdfPTable table = new PdfPTable(headers.length);
        table.setWidthPercentage(100);

        float[] columnWidths = new float[headers.length];
        for (int i = 0; i < headers.length; i++) {
            columnWidths[i] = 1f;
        }
        table.setWidths(columnWidths);

        addTableHeader(table, headers);

        int no = 1;
        for (T item : dataList) {
            List<String> row = item.toPdfRow();

            // Tambahkan nomor urut jika kolom "No" digunakan
            for (int i = 0; i < headers.length; i++) {
                String value;
                if (i == 0 && headers[0].equalsIgnoreCase("No")) {
                    value = String.valueOf(no++);
                } else {
                    value = row.get(i - (headers[0].equalsIgnoreCase("No") ? 1 : 0));
                }
                PdfPCell cell = new PdfPCell(new Phrase(value));
                cell.setPadding(5);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(cell);
            }
        }

        document.add(table);

        addFooter(document, kota, namaDirektur);
        document.close();

        openPdf(outputFilePath);
    }

    private static void addTableHeader(PdfPTable table, String[] headers) {
        Font headFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);
        BaseColor bgColor = new BaseColor(63, 81, 181);
        for (String h : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(h, headFont));
            cell.setBackgroundColor(bgColor);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(5);
            table.addCell(cell);
        }
    }

    private static void addKopSurat(Document document) throws Exception {
        URL imageUrl = ReportUtil.class.getClassLoader().getResource("images/kopOK.png");
        if (imageUrl == null) {
            throw new RuntimeException("File gambar kop surat tidak ditemukan di resources/images/kopOK.png");
        }
        byte[] imageBytes = readImageBytes(imageUrl);
        Image kop = Image.getInstance(imageBytes);
        kop.scaleToFit(530, 100);
        kop.setAlignment(Image.ALIGN_CENTER);
        document.add(kop);
    }

    private static void addFooter(Document document, String kota, String namaDirektur) throws DocumentException {
        String tanggal = new SimpleDateFormat("EEEE, dd MMMM yyyy").format(new Date());

        Font normalFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
        Font boldFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);

        // Buat tabel 1 kolom, posisinya di kanan
        PdfPTable footerTable = new PdfPTable(1);
        footerTable.setWidthPercentage(40); // lebar blok tanda tangan (40% halaman)
        footerTable.setHorizontalAlignment(Element.ALIGN_RIGHT); // posisikan ke kanan halaman
        footerTable.setSpacingBefore(40f); // jarak ke bawah dari elemen sebelumnya (2 spasi double kira-kira)

        // Kota + tanggal (tengah di blok)
        PdfPCell cell1 = new PdfPCell(new Phrase(kota + ", " + tanggal, normalFont));
        cell1.setBorder(Rectangle.NO_BORDER);
        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
        footerTable.addCell(cell1);

        // Jabatan (tengah di blok)
        PdfPCell cell2 = new PdfPCell(new Phrase("Direktur", normalFont));
        cell2.setBorder(Rectangle.NO_BORDER);
        cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
        footerTable.addCell(cell2);

        // Spasi untuk tanda tangan
        PdfPCell space = new PdfPCell(new Phrase("\n\n\n", normalFont));
        space.setBorder(Rectangle.NO_BORDER);
        footerTable.addCell(space);

        // Nama direktur (tengah di blok)
        PdfPCell cell4 = new PdfPCell(new Phrase(namaDirektur, boldFont));
        cell4.setBorder(Rectangle.NO_BORDER);
        cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
        footerTable.addCell(cell4);

        document.add(footerTable);
    }



    private static byte[] readImageBytes(URL imageUrl) throws Exception {
        try (InputStream is = imageUrl.openStream();
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int n;
            while ((n = is.read(buffer)) != -1) {
                baos.write(buffer, 0, n);
            }
            return baos.toByteArray();
        }
    }

    private static void openPdf(String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                System.err.println("File PDF tidak ditemukan: " + filePath);
                return;
            }
            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.OPEN)) {
                    desktop.open(file);
                    System.out.println("Membuka PDF dengan aplikasi default.");
                } else {
                    System.err.println("Action OPEN tidak didukung di Desktop.");
                }
            } else {
                System.err.println("Desktop tidak didukung di sistem ini.");
            }
        } catch (IOException e) {
            System.err.println("Gagal membuka file PDF: " + e.getMessage());
        }
    }
}
