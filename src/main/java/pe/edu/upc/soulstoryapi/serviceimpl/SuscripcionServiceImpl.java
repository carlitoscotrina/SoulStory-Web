package pe.edu.upc.soulstoryapi.serviceimpl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upc.soulstoryapi.dto.EstadoSuscripcionDTO;
import pe.edu.upc.soulstoryapi.dto.PagoRespuestaDTO;
import pe.edu.upc.soulstoryapi.dto.ProcesarPagoDTO;
import pe.edu.upc.soulstoryapi.dto.SuscripcionRespuestaDTO;
import pe.edu.upc.soulstoryapi.entity.AdultoMayor;
import pe.edu.upc.soulstoryapi.entity.Pago;
import pe.edu.upc.soulstoryapi.entity.Plan;
import pe.edu.upc.soulstoryapi.entity.Suscripcion;
import pe.edu.upc.soulstoryapi.exception.AccesoNoAutorizadoException;
import pe.edu.upc.soulstoryapi.exception.AdultoMayorNoEncontradoException;
import pe.edu.upc.soulstoryapi.exception.PlanNoEncontradoException;
import pe.edu.upc.soulstoryapi.exception.SuscripcionNoEncontradaException;
import pe.edu.upc.soulstoryapi.exception.SuscripcionYaActivaException;
import pe.edu.upc.soulstoryapi.exception.UsuarioNoEncontradoException;
import pe.edu.upc.soulstoryapi.repository.AdultoMayorRepository;
import pe.edu.upc.soulstoryapi.repository.PagoRepository;
import pe.edu.upc.soulstoryapi.repository.PlanRepository;
import pe.edu.upc.soulstoryapi.repository.SuscripcionRepository;
import pe.edu.upc.soulstoryapi.repository.UsuarioRepository;
import pe.edu.upc.soulstoryapi.service.PasarelaPagoService;
import pe.edu.upc.soulstoryapi.service.SuscripcionService;

import java.time.LocalDateTime;

@Service
public class SuscripcionServiceImpl implements SuscripcionService {

    private final SuscripcionRepository suscripcionRepository;
    private final PagoRepository pagoRepository;
    private final PlanRepository planRepository;
    private final AdultoMayorRepository adultoMayorRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasarelaPagoService pasarelaPagoService;

    public SuscripcionServiceImpl(
            SuscripcionRepository suscripcionRepository,
            PagoRepository pagoRepository,
            PlanRepository planRepository,
            AdultoMayorRepository adultoMayorRepository,
            UsuarioRepository usuarioRepository,
            PasarelaPagoService pasarelaPagoService) {

        this.suscripcionRepository = suscripcionRepository;
        this.pagoRepository = pagoRepository;
        this.planRepository = planRepository;
        this.adultoMayorRepository = adultoMayorRepository;
        this.usuarioRepository = usuarioRepository;
        this.pasarelaPagoService = pasarelaPagoService;
    }

    // HU-32
    @Override
    public SuscripcionRespuestaDTO seleccionarPlan(
            Long idAdultoMayor,
            Long idPlan) {

        validarAdultoMayor(idAdultoMayor);
        Plan plan = buscarPlan(idPlan);

        if (suscripcionRepository
                .findFirstByIdAdultoMayorAndIdPlanAndEstadoTrueOrderByIdSuscripcionDesc(
                        idAdultoMayor,
                        idPlan
                )
                .isPresent()) {

            throw new SuscripcionYaActivaException(
                    "El adulto mayor ya cuenta con una suscripción activa para este plan"
            );
        }

        Suscripcion suscripcion = new Suscripcion();
        suscripcion.setFechaInicio(null);
        suscripcion.setFechaFin(null);
        suscripcion.setEstado(false);
        suscripcion.setIdAdultoMayor(idAdultoMayor);
        suscripcion.setIdPlan(idPlan);
        suscripcion.setIdPago(null);

        return convertirSuscripcionDTO(
                suscripcionRepository.save(suscripcion),
                plan.getNombrePlan()
        );
    }

    // HU-33
    @Override
    @Transactional
    public PagoRespuestaDTO procesarPago(
            Long idAdultoMayor,
            Integer idSuscripcion,
            ProcesarPagoDTO dto) {

        AdultoMayor adultoMayor = validarAdultoMayor(idAdultoMayor);
        Suscripcion suscripcion = buscarSuscripcion(idSuscripcion);

        validarPropietario(suscripcion, idAdultoMayor);

        if (Boolean.TRUE.equals(suscripcion.getEstado())) {
            throw new SuscripcionYaActivaException(
                    "La suscripción ya está activa"
            );
        }

        validarMetodoPago(dto);

        Plan plan = buscarPlan(suscripcion.getIdPlan());

        usuarioRepository
                .findById(adultoMayor.getIdUsuario())
                .orElseThrow(() ->
                        new UsuarioNoEncontradoException(
                                "El usuario asociado al adulto mayor no existe"
                        )
                );

        pasarelaPagoService.procesarPago(dto.getMetodoPago());

        LocalDateTime fechaInicio = LocalDateTime.now();

        Pago pago = new Pago();
        pago.setMonto(plan.getPrecio());
        pago.setFechaPago(fechaInicio);
        pago.setMetodoPago(dto.getMetodoPago());
        pago.setEstado(true);
        pago.setIdUsuario(adultoMayor.getIdUsuario());

        Pago pagoGuardado = pagoRepository.save(pago);

        suscripcion.setIdPago(pagoGuardado.getIdPago());
        suscripcion.setEstado(true);
        suscripcion.setFechaInicio(fechaInicio);
        suscripcion.setFechaFin(
                fechaInicio.plusMonths(DURACION_SUSCRIPCION_MESES)
        );

        Suscripcion suscripcionGuardada = suscripcionRepository.save(
                suscripcion
        );

        return new PagoRespuestaDTO(
                pagoGuardado.getIdPago(),
                pagoGuardado.getMonto(),
                pagoGuardado.getFechaPago(),
                pagoGuardado.getMetodoPago(),
                pagoGuardado.getEstado(),
                suscripcionGuardada.getIdSuscripcion(),
                suscripcionGuardada.getEstado(),
                "Pago procesado correctamente"
        );
    }

    // HU-34
    @Override
    public EstadoSuscripcionDTO consultarEstado(Long idAdultoMayor) {

        validarAdultoMayor(idAdultoMayor);

        return suscripcionRepository
                .findFirstByIdAdultoMayorOrderByIdSuscripcionDesc(
                        idAdultoMayor
                )
                .map(this::convertirEstadoDTO)
                .orElseGet(() -> new EstadoSuscripcionDTO(
                        "SIN_SUSCRIPCION",
                        null,
                        null,
                        null,
                        null,
                        null
                ));
    }

    private AdultoMayor validarAdultoMayor(Long idAdultoMayor) {

        return adultoMayorRepository
                .findById(idAdultoMayor)
                .orElseThrow(() ->
                        new AdultoMayorNoEncontradoException(
                                "El adulto mayor no existe"
                        )
                );
    }

    private Plan buscarPlan(Long idPlan) {

        return planRepository
                .findById(idPlan)
                .orElseThrow(() ->
                        new PlanNoEncontradoException(
                                "El plan no existe"
                        )
                );
    }

    private Suscripcion buscarSuscripcion(Integer idSuscripcion) {

        return suscripcionRepository
                .findById(idSuscripcion)
                .orElseThrow(() ->
                        new SuscripcionNoEncontradaException(
                                "La suscripción no existe"
                        )
                );
    }

    private void validarPropietario(
            Suscripcion suscripcion,
            Long idAdultoMayor) {

        if (!idAdultoMayor.equals(suscripcion.getIdAdultoMayor())) {
            throw new AccesoNoAutorizadoException(
                    "La suscripción no pertenece a este adulto mayor"
            );
        }
    }

    private void validarMetodoPago(ProcesarPagoDTO dto) {

        if (dto == null
                || dto.getMetodoPago() == null
                || dto.getMetodoPago().isBlank()) {

            throw new IllegalArgumentException(
                    "El método de pago es obligatorio"
            );
        }
    }

    private SuscripcionRespuestaDTO convertirSuscripcionDTO(
            Suscripcion suscripcion,
            String nombrePlan) {

        return new SuscripcionRespuestaDTO(
                suscripcion.getIdSuscripcion(),
                suscripcion.getFechaInicio(),
                suscripcion.getFechaFin(),
                suscripcion.getEstado(),
                suscripcion.getIdAdultoMayor(),
                suscripcion.getIdPlan(),
                suscripcion.getIdPago(),
                nombrePlan
        );
    }

    private EstadoSuscripcionDTO convertirEstadoDTO(
            Suscripcion suscripcion) {

        String estado = calcularEstado(suscripcion);
        String nombrePlan = obtenerNombrePlan(suscripcion.getIdPlan());

        return new EstadoSuscripcionDTO(
                estado,
                suscripcion.getIdSuscripcion(),
                suscripcion.getIdPlan(),
                nombrePlan,
                suscripcion.getFechaInicio(),
                suscripcion.getFechaFin()
        );
    }

    private String calcularEstado(Suscripcion suscripcion) {

        if (Boolean.TRUE.equals(suscripcion.getEstado())
                && suscripcion.getFechaFin() != null) {

            if (suscripcion.getFechaFin().isAfter(LocalDateTime.now())) {
                return "ACTIVA";
            }

            return "VENCIDA";
        }

        if (!Boolean.TRUE.equals(suscripcion.getEstado())
                && suscripcion.getFechaInicio() != null
                && suscripcion.getIdPago() != null) {

            return "CANCELADA";
        }

        return "PENDIENTE";
    }

    private String obtenerNombrePlan(Long idPlan) {

        if (idPlan == null) {
            return null;
        }

        return planRepository
                .findById(idPlan)
                .map(Plan::getNombrePlan)
                .orElse(null);
    }
}
