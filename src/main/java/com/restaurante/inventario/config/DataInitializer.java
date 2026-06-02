package com.restaurante.inventario.config;

import com.restaurante.inventario.model.OpcionMenu;
import com.restaurante.inventario.model.Rol;
import com.restaurante.inventario.model.Usuario;
import com.restaurante.inventario.repository.OpcionMenuRepository;
import com.restaurante.inventario.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final OpcionMenuRepository opcionMenuRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UsuarioRepository usuarioRepository,
                           OpcionMenuRepository opcionMenuRepository,
                           PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.opcionMenuRepository = opcionMenuRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (usuarioRepository.count() == 0) {
            Usuario admin = new Usuario();
            admin.setNombre("Administrador");
            admin.setEmail("admin@restaurante.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRol(Rol.ADMIN);
            usuarioRepository.save(admin);
            System.out.println("Usuario admin creado: admin@restaurante.com / admin123");
        }

        if (opcionMenuRepository.count() == 0) {
            seedMenu();
            System.out.println("Opciones de menú creadas");
        }
    }

    private void seedMenu() {
        OpcionMenu general = crearOpcion(null, "General", null, "ADMIN,CHEF,MESERO", "dashboard", 1);
        OpcionMenu gestion = crearOpcion(null, "Gestión", null, "ADMIN,CHEF,MESERO", "inventory_2", 2);
        OpcionMenu movimientos = crearOpcion(null, "Movimientos", null, "ADMIN", "swap_vert", 3);
        OpcionMenu operaciones = crearOpcion(null, "Operaciones", null, "ADMIN,MESERO,CHEF", "point_of_sale", 4);

        crearOpcion(general, "Dashboard", "/dashboard", "ADMIN", "dashboard", 1);
        crearOpcion(gestion, "Productos", "/productos", "ADMIN,CHEF", "inventory_2", 1);
        crearOpcion(gestion, "Platos", "/platos", "ADMIN,CHEF,MESERO", "restaurant", 2);
        crearOpcion(gestion, "Usuarios", "/usuarios", "ADMIN", "people", 3);
        crearOpcion(movimientos, "Listado", "/movimientos", "ADMIN", "swap_vert", 1);
        crearOpcion(movimientos, "Entrada", "/movimientos/entrada", "ADMIN", "add_circle", 2);
        crearOpcion(movimientos, "Ajuste", "/movimientos/ajuste", "ADMIN", "tune", 3);
        crearOpcion(operaciones, "Ventas", "/venta", "ADMIN,MESERO", "point_of_sale", 1);
        crearOpcion(operaciones, "Menú", "/menu", "MESERO", "menu_book", 2);
        crearOpcion(operaciones, "Stock", "/stock", "CHEF", "warehouse", 3);
    }

    private OpcionMenu crearOpcion(OpcionMenu padre, String nombre, String ruta,
                                    String roles, String icono, int orden) {
        OpcionMenu opcion = new OpcionMenu();
        opcion.setNombre(nombre);
        opcion.setPadre(padre);
        opcion.setRuta(ruta);
        opcion.setRoles(roles);
        opcion.setIcono(icono);
        opcion.setOrden(orden);
        return opcionMenuRepository.save(opcion);
    }
}
