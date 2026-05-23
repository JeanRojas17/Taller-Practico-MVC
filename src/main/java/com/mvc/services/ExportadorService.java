package com.mvc.services;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.io.*;

public class ExportadorService {

    public static void exportarExcel(JTable tabla, String titulo) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Guardar como Excel");
        chooser.setSelectedFile(new File(titulo+ ".xlsx"));

        if (chooser.showSaveDialog(null) != JFileChooser.APPROVE_OPTION) return;

        File archivo = chooser.getSelectedFile();
        if(!archivo.getName().endsWith(".xlsx")) {
            archivo = new File(archivo.getAbsolutePath()+ ".xlsx");
        }

        TableModel modelo = tabla.getModel();

        try(Workbook workbook = new XSSFWorkbook()) {
            Sheet hoja = workbook.createSheet(titulo);

            CellStyle estiloHeader = workbook.createCellStyle();
            Font fuenteHeader = workbook.createFont();
            fuenteHeader.setBold(true);
            estiloHeader.setFont(fuenteHeader);
            estiloHeader.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
            estiloHeader.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            Row rowHeader = hoja.createRow(0);

            for(int col = 0; col < modelo.getColumnCount(); col++) {
                Cell celda = rowHeader.createCell(col);
                celda.setCellValue(modelo.getColumnName(col));
                celda.setCellStyle(estiloHeader);
            }

            for(int fila = 0; fila < modelo.getRowCount(); fila++) {
                Row row = hoja.createRow(fila + 1);

                for(int col = 0; col < modelo.getColumnCount(); col++) {
                    Object val = modelo.getValueAt(fila, col);
                    row.createCell(col).setCellValue(val != null ? val.toString() : "");
                }
            }

            for(int col = 0; col < modelo.getColumnCount(); col++) {
                hoja.autoSizeColumn(col);
            }

            try(FileOutputStream fos = new FileOutputStream(archivo)) {
                workbook.write(fos);
            }

            JOptionPane.showMessageDialog(null,
                "Archivo exportado correctamente:\n" +archivo.getAbsolutePath(),
                "Exportar a Excel", JOptionPane.INFORMATION_MESSAGE);

        } catch(Exception e) {
            JOptionPane.showMessageDialog(null,
                "Error al exportar: " +e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void exportarPDF(JTable tabla, String titulo) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Guardar como PDF");
        chooser.setSelectedFile(new File(titulo+ ".pdf"));

        if (chooser.showSaveDialog(null) != JFileChooser.APPROVE_OPTION) return;

        File archivo = chooser.getSelectedFile();
        if(!archivo.getName().endsWith(".pdf")) {
            archivo = new File(archivo.getAbsolutePath()+ ".pdf");
        }

        TableModel modelo = tabla.getModel();
        Document document = new Document(PageSize.A4.rotate());

        try {
            PdfWriter.getInstance(document, new FileOutputStream(archivo));
            document.open();

            com.itextpdf.text.Font fuenteTitulo = new com.itextpdf.text.Font(
                com.itextpdf.text.Font.FontFamily.HELVETICA, 16,
                com.itextpdf.text.Font.BOLD, BaseColor.DARK_GRAY
            );

            Paragraph parrafTitulo = new Paragraph(titulo, fuenteTitulo);
            parrafTitulo.setAlignment(Element.ALIGN_CENTER);
            parrafTitulo.setSpacingAfter(14);
            document.add(parrafTitulo);

            PdfPTable pdfTable = new PdfPTable(modelo.getColumnCount());
            pdfTable.setWidthPercentage(100);

            com.itextpdf.text.Font fuenteHeader = new com.itextpdf.text.Font(
                com.itextpdf.text.Font.FontFamily.HELVETICA, 10,
                com.itextpdf.text.Font.BOLD, BaseColor.WHITE
            );

            for(int col = 0; col < modelo.getColumnCount(); col++) {
                PdfPCell celda = new PdfPCell(new Phrase(modelo.getColumnName(col), fuenteHeader));
                celda.setBackgroundColor(new BaseColor(41, 128, 185));
                celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                celda.setPadding(6);
                pdfTable.addCell(celda);
            }

            com.itextpdf.text.Font fuenteDato = new com.itextpdf.text.Font(
                com.itextpdf.text.Font.FontFamily.HELVETICA, 9
            );

            for(int fila = 0; fila < modelo.getRowCount(); fila++) {
                BaseColor colorFila = fila % 2 == 0
                    ? BaseColor.WHITE
                    : new BaseColor(235, 241, 248);

                for(int col = 0; col < modelo.getColumnCount(); col++) {
                    Object val = modelo.getValueAt(fila, col);
                    PdfPCell celda = new PdfPCell(new Phrase(val != null ? val.toString() : "", fuenteDato));
                    celda.setBackgroundColor(colorFila);
                    celda.setPadding(5);
                    pdfTable.addCell(celda);
                }
            }

            document.add(pdfTable);
            document.close();

            JOptionPane.showMessageDialog(null,
                "Archivo exportado correctamente:\n" +archivo.getAbsolutePath(),
                "Exportar a PDF", JOptionPane.INFORMATION_MESSAGE);

        } catch(Exception e) {
            JOptionPane.showMessageDialog(null,
                "Error al exportar: " +e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}