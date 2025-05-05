package com.kingmalitha.springbooteventticketplatform.services.impl;

import com.kingmalitha.springbooteventticketplatform.domain.entities.Ticket;
import com.kingmalitha.springbooteventticketplatform.domain.entities.TicketStatusEnum;
import com.kingmalitha.springbooteventticketplatform.domain.entities.TicketType;
import com.kingmalitha.springbooteventticketplatform.domain.entities.User;
import com.kingmalitha.springbooteventticketplatform.exceptions.TicketSoldOutException;
import com.kingmalitha.springbooteventticketplatform.exceptions.UserNotFoundException;
import com.kingmalitha.springbooteventticketplatform.repositories.TicketRepository;
import com.kingmalitha.springbooteventticketplatform.repositories.TicketTypeRepository;
import com.kingmalitha.springbooteventticketplatform.repositories.UserRepository;
import com.kingmalitha.springbooteventticketplatform.services.QrCodeService;
import com.kingmalitha.springbooteventticketplatform.services.TicketTypeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketTypeServiceImpl implements TicketTypeService {
    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;
    private final TicketTypeRepository ticketTypeRepository;
    private final QrCodeService qrCodeService;


    @Override
    @Transactional
    public Ticket purchaseTicket(UUID userId, UUID ticketTypeId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(
                String.format("User with id %s not found", userId)
        ));

        TicketType ticketType = ticketTypeRepository.findByIdWithLock(ticketTypeId)
                .orElseThrow(() -> new RuntimeException(
                String.format("Ticket type with id %s not found", ticketTypeId)
        ));

//        - `int` is a primitive data type in Java, representing a 32-bit signed integer.
//        - `Integer` is a wrapper class for `int` in Java, providing methods and
//        use in collections and generics.
//        - `int` cannot be `null`, while `Integer` can be `null`.
//        - `Integer` objects are used when an object reference is required,
//        such as in collections like `List<Integer>`.

        int purchasedTickets = ticketRepository.countByTicketTypeId(ticketTypeId);
        Integer totalAvailable = ticketType.getTotalAvailable();

        if (purchasedTickets +1 > totalAvailable) {
            throw new TicketSoldOutException();
        }

        Ticket ticket = new Ticket();
        ticket.setStatus(TicketStatusEnum.PURCHASED);
        ticket.setTicketType(ticketType);
        ticket.setPurchaser(user);

        Ticket savedTicket = ticketRepository.save(ticket);
        qrCodeService.generateQrCode(savedTicket);

        return ticketRepository.save(savedTicket);
    }
}
