package com.kingmalitha.springbooteventticketplatform.services.impl;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.kingmalitha.springbooteventticketplatform.domain.entities.QrCode;
import com.kingmalitha.springbooteventticketplatform.domain.entities.QrCodeStatusEnum;
import com.kingmalitha.springbooteventticketplatform.domain.entities.Ticket;
import com.kingmalitha.springbooteventticketplatform.exceptions.QrCodeGenerationException;
import com.kingmalitha.springbooteventticketplatform.exceptions.QrCodeNotFoundException;
import com.kingmalitha.springbooteventticketplatform.repositories.QrCodeRepository;
import com.kingmalitha.springbooteventticketplatform.services.QrCodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class QrCodeServiceImpl implements QrCodeService {

    private static final int QR_HEIGHT = 300;
    private static final int QR_WIDTH = 300;

    private final QrCodeRepository qrCodeRepository;
    private final QRCodeWriter qrCodeWriter;
    @Override
    public QrCode generateQrCode(Ticket ticket) {
        try {
            UUID uniqueId = UUID.randomUUID();

            //GENERATE QR CODE USING ZXING LIBRARY
            String qrCodeImage = generateQrCodeImage(uniqueId);

            QrCode qrCode = new QrCode();

            qrCode.setId(uniqueId);
            qrCode.setTicket(ticket);
            qrCode.setValue(qrCodeImage);
            qrCode.setStatus(QrCodeStatusEnum.ACTIVE);

//      `save`: Persists the entity, but the changes may not be immediately written to the database.
//      The actual SQL execution can be delayed until the transaction is committed or the persistence context is flushed.
//
//      `saveAndFlush`: Persists the entity and immediately flushes changes to the database,
//      executing the SQL right away.
//
//      **When to use:**
//            - Use `save` for most cases, especially within transactional
//            methods where immediate DB write is not required.
//            - Use `saveAndFlush` when you need the changes to be visible in the
//            database immediately
//            (e.g., when subsequent code depends on the data being present in the DB).

            return qrCodeRepository.saveAndFlush(qrCode);

        } catch (WriterException | IOException e) {
            throw new QrCodeGenerationException("Failed to generate QR code", e);

        }
    }

    @Override
    public byte[] getQrCodeImageForUserAndTicket(UUID userId, UUID ticketId) {
        QrCode qrCode = qrCodeRepository.findByTicketIdAndTicketPurchaserId(ticketId, userId)
                .orElseThrow(() -> new QrCodeNotFoundException("QR code not " +
                        "found for the given user and ticket"));

        try {
            return java.util.Base64.getDecoder().decode(qrCode.getValue());
        } catch (IllegalArgumentException e) {
            log.error("Failed to decode QR code image for ticketId: {} and userId: {}",
                    ticketId, userId, e);
            throw new QrCodeGenerationException("Failed to decode QR code image", e);
        }
    }

    private String generateQrCodeImage(UUID uniqueId) throws WriterException, IOException {
       BitMatrix bitMatrix = qrCodeWriter.encode(uniqueId.toString(),
               BarcodeFormat.QR_CODE,
                QR_WIDTH, QR_HEIGHT);

       BufferedImage qrCodeImage =
               MatrixToImageWriter.toBufferedImage(bitMatrix);

       try(ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
           ImageIO.write(qrCodeImage, "PNG", byteArrayOutputStream);
           byte[] pngData = byteArrayOutputStream.toByteArray();
           return java.util.Base64.getEncoder().encodeToString(pngData);
       }
    }
}
