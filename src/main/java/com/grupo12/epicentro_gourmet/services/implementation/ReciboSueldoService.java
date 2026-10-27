package com.grupo12.epicentro_gourmet.services.implementation;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.springframework.stereotype.Service;

import com.grupo12.epicentro_gourmet.entities.Empleado;
import com.grupo12.epicentro_gourmet.entities.Festival;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

@Service
public class ReciboSueldoService {

    private static final DateTimeFormatter FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final Locale AR = Locale.forLanguageTag("es-AR");

    public byte[] generar(Empleado empleado, Festival festival, double sueldo) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Document doc = new Document(PageSize.A4, 50, 50, 50, 50);
            PdfWriter.getInstance(doc, out);
            doc.open();

            Font titulo = new Font(Font.HELVETICA, 20, Font.BOLD);
            Font subtitulo = new Font(Font.HELVETICA, 12, Font.NORMAL);
            Font negrita = new Font(Font.HELVETICA, 11, Font.BOLD);
            Font normal = new Font(Font.HELVETICA, 11, Font.NORMAL);

            doc.add(new Paragraph("Epicentro Gourmet", titulo));
            doc.add(new Paragraph("Recibo de sueldo", subtitulo));
            doc.add(new Paragraph(" "));

            PdfPTable datos = new PdfPTable(2);
            datos.setWidthPercentage(100);
            fila(datos, "Empleado", empleado.getNombre() + " " + empleado.getApellido(), negrita, normal);
            fila(datos, "DNI", empleado.getDni(), negrita, normal);
            fila(datos, "Puesto", empleado.getClass().getSimpleName(), negrita, normal);
            fila(datos, "Fecha de ingreso", empleado.getFechaIngreso().format(FECHA), negrita, normal);
            fila(datos, "Unidad de venta",
                    empleado.getUnidadAsignada() != null
                            ? empleado.getUnidadAsignada().getNombreComercial() : "-", negrita, normal);
            fila(datos, "Festival", festival.getNombre(), negrita, normal);
            fila(datos, "Período",
                    festival.getFechaInicio().format(FECHA) + " al " + festival.getFechaFin().format(FECHA),
                    negrita, normal);
            fila(datos, "Duración", festival.getDuracionEnDias() + " días", negrita, normal);
            doc.add(datos);

            doc.add(new Paragraph(" "));
            Paragraph total = new Paragraph("Total a cobrar: " + String.format(AR, "$ %,.2f", sueldo),
                    new Font(Font.HELVETICA, 14, Font.BOLD));
            total.setAlignment(Element.ALIGN_RIGHT);
            doc.add(total);

            doc.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new IllegalStateException("No se pudo generar el recibo", e);
        }
    }

    private void fila(PdfPTable tabla, String etiqueta, String valor, Font f1, Font f2) {
        PdfPCell c1 = new PdfPCell(new Phrase(etiqueta, f1));
        PdfPCell c2 = new PdfPCell(new Phrase(valor, f2));
        c1.setPadding(6);
        c2.setPadding(6);
        tabla.addCell(c1);
        tabla.addCell(c2);
    }
}