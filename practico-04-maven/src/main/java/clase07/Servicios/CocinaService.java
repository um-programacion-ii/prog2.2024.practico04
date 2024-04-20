package clase07.Servicios;

import clase07.Entidades.*;
import clase07.Excepciones.StockInsuficiente;
import clase07.Excepciones.VidaUtilInsuficiente;
import clase07.Interfaces.Cocinable;
import clase07.Recetas.*;

import java.util.HashMap;
import java.util.Map;

public class CocinaService {
    private static volatile CocinaService instancia = null;
    private static final Object lock = new Object();

    private static final Map<Integer, Cocinable> recetas = new HashMap<>();

    static {
        recetas.put(1, new ArrozConLeche());
        recetas.put(2, new HuevoDuro());
        recetas.put(3, new TerneraAlHorno());
        recetas.put(4, new EnsaladaCesar());
        recetas.put(5, new SopaDeVerduras());
        recetas.put(6, new TartaDeManzana());
    }

    private CocinaService() {}

    public static CocinaService obtenerInstancia() {
        if (instancia == null) {
            synchronized (lock) {
                if (instancia == null) {
                    instancia = new CocinaService();
                }
            }
        }
        return instancia;
    }

    public static void comenzarACocinar(int numeroReceta, Despensa despensa, Estante estante) throws VidaUtilInsuficiente, StockInsuficiente {
        Receta receta = (Receta) recetas.get(numeroReceta);

        ((Cocinable) receta).cocinar();
        for (Elemento elemento : receta.getIngredientes()) {
            String nombreIngrediente = elemento.getNombre();
            int cantidadRequerida = elemento.getCantidad();
            despensa.getIngrediente(nombreIngrediente, cantidadRequerida);
        }
        for (Utensilio utensilio : receta.getUtensilios()) {
            String nombreUtensilio = utensilio.getNombre();
            int vidaUtilRequerida = utensilio.getVidaUtil();
            estante.getUtensilio(nombreUtensilio, vidaUtilRequerida);
        }
    }
}