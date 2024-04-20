package clase07.Entidades;

import clase07.Excepciones.VidaUtilInsuficiente;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

@Data
@AllArgsConstructor
public class Estante {
    private static Estante instance = null;
    private Map<String, Queue<Utensilio>> utensilios= new HashMap<>();

    private Estante() {
    }
    public static Estante getInstance() {
        if (instance == null) {
            instance = new Estante();
        }
        return instance;
    }

    public synchronized void addUtensilio(Utensilio nuevoUtensilio) {
        String nombre = nuevoUtensilio.getNombre().toString();
        utensilios.putIfAbsent(nombre, new LinkedList<>());
        utensilios.get(nombre).add(nuevoUtensilio);
    }

    public synchronized void getUtensilio(String nombre, int vidaUtilRequerida) throws VidaUtilInsuficiente {
        Queue<Utensilio> utensiliosDeEsteTipo = utensilios.get(nombre);
        if (utensiliosDeEsteTipo == null || utensiliosDeEsteTipo.isEmpty()) {
            throw new VidaUtilInsuficiente("No hay más utensilios de tipo " + nombre + " disponibles.");
        }
        Utensilio utensilio = utensiliosDeEsteTipo.poll();
        utensilio.utilizar(vidaUtilRequerida);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            utensiliosDeEsteTipo.add(utensilio);
        }
    }

    public boolean checkUtensilio(String nombre, int vidaUtil) throws VidaUtilInsuficiente {
        if (utensilios.containsKey(nombre)) {
            Queue<Utensilio> utensiliosDeEsteTipo = utensilios.get(nombre);
            if (!utensiliosDeEsteTipo.isEmpty() && utensiliosDeEsteTipo.peek().getVidaUtil() >= vidaUtil) {
                return true;
            } else {
                throw new VidaUtilInsuficiente("No hay suficiente vida útil de " + nombre + " en la despensa.");
            }
        } else {
            throw new VidaUtilInsuficiente("No es posible retirar " + nombre + ", no hay en la despensa.");
        }
    }

    public void mostrarUtensilios() {
        System.out.println("Utensilios en la despensa:");
        for (Map.Entry<String, Queue<Utensilio>> entry : utensilios.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue().peek().getVidaUtil());
        }
    }

    public Map<String, Queue<Utensilio>> getUtensilios() {
        return utensilios;
    }

}
