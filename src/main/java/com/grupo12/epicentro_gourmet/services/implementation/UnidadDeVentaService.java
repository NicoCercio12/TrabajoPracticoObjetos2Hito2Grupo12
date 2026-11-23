package com.grupo12.epicentro_gourmet.services.implementation;
import com.grupo12.epicentro_gourmet.dtos.UnidadDeVentaDTO;
import com.grupo12.epicentro_gourmet.dtos.ConsultarAntiguedadEmpleadosResponseDTO;
import com.grupo12.epicentro_gourmet.dtos.EmpleadoUnidadDTO;
import com.grupo12.epicentro_gourmet.dtos.ModificarPlatoDeUnidadResponseDTO;
import com.grupo12.epicentro_gourmet.entities.FoodTruck;
import com.grupo12.epicentro_gourmet.entities.PuestoDesarmable;
import com.grupo12.epicentro_gourmet.entities.UnidadDeVenta;
import com.grupo12.epicentro_gourmet.entities.Plato;
import com.grupo12.epicentro_gourmet.enums.TipoEstadoEmpleado;
import com.grupo12.epicentro_gourmet.enums.TipoUnidad;
import com.grupo12.epicentro_gourmet.entities.Empleado;
import com.grupo12.epicentro_gourmet.repositories.IUnidadDeVentaRepository;
import com.grupo12.epicentro_gourmet.services.IUnidadDeVentaService;
import lombok.RequiredArgsConstructor;

import org.apache.commons.lang3.ObjectUtils.Null;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;


  
@Service
@RequiredArgsConstructor
public class UnidadDeVentaService implements IUnidadDeVentaService {

    private final IUnidadDeVentaRepository unidadDeVentaRepository;

    @Override
    @Transactional(readOnly = true)
   public List<UnidadDeVentaDTO> obtenerUnidadesPorFestivalId(Long festivalId) {
        // Consulta las unidades asociadas a dicho festival en la BD
        List<UnidadDeVenta> unidades = unidadDeVentaRepository.findByFestivalesId(festivalId);

        // Mapea la lista a DTOs
        return unidades.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    private UnidadDeVentaDTO convertirADTO(UnidadDeVenta uv) {
        UnidadDeVentaDTO.UnidadDeVentaDTOBuilder builder = UnidadDeVentaDTO.builder()
                .id(uv.getId())
                .codigoUnico(uv.getCodigoUnico())
                .nombreComercial(uv.getNombreComercial())
                .superficie(uv.getSuperficie());

        if (uv.getResponsable() != null) {
            builder.responsable(uv.getResponsable().getNombre() + " " + uv.getResponsable().getApellido());
        } else {
            builder.responsable("Sin Asignar");
        }

        // Especialización según tipo
        if (uv instanceof FoodTruck ft) {
            builder.tipoUnidad("Food Truck")
                   .patente(ft.getPatente())
                   .requiereElectricidad(ft.isRequiereElectricidad());
        } else if (uv instanceof PuestoDesarmable pd) {
            builder.tipoUnidad("Puesto Desarmable")
                   .cantCarpas(pd.getCantCarpas())
                   .tiempoMontajeMinutos(pd.getTiempoMontajeMinutos());
        }

        return builder.build();
    }

    @Override
    public UnidadDeVenta buscarPorId(Long idUnidad) {
        return unidadDeVentaRepository.findById(idUnidad)
                .orElseThrow(() -> new IllegalArgumentException("Unidad de Venta no encontrada con ID: " + idUnidad));
    }

    @Override
    public double calcularCanon(Long idUnidad) {
        UnidadDeVenta unidad = buscarPorId(idUnidad);
        return unidad.calcularCanon();
    }

    //FUNCION AUXILIAR PARA MAPEAR EL DTO RESPONSE
    private ModificarPlatoDeUnidadResponseDTO mapearResponse(UnidadDeVenta unidadDeVenta,Plato plato){

        ModificarPlatoDeUnidadResponseDTO UnidadResponse =new ModificarPlatoDeUnidadResponseDTO();
        UnidadResponse.setCodigoUnico(unidadDeVenta.getCodigoUnico());
        UnidadResponse.setId(unidadDeVenta.getId());
        UnidadResponse.setNombreComercial(unidadDeVenta.getNombreComercial());
        UnidadResponse.setSuperficie(unidadDeVenta.getSuperficie());
        UnidadResponse.setPlato(plato);

        if(unidadDeVenta instanceof FoodTruck ){
            FoodTruck foodTruck = (FoodTruck)unidadDeVenta;
            UnidadResponse.setPatente(foodTruck.getPatente());
            UnidadResponse.setRequiereElectricidad(foodTruck.isRequiereElectricidad());}else{


                PuestoDesarmable puestoDesarmable=(PuestoDesarmable)unidadDeVenta;
                UnidadResponse.setCantCarpas(puestoDesarmable.getCantCarpas());
                UnidadResponse.setTiempoMontajeMinutos(puestoDesarmable.getTiempoMontajeMinutos());

            }

            return UnidadResponse;



    }

    // API - REST TRANSACCIONES

    //1) CU _MODIFICAR PRECIO DE COSTO DEL PALTO DE UNA UNIDAD, ALUMNO LUCAS LA ROSA 
    @Override
    @Transactional 
     public ModificarPlatoDeUnidadResponseDTO modificarPlatoDeUnaUnidad(String codigoUnico,String nombrePlato, double precioPlato){
    
        UnidadDeVenta unidadAux = unidadDeVentaRepository.findByCodigoUnicoAndNombrePlato(codigoUnico,nombrePlato);
        Set<Plato>lstPlatos= unidadAux.getLstPlatos();
        Plato platoModificado=null;
        for (Plato p:lstPlatos){
            if(p.getNombre().equalsIgnoreCase(nombrePlato)){
                p.setCostoProduccion(precioPlato);
                platoModificado=p;
                break;

            }

            
        }

        if (platoModificado == null) {
        throw new RuntimeException("No se cargo el plato");
    } 
        UnidadDeVenta unidad = unidadDeVentaRepository.save(unidadAux);
        return(mapearResponse(unidad,platoModificado));
    }

//1)CU_ CONSULTAR ANTIGUEDAD DE LOS EMPLEADOS DE UNA UNIDAD DE VENTA, ALUMNO LA ROSA LUCAS
@Override 
@Transactional (readOnly = true)
public ConsultarAntiguedadEmpleadosResponseDTO consultarAntiguedadEmpleados(String codigoUnico,int antiguedad,TipoEstadoEmpleado estado){


UnidadDeVenta unidadAux=unidadDeVentaRepository.findByCodigoUnico(codigoUnico);

Set<Empleado>lstEmpleados=unidadAux.getLstStaff();
Set<EmpleadoUnidadDTO>lstEmpleadosDTO=new HashSet<>();


for(Empleado e:lstEmpleados){
   
    if(estado==TipoEstadoEmpleado.ACTIVO){

        if(e.calcularAntiguedad()>antiguedad && e.isActivo()){
             EmpleadoUnidadDTO empDTO =new EmpleadoUnidadDTO();
            
    
            empDTO.setIdEmpleado(e.getIdEmpleado());
            empDTO.setNombre(e.getNombre());
            empDTO.setApellido(e.getApellido());
            empDTO.setDni(e.getDni());
            empDTO.setEmail(e.getEmail());
            empDTO.setFechaIngreso(e.getFechaIngreso());
            empDTO.setFechaNacimiento(e.getFechaNacimiento());
            empDTO.setAntiguedad(e.calcularAntiguedad());

            lstEmpleadosDTO.add(empDTO);
}}

    if(estado==TipoEstadoEmpleado.INACTIVO){
        if(e.calcularAntiguedad()>antiguedad && !e.isActivo()){

             EmpleadoUnidadDTO empDTO =new EmpleadoUnidadDTO();
    
            empDTO.setIdEmpleado(e.getIdEmpleado());
            empDTO.setNombre(e.getNombre());
            empDTO.setApellido(e.getApellido());
            empDTO.setDni(e.getDni());
            empDTO.setEmail(e.getEmail());
            empDTO.setFechaIngreso(e.getFechaIngreso());
            empDTO.setFechaNacimiento(e.getFechaNacimiento());
            empDTO.setAntiguedad(e.calcularAntiguedad());

            lstEmpleadosDTO.add(empDTO);
}}

    if(estado==TipoEstadoEmpleado.AMBOS){
        if(e.calcularAntiguedad()>antiguedad){

            EmpleadoUnidadDTO empDTO =new EmpleadoUnidadDTO();
            empDTO.setIdEmpleado(e.getIdEmpleado());
            empDTO.setNombre(e.getNombre());
            empDTO.setApellido(e.getApellido());
            empDTO.setDni(e.getDni());
            empDTO.setEmail(e.getEmail());
            empDTO.setFechaIngreso(e.getFechaIngreso());
            empDTO.setFechaNacimiento(e.getFechaNacimiento());
            empDTO.setAntiguedad(e.calcularAntiguedad());

            lstEmpleadosDTO.add(empDTO);


        }



}

}

ConsultarAntiguedadEmpleadosResponseDTO UnidadResponse =new ConsultarAntiguedadEmpleadosResponseDTO();
UnidadResponse.setCodigoUnico(unidadAux.getCodigoUnico());
UnidadResponse.setId(unidadAux.getId());
UnidadResponse.setNombreComercial(unidadAux.getNombreComercial());
UnidadResponse.setSuperficie(unidadAux.getSuperficie());
if(unidadAux instanceof PuestoDesarmable ){
UnidadResponse.setTipoUnidad(TipoUnidad.PUESTO_DESARMABLE);

}else{
UnidadResponse.setTipoUnidad(TipoUnidad.FOOD_TRUCK);

}
UnidadResponse.setLstEmpleados(lstEmpleadosDTO);

return UnidadResponse;
}


}