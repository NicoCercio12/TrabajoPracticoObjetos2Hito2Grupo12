
package com.grupo12.epicentro_gourmet.services.implementation;

import com.grupo12.epicentro_gourmet.dtos.RentabilidadDiariaDTO;
import com.grupo12.epicentro_gourmet.entities.Festival;
import com.grupo12.epicentro_gourmet.entities.UnidadDeVenta;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Service
public class PdfRentabilidadService {

    private static final Color BORGOÑA = new Color(107, 29, 40);
    private static final Color GRIS = new Color(90, 90, 90);
    private static final Color AZUL = new Color(40, 105, 180);

    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final NumberFormat moneda =
            NumberFormat.getCurrencyInstance(new Locale("es", "AR"));

    public byte[] generarPdf(
            Festival festival,
            UnidadDeVenta unidad,
            LocalDate fechaInicio,
            LocalDate fechaFin,
            List<RentabilidadDiariaDTO> datos) {

        Document documento = new Document(PageSize.A4.rotate(), 30, 30, 30, 30);
        ByteArrayOutputStream salida = new ByteArrayOutputStream();

        try {
            PdfWriter escritor = PdfWriter.getInstance(documento, salida);
            documento.open();

            Font titulo = FontFactory.getFont(
                    FontFactory.HELVETICA_BOLD, 18, BORGOÑA);

            Font subtitulo = FontFactory.getFont(
                    FontFactory.HELVETICA, 11, GRIS);

            Font encabezado = FontFactory.getFont(
                    FontFactory.HELVETICA_BOLD, 8, Color.WHITE);

            Font normal = FontFactory.getFont(
                    FontFactory.HELVETICA, 8, Color.BLACK);

            Paragraph tituloPdf = new Paragraph(
                    "Reporte de rentabilidad neta", titulo);
            tituloPdf.setAlignment(Element.ALIGN_CENTER);
            tituloPdf.setSpacingAfter(12);
            documento.add(tituloPdf);

            documento.add(new Paragraph(
                    "Festival: " + festival.getNombre(), subtitulo));

            documento.add(new Paragraph(
                    "Unidad de venta: " + unidad.getNombreComercial(),
                    subtitulo));

            documento.add(new Paragraph(
                    "Período: " + fechaInicio.format(FORMATO_FECHA)
                            + " al " + fechaFin.format(FORMATO_FECHA),
                    subtitulo));

            documento.add(new Paragraph(" "));

            PdfPTable tabla = new PdfPTable(
                    new float[]{1.1f, 1.35f, 1.35f, 1.25f, 1.15f, 1.4f, 1.5f});

            tabla.setWidthPercentage(100);
            tabla.setHeaderRows(1);

            agregarEncabezado(tabla, "Fecha", encabezado);
            agregarEncabezado(tabla, "Recaudación", encabezado);
            agregarEncabezado(tabla, "Costo platos", encabezado);
            agregarEncabezado(tabla, "Sueldos", encabezado);
            agregarEncabezado(tabla, "Canon", encabezado);
            agregarEncabezado(tabla, "Rentabilidad diaria", encabezado);
            agregarEncabezado(tabla, "Rentabilidad acumulada", encabezado);

            double totalRecaudacion = 0;
            double totalCostos = 0;
            double totalSueldos = 0;
            double totalCanon = 0;

            for (RentabilidadDiariaDTO dia : datos) {
                agregarCelda(tabla,
                        dia.getFecha().format(FORMATO_FECHA), normal);
                agregarCelda(tabla,
                        moneda.format(dia.getRecaudacion()), normal);
                agregarCelda(tabla,
                        moneda.format(dia.getCostoPlatos()), normal);
                agregarCelda(tabla,
                        moneda.format(dia.getSueldos()), normal);
                agregarCelda(tabla,
                        moneda.format(dia.getCanon()), normal);
                agregarCelda(tabla,
                        moneda.format(dia.getRentabilidadNeta()), normal);
                agregarCelda(tabla,
                        moneda.format(dia.getRentabilidadAcumulada()), normal);

                totalRecaudacion += dia.getRecaudacion();
                totalCostos += dia.getCostoPlatos();
                totalSueldos += dia.getSueldos();
                totalCanon += dia.getCanon();
            }

            documento.add(tabla);

            double rentabilidadTotal =
                    totalRecaudacion - totalCostos
                            - totalSueldos - totalCanon;

            Paragraph resumen = new Paragraph(
                    "\nRentabilidad neta del período: "
                            + moneda.format(rentabilidadTotal),
                    FontFactory.getFont(
                            FontFactory.HELVETICA_BOLD, 12, BORGOÑA));

            resumen.setSpacingBefore(10);
            resumen.setSpacingAfter(12);
            documento.add(resumen);

            Paragraph tituloGrafico = new Paragraph(
                    "Evolución de la rentabilidad",
                    FontFactory.getFont(
                            FontFactory.HELVETICA_BOLD, 13, BORGOÑA));

            tituloGrafico.setSpacingBefore(8);
            tituloGrafico.setSpacingAfter(8);
            documento.add(tituloGrafico);

            if (!datos.isEmpty()) {
                PdfContentByte contenido = escritor.getDirectContent();
                PdfTemplate grafico = contenido.createTemplate(760, 270);

                dibujarGrafico(grafico, datos);

                Image imagenGrafico = Image.getInstance(grafico);
                imagenGrafico.scaleToFit(760, 270);
                imagenGrafico.setAlignment(Element.ALIGN_CENTER);

                documento.add(imagenGrafico);
            }

            documento.close();
            return salida.toByteArray();

        } catch (DocumentException | IOException e) {
            if (documento.isOpen()) {
                documento.close();
            }

            throw new IllegalStateException(
                    "No se pudo generar el PDF de rentabilidad.", e);
        }
    }

    private void agregarEncabezado(
            PdfPTable tabla, String texto, Font fuente) {

        PdfPCell celda = new PdfPCell(new Phrase(texto, fuente));
        celda.setBackgroundColor(BORGOÑA);
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
        celda.setPadding(6);
        tabla.addCell(celda);
    }

    private void agregarCelda(
            PdfPTable tabla, String texto, Font fuente) {

        PdfPCell celda = new PdfPCell(new Phrase(texto, fuente));
        celda.setPadding(5);
        celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
        tabla.addCell(celda);
    }

    private void dibujarGrafico(
            PdfTemplate grafico,
            List<RentabilidadDiariaDTO> datos)
            throws DocumentException, IOException {

        float ancho = 760;
        float alto = 270;

        float margenIzquierdo = 55;
        float margenDerecho = 20;
        float margenInferior = 40;
        float margenSuperior = 25;

        float anchoUtil = ancho - margenIzquierdo - margenDerecho;
        float altoUtil = alto - margenInferior - margenSuperior;

        double minimo = 0;
        double maximo = 0;

        for (RentabilidadDiariaDTO dia : datos) {
            minimo = Math.min(minimo, Math.min(
                    dia.getRentabilidadNeta(),
                    dia.getRentabilidadAcumulada()));

            maximo = Math.max(maximo, Math.max(
                    dia.getRentabilidadNeta(),
                    dia.getRentabilidadAcumulada()));
        }

        if (maximo == minimo) {
            maximo = minimo + 1;
        }

        double rango = maximo - minimo;
        PdfContentByte lienzo = grafico;

        lienzo.saveState();

        // Ejes.
        lienzo.setColorStroke(Color.GRAY);
        lienzo.setLineWidth(0.8f);
        lienzo.moveTo(margenIzquierdo, margenInferior);
        lienzo.lineTo(ancho - margenDerecho, margenInferior);
        lienzo.moveTo(margenIzquierdo, margenInferior);
        lienzo.lineTo(margenIzquierdo, alto - margenSuperior);
        lienzo.stroke();

        // Línea horizontal de valor cero.
        if (minimo < 0 && maximo > 0) {
            float yCero = margenInferior
                    + (float) ((0 - minimo) / rango * altoUtil);

            lienzo.setColorStroke(Color.LIGHT_GRAY);
            lienzo.moveTo(margenIzquierdo, yCero);
            lienzo.lineTo(ancho - margenDerecho, yCero);
            lienzo.stroke();
        }

        BaseFont fuente = BaseFont.createFont(
                BaseFont.HELVETICA,
                BaseFont.WINANSI,
                BaseFont.NOT_EMBEDDED);

        lienzo.setFontAndSize(fuente, 9);
        lienzo.setColorFill(Color.DARK_GRAY);

        lienzo.beginText();
        lienzo.showTextAligned(
                Element.ALIGN_LEFT,
                String.format(Locale.US, "%.2f", maximo),
                3, alto - margenSuperior, 0);

        lienzo.showTextAligned(
                Element.ALIGN_LEFT,
                String.format(Locale.US, "%.2f", minimo),
                3, margenInferior, 0);
        lienzo.endText();

        // Rentabilidad diaria.
        dibujarSerie(
                lienzo, datos, false,
                minimo, rango, margenIzquierdo,
                margenInferior, anchoUtil, altoUtil,
                BORGOÑA);

        // Rentabilidad acumulada.
        dibujarSerie(
                lienzo, datos, true,
                minimo, rango, margenIzquierdo,
                margenInferior, anchoUtil, altoUtil,
                AZUL);

        // Fechas: como máximo, seis etiquetas para mantener la legibilidad.
        lienzo.setColorFill(Color.DARK_GRAY);
        lienzo.beginText();

        int cantidadEtiquetas = Math.min(6, datos.size());

        for (int i = 0; i < cantidadEtiquetas; i++) {
            int indice = cantidadEtiquetas == 1
                    ? 0
                    : (int) Math.round(
                    i * (datos.size() - 1.0)
                            / (cantidadEtiquetas - 1));

            float x = datos.size() == 1
                    ? margenIzquierdo
                    : margenIzquierdo
                    + indice * anchoUtil / (datos.size() - 1);

            String fecha = datos.get(indice).getFecha()
                    .format(DateTimeFormatter.ofPattern("dd/MM"));

            lienzo.showTextAligned(
                    Element.ALIGN_CENTER, fecha,
                    x, margenInferior - 16, 0);
        }

        lienzo.endText();

        // Referencias de colores.
        lienzo.setColorStroke(BORGOÑA);
        lienzo.setLineWidth(2);
        lienzo.moveTo(480, alto - 12);
        lienzo.lineTo(500, alto - 12);
        lienzo.stroke();

        lienzo.setColorStroke(AZUL);
        lienzo.moveTo(585, alto - 12);
        lienzo.lineTo(605, alto - 12);
        lienzo.stroke();

        lienzo.setColorFill(Color.DARK_GRAY);
        lienzo.beginText();
        lienzo.setFontAndSize(fuente, 8);
        lienzo.showTextAligned(
                Element.ALIGN_LEFT, "Diaria", 504, alto - 15, 0);
        lienzo.showTextAligned(
                Element.ALIGN_LEFT, "Acumulada", 609, alto - 15, 0);
        lienzo.endText();

        lienzo.restoreState();
    }

    private void dibujarSerie(
            PdfContentByte lienzo,
            List<RentabilidadDiariaDTO> datos,
            boolean acumulada,
            double minimo,
            double rango,
            float margenIzquierdo,
            float margenInferior,
            float anchoUtil,
            float altoUtil,
            Color color) {

        lienzo.setColorStroke(color);
        lienzo.setLineWidth(2);

        for (int i = 0; i < datos.size(); i++) {
            RentabilidadDiariaDTO dia = datos.get(i);

            double valor = acumulada
                    ? dia.getRentabilidadAcumulada()
                    : dia.getRentabilidadNeta();

            float x = datos.size() == 1
                    ? margenIzquierdo + anchoUtil / 2
                    : margenIzquierdo + i * anchoUtil
                    / (datos.size() - 1);

            float y = margenInferior
                    + (float) ((valor - minimo) / rango * altoUtil);

            if (i == 0) {
                lienzo.moveTo(x, y);
            } else {
                lienzo.lineTo(x, y);
            }
        }

        lienzo.stroke();
    }
}
