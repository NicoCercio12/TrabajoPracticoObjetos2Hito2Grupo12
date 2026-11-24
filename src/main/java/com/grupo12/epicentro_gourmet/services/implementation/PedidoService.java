package com.grupo12.epicentro_gourmet.services.implementation;

import com.grupo12.epicentro_gourmet.dtos.PedidoRequestDTO;
import com.grupo12.epicentro_gourmet.dtos.PedidoResumenDTO;
import com.grupo12.epicentro_gourmet.dtos.PlatoDTO;
import com.grupo12.epicentro_gourmet.entities.Festival;
import com.grupo12.epicentro_gourmet.entities.ItemPedido;
import com.grupo12.epicentro_gourmet.entities.Pedido;
import com.grupo12.epicentro_gourmet.entities.Plato;
import com.grupo12.epicentro_gourmet.entities.UnidadDeVenta;
import com.grupo12.epicentro_gourmet.repositories.IFestivalRepository;
import com.grupo12.epicentro_gourmet.repositories.IPedidoRepository;
import com.grupo12.epicentro_gourmet.repositories.IPlatoRepository;
import com.grupo12.epicentro_gourmet.repositories.IUnidadDeVentaRepository;
import com.grupo12.epicentro_gourmet.services.IPedidoService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class PedidoService implements IPedidoService {

    private final IPedidoRepository repository;
    private final IFestivalRepository festivalRepository;
    private final IUnidadDeVentaRepository unidadDeVentaRepository;
    private final IPlatoRepository platoRepository;

    public PedidoService(
            IPedidoRepository repository,
            IFestivalRepository festivalRepository,
            IUnidadDeVentaRepository unidadDeVentaRepository,
            IPlatoRepository platoRepository
    ) {
        this.repository = repository;
        this.festivalRepository = festivalRepository;
        this.unidadDeVentaRepository = unidadDeVentaRepository;
        this.platoRepository = platoRepository;
    }

    @Override
    public PlatoDTO obtenerPlatoEstrella(Long festivalId, Long unidadId) {
        List<Plato> platos = repository.encontrarPlatoEstrella(festivalId, unidadId, PageRequest.of(0, 1));

        if (platos == null || platos.isEmpty()) {
            return null;
        }

        Plato plato = platos.get(0);

        return PlatoDTO.builder()
                .idPlato(plato.getIdPlato())
                .nombre(plato.getNombre())
                .precioVenta(plato.getPrecioVenta())
                .costoProduccion(plato.getCostoProduccion())
                .build();
    }

    @Override
    @Transactional
    public PedidoResumenDTO crearPedido(PedidoRequestDTO requestDTO) {
        Festival festival = festivalRepository.findById(requestDTO.getFestivalId())
                .orElseThrow(() -> new IllegalArgumentException("Festival no encontrado"));

        UnidadDeVenta unidad = unidadDeVentaRepository.findById(requestDTO.getUnidadId())
                .orElseThrow(() -> new IllegalArgumentException("Unidad de venta no encontrada"));

        Pedido pedido = new Pedido(festival, unidad);
        pedido.setFecha(requestDTO.getFecha() != null ? requestDTO.getFecha() : LocalDate.now());

        if (requestDTO.getItems() != null) {
            for (PedidoRequestDTO.ItemPedidoRequest itemReq : requestDTO.getItems()) {
                Plato plato = platoRepository.findById(itemReq.getPlatoId())
                        .orElseThrow(() -> new IllegalArgumentException("Plato no encontrado"));

                ItemPedido item = new ItemPedido();
                item.setPedido(pedido);
                item.setPlato(plato);
                item.setCantidad(itemReq.getCantidad());
                pedido.getLstItems().add(item);
            }
        }

        Pedido guardado = repository.save(pedido);
        return mapearAResumenDTO(guardado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PedidoResumenDTO> buscarPedidosConFiltros(
            Long festivalId,
            Long unidadId,
            LocalDate fechaDesde,
            LocalDate fechaHasta
    ) {
        return repository.findAll().stream()
                .filter(p -> festivalId == null || (p.getFestival() != null && festivalId.equals(p.getFestival().getId())))
                .filter(p -> unidadId == null || (p.getUnidadDeVenta() != null && unidadId.equals(p.getUnidadDeVenta().getId())))
                .filter(p -> fechaDesde == null || (p.getFecha() != null && !p.getFecha().isBefore(fechaDesde)))
                .filter(p -> fechaHasta == null || (p.getFecha() != null && !p.getFecha().isAfter(fechaHasta)))
                .map(this::mapearAResumenDTO)
                .toList();
    }

    @Override
    @Transactional
    public PedidoResumenDTO actualizarPedido(Long idPedido, PedidoRequestDTO requestDTO) {
        Pedido pedido = repository.findById(idPedido)
                .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado con ID: " + idPedido));

        if (requestDTO.getFestivalId() != null) {
            Festival festival = festivalRepository.findById(requestDTO.getFestivalId())
                    .orElseThrow(() -> new IllegalArgumentException("Festival no encontrado"));
            pedido.setFestival(festival);
        }

        if (requestDTO.getUnidadId() != null) {
            UnidadDeVenta unidad = unidadDeVentaRepository.findById(requestDTO.getUnidadId())
                    .orElseThrow(() -> new IllegalArgumentException("Unidad de venta no encontrada"));
            pedido.setUnidadDeVenta(unidad);
        }

        if (requestDTO.getFecha() != null) {
            pedido.setFecha(requestDTO.getFecha());
        }

        if (requestDTO.getItems() != null) {
            pedido.getLstItems().clear();
            for (PedidoRequestDTO.ItemPedidoRequest itemReq : requestDTO.getItems()) {
                Plato plato = platoRepository.findById(itemReq.getPlatoId())
                        .orElseThrow(() -> new IllegalArgumentException("Plato no encontrado"));

                ItemPedido item = new ItemPedido();
                item.setPedido(pedido);
                item.setPlato(plato);
                item.setCantidad(itemReq.getCantidad());
                pedido.getLstItems().add(item);
            }
        }

        Pedido actualizado = repository.save(pedido);
        return mapearAResumenDTO(actualizado);
    }

    @Override
    @Transactional
    public void eliminarPedido(Long idPedido) {
        Pedido pedido = repository.findById(idPedido)
                .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado con ID: " + idPedido));
        repository.delete(pedido);
    }

    private PedidoResumenDTO mapearAResumenDTO(Pedido pedido) {
        Long festId = pedido.getFestival() != null ? pedido.getFestival().getId() : null;
        Long unidId = pedido.getUnidadDeVenta() != null ? pedido.getUnidadDeVenta().getId() : null;
        int cantItems = pedido.getLstItems() != null ? pedido.getLstItems().size() : 0;

        return PedidoResumenDTO.builder()
                .idPedido(pedido.getIdPedido())
                .fecha(pedido.getFecha())
                .festivalId(festId)
                .unidadId(unidId)
                .cantidadItems(cantItems)
                .build();
    }
}