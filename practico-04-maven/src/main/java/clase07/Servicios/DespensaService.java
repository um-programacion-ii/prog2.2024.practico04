package clase07.Servicios;

import clase07.Entidades.*;
import clase07.Excepciones.StockInsuficiente;
import clase07.Excepciones.VidaUtilInsuficiente;
import clase07.Interfaces.Cocinable;
import clase07.Interfaces.Ingrediente;
import clase07.Recetas.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class DespensaService {

    private static final Map<Integer, Cocinable> recetas = new HashMap<>();

    static {
        recetas.put(1, new ArrozConLeche());
        recetas.put(2, new HuevoDuro());
        recetas.put(3, new TerneraAlHorno());
        recetas.put(4, new EnsaladaCesar());
        recetas.put(5, new SopaDeVerduras());
        recetas.put(6, new TartaDeManzana());
    }


    public static boolean verificarStockIngredientes(int numeroReceta, Despensa despensa) {
        Receta receta = (Receta) recetas.get(numeroReceta);

        boolean ingredientesSuficientes = true;
        for (Elemento elemento : receta.getIngredientes()) {
            String nombreIngrediente = elemento.getNombre();
            int cantidadRequerida = elemento.getCantidad();
            try {
                despensa.checkIngrediente(nombreIngrediente, cantidadRequerida);
            } catch (StockInsuficiente e) {
                ingredientesSuficientes = false;
                System.out.println(e.getMessage());
                break;
            }
        }
        return ingredientesSuficientes;
    }


    public static boolean verificarVidaUtilUtensilios(int numeroReceta, Estante estante) {
        Receta receta = (Receta) recetas.get(numeroReceta);

        boolean utensiliosSuficientes = true;
        for (Utensilio utensilio : receta.getUtensilios()) {
            String nombreUtensilio = utensilio.getNombre();
            int vidaUtilRequerida = utensilio.getVidaUtil();
            try {
                utensiliosSuficientes = estante.checkUtensilio(nombreUtensilio, vidaUtilRequerida);
            } catch (VidaUtilInsuficiente e) {
                utensiliosSuficientes = false;
                System.out.println(e.getMessage());
                break;
            }
        }
        return utensiliosSuficientes;
    }

    public static void renovarUtensilios(Estante estante) {
        for (Queue<Utensilio> utensilios : estante.getUtensilios().values()) {
            for (Utensilio utensilio : utensilios) {
                if (utensilio.getVidaUtil() <= 0) {
                    utensilio.setVidaUtil(500);
                }
            }
        }
    }

    public static void renovarIngredientes(Despensa despensa) {
        for (Ingrediente ingrediente : despensa.getElemento().values()) {
            if (ingrediente.getCantidad() <= 0) {
                ingrediente.setCantidad(5000);
            }
        }
    }
}